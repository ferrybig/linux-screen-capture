/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture;

import me.ferrybig.screencapture.ui.MousePanel;
import me.ferrybig.screencapture.ui.PaintPanel;
import me.ferrybig.screencapture.ui.PicturePanel;
import java.awt.AWTException;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import me.ferrybig.screencapture.tools.AreaSelectTool;
import me.ferrybig.screencapture.tools.ExitTool;
import me.ferrybig.screencapture.tools.ToolInfo;
import me.ferrybig.screencapture.tools.WindowSelectTool;
import me.ferrybig.screencapture.ui.ContainerPanel;
import me.ferrybig.screencapture.ui.ToolbarPanel;

/**
 *
 * @author fernando
 */
public class Main {

	private static void readProcess(Process p, Consumer<String> onLine) throws IOException, InterruptedException {
		try (BufferedReader e = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
			String line;
			while ((line = e.readLine()) != null) {
				onLine.accept(line);
			}
		} finally {
			String base = "Process exited with code: \n";
			StringBuilder b = new StringBuilder(base);
			try (BufferedReader e = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
				String line;
				while ((line = e.readLine()) != null) {
					b.append(line).append('\n');
				}
			}
			int exitCode = p.waitFor();
			if (exitCode != 0 || b.length() != base.length()) {
				b.insert(base.length() - 1, String.valueOf(exitCode));
				throw new IOException(b.toString());
			}
		}
	}

	public static void main(String[] args) throws AWTException, IOException, InterruptedException {
		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		//Rectangle screenRect = new Rectangle(
		System.out.println(screenRect);
		BufferedImage result;
		boolean directCaptue = false;
		System.out.println("Capturing screen");
		if (directCaptue) {
			result = new BufferedImage(screenRect.width, screenRect.height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = result.createGraphics();
			try {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice[] screens = ge.getScreenDevices();
				for (GraphicsDevice screen : screens) {
					Robot robotForScreen = new Robot();
					Rectangle bounds = screen.getConfigurations()[0].getBounds();
					BufferedImage capture = robotForScreen.createScreenCapture(screenRect);
					System.out.println("Making capture:");
					System.out.println("Rectangle: " + bounds);
					System.out.println("Screen: " + screen);
					g.drawImage(capture, null, bounds.x, bounds.y);
					ImageIO.write(capture, "png", new File(screen.toString() + ".png"));
				}
			} finally {
				g.dispose();
			}
		} else {
			// xwininfo -children -root
			File tempFile = File.createTempFile("screenshot-", ".png");
			tempFile.deleteOnExit();
			Process p = Runtime.getRuntime().exec("import -window root " + tempFile);
			p.getOutputStream().close();
			readProcess(p, (l) -> {
			});
			result = ImageIO.read(tempFile);
		}
		System.out.println("Capturing windows");
		List<WindowInfo> windows = new ArrayList<>();
		{
//			Map<String, WindowInfo> infoMap = new HashMap<>();
//			{
//				Process p = Runtime.getRuntime().exec("xwininfo -children -root");
//				p.getOutputStream().close();
//				Pattern pattern = Pattern.compile("^ {5}(0x[0-9a-f]{6,10}) (?:\"([^\"]*)\"|\\(has no name\\)): \\([^)]*\\)  (\\d+)x(\\d+)\\+(-?\\d+)\\+(-?\\d+)");
//				readProcess(p, (l) -> {
//					Matcher m = pattern.matcher(l);
//					if (m.find()) {
//						infoMap.put(m.group(1), new WindowInfo(m.group(2), new Rectangle(
//								Integer.parseInt(m.group(5)),
//								Integer.parseInt(m.group(6)),
//								Integer.parseInt(m.group(3)),
//								Integer.parseInt(m.group(4))
//						)));
//					}
//				});
//			}
			List<String> windowIds = new ArrayList<>();
			{
				Process p = Runtime.getRuntime().exec("xprop -root");

				Pattern patternPropKey = Pattern.compile("_NET_CLIENT_LIST_STACKING\\(WINDOW\\)");
				Pattern patternPropValue = Pattern.compile("0x[0-9a-f]{6,10}");
				p.getOutputStream().close();
				readProcess(p, (l) -> {
					Matcher m = patternPropKey.matcher(l);
					if (m.find()) {
						Matcher value = patternPropValue.matcher(l);
						while (value.find()) {
							windowIds.add(value.group());
						}
					}
				});
			}
			{
				List<Process> windowInfo = new ArrayList<>(windowIds.size());
				for (String windowId : windowIds) {
					Process p = Runtime.getRuntime().exec("xwininfo -id " + windowId);
					p.getOutputStream().close();
					windowInfo.add(p);
				}
				for (Process p : windowInfo) {
					Map<Pattern, Consumer<Matcher>> patternMap = new LinkedHashMap<>();
					WindowInfoBuilder builder = new WindowInfoBuilder();
					Rectangle geometry = new Rectangle();
					builder.setDimension(geometry);

					patternMap.put(Pattern.compile("xwininfo: Window id: 0x[0-9a-f]{6,10} (?:\"([^\"]*)\"|\\(has no name\\))"), m -> {
						builder.setName(m.group(1));
					});
					patternMap.put(Pattern.compile("Absolute upper-left Y:  ?(-?\\d+)"), m -> {
						geometry.y = Integer.parseInt(m.group(1), 10);
					});
					patternMap.put(Pattern.compile("Absolute upper-left X:  ?(-?\\d+)"), m -> {
						geometry.x = Integer.parseInt(m.group(1), 10);
					});
					patternMap.put(Pattern.compile("Width:  ?(-?\\d+)"), m -> {
						geometry.width = Integer.parseInt(m.group(1), 10);
					});
					patternMap.put(Pattern.compile("Height:  ?(-?\\d+)"), m -> {
						geometry.height = Integer.parseInt(m.group(1), 10);
					});

					readProcess(p, (l) -> {
						for (Map.Entry<Pattern, Consumer<Matcher>> entry : patternMap.entrySet()) {
							Matcher m = entry.getKey().matcher(l);
							if (m.find()) {
								entry.getValue().accept(m);
							}
						}
					});
					windows.add(builder.createWindowInfo());
				}
			}

		}

		System.out.println("Done!");
		windows.forEach(System.out::println);
		List<ToolInfo> tools = new ArrayList<>();
		tools.add(new ToolInfo(WindowSelectTool::new, Toolkit.getDefaultToolkit().getImage(Main.class.getResource("test.png")), "Windows"));
		tools.add(new ToolInfo(AreaSelectTool::new, Toolkit.getDefaultToolkit().getImage(Main.class.getResource("test.png")), "Select"));
		tools.add(new ToolInfo(ExitTool::new, Toolkit.getDefaultToolkit().getImage(Main.class.getResource("test.png")), "Exit"));
		ScreenCaptureModel model = new ScreenCaptureModel(windows, tools, result);
		SwingUtilities.invokeLater(() -> {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] screens = ge.getScreenDevices();
			for (GraphicsDevice screen : screens) {
				Rectangle bounds = screen.getConfigurations()[0].getBounds();
				makeFrame(model, screen, bounds);

			}
		});
	}

	private static void makeFrame(ScreenCaptureModel model, GraphicsDevice device, Rectangle bounds) {
		Point offset = bounds.getLocation();
		JFrame frame = new JFrame();
		Container contentFrame = frame.getContentPane();
		contentFrame.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 1;

		contentFrame.add(new MousePanel(model, offset), constraints);
		contentFrame.add(new ContainerPanel(offset, new ToolbarPanel(model)), constraints);
		contentFrame.add(new PaintPanel(model, offset), constraints);
		contentFrame.add(new PicturePanel(model, offset), constraints);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(bounds.width, bounds.height);
		frame.setContentPane(contentFrame);
		frame.setUndecorated(true);
		frame.setVisible(true);
		device.setFullScreenWindow(frame);
		model.doneListener.addListener(() -> {
			device.setFullScreenWindow(null);
			frame.setVisible(false);
			frame.dispose();
		});
		// device.setFullScreenWindow(frame);
	}

}

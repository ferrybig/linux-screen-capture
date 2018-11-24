/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture;

import me.ferrybig.screencapture.events.EventBus;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import me.ferrybig.screencapture.events.MouseEvent;
import me.ferrybig.screencapture.events.MouseEventBus;
import me.ferrybig.screencapture.events.MouseListener;
import me.ferrybig.screencapture.tools.Tool;
import me.ferrybig.screencapture.tools.ToolInfo;

/**
 *
 * @author fernando
 */
public class ScreenCaptureModel {

	private final List<WindowInfo> infoList;
	private final List<ToolInfo> tools;
	private final BufferedImage background;
	private Tool selectedTool;
	private ToolInfo selectedToolInfo;
	private Rectangle highlighted = null;
	private Point toolbarLocation = new Point();
	private Mouse mouse = new Mouse(new Point(), false, false);
	public final EventBus<Rectangle> listenersHighlighted = new EventBus();
	public final EventBus<Mouse> listenersMouse = new EventBus();
	public final EventBus<ToolInfo> listenersTool = new EventBus();
	public final EventBus<Point> listenersToolbarLocation = new EventBus();
	public final EventBus<Void> doneListener = new EventBus();
	public final MouseEventBus listenersMouseEvent = new MouseEventBus();
	private static final Logger LOG = Logger.getLogger(ScreenCaptureModel.class.getName());

	public ScreenCaptureModel(List<WindowInfo> infoList, List<ToolInfo> tools, BufferedImage background) {
		this.infoList = infoList;
		this.tools = tools;
		this.background = background;
		this.listenersMouse.addListener(() -> this.listenersMouseEvent.updateMouse(this.mouse));
		this.listenersMouseEvent.addListener(new MouseListener() {
			@Override
			public void mouseClick(MouseEvent evt) {
				LOG.info("click: " + evt.toString());
			}

			@Override
			public void mouseMove(MouseEvent evt) {
				LOG.info("move: " + evt.toString());
			}

			@Override
			public void mousePress(MouseEvent evt) {
				LOG.info("press: " + evt.toString());
			}

			@Override
			public void mouseRelease(MouseEvent evt) {
				LOG.info("release: " + evt.toString());
			}
		});
		this.setToolInfo(this.tools.get(0));
	}

	public List<ToolInfo> getTools() {
		return tools;
	}

	public List<WindowInfo> getInfoList() {
		return infoList;
	}

	public Rectangle getHighlighted() {
		return highlighted;
	}

	public BufferedImage getBackground() {
		return background;
	}

	public Point getToolbarLocation() {
		return toolbarLocation;
	}

	public Mouse getMouse() {
		return mouse;
	}

	public void setToolInfo(ToolInfo info) {
		if (selectedTool != null) {
			selectedTool.close();
		}
		ToolInfo old = this.selectedToolInfo;
		this.selectedToolInfo = info;
		this.selectedTool = info.getTool().apply(this);
		this.listenersTool.fireEvent(old, info);
	}

	public void setHighlighted(Rectangle newValue) {
		Rectangle old = this.highlighted;
		this.highlighted = newValue;
		this.listenersHighlighted.fireEvent(old, newValue);
	}

	public void setMouse(Mouse newValue) {
		Mouse old = this.mouse;
		this.mouse = newValue;
		this.listenersMouse.fireEvent(old, newValue);
	}

	public void setToolbarLocation(Point newValue) {
		Point old = this.toolbarLocation;
		this.toolbarLocation = newValue;
		this.listenersToolbarLocation.fireEvent(old, newValue);
	}

	public void continueSelection() {
		if (highlighted != null) {
			doneListener.fireEvent(null, null);
			BufferedImage subimage = background.getSubimage(highlighted.x, highlighted.y, highlighted.width, highlighted.height);
			new Thread(() -> {
				try {
					File tempFile = File.createTempFile("screenshot-", ".png");
					tempFile.deleteOnExit();
					ImageIO.write(subimage, "png", tempFile);
					ProcessBuilder p = new ProcessBuilder("wine", "/home/fernando/.wine/drive_c/Program Files/ShareX/ShareX.exe", "Z:" + tempFile.getAbsolutePath().replace("/", "\\"));
					p.inheritIO();
					p.start().waitFor();
					System.out.println("Sleeping 60 sec...");
					Thread.sleep(60000);
				} catch (IOException | InterruptedException ex) {
					Logger.getLogger(ScreenCaptureModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}).start();
		}
	}

	public void exit() {
		doneListener.fireEvent(null, null);
	}
}

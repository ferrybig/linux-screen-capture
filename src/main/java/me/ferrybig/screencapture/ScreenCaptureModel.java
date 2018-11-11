/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture;

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

/**
 *
 * @author fernando
 */
public class ScreenCaptureModel {
	private final List<WindowInfo> infoList;
	private Rectangle highlighted = null;
	private final BufferedImage background;
	private Point toolbarLocation = new Point();
	private Point mouseLocation = new Point();
	public final EventBus<Rectangle> listenersHighlighted = new EventBus();
	public final EventBus<Point> listenersMouseLocation = new EventBus();
	public final EventBus<Point> listenersToolbarLocation = new EventBus();
	public final EventBus<Void> doneListener = new EventBus();

	public Rectangle getHighlighted() {
		return highlighted;
	}

	public BufferedImage getBackground() {
		return background;
	}

	public Point getToolbarLocation() {
		return toolbarLocation;
	}

	public Point getMouseLocation() {
		return mouseLocation;
	}

	public ScreenCaptureModel(List<WindowInfo> infoList, BufferedImage background) {
		this.infoList = infoList;
		this.background = background;
	}

	private void recomputeActive() {
		Rectangle oldActive = this.highlighted;
		WindowInfo newActive = null;
		for (WindowInfo w : infoList) {
			if (w.contains(this.mouseLocation)) {
				newActive = w;
			}
		}
		if (newActive.getDimension() != oldActive) {
			this.setHighlighted(newActive.getDimension());
		}
	}

	public void setHighlighted(Rectangle newValue) {
		Rectangle old = this.highlighted;
		this.highlighted = newValue;
		this.listenersHighlighted.fireEvent(old, newValue);
	}

	public void setMouseLocation(Point newValue) {
		Point old = this.mouseLocation;
		this.mouseLocation = newValue;
		this.listenersMouseLocation.fireEvent(old, newValue);
		this.recomputeActive();
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
}


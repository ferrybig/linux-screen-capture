/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;
import me.ferrybig.screencapture.ScreenCaptureModel;

/**
 *
 * @author fernando
 */
public class MousePanel extends JPanel {

	private final ScreenCaptureModel model;
	private final Point offset;
	private final MouseAdapter listener = new MouseAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			MousePanel.this.model.setMouse(model.getMouse()
					.setPosition(new Point(offset.x + e.getX(), offset.y + e.getY())));
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			MousePanel.this.model.setMouse(model.getMouse()
					.setPosition(new Point(offset.x + e.getX(), offset.y + e.getY()))
					.setLeft(e.getButton() == MouseEvent.BUTTON1)
					.setRight(e.getButton() == MouseEvent.BUTTON2));
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			MousePanel.this.model.setMouse(model.getMouse()
					.setPosition(new Point(offset.x + e.getX(), offset.y + e.getY()))
					.setLeft(e.getButton() == MouseEvent.BUTTON1)
					.setRight(e.getButton() == MouseEvent.BUTTON2));
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			MousePanel.this.model.setMouse(model.getMouse()
					.setPosition(new Point(offset.x + e.getX(), offset.y + e.getY()))
					.setLeft(e.getButton() == MouseEvent.BUTTON1)
					.setRight(e.getButton() == MouseEvent.BUTTON2));
		}

		@Override
		public void mousePressed(MouseEvent e) {
			MousePanel.this.model.setMouse(model.getMouse()
					.setPosition(new Point(offset.x + e.getX(), offset.y + e.getY()))
					.setLeft(e.getButton() == MouseEvent.BUTTON1)
					.setRight(e.getButton() == MouseEvent.BUTTON2));
		}

	};

	public MousePanel(ScreenCaptureModel model, Point offset) {
		this.model = model;
		this.offset = offset;
		this.setOpaque(false);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
	}

}

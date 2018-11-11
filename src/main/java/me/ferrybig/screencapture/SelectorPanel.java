/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture;

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

/**
 *
 * @author fernando
 */
public class SelectorPanel extends JPanel {

	private final ScreenCaptureModel model;
	private final Point offset;
	private final MouseAdapter listener = new MouseAdapter() {
		@Override
		public void mouseMoved(MouseEvent e) {
			SelectorPanel.this.model.setMouseLocation(new Point(offset.x + e.getX(), offset.y + e.getY()));
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			model.continueSelection();
		}

	};

	public SelectorPanel(ScreenCaptureModel model, Point offset) {
		this.model = model;
		this.offset = offset;
		this.setOpaque(false);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
		this.model.listenersHighlighted.addListener(() -> {
			this.repaint();
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.model.getHighlighted() != null) {
			Rectangle geometry = this.model.getHighlighted();
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(Color.RED);
			g2.setStroke(new BasicStroke(5));
			g2.drawRect(geometry.x - offset.x, geometry.y - offset.y, geometry.width, geometry.height);
		}
	}

}

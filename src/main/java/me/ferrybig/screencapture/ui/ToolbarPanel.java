/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import javax.swing.JPanel;
import me.ferrybig.screencapture.ScreenCaptureModel;
import me.ferrybig.screencapture.events.MouseEvent;
import me.ferrybig.screencapture.events.MouseListener;
import me.ferrybig.screencapture.tools.ToolInfo;

/**
 *
 * @author fernando
 */
public class ToolbarPanel extends JPanel {

	private final ScreenCaptureModel model;
	private final Point offset = new Point(0, 0);
	private static final int WIDTH = 128;
	private static final int HEIGHT = 32;
	private int highligthedCell = -1;
	private final MouseListener listener = new MouseListener() {
		@Override
		public void mouseClick(MouseEvent evt) {
			Point newPoint = new Point(
					evt.getMouse().getPosition().x - offset.x,
					evt.getMouse().getPosition().y - offset.y
			);
			if (newPoint.y < ToolbarPanel.HEIGHT
					&& newPoint.y >= 0
					&& newPoint.x < ToolbarPanel.WIDTH
					&& newPoint.x >= 0) {
				evt.markConsumed();
				int index = newPoint.x / 32;
				if (index < model.getTools().size()) {
					model.setToolInfo(model.getTools().get(index));
				}
			}
		}

		@Override
		public void mouseMove(MouseEvent evt) {
			Point newPoint = new Point(
					evt.getMouse().getPosition().x - offset.x,
					evt.getMouse().getPosition().y - offset.y
			);
			if (newPoint.y < ToolbarPanel.HEIGHT
					&& newPoint.y >= 0
					&& newPoint.x < ToolbarPanel.WIDTH
					&& newPoint.x >= 0) {
				evt.markConsumed();
				int index = newPoint.x / 32;
				if (index < model.getTools().size()) {
					changeHighlightedCell(index);
				} else {
					changeHighlightedCell(-1);
				}
			} else {
				changeHighlightedCell(-1);
			}
		}

		@Override
		public void mousePress(MouseEvent evt) {

			Point newPoint = new Point(
					evt.getMouse().getPosition().x - offset.x,
					evt.getMouse().getPosition().y - offset.y
			);
			if (newPoint.y < ToolbarPanel.HEIGHT
					&& newPoint.y >= 0
					&& newPoint.x < ToolbarPanel.WIDTH
					&& newPoint.x >= 0) {
				evt.markConsumed();
			}
		}

		@Override
		public void mouseRelease(MouseEvent evt) {

			Point newPoint = new Point(
					evt.getMouse().getPosition().x - offset.x,
					evt.getMouse().getPosition().y - offset.y
			);
			if (newPoint.y < ToolbarPanel.HEIGHT
					&& newPoint.y >= 0
					&& newPoint.x < ToolbarPanel.WIDTH
					&& newPoint.x >= 0) {
				evt.markConsumed();
			}
		}
	};

	public ToolbarPanel(ScreenCaptureModel model) {
		this.model = model;
		this.setPreferredSize(new Dimension(128, 32));
		this.setOpaque(true);
		this.model.listenersMouseEvent.addListener(listener);
	}

	private void changeHighlightedCell(int highligthedCell) {
		if (this.highligthedCell != highligthedCell) {
			this.highligthedCell = highligthedCell;
			this.repaint();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		List<ToolInfo> tools = this.model.getTools();
		for (int i = 0; i < tools.size(); i++) {
			ToolInfo info = tools.get(i);
			g.drawImage(info.getIcon(), i * 32, 0, 32, 32, this);
		}
		if (this.highligthedCell != -1) {
			g.setColor(Color.BLACK);
			g.drawRect(this.highligthedCell * 32, 0, 32, 32);
		}
	}

}

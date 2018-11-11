/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author fernando
 */
public class PicturePanel extends JPanel {

	private final ScreenCaptureModel model;
	private final Point offset;

	public PicturePanel(ScreenCaptureModel model, Point offset) {
		this.model = model;
		this.offset = offset;
		this.setOpaque(true);
		this.setBackground(Color.GRAY);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.model.getBackground(), -offset.x, -offset.y, this);
	}


}

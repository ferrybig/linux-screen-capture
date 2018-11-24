/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture.ui;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author fernando
 */
public class ContainerPanel extends JPanel {

	private final Point offset;
	private final JComponent other;

	public ContainerPanel(Point offset, JComponent other) {
		this.offset = offset;
		this.other = other;
		Dimension d = other.getPreferredSize();
		other.setBounds(offset.x, offset.y, d.width, d.height);
		this.setLayout(null);
		this.add(other);
		this.setOpaque(false);
	}

}

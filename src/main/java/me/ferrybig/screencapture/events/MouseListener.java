/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture.events;

/**
 *
 * @author fernando
 */
public interface MouseListener {

	public void mouseClick(MouseEvent evt);

	public void mouseMove(MouseEvent evt);

	public void mousePress(MouseEvent evt);

	public void mouseRelease(MouseEvent evt);
}

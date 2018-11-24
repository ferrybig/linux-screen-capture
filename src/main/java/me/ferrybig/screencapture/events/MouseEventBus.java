/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture.events;

import java.awt.Point;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.ferrybig.screencapture.Mouse;

/**
 *
 * @author fernando
 */
public class MouseEventBus {

	private Mouse lastMouse;
	private long lastDown = 0;
	private Point lastDownLocation;
	private final List<MouseListener> listeners = new CopyOnWriteArrayList<>();

	public void updateMouse(Mouse newMouse) {
		if (this.lastMouse == null) {
			this.lastMouse = newMouse;
			return;
		}
		MouseEvent evt = new MouseEvent(newMouse);
		if (newMouse.getX() != lastMouse.getX() || newMouse.getY() != lastMouse.getY()) {
			this.fireMouseMoveEvent(evt);
		}

		if (newMouse.isLeft() && !lastMouse.isLeft()) {
			lastDown = System.currentTimeMillis();
			lastDownLocation = newMouse.getPosition();
			this.fireMousePressEvent(evt);
		}
		if (!newMouse.isLeft() && lastMouse.isLeft()) {
			long now = System.currentTimeMillis();
			this.fireMouseReleaseEvent(evt);
			if (now - 750 < lastDown && lastDownLocation.distanceSq(newMouse.getPosition()) < 10) {
				this.fireMouseClickEvent(evt);
			}
		}

		this.lastMouse = newMouse;
	}

	private void fireMouseClickEvent(MouseEvent evt) {
		listeners.forEach(e -> e.mouseClick(evt));
	}

	private void fireMouseMoveEvent(MouseEvent evt) {
		listeners.forEach(e -> e.mouseMove(evt));
	}

	private void fireMousePressEvent(MouseEvent evt) {
		listeners.forEach(e -> e.mousePress(evt));
	}

	private void fireMouseReleaseEvent(MouseEvent evt) {
		listeners.forEach(e -> e.mouseRelease(evt));
	}

	public boolean addListener(MouseListener e) {
		return listeners.add(e);
	}

	public boolean removeListener(MouseListener o) {
		return listeners.remove(o);
	}

}

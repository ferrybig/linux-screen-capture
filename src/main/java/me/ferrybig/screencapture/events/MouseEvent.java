/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture.events;

import java.util.Objects;
import me.ferrybig.screencapture.Mouse;

/**
 *
 * @author fernando
 */
public class MouseEvent {

	private final Mouse mouse;
	private boolean consumed = false;

	public MouseEvent(Mouse mouse) {
		this.mouse = mouse;
	}

	public Mouse getMouse() {
		return mouse;
	}

	@Override
	public String toString() {
		return "MouseEvent{" + "mouse=" + mouse + '}';
	}

	public boolean isConsumed() {
		return consumed;
	}

	public void markConsumed() {
		this.consumed = true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + Objects.hashCode(this.mouse);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MouseEvent other = (MouseEvent) obj;
		if (!Objects.equals(this.mouse, other.mouse)) {
			return false;
		}
		return true;
	}

}

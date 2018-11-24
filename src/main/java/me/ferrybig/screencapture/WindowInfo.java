/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Objects;

/**
 *
 * @author fernando
 */
public class WindowInfo {

	private final String name;
	private final Rectangle dimension;

	public WindowInfo(String name, Rectangle dimension) {
		this.name = name;
		this.dimension = dimension;
	}

	public String getName() {
		return name;
	}

	public Rectangle getDimension() {
		return dimension;
	}

	@Override
	public String toString() {
		return "WindowInfo{" + "name=" + name + ", dimension=" + dimension + '}';
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + Objects.hashCode(this.name);
		hash = 71 * hash + Objects.hashCode(this.dimension);
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
		final WindowInfo other = (WindowInfo) obj;
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.dimension, other.dimension)) {
			return false;
		}
		return true;
	}

	public boolean contains(Point p) {
		return dimension.contains(p);
	}

}

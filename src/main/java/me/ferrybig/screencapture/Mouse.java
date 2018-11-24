/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture;

import java.awt.Point;
import java.util.Objects;

/**
 *
 * @author fernando
 */
public class Mouse {

	private final Point position;
	private final boolean left;
	private final boolean right;

	public Mouse(Point position, boolean left, boolean right) {
		this.position = position;
		this.left = left;
		this.right = right;
	}

	public Mouse setLeft(boolean left) {
		return new Mouse(position, left, right);
	}

	public Mouse setRight(boolean right) {
		return new Mouse(position, left, right);
	}

	public Mouse setPosition(Point position) {
		return new Mouse(position, left, right);
	}

	public Point getPosition() {
		return position;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}

	public double getX() {
		return position.getX();
	}

	public double getY() {
		return position.getY();
	}

	@Override
	public String toString() {
		return "Mouse{" + "position=" + position + ", left=" + left + ", right=" + right + '}';
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 47 * hash + Objects.hashCode(this.position);
		hash = 47 * hash + (this.left ? 1 : 0);
		hash = 47 * hash + (this.right ? 1 : 0);
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
		final Mouse other = (Mouse) obj;
		if (this.left != other.left) {
			return false;
		}
		if (this.right != other.right) {
			return false;
		}
		if (!Objects.equals(this.position, other.position)) {
			return false;
		}
		return true;
	}
}

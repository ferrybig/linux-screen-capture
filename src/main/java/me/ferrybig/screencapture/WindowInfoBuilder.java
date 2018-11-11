/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture;

import java.awt.Rectangle;


public class WindowInfoBuilder {

	private String name;
	private Rectangle dimension;

	public WindowInfoBuilder() {
	}

	public WindowInfoBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public WindowInfoBuilder setDimension(Rectangle dimension) {
		this.dimension = dimension;
		return this;
	}

	public WindowInfo createWindowInfo() {
		return new WindowInfo(name, dimension);
	}

}

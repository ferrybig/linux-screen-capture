/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture.tools;

import java.awt.Image;
import java.util.function.Function;
import me.ferrybig.screencapture.ScreenCaptureModel;

/**
 *
 * @author fernando
 */
public class ToolInfo {

	private final Function<ScreenCaptureModel, Tool> tool;
	private final Image icon;
	private final String tooltip;

	public ToolInfo(Function<ScreenCaptureModel, Tool> tool, Image icon, String tooltip) {
		this.tool = tool;
		this.icon = icon;
		this.tooltip = tooltip;
	}

	public Function<ScreenCaptureModel, Tool> getTool() {
		return tool;
	}

	public Image getIcon() {
		return icon;
	}

	public String getTooltip() {
		return tooltip;
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture.tools;

import java.awt.Rectangle;
import me.ferrybig.screencapture.ScreenCaptureModel;
import me.ferrybig.screencapture.WindowInfo;
import me.ferrybig.screencapture.events.MouseEvent;
import me.ferrybig.screencapture.events.MouseListener;

/**
 *
 * @author fernando
 */
public class ExitTool implements Tool {

	public ExitTool(ScreenCaptureModel model) {
		model.exit();
	}

	@Override
	public void close() {
	}
}

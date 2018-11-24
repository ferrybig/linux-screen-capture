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
public class WindowSelectTool implements Tool, MouseListener {

	private final ScreenCaptureModel model;

	public WindowSelectTool(ScreenCaptureModel model) {
		this.model = model;
		this.model.listenersMouseEvent.addListener(this);
	}

	@Override
	public void close() {
		this.model.listenersMouseEvent.removeListener(this);
	}

	private void recomputeActive() {
		Rectangle oldActive = this.model.getHighlighted();
		WindowInfo newActive = null;
		for (WindowInfo w : model.getInfoList()) {
			if (w.contains(this.model.getMouse().getPosition())) {
				newActive = w;
			}
		}
		if (newActive.getDimension() != oldActive) {
			this.model.setHighlighted(newActive.getDimension());
		}
	}

	@Override
	public void mouseClick(MouseEvent evt) {
		if (evt.isConsumed()) {
			return;
		}
		this.model.continueSelection();
	}

	@Override
	public void mouseMove(MouseEvent evt) {
		if (evt.isConsumed()) {
			return;
		}
		this.recomputeActive();
	}

	@Override
	public void mousePress(MouseEvent evt) {
	}

	@Override
	public void mouseRelease(MouseEvent evt) {
	}

}

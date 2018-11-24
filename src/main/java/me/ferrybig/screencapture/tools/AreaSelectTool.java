/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture.tools;

import java.awt.Point;
import java.awt.Rectangle;
import me.ferrybig.screencapture.ScreenCaptureModel;
import me.ferrybig.screencapture.WindowInfo;
import me.ferrybig.screencapture.events.MouseEvent;
import me.ferrybig.screencapture.events.MouseListener;

/**
 *
 * @author fernando
 */
public class AreaSelectTool implements Tool, MouseListener {

	private final ScreenCaptureModel model;
	private Point selectionStart = null;

	public AreaSelectTool(ScreenCaptureModel model) {
		this.model = model;
		this.model.listenersMouseEvent.addListener(this);
		this.model.setHighlighted(null);
	}

	@Override
	public void close() {
		this.model.listenersMouseEvent.removeListener(this);
	}

	private void makeSelection(Point other) {
		if (this.selectionStart == null) {
			return;
		}
		int x = Math.min(this.selectionStart.x, other.x);
		int y = Math.min(this.selectionStart.y, other.y);
		this.model.setHighlighted(new Rectangle(
				x,
				y,
				Math.max(this.selectionStart.x, other.x) - x,
				Math.max(this.selectionStart.y, other.y) - y
		));
	}

	@Override
	public void mouseClick(MouseEvent evt) {
		if (evt.isConsumed()) {
			return;
		}
		if (evt.getMouse().isRight()) {
			this.selectionStart = null;
		}
		this.model.continueSelection();
	}

	@Override
	public void mouseMove(MouseEvent evt) {
		if (evt.isConsumed()) {
			return;
		}
		if (evt.getMouse().isRight()) {
			this.selectionStart = null;
		}
		this.makeSelection(evt.getMouse().getPosition());
	}

	@Override
	public void mousePress(MouseEvent evt) {
		if (evt.isConsumed()) {
			return;
		}
		if (evt.getMouse().isRight()) {
			this.selectionStart = null;
		}
		this.selectionStart = evt.getMouse().getPosition();
		this.makeSelection(evt.getMouse().getPosition());
	}

	@Override
	public void mouseRelease(MouseEvent evt) {
		if (evt.isConsumed()) {
			return;
		}
		if (evt.getMouse().isRight()) {
			this.selectionStart = null;
		}
		if (this.selectionStart == null) {
			return;
		}
		Rectangle selection = this.model.getHighlighted();
		if (selection.width > 10 && selection.height > 10) {
			this.model.continueSelection();
		}
		this.model.setHighlighted(null);

	}
}

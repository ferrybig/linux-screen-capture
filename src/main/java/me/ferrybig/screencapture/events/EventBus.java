/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.ferrybig.screencapture.events;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author fernando
 */
public class EventBus<T> {

	private final List<Runnable> listeners = new CopyOnWriteArrayList<>();

	public void fireEvent(T oldVal, T newVal) {
		listeners.forEach(Runnable::run);
	}

	public boolean addListener(Runnable e) {
		return listeners.add(e);
	}

	public boolean removeListener(Runnable o) {
		return listeners.remove(o);
	}
}

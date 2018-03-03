package cn.edu.jxau.lang;

import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

public class Button {

	private Set<EventListener> set;

	public Button() {
		set = new HashSet<>();
	}

	public void addEventListener(EventListener listener) {
		if (listener == null) {
			return;
		}
		set.add(listener);
	}

	public void click() {
		notify(ClickListener.class);
	}

	public void doubleclick() {
		notify(DoubleClickListener.class);
	}

	public void any() {
		notify(null);
	}

	private void notify(Class<? extends EventListener> clazz) {

		if (clazz == null) {
			for (EventListener listener : set) {
				if (listener instanceof ClickListener) {
					((ClickListener) listener).action(new ClickEvent(this, "click"));
				} else if (listener instanceof DoubleClickListener) {
					((DoubleClickListener) listener).action(new DoubleClickEvent(this, "double click"));
				}
			}
			return;
		}
		for (EventListener listener : set) {
			if (clazz.isInstance(listener) && listener instanceof ClickListener) {
				((ClickListener) listener).action(new ClickEvent(this, "click"));
			} else if (clazz.isInstance(listener) && listener instanceof DoubleClickListener) {
				((DoubleClickListener) listener).action(new DoubleClickEvent(this, "double click"));
			}
		}
	}
}

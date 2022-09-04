package cn.rainbow.oxygen.event.events;

import cn.rainbow.oxygen.event.Event;

public class EventClickMouse extends Event {
	
	public int button;
	
	public EventClickMouse(int button) {
        this.button = button;
    }

}

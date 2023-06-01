package cn.rainbow.oxygen.event.events;

import cn.rainbow.oxygen.event.Event;

public class ClickMouseEvent extends Event {
	
	public int button;
	
	public ClickMouseEvent(int button) {
        this.button = button;
    }

}

package me.Oxygen.event.events;

import me.Oxygen.event.Event;

public class EventChat extends Event {
	private boolean cancelled;
	private String message;

	public EventChat(String message) {
		this.message = message;
		this.cancelled = false;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

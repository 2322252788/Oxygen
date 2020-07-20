package me.Oxygen.event.events;

import me.Oxygen.event.Event;

public class EventJump extends Event {
	
	private boolean cancelled;
	
	private float motion;
	
	public EventJump(float motion) {
		this.motion = motion;
	}

    public final float getMotion() {
        return this.motion;
    }

    public final void setMotion(float f) {
        this.motion = f;
    }

	public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}

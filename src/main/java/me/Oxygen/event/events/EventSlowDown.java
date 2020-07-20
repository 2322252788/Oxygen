package me.Oxygen.event.events;

import me.Oxygen.event.Event;

public class EventSlowDown extends Event {
	
	private float strafe;
    private float forward;
    
	public EventSlowDown() {
		
	}
	
	public EventSlowDown(float strafe, float forward) {
        this.strafe = strafe;
        this.forward = forward;
    }
	
	public final float getStrafe() {
        return this.strafe;
    }

    public final void setStrafe(float f) {
        this.strafe = f;
    }

    public final float getForward() {
        return this.forward;
    }

    public final void setForward(float f) {
        this.forward = f;
    }
    
}

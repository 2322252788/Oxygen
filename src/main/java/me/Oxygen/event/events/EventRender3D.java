package me.Oxygen.event.events;

import me.Oxygen.event.Event;

public class EventRender3D extends Event {

	public float particlTicks;

	public EventRender3D(float particlTicks) {
		this.particlTicks = particlTicks;
	}

    public float getPartialTicks() {
        return this.particlTicks;
    }
}

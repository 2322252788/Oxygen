package cn.rainbow.oxygen.event.events;

import cn.rainbow.oxygen.event.Event;

public class EventStrafe extends Event {

    private final float strafe;
    private final float forward;
    private final float friction;

    public EventStrafe(final float strafe, final float forward, final float friction){
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
    }

    public float getStrafe() {
        return strafe;
    }

    public float getForward() {
        return forward;
    }

    public float getFriction() {
        return friction;
    }
}

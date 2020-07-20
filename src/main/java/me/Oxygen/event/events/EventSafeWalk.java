package me.Oxygen.event.events;

import me.Oxygen.event.Event;

public class EventSafeWalk extends Event
{
    public boolean safe;
    
    public EventSafeWalk(final boolean safe) {
        this.safe = safe;
    }
    
    public void setSafe(final boolean safe) {
        this.safe = safe;
    }
    
    public boolean getSafe() {
        return this.safe;
    }
}

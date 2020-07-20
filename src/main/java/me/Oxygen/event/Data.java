package me.Oxygen.event;

import java.lang.reflect.Method;

/**
 * Oxygen & Leakey Hacked Client
 * 
 * Event API
 * 
 * @author Pipi
 */
public class Data {

    private Object source;
    private Method target;
    private EventPriority priority;

    public Data(Object source, Method target, EventPriority priority) {
        this.source = source;
        this.target = target;
        this.priority = priority;
    }

    public Object getSource() {
        return source;
    }

    public Method getTarget() {
        return target;
    }

    public EventPriority getPriority() {
        return priority;
    }
}

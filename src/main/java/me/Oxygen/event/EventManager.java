package me.Oxygen.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Oxygen & Leakey Hacked Client
 * 
 * Event API
 * 
 * @author Pipi
 */
public class EventManager {

    private HashMap<Class<? extends Event>, CopyOnWriteArrayList<Data>> REGISTRY_MAP;

    public EventManager() {
        REGISTRY_MAP = new HashMap<Class<? extends Event>, CopyOnWriteArrayList<Data>>();
    }
    
    public void register(Object o) {
        Arrays.stream(o.getClass().getDeclaredMethods()).forEach(method -> {
            if (!isMethodBad(method)) {
            	Data methodData = new Data(o, method, method.getAnnotation(EventTarget.class).priority());
            	
            	if (!methodData.getTarget().isAccessible()) {
            		methodData.getTarget().setAccessible(true);
            	}
            	
            	List<Class<? extends Event>> events = EventManager.getEvents(o);        	

        		for (Class<? extends Event> clazz : events) {	
        			if (REGISTRY_MAP.containsKey(clazz)) {
        	            if (!REGISTRY_MAP.get(clazz).contains(methodData))
        	                REGISTRY_MAP.get(clazz).add(methodData);
        	        } else {
        	            REGISTRY_MAP.put(clazz, new CopyOnWriteArrayList<Data>(Collections.singletonList(methodData)));
        	        }
        		}
            }
        });
        
        REGISTRY_MAP.values().forEach(flexibleArray -> flexibleArray.sort(((o1, o2) -> (o1.getPriority().getValue() - o2.getPriority().getValue()))));
    }

    public void unregister(Object o) {      
        List<Class<? extends Event>> events = this.getEvents(o);
        for (Class<? extends Event> event : events) {
            if (this.isEventRegistered(event)) {
            	REGISTRY_MAP.values().forEach(flexibleArray -> flexibleArray.removeIf(methodData -> methodData.getSource().equals(o)));
                REGISTRY_MAP.entrySet().removeIf(hashSetEntry -> hashSetEntry.getValue().isEmpty());
            }
        }
    }

    private boolean isMethodBad(Method method) {
        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
    }
    
    private boolean isEventRegistered(Object event) {
        return this.REGISTRY_MAP.containsKey(event);
    }
    
	private static List<Class<? extends Event>> getEvents(Object event) {
		ArrayList<Class<? extends Event>> events = new ArrayList<Class<? extends Event>>();
		Method[] methods = event.getClass().getDeclaredMethods();
		int var1 = methods.length;

		for (int var2 = 0; var2 < var1; ++var2) {
			Method method = methods[var2];
			if (method.isAnnotationPresent(EventTarget.class)) {
				EventTarget ireg = method.getAnnotation(EventTarget.class);
				Class[] methodEvent = ireg.events();
				int i = methodEvent.length;
				for (int var3 = 0; var3 < i; ++var3) {
					Class eventClass = methodEvent[var3];
					events.add((Class<? extends Event>) eventClass);
				}
			}
		}

		return events;
	}

    public CopyOnWriteArrayList<Data> get(Class<? extends Event> clazz) {
        return REGISTRY_MAP.get(clazz);
    }

}

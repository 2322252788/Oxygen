package cn.rainbow.oxygen.event;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {

	private static final HashMap<Class<? extends Event>, CopyOnWriteArrayList<Data>> REGISTRY_MAP = new HashMap<>();

	public static void register(Object o) {
		for (final Method method : o.getClass().getDeclaredMethods()) {
			if (isTargetMethod(method)) {
				EventTarget eventTarget = method.getAnnotation(EventTarget.class);
				if (eventTarget.events().length == 0) continue;

				Data data = new Data(o, method, eventTarget.priority());
				data.getTarget().setAccessible(true);

				for (Class<? extends Event> eventClass: eventTarget.events()) {
					if (REGISTRY_MAP.containsKey(eventClass)) {
						if (!REGISTRY_MAP.get(eventClass).contains(data))
							REGISTRY_MAP.get(eventClass).add(data);
					} else {
						REGISTRY_MAP.put(eventClass, new CopyOnWriteArrayList<>(Collections.singletonList(data)));
					}
				}
			}
		}
		REGISTRY_MAP.values().forEach(
				data -> data.sort((Comparator.comparingInt(o2 -> o2.getPriority().getValue())))
		);
	}

	public static void unregister(Object o) {
		List<Class<? extends Event>> events = getEvents(o);
		for (Class<? extends Event> event : events) {
			if (isEventRegistered(event)) {
				REGISTRY_MAP.values().forEach(flexibleArray -> flexibleArray.removeIf(methodData -> methodData.getSource().equals(o)));
			}
		}
	}

	private static boolean isTargetMethod(Method method) {
		return method.getParameterTypes().length == 1 && method.isAnnotationPresent(EventTarget.class);
	}

	private static boolean isEventRegistered(Object event) {
		return REGISTRY_MAP.containsKey(event);
	}

	private static List<Class<? extends Event>> getEvents(Object event) {
		ArrayList<Class<? extends Event>> events = new ArrayList<>();
		for (Method method: event.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(EventTarget.class)) {
				EventTarget eventTarget = method.getAnnotation(EventTarget.class);
				if (eventTarget.events().length == 0) continue;
				events.addAll(Arrays.asList(eventTarget.events()));
			}
		}
		return events;
	}

	public static CopyOnWriteArrayList<Data> get(Class<? extends Event> clazz) {
		return REGISTRY_MAP.get(clazz);
	}

}

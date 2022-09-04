package cn.rainbow.oxygen.event.events;

import cn.rainbow.oxygen.event.Event;
import net.minecraft.entity.Entity;

public class EventAttack extends Event {
	private Entity ent;
	
	public EventAttack(Entity e){	
		this.ent = e;
	}
	public Entity getEntity(){
		return this.ent;
	}
}

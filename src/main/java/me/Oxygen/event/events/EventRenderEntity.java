package me.Oxygen.event.events;

import me.Oxygen.event.Event;
import net.minecraft.entity.EntityLivingBase;

public class EventRenderEntity extends Event {

    private EntityLivingBase entity;
    
    private RenderType type;

    public EventRenderEntity(EntityLivingBase entity, RenderType type) {
        this.entity = entity;
        this.type = type;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public boolean isPre() {
        if(type == RenderType.PRE) {
        	return true;
        }
        return false;
    }

    public boolean isPost() {
    	if(type == RenderType.POST) {
        	return true;
        }
        return false;
    }
    
    public static enum RenderType{
    	PRE, POST
    }

}

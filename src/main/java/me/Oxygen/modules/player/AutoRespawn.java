package me.Oxygen.modules.player;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventPriority;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventTick;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;

@ModuleRegister(name = "AutoRespawn", category = Category.PLAYER)
public class AutoRespawn extends Module{
	
	@EventTarget(priority = EventPriority.HIGH, events = EventTick.class)
    public void onEvent(Event event) {
		if(event instanceof EventTick) {
        if (!this.mc.thePlayer.isEntityAlive()) 
           mc.thePlayer.respawnPlayer();
		}
     }

}

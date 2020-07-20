package me.Oxygen.modules.movement;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;

@ModuleRegister(name = "Sprint", category = Category.MOVEMENT)
public class Sprint extends Module {
	
	@EventTarget(events = {EventUpdate.class})
	public void onEvent(Event event) {
		if(event instanceof EventUpdate) {
			if ((mc.thePlayer.moveForward > 0.0F) && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally) {
				if(mc.thePlayer.moveForward <= 0.0F && mc.thePlayer.isCollidedVertically){
				mc.thePlayer.motionX *= 1.185;
				mc.thePlayer.motionZ *= 1.185;
			}
			mc.thePlayer.setSprinting(true);
		} else{
			mc.thePlayer.setSprinting(false);
		}	
		}
	}
	
}

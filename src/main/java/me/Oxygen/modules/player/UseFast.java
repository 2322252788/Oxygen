package me.Oxygen.modules.player;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventMotion.MotionType;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleRegister(name = "UseFast", category = Category.PLAYER)
public class UseFast extends Module {
	
	private boolean canBoost;
	
	@EventTarget(events = EventMotion.class)
	private final void onUpdate(Event event) {
		if(event instanceof EventMotion) {
			EventMotion em = (EventMotion)event;
		if(em.getMotionType() == MotionType.PRE) {
		Item usingItem = mc.thePlayer.getItemInUse().getItem();
		if (usingItem instanceof ItemFood 
				|| usingItem instanceof ItemBucketMilk 
				|| usingItem instanceof ItemPotion){
			if (mc.thePlayer.getItemInUseDuration() == 16) {
		         this.canBoost = true;
		      }

		      if (mc.thePlayer.onGround && this.canBoost && !(mc.thePlayer.getItemInUse().getItem() instanceof ItemBow)) {
		         this.canBoost = false;

		         for(int i = 0; i < 20; ++i) {
		            this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
		         }
		      }
		}
	}
    
    if (!mc.thePlayer.isUsingItem()) {
        return;
    }
		}
	}

}

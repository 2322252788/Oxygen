package me.Oxygen.modules.movement;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.event.events.EventMotion.MotionType;
import me.Oxygen.injection.interfaces.IC03PacketPlayer;
import me.Oxygen.injection.interfaces.IEntity;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.value.Value;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;

@ModuleRegister(name = "NoFall", category = Category.MOVEMENT)
public class NoFall extends Module {
	
	private Value<String> mode = new Value<String>("NoFall", "Mode", 0);
	
	public NoFall() {
		this.mode.mode.add("Hypixel");
		this.mode.mode.add("Hypixel2");
		this.mode.mode.add("Hypixel3");
		this.mode.addValue("SpoofGround");
	}
	
	@EventTarget(events = {EventMotion.class, EventPacket.class})
	private final void onEvent(final Event event) {
		if(event instanceof EventMotion) {
			EventMotion em = (EventMotion)event;
		if(em.getMotionType() == MotionType.PRE) {
		this.setDisplayName(mode.getModeAt(mode.getCurrentMode()));
		switch(mode.getModeAt(mode.getCurrentMode())) {
		
		case "Hypixel" :
			if (mc.thePlayer.motionY < 0.0D && mc.thePlayer.fallDistance > 2D && !mc.thePlayer.isCollidedVertically) {

				if (mc.thePlayer.fallDistance <= 20.0F) {
					em.setGround(true);
				}

				if (mc.thePlayer.fallDistance <= 20.0F || mc.thePlayer.ticksExisted % 3 == 0) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
				}
			}
			
			break;
			
		case "Hypixel2" :
			if (this.mc.thePlayer.onGround) {
			} 
		      if (this.mc.thePlayer.fallDistance > 2.9F && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isInLava())
		      {
		        
		        em.setGround(true);
		      }
		      
			break;
			
		case "Hypixel3" :
			if (!(mc.thePlayer.fallDistance > 3.0f) || !this.isBlockUnder()  || mc.thePlayer.posY % 0.0625 == 0.0 && mc.thePlayer.posY % 0.015256 == 0.0) break;
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
            mc.thePlayer.fallDistance = 0.0f;
            
			break;
			
		}
		}
		if(em.getMotionType() == MotionType.POST) {
			if (this.mode.isCurrentMode("Hypixel3") && mc.thePlayer.fallDistance > 3.0f && this.isBlockUnder()  && (mc.thePlayer.posY % 0.0625 != 0.0 || mc.thePlayer.posY % 0.015256 != 0.0)) {
	            mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
	            mc.thePlayer.fallDistance = 0.0f;
	        }
		}
		}
		if(event instanceof EventPacket) {
			EventPacket ep = (EventPacket)event;
			final Packet<?> packet = ep.getPacket();
	        if(packet instanceof C03PacketPlayer && mode.isCurrentMode("SpoofGround") && mc.thePlayer.fallDistance > 3) {
	        	((IC03PacketPlayer) packet).setOnGround(true);
	        }
		}
	}
	
	private final boolean isBlockUnder() {
	      if(mc.thePlayer.posY < 0.0D) {
	         return false;
	      } else {
	         int off = 0;

	         while(true) {
	            if(off >= (int)mc.thePlayer.posY + 2) {
	               return false;
	            }

	            AxisAlignedBB bb = ((IEntity)mc.thePlayer).getBoundingBox().offset(0.0D, (double)(-off), 0.0D);
	            if(!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
	               return true;
	            }

	            off += 2;
	         }
	      }
	   }
	
}

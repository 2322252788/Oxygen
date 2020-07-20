package me.Oxygen.modules.combat;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.event.events.EventPacket.PacketType;
import me.Oxygen.injection.interfaces.IS12PacketEntityVelocity;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.value.Value;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

@ModuleRegister(name = "Velocity", category = Category.COMBAT)
public class Velocity extends Module {

	public Value<String> mode = new Value<String>("Velocity", "Mode", 0);
	private Value<Double> horizontal = new Value<Double>("Velocity_Horizontal", 0.03, 0.0, 1.0, 0.01);
	private Value<Double> vertical = new Value<Double>("Velocity_Vertical", 0.02, 0.0, 1.0, 0.01);
	private Value<Boolean> hyt = new Value<Boolean>("Velocity_HuaYuTing",false);
	private Value<Boolean> explosion = new Value<Boolean>("Explosion",false);

    public Velocity() {
    	this.mode.addValue("Normal");
    	this.mode.addValue("Simple");
    	this.mode.addValue("AAC");
    }

    @EventTarget(events = EventPacket.class)
    public void onPacketReceive(Event event) {
    	if(event instanceof EventPacket) {
    	this.setDisplayName(mode.getModeAt(mode.getCurrentMode()));
    	EventPacket ep = (EventPacket)event;
    	final Packet<?> packet = ep.getPacket();
    	
    	if (packet instanceof S12PacketEntityVelocity && mc.thePlayer != null && mc.theWorld != null && this.mode.isCurrentMode("Simple")) {
    		final S12PacketEntityVelocity packetEntityVelocity = (S12PacketEntityVelocity)packet;
            if (mc.theWorld.getEntityByID(packetEntityVelocity.getEntityID()) == mc.thePlayer) {
            		final double horizontal = this.horizontal.getValueState();
                    final double vertical = this.vertical.getValueState();
                    if (horizontal == 0.0 && vertical == 0.0) {
                        ep.setCancelled(true);
                    }
                    ((IS12PacketEntityVelocity)packetEntityVelocity).setX((int)(packetEntityVelocity.getMotionX() * horizontal));
                    ((IS12PacketEntityVelocity)packetEntityVelocity).setY((int)(packetEntityVelocity.getMotionY() * vertical));
                    ((IS12PacketEntityVelocity)packetEntityVelocity).setZ((int)(packetEntityVelocity.getMotionZ() * horizontal));
            	
            }
    	}

    	if (hyt.getValueState() && packet instanceof S12PacketEntityVelocity){
			final S12PacketEntityVelocity packetEntityVelocity = (S12PacketEntityVelocity)packet;
			((IS12PacketEntityVelocity)packetEntityVelocity).setX((int)(packetEntityVelocity.getMotionX() * 40 * 0.01));
			((IS12PacketEntityVelocity)packetEntityVelocity).setY((int)(packetEntityVelocity.getMotionY() * 40 * 0.01));
			((IS12PacketEntityVelocity)packetEntityVelocity).setZ((int)(packetEntityVelocity.getMotionZ() * 40 * 0.01));
		}

			if(explosion.getValueState() && (packet instanceof S27PacketExplosion)) {
				ep.setCancelled(true); // No anticheat detects antiknockback with TNT
			}
    	
    	if (packet instanceof S27PacketExplosion && this.mode.isCurrentMode("Simple")) {
            ep.setCancelled(true);
        }
    	
    	if(ep.getPacketType() == PacketType.Recieve) {
        if (this.mode.isCurrentMode("Normal")) {
        	if (ep.getPacket() instanceof S12PacketEntityVelocity || ep.getPacket() instanceof S27PacketExplosion) {
        		ep.setCancelled(true);
        	}            
        }    
		if(this.mode.isCurrentMode("AAC")) {
			if (this.mc.thePlayer.hurtTime > 0) {
				mc.thePlayer.motionX *= 0.6;
                mc.thePlayer.motionZ *= 0.6;
            } else {
            	mc.thePlayer.motionX *= 1.0;
                mc.thePlayer.motionZ *= 1.0;
            }
		}
    }
}
    }
}

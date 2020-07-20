package me.Oxygen.modules.movement;

import me.Oxygen.Oxygen;
import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventJump;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventMove;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.injection.interfaces.*;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.value.Value;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@ModuleRegister(name = "HYTLongJump", category = Category.MOVEMENT)
public class HYTLongJump extends Module {
	private final Value<String> modeValue = new Value<String>("HYTLongJump", "Mode", 0);
    private final Value<Double> lagPowerValue = new Value<Double>("HYTLongJump_LagPower", 20.0, 5.0, 30.0, 1.0);
    private final Value<Double> newLagPowerValue = new Value<Double>("HYTLongJump_NewLagPower", 20.0, 5.0, 50.0, 1.0);
    private final Value<Double> timeValue = new  Value<Double>("HYTLongJump_Time", 100.0, 100.0, 1000.0, 100.0);
    private final Value<Boolean> untime = new Value<Boolean>("HYTLongJump_UnTime", false);
    private boolean vc = false;
    private final List<Packet> packets = new ArrayList<>();
    private final LinkedList<double[]> positions = new LinkedList<>();
    private boolean disableLogger;
  
    private boolean jumped;
    private boolean canBoost;
    
    public HYTLongJump() {
		this.modeValue.addValue("Lga");
		this.modeValue.addValue("NewLga");
	}
    
    @Override
    public void onEnable() {
    	if(this.untime.getValueState()) { 
    		 new Thread(new Runnable() {
    	            @Override
    	            public void run() {
    	                try {
    	                	vc = true;
    	                    Thread.sleep((long) timeValue.getValueState().floatValue());
    	                    if(Oxygen.INSTANCE.ModMgr.getModuleByName("HYTLongJump").isEnabled() && vc) {
    	                    	Oxygen.INSTANCE.ModMgr.getModuleByName("HYTLongJump").set(false);
    	                    	}
    	                    Thread.currentThread().interrupt();
    	                } catch (InterruptedException ex) {
    	                    ex.printStackTrace();
    	                }
    	            }
    	        }).start();
    	};      
        synchronized (positions) {
            positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + (mc.thePlayer.getEyeHeight() / 2), mc.thePlayer.posZ});
            positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
    	vc = false;
        ((IMinecraft)mc).getTimer().timerSpeed = 1F;
        if (mc.thePlayer == null)
            return;
        blink();
        super.onDisable();
    }

    @EventTarget(events = {EventMove.class, EventJump.class, EventPacket.class,
    		EventMotion.class, EventUpdate.class})
    private final void onEvent(final Event e) {
    	if(e instanceof EventMove) {
    		EventMove em = (EventMove)e;
        if(modeValue.isCurrentMode("NewLga")){
            if (!isMoving() && jumped) {
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
                em.setX(0.0);
                em.setZ(0.0);
            }
        }
    }
    	if(e instanceof EventJump) {
    		if(modeValue.isCurrentMode("NewLga")){
                jumped = true;
                canBoost = true;
            }
    	}
    	if(e instanceof EventPacket) {
    		EventPacket ep = (EventPacket)e;
    		final Packet<?> packet = ep.getPacket();

            if (mc.thePlayer == null || disableLogger)
                return;

            if (packet instanceof C03PacketPlayer) // Cancel all movement stuff
                ep.setCancelled(false);

            if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook ||
                    packet instanceof C08PacketPlayerBlockPlacement ||
                    packet instanceof C0APacketAnimation ||
                    packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity) {
            	ep.setCancelled(true);

                packets.add(packet);
            }
    	}
    	
    	if(e instanceof EventMotion) {
    		if(modeValue.isCurrentMode("Lga")){
                if (isMoving()) {
                    ((IMinecraft)mc).getTimer().timerSpeed = 1.0F;

                    if (mc.thePlayer.onGround) {
                        strafe(lagPowerValue.getValueState().floatValue());
                        mc.thePlayer.motionY = 0.42F;
                    }
                    strafe(lagPowerValue.getValueState().floatValue());
                } else {
                    mc.thePlayer.motionX = mc.thePlayer.motionZ = 0D;
                }
            }
    	}
    	if(e instanceof EventUpdate) {
    		synchronized (positions) {
                positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
            }

            if(modeValue.isCurrentMode("NewLga")){
                if (jumped) {
                    if (mc.thePlayer.onGround || mc.thePlayer.capabilities.isFlying) {
                        jumped = false;

                        mc.thePlayer.motionX = 0;
                        mc.thePlayer.motionZ = 0;
                        return;
                    }
                    strafe(getSpeed() * (canBoost ? newLagPowerValue.getValueState().floatValue() : 1F));
                    canBoost = false;
                }

                if(mc.thePlayer.onGround && isMoving()) {
                    jumped = true;
                    mc.thePlayer.jump();
                }
            }
    	}
   }
    
    private final boolean isMoving() {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F);
    }
    
    private final float getSpeed() {
        return (float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
    }
    
    private final void strafe(final float speed) {
        if(!isMoving())
            return;

        final double yaw = getDirection();
        mc.thePlayer.motionX = -Math.sin(yaw) * speed;
        mc.thePlayer.motionZ = Math.cos(yaw) * speed;
    }
    
    private final double getDirection() {
        float rotationYaw = mc.thePlayer.rotationYaw;

        if(mc.thePlayer.moveForward < 0F)
            rotationYaw += 180F;

        float forward = 1F;
        if(mc.thePlayer.moveForward < 0F)
            forward = -0.5F;
        else if(mc.thePlayer.moveForward > 0F)
            forward = 0.5F;

        if(mc.thePlayer.moveStrafing > 0F)
            rotationYaw -= 90F * forward;

        if(mc.thePlayer.moveStrafing < 0F)
            rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    private final void blink() {
        try {
            disableLogger = true;

            final Iterator<Packet> packetIterator = packets.iterator();
            for (; packetIterator.hasNext(); ) {
                mc.getNetHandler().addToSendQueue(packetIterator.next());
                packetIterator.remove();
            }

            disableLogger = false;
        } catch (final Exception e) {
            e.printStackTrace();
            disableLogger = false;
        }

        synchronized (positions) {
            positions.clear();
        }
    }


    
}

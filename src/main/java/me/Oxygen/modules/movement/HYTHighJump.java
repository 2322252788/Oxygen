package me.Oxygen.modules.movement;

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

import me.Oxygen.Oxygen;
import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventJump;
import me.Oxygen.event.events.EventKey;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.modules.combat.Velocity;
import me.Oxygen.utils.other.MoveUtil;
import me.Oxygen.value.Value;

@ModuleRegister(name = "HYTHighJump", category = Category.MOVEMENT)
public class HYTHighJump extends Module {
    
	private final List<Packet> packets = new ArrayList<>();
    private final LinkedList<double[]> positions = new LinkedList<>();
    private boolean disableLogger;

    private final Value<String> modeValue = new Value<String>("HYTHighJump", "Mode", 0);
    private final Value<Double> bugPowerValue = new Value<Double>("HYTHighJump_BugPower", 5.0, 3.0, 10.0,1.0);
    private final Value<Double> lagPowerValue = new Value<Double>("HYTHighJump_FlagPower", 5.0, 3.0, 10.0,1.0);
    private final Value<Boolean> velocity = new Value<Boolean>("HYTHighJump_VelocityCheck", false);
    
    public HYTHighJump() {
		this.modeValue.addValue("Lga");
		this.modeValue.addValue("Bug");
	}
    @Override
    public void onDisable() {
        if(modeValue.isCurrentMode("Lga")){
            if(mc.thePlayer == null)
                return;

            blink();
        }
        super.onDisable();
    }
    
    @EventTarget(events = {EventPacket.class, EventJump.class, EventKey.class, EventUpdate.class})
    private final void onEvent(Event e) {
    	if(e instanceof EventPacket) {
    		EventPacket ep = (EventPacket)e;
    		if(modeValue.isCurrentMode("Lga")){
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
    	}
    	if(e instanceof EventJump) {
    		EventJump ej = (EventJump)e;
    		if(modeValue.isCurrentMode("Lga")){
    			
    			if(velocity.getValueState() && Oxygen.INSTANCE.ModMgr.getModule(Velocity.class).isEnabled()) {
    				Oxygen.INSTANCE.ModMgr.getModule(Velocity.class).set(false);
    				this.tellPlayer("检测到Velocity开启，已自动关闭", "HYTHighJump");
    			}
    			
                ej.setMotion(ej.getMotion() * lagPowerValue.getValueState().floatValue());

            }
    	}
    	if(e instanceof EventKey) {
    		EventKey ek = (EventKey)e;
    		if(modeValue.isCurrentMode("Bug")){
                if (ek.getKey() == 57) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + bugPowerValue.getValueState().floatValue(), mc.thePlayer.posZ, true));
                    MoveUtil.forward(0.04D);
                }
            }
    	}
    	if(e instanceof EventUpdate) {
    		if(modeValue.isCurrentMode("Lga")){
                synchronized(positions) {
                    positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
                }
            }
    	}
    }

    @Override
    public void onEnable() {
        if(modeValue.isCurrentMode("Lga")){
            synchronized (positions) {
                positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + (mc.thePlayer.getEyeHeight() / 2), mc.thePlayer.posZ});
                positions.add(new double[]{mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
            }
        }

        if(modeValue.isCurrentMode("Bug")){
            this.tellPlayer("请按下空格开启高跳", "HYTHighJump");
        }
        super.onEnable();
    }

    private final void blink() {
        try {
            disableLogger = true;

            final Iterator<Packet> packetIterator = packets.iterator();
            for(; packetIterator.hasNext(); ) {
                mc.getNetHandler().addToSendQueue(packetIterator.next());
                packetIterator.remove();
            }

            disableLogger = false;
        }catch(final Exception e) {
            e.printStackTrace();
            disableLogger = false;
        }

        synchronized(positions) {
            positions.clear();
        }
    }

}

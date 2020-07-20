package me.Oxygen.modules.world;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventMotion.MotionType;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.event.events.EventPacket.PacketType;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.utils.other.MathUtil;
import me.Oxygen.utils.timer.TimeHelper;
import me.Oxygen.value.Value;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleRegister(name = "CivBreak", category = Category.WORLD)
public class CivBreak
extends Module {
	private final Value<Double> range = new Value<Double>("CivBreak_Range", 1.0, 0.0, 20.0, 5.0);
	private final Value<Double> delay = new Value<Double>("CivBreak_Delay", 1000.0, 0.0, 5000.0, 5.0);
    private BlockPos blockPos;
    private EnumFacing enumFacing;
    private final TimeHelper timer = new TimeHelper();

    @EventTarget(events = {EventMotion.class, EventPacket.class})
    private final void onEvent(final Event e) {
    	if(e instanceof EventMotion) {
    		EventMotion em = (EventMotion)e;
    	if(em.getMotionType() == MotionType.PRE) {
        if (this.blockPos == null) {
            return;
        }
        if (this.mc.thePlayer.getDistance((double)this.blockPos.getX(), (double)((float)this.blockPos.getY() + 0.5f), (double)this.blockPos.getZ()) <= this.range.getValue()) {
            float[] angles = getAngles(this.mc.thePlayer, this.blockPos);
            this.mc.thePlayer.rotationYaw = angles[0];
            this.mc.thePlayer.rotationPitch = angles[1];
        }
        if (this.blockPos == null || mc.theWorld.getBlockState(this.blockPos) == null) {
            return;
        }
        double n = this.delay.getValue() / 2.0;
        if (timer.hasTimeElapsed((long) (n + (double)MathUtil.getRandom((int)(this.delay.getValue() / 2)))) && this.mc.thePlayer.getDistance((double)this.blockPos.getX(), (double)this.blockPos.getY(), (double)this.blockPos.getZ()) <= this.range.getValue()) {
        	this.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C0APacketAnimation());
        	this.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.blockPos, this.enumFacing));
            this.timer.reset();
        }
    	}
    	}
    	if(e instanceof EventPacket) {
    		EventPacket ep = (EventPacket)e;
    		if(ep.getPacketType() == PacketType.Send) {
    	        C07PacketPlayerDigging packet;
    	        if (ep.getPacket() instanceof C07PacketPlayerDigging && (packet = (C07PacketPlayerDigging)ep.getPacket()).getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
    	            if (this.blockPos != packet.getPosition()) {
    	                this.blockPos = packet.getPosition();
    	                this.enumFacing = packet.getFacing();
    	            } else {
    	                e.setCancelled(true);
    	            }
    	        }
    	    	}
    	}
    }
    
    private final static float[] getAngles(final EntityPlayerSP player,final BlockPos blockPos) {
        double difX = blockPos.getX() + 0.5D - player.posX;
        double difY = blockPos.getY() - (player.posY + player.getEyeHeight());
        double difZ = blockPos.getZ() + 0.5D - player.posZ;
        double sqrt = Math.sqrt(difX * difX + difZ * difZ);
        float yaw = (float) (Math.atan2(difZ, difX) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(difY, sqrt) * 180.0D / Math.PI);
        return new float[] { yaw, pitch };
    }

    public void onDisable() {
        super.onDisable();
        this.blockPos = null;
        this.enumFacing = null;
        this.timer.reset();
    }
}

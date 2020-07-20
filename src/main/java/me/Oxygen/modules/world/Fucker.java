package me.Oxygen.modules.world;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventMotion.MotionType;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.value.Value;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleRegister(name = "Fucker", category = Category.WORLD)
public class Fucker
extends Module {
	
	private int xPos;
    private int yPos;
    private int zPos;
    private static int radius = 5;
    private Value<String> mode = new Value<String>("Fucker", "Mode", 0);
	
    public Fucker() {
        mode.addValue("Bed");
        mode.addValue("Cake");
        mode.addValue("Egg");
    }

    @EventTarget(events = EventMotion.class)
    private final void onUpdate(final Event event) {
    	if(event instanceof EventMotion) {
    		EventMotion em = (EventMotion)event;
    	if(em.getMotionType() == MotionType.PRE) {
    	this.setDisplayName(this.mode.getModeAt(this.mode.getCurrentMode()));
        int x = - radius;
        while (x < radius) {
            int y = radius;
            while (y > - radius) {
                int z = - radius;
                while (z < radius) {
                    this.xPos = (int)mc.thePlayer.posX + x;
                    this.yPos = (int)mc.thePlayer.posY + y;
                    this.zPos = (int)mc.thePlayer.posZ + z;
                    BlockPos blockPos = new BlockPos(this.xPos, this.yPos, this.zPos);
                    Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                    if (block.getBlockState().getBlock() == Block.getBlockById(92) && this.mode.isCurrentMode("Cake")) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                    } else if (block.getBlockState().getBlock() == Block.getBlockById(122) && this.mode.isCurrentMode("Egg")) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                    } else if (block.getBlockState().getBlock() == Block.getBlockById(26) && this.mode.isCurrentMode("Bed")) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                    }
                    ++z;
                }
                --y;
            }
            ++x;
        }
    	}
    }
   }
    
}

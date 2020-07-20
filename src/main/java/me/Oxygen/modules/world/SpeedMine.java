package me.Oxygen.modules.world;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventDamageBlock;
import me.Oxygen.injection.interfaces.IPlayerControllerMP;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.value.Value;
import net.minecraft.block.Block;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;

@ModuleRegister(name = "SpeedMine", category = Category.WORLD)
public class SpeedMine extends Module
{
	private final Value<String> mode = new Value<String>("SpeedMine", "Mode", 0);
	private final Value<Double> speed = new Value<Double>("SpeedMine_Speed", 0.3, 0.1, 1.1, 0.01);;
    
    public SpeedMine() {
        mode.addValue("Normal");
        mode.addValue("New");
    }
    
    public Block getBlock(double x,double y,double z){
		BlockPos bp = new BlockPos(x, y, z);
		return mc.theWorld.getBlockState(bp).getBlock();
	}
    
    @EventTarget(events = EventDamageBlock.class)
	private final void onTick(final Event e) {
    	EventDamageBlock edb = (EventDamageBlock)e; 
    	if(mode.isCurrentMode("Normal")) {
        final IPlayerControllerMP playerControllerMP = (IPlayerControllerMP)mc.playerController;
        playerControllerMP.setBlockHitDelay(0);
        if (playerControllerMP.getCurBlockDamageMP() >= 1.0 - this.speed.getValueState()) {
                playerControllerMP.setCurBlockDamageMP(1.0f);
        }
        }
    	if(mode.isCurrentMode("New")) {
    		final IPlayerControllerMP playerControllerMP = (IPlayerControllerMP)mc.playerController;
    		mc.thePlayer.swingItem();
            playerControllerMP.setCurBlockDamageMP(playerControllerMP.getCurBlockDamageMP()+ getBlock(edb.getPos().getX(), edb.getPos().getY(), edb.getPos().getZ()).getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, edb.getPos()) * speed.getValueState().floatValue());
            if (playerControllerMP.getCurBlockDamageMP() >= 0.7f) {
                playerControllerMP.setCurBlockDamageMP(1.0f);
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, edb.getPos(), edb.getFac()));
            }
    	}
    }
}


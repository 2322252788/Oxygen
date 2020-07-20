package me.Oxygen.modules.world;

import me.Oxygen.Oxygen;
import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMove;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.modules.movement.Fly;
import me.Oxygen.utils.timer.Timer;
import me.Oxygen.value.Value;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

@ModuleRegister(name = "AntiFall", category = Category.WORLD)
public class AntiFall extends Module {
	
	private BlockPos lastOG;
	private final Timer timer = new Timer();
	
	private final Value<Double> distance = new Value<Double>("AntiFall_Distance", 10.0, 5.0, 30.0, 0.1);
	private final Value<Boolean> voids = new Value<Boolean>("AntiFall_Void", true);
	
	@EventTarget(events = EventMove.class)
	private final void onEvent(Event e) {
		if(e instanceof EventMove) {
		if(mc.thePlayer.onGround)
            lastOG = mc.thePlayer.getPosition();
		
		if(this.voids.getValueState()) {
			if (!this.isBlockUnder() && !mc.thePlayer.capabilities.isFlying && this.mc.thePlayer !=null) {			
				if (!mc.thePlayer.isCollidedVertically) {
					int dist = (int) this.distance.getValueState().intValue();
					if (!mc.thePlayer.onGround && lastOG.getY() - mc.thePlayer.getPosition().getY() >= (float)dist && !Oxygen.INSTANCE.ModMgr.getModule(Fly.class).isEnabled() && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ)).getBlock() == Blocks.air && !this.isBlockUnder()) {
						mc.thePlayer.setPosition(lastOG.getX() - 0.12, lastOG.getY(), lastOG.getZ() - 0.12);	                 
		            }
				}
				
		        this.timer.reset();
		        return;
		    }    
		}else {
			if (!mc.thePlayer.capabilities.isFlying) {			
				if (!mc.thePlayer.isCollidedVertically) {
					int dist = (int) this.distance.getValueState().intValue();
					if (!mc.thePlayer.onGround && lastOG.getY() - mc.thePlayer.getPosition().getY() >= (float)dist && !Oxygen.INSTANCE.ModMgr.getModule(Fly.class).isEnabled() && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ)).getBlock() == Blocks.air) {
						mc.thePlayer.setPosition(lastOG.getX() - 0.12, lastOG.getY(), lastOG.getZ() - 0.12);	                 
		            }
				}
				
		        this.timer.reset();
		        return;
		    }
		}
	}
}
	
	private final boolean isBlockUnder() {
		for(int i = (int)mc.thePlayer.posY; i > 0; --i) {
			BlockPos pos = new BlockPos(mc.thePlayer.posX, (double)i, mc.thePlayer.posZ);
	        if (!(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir)) {
	            return true;
	        }
	    }		
	    return false;
	}
}

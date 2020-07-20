package me.Oxygen.event.events;


import me.Oxygen.event.Event;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class EventDamageBlock extends Event {
	
	    public BlockPos pos;
	    public EnumFacing fac;
		private boolean cancelled;
		
	    public EventDamageBlock(BlockPos p_180512_1_, EnumFacing p_180512_2_) {
	    	this.pos = p_180512_1_;
	    	this.fac = p_180512_2_;
	    	this.cancelled = false;
	    }

	    public BlockPos getPos() {
		    return pos;
	    }
	    
	    public EnumFacing getFac() {
	        return fac;
	    }
	    
	    public boolean isCancelled() {
	        return cancelled;
	    }

	    public void setCancelled(boolean cancelled) {
	        this.cancelled = cancelled;
	    }
	    
	}

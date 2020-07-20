package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.Oxygen.Oxygen;
import me.Oxygen.event.events.EventAttack;
import me.Oxygen.event.events.EventDamageBlock;
import me.Oxygen.injection.interfaces.IPlayerControllerMP;
import me.Oxygen.modules.combat.Reach;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldSettings;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP implements IPlayerControllerMP {
	
	@Shadow
	private WorldSettings.GameType currentGameType;
	
	@Shadow
	private float curBlockDamageMP;
	
	@Shadow
    private int blockHitDelay;

	@Shadow
	private void syncCurrentPlayItem(){};


	@Overwrite
    public float getBlockReachDistance() {    	
    	if (Oxygen.INSTANCE.ModMgr.getModule(Reach.class).isEnabled()) {
    		return (float) Reach.range.getValueState().floatValue();
    	}else {
    		return this.currentGameType.isCreative() ? 5.0F : 4.5F;    
    	} 
    }
	
	@Inject(method = "attackEntity", at = @At("HEAD"))
    public void eventAttack(EntityPlayer playerIn, Entity targetEntity, CallbackInfo ci) {
		new EventAttack(targetEntity).call();;
    }
	
	@Inject(method = "onPlayerDamageBlock",at = @At("HEAD"), cancellable = true)
	public void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		EventDamageBlock event = new EventDamageBlock(posBlock, directionFacing);
	     event.call();
	     if (event.isCancelled()) {
	    	 callbackInfoReturnable.setReturnValue(false);
	     }
	}

	@Override
	public float getCurBlockDamageMP() {
		return this.curBlockDamageMP;
	}

	@Override
	public void SyncCurrentPlayItem() {
		this.syncCurrentPlayItem();
	}

	@Override
	public void setCurBlockDamageMP(float damage) {
		this.curBlockDamageMP = damage;
	}
	
	@Override
    public void setBlockHitDelay(final int i) {
        this.blockHitDelay = i;
    }

    
}

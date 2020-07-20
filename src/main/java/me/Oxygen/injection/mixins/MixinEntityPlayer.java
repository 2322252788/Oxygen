package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.Oxygen.injection.interfaces.IEntityPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer implements IEntityPlayer {
	
	@Shadow
	private int itemInUseCount;
	
	@Shadow
    public abstract int getItemInUseDuration();
	
	@Shadow
    public abstract ItemStack getItemInUse();
	
	@Shadow
    public abstract boolean isUsingItem();

	@Override
	public int getItemInUseCount() {
		return this.itemInUseCount;
	}

	@Override
	public void setItemInUseCount(int count) {
		this.itemInUseCount = count;
	}

}

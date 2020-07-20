package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import me.Oxygen.event.events.EventJump;
import me.Oxygen.injection.interfaces.IEntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity implements IEntityLivingBase {

	public MixinEntityLivingBase(World worldIn) {
		super(worldIn);
	}

	@Shadow
	private int jumpTicks;
	
	@Shadow
    protected abstract float getJumpUpwardsMotion();
	
	 @Shadow
	public abstract PotionEffect getActivePotionEffect(Potion potionIn);
	
	@Shadow
    protected abstract int getArmSwingAnimationEnd();

	@Override
	public void setJumpTicks(int tick) {
		this.jumpTicks = tick;
	}
	
	@Shadow
    public abstract boolean isPotionActive(Potion potionIn);
	
	@Overwrite
    protected void jump() {
        final EventJump jumpEvent = new EventJump(this.getJumpUpwardsMotion());
        jumpEvent.call();
        if(jumpEvent.isCancelled())
            return;

        this.motionY = jumpEvent.getMotion();

        if(this.isPotionActive(Potion.jump))
            this.motionY += (double) ((float) (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);

        if(this.isSprinting()) {
            float f = this.rotationYaw * 0.017453292F;
            this.motionX -= (double) (MathHelper.sin(f) * 0.2F);
            this.motionZ += (double) (MathHelper.cos(f) * 0.2F);
        }

        this.isAirBorne = true;
    }
	
	@Override
    public int runGetArmSwingAnimationEnd() {
        return this.getArmSwingAnimationEnd();
    }
	
}

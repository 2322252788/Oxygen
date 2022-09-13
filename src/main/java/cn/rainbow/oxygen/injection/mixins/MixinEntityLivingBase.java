package cn.rainbow.oxygen.injection.mixins;

import cn.rainbow.oxygen.event.events.JumpEvent;
import cn.rainbow.oxygen.event.events.LivingUpdateEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {

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
	
	@Shadow
    public abstract boolean isPotionActive(Potion potionIn);
	
	/**
     * @author Rainbow
     * @reason regEvent
     */
    @Overwrite
    protected void jump() {
        final JumpEvent jumpEvent = new JumpEvent(this.getJumpUpwardsMotion());
        jumpEvent.call();
        if(jumpEvent.isCancelled())
            return;

        this.motionY = jumpEvent.getMotion();

        if(this.isPotionActive(Potion.jump))
            this.motionY += (float) (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;

        if(this.isSprinting()) {
            float f = this.rotationYaw * 0.017453292F;
            this.motionX -= MathHelper.sin(f) * 0.2F;
            this.motionZ += MathHelper.cos(f) * 0.2F;
        }

        this.isAirBorne = true;
    }

    @Inject(method = "onEntityUpdate", at = @At(value = "HEAD"))
    public void onEntityUpdate(CallbackInfo ci) {
        LivingUpdateEvent event = new LivingUpdateEvent(this);
        event.call();
    }
	
}

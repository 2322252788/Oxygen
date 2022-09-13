package cn.rainbow.oxygen.injection.mixins;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.event.events.StrafeEvent;
import cn.rainbow.oxygen.injection.interfaces.IEntity;
import cn.rainbow.oxygen.module.modules.combat.Hitbox;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity implements IEntity {
	
	@Shadow
    private AxisAlignedBB boundingBox;
	
	@Shadow
	private int nextStepDistance;
	
	@Shadow
	private int fire;

	@Shadow
	public boolean isInWeb;

	@Inject(method = "moveFlying", at = @At("HEAD"), cancellable = true)
	private void handleRotations(float strafe, float forward, float friction, final CallbackInfo callbackInfo) {
		//noinspection ConstantConditions
		if ((Object) this != Minecraft.getMinecraft().thePlayer)
			return;

		final StrafeEvent strafeEvent = new StrafeEvent(strafe, forward, friction);
		strafeEvent.call();

		if (strafeEvent.isCancelled())
			callbackInfo.cancel();
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public float getCollisionBorderSize()
	{
		if(Oxygen.INSTANCE.moduleManager.getModule("Hitbox").getEnabled()) {
			return 0.1F + Hitbox.getSize();
		} else {
			return 0.1F;
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}

	@Override
	public int getNextStepDistance() {
		return this.nextStepDistance;
	}

	@Override
	public void setNextStepDistance(int distance) {
		this.nextStepDistance = distance;
	}

	@Override
	public int getFire() {
		return this.fire;		
	}

	@Override
	public void setFire(int tick) {
		this.fire = tick;
	}

	@Override
	public boolean getInWeb() {
		return isInWeb;
	}

	@Override
	public void setInWeb(boolean inWeb) {
		isInWeb = inWeb;
	}

}

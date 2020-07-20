package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import me.Oxygen.Oxygen;
import me.Oxygen.injection.interfaces.IEntity;
import me.Oxygen.modules.combat.Hitbox;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

@Mixin(Entity.class)
public class MixinEntity implements IEntity {
	
	@Shadow
    private AxisAlignedBB boundingBox;
	
	@Shadow
	private int nextStepDistance;
	
	@Shadow
	private int fire;

	@Overwrite
	public float getCollisionBorderSize() {
    	if (Oxygen.INSTANCE.ModMgr.getModule(Hitbox.class).isEnabled()) {
			return (float) Hitbox.size.getValueState().floatValue();
		}
    	
    	return 0.1F;
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

}

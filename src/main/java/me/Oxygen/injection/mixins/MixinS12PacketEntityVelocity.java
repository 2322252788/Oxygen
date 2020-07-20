package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.Oxygen.injection.interfaces.IS12PacketEntityVelocity;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

@Mixin(S12PacketEntityVelocity.class)
public class MixinS12PacketEntityVelocity implements IS12PacketEntityVelocity{
	
	@Shadow
	private int motionX;
	
	@Shadow
    private int motionY;
    
	@Shadow
    private int motionZ;

	@Override
	public void setX(int x) {
		motionX = x;
	}

	@Override
	public void setY(int y) {
		motionY = y;
	}

	@Override
	public void setZ(int z) {
		motionZ = z;
	}

}

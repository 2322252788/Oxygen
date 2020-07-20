package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.Oxygen.injection.interfaces.IC03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;

@Mixin(C03PacketPlayer.class)
public class MixinC03PacketPlayer implements IC03PacketPlayer {

	@Shadow
    private double x;
	
	@Shadow
    private double y;
	
	@Shadow
    private double z;
	
	@Shadow
    private float yaw;
	
	@Shadow
    private float pitch;
	
	@Shadow
    private boolean onGround;
	
	@Shadow
    private boolean rotating;
	
	@Override
	public double getPosX() {
		return x;
	}
	
	@Override
	public double getPosY() {
		return this.y;
	}
	
	@Override
	public double getPosZ() {
		return this.z;
	}

	@Override
	public void setPosX(double x) {
		this.x = x;
	}

	@Override
	public void setPosY(double y) {
		this.y = y;
	}

	@Override
	public void setPosZ(double z) {
		this.z = z;
	}

	@Override
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	@Override
	public float getYaw() {
		return this.yaw;
	}

	@Override
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	@Override
	public float getPitch() {
		return this.pitch;
	}

	@Override
	public boolean isOnGround() {
		return this.onGround;
	}

	@Override
	public void setOnGround(boolean ground) {
		this.onGround = ground;
	}

	@Override
	public void setRotating(boolean rotating) {
		this.rotating = rotating;
	}

	@Override
	public boolean getRotating() {
		return this.rotating;
	}

    
}

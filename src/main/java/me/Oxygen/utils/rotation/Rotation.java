package me.Oxygen.utils.rotation;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public final class Rotation
{
    private float yaw;
    private float pitch;
    
    public final void toPlayer(final EntityPlayer player) {
        float var2 = this.yaw;
        boolean var3 = false;
        if (!Float.isNaN(var2)) {
            var2 = this.pitch;
            var3 = false;
            if (!Float.isNaN(var2)) {
                this.fixedSensitivity(Minecraft.getMinecraft().gameSettings.mouseSensitivity);
                player.rotationYaw = this.yaw;
                player.rotationPitch = this.pitch;
            }
        }
    }
    
    public final void fixedSensitivity(final float sensitivity) {
        final float f = sensitivity * 0.6f + 0.2f;
        final float gcd = f * f * f * 1.2f;
        this.yaw -= this.yaw % gcd;
        this.pitch -= this.pitch % gcd;
    }
    
    public final float getYaw() {
        return this.yaw;
    }
    
    public final void setYaw(final float var1) {
        this.yaw = var1;
    }
    
    public final float getPitch() {
        return this.pitch;
    }
    
    public final void setPitch(final float var1) {
        this.pitch = var1;
    }
    
    public Rotation(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public final float component1() {
        return this.yaw;
    }
    
    public final float component2() {
        return this.pitch;
    }
    
    public final Rotation copy(final float yaw, final float pitch) {
        return new Rotation(yaw, pitch);
    }
    
    public static Rotation copy$default(final Rotation var0, float var1, float var2, final int var3, final Object var4) {
        if ((var3 & 0x1) != 0x0) {
            var1 = var0.yaw;
        }
        if ((var3 & 0x2) != 0x0) {
            var2 = var0.pitch;
        }
        return var0.copy(var1, var2);
    }
    
    @Override
    public String toString() {
        return "Rotation(yaw=" + this.yaw + ", pitch=" + this.pitch + ")";
    }
    
    @Override
    public int hashCode() {
        return Float.hashCode(this.yaw) * 31 + Float.hashCode(this.pitch);
    }
    
    @Override
    public boolean equals(final Object var1) {
        if (this != var1) {
            if (var1 instanceof Rotation) {
                final Rotation var2 = (Rotation)var1;
                if (Float.compare(this.yaw, var2.yaw) == 0 && Float.compare(this.pitch, var2.pitch) == 0) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}

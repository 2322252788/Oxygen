package me.Oxygen.utils.Liquidbounce.rotation;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class Rotation {

    private float yaw;
    private float pitch;

    public final void toPlayer(final EntityPlayer player) {
        //Intrinsics.checkParameterIsNotNull((Object)player, "player");
        if (Float.isNaN(this.yaw) || Float.isNaN(this.pitch)) {
            return;
        }
        this.fixedSensitivity(Minecraft.getMinecraft().gameSettings.mouseSensitivity);
        player.rotationYaw = this.yaw;
        player.rotationPitch = this.pitch;
    }

    public final void fixedSensitivity(float sensitivity) {
        float f = sensitivity * 0.6f + 0.2f;
        float gcd = f * f * f * 1.2f;
        this.yaw -= this.yaw % gcd;
        this.pitch -= this.pitch % gcd;
    }

    public final float getYaw() {
        return this.yaw;
    }

    public final void setYaw(float f) {
        this.yaw = f;
    }

    public final float getPitch() {
        return this.pitch;
    }

    public final void setPitch(float f) {
        this.pitch = f;
    }

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public final float component1() {
        return this.yaw;
    }

    public final float component2() {
        return this.pitch;
    }

}

package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.Oxygen.injection.interfaces.IS08PacketPlayerPosLook;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@Mixin(S08PacketPlayerPosLook.class)
public class MixinS08PacketPlayerPosLook implements IS08PacketPlayerPosLook
{
    @Shadow
    private float yaw;
    
    @Shadow
    private float pitch;
    
    @Override
    public void setYaw(final float y) {
        this.yaw = y;
    }
    
    @Override
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
}

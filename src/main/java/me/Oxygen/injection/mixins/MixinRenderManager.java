package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import me.Oxygen.injection.interfaces.IRenderManager;
import net.minecraft.client.renderer.entity.RenderManager;

@Mixin(RenderManager.class)
public class MixinRenderManager implements IRenderManager
{
    @Shadow
    private double renderPosX;
    
    @Shadow
    private double renderPosY;
    
    @Shadow
    private double renderPosZ;
    
    @Override
    public double getRenderPosX() {
        return this.renderPosX;
    }
    
    @Override
    public double getRenderPosY() {
        return this.renderPosY;
    }
    
    @Override
    public double getRenderPosZ() {
        return this.renderPosZ;
    }
}

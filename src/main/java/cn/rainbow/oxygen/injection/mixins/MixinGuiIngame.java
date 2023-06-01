package cn.rainbow.oxygen.injection.mixins;

import cn.rainbow.oxygen.event.events.Render2DEvent;
import cn.rainbow.oxygen.event.events.RenderGuiEvent;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;

@Mixin(GuiIngame.class)
public class MixinGuiIngame extends Gui {
	
	@Inject(method = { "renderTooltip" }, at = { @At("HEAD") })
    private void renderTooltip1(final ScaledResolution sr, final float partialTicks, final CallbackInfo ci) {
        new Render2DEvent(sr, partialTicks).call();
        GlStateManager.color(1, 1, 1);
	}

    @Inject(method = { "renderTooltip" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;disableBlend()V") })
    private void renderTooltip2(final ScaledResolution sr, final float partialTicks, final CallbackInfo ci) {
        new RenderGuiEvent(sr).call();
        //ClientUtil.INSTANCE.drawNotifications();
    }

}

package cn.rainbow.oxygen.injection.mixins;

import cn.rainbow.oxygen.event.events.Render2DEvent;
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
    private void renderTooltip2(final ScaledResolution sr, final float partialTicks, final CallbackInfo ci) {
        new Render2DEvent(sr, partialTicks).call();
        GlStateManager.color(1, 1, 1);
        //ClientUtil.INSTANCE.drawNotifications();
	}

}

package me.Oxygen.injection.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.Oxygen.Oxygen;
import me.Oxygen.event.events.EventRender2D;
import me.Oxygen.event.events.EventRenderGui;
import me.Oxygen.modules.render.HUD;
import me.Oxygen.utils.ClientUtil;
import me.Oxygen.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

@Mixin(GuiIngame.class)
public class MixinGuiIngame extends Gui {
	
	@Inject(method = { "renderTooltip" }, at = { @At("HEAD") }, cancellable = true)
    private void renderTooltip(final ScaledResolution sr, final float partialTicks, final CallbackInfo ci) {
		
        new EventRender2D(partialTicks).call();
        
        if(Oxygen.INSTANCE.ModMgr.getModule(HUD.class).isEnabled() && HUD.hotbar.getValueState().booleanValue()) {
			RenderUtil.drawRect(sr.getScaledWidth() / 2 - 92, sr.getScaledHeight() - 23, sr.getScaledWidth() / 2 + 91, sr.getScaledHeight(), Integer.MIN_VALUE);
		   	if(Minecraft.getMinecraft().thePlayer.inventory.currentItem == 0) {
		   		RenderUtil.drawRect(sr.getScaledWidth() / 2 - 92, sr.getScaledHeight() - 23, (sr.getScaledWidth() / 2 + 91) - 20 * 8, sr.getScaledHeight(), Integer.MAX_VALUE);
		   	} else {
		   		RenderUtil.drawRect((sr.getScaledWidth() / 2) - 92 + Minecraft.getMinecraft().thePlayer.inventory.currentItem * 20, sr.getScaledHeight() - 23, (sr.getScaledWidth() / 2) + 91 - 20 * (8 - Minecraft.getMinecraft().thePlayer.inventory.currentItem), sr.getScaledHeight(), Integer.MAX_VALUE);
		   	}
		   	GlStateManager.disableBlend();
		}
    }
	
	@Inject(method = { "renderTooltip" }, at = { @At("RETURN") }, cancellable = true)
    private void renderTooltip2(final ScaledResolution sr, final float partialTicks, final CallbackInfo ci) {
        new EventRenderGui(sr).call();
        ClientUtil.INSTANCE.drawNotifications();
	}

}

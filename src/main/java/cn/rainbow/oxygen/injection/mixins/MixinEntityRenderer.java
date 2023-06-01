package cn.rainbow.oxygen.injection.mixins;

import cn.rainbow.oxygen.event.events.Render3DEvent;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.shader.ShaderGroup;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
	Minecraft mc = Minecraft.getMinecraft();
	@Shadow
	private ShaderGroup theShaderGroup;
	
	@Shadow
	@Final
	private int[] lightmapColors;
	
	@Shadow
	@Final
	private DynamicTexture lightmapTexture;
	
	@Shadow
	private float torchFlickerX;

	@Shadow
	public void setupCameraTransform(float partialTicks, int pass) {}
	
	@Shadow
	private void orientCamera(float partialTicks) {}

	@Inject(method = "renderWorldPass", at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;disableFog()V", shift = At.Shift.AFTER) })
	private void eventRender3D(int pass, float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
		new Render3DEvent(partialTicks).call();
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
	}

}

package me.Oxygen.injection.mixins;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.Oxygen.event.events.EventRender3D;
import me.Oxygen.injection.interfaces.IEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.shader.ShaderGroup;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer implements IEntityRenderer{
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
	
	@Override
	@Shadow
	public void setupCameraTransform(float partialTicks, int pass) {}
	
	@Shadow
	private void orientCamera(float partialTicks) {}
	
	@Override
	public void orientCamera1(float partialTicks) {
		this.orientCamera(partialTicks);
	}
	
	@Override
	public int[] getLightmapColors() {
		return this.lightmapColors;
	}

	@Override
	public DynamicTexture getLightmapTexture() {
		return this.lightmapTexture;
	}

	@Override
	public float getTorchFlickerX() {
		return this.torchFlickerX;
	}

	@Override
	public ShaderGroup getTheShaderGroup() {
		return this.theShaderGroup;
	}

	@Override
	public void setTheShaderGroup(ShaderGroup group) {
		this.theShaderGroup = group;
	}

	@Inject(method = "renderWorldPass", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.BEFORE))
	private void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
		new EventRender3D(partialTicks).call();
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
	}
	
	


	@Override
    public void runSetupCameraTransform(final float partialTicks, final int pass) {
        this.setupCameraTransform(partialTicks, pass);
    }

	
}

package me.Oxygen.injection.interfaces;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.shader.ShaderGroup;

public interface IEntityRenderer
{
    void runSetupCameraTransform(final float p0, final int p1);

	void setTheShaderGroup(ShaderGroup group);

	ShaderGroup getTheShaderGroup();

	float getTorchFlickerX();

	DynamicTexture getLightmapTexture();

	int[] getLightmapColors();

	void orientCamera1(float partialTicks);

	void setupCameraTransform(float partialTicks, int pass);
    
}

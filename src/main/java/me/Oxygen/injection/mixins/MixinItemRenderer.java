package me.Oxygen.injection.mixins;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import me.Oxygen.modules.render.Animation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {
	
    @Shadow
    private Minecraft mc;
    
    @Shadow
    private float equippedProgress;
    
    @Shadow
    private float prevEquippedProgress;
    
    @Shadow
    private ItemStack itemToRender;
    
    @Shadow
    private RenderManager renderManager;
    
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks)
    {
        float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.rotateArroundXAndY(f2, f3);
        this.setLightMapFromPlayer(abstractclientplayer);
        this.rotateWithPlayerRotations((EntityPlayerSP)abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (this.itemToRender != null)
        {
            if (this.itemToRender.getItem() instanceof net.minecraft.item.ItemMap)
            {
                this.renderItemMap(abstractclientplayer, f2, f, f1);
            }
            else if (abstractclientplayer.getItemInUseCount() > 0)
            {
                EnumAction enumaction = this.itemToRender.getItemUseAction();

                switch (enumaction)
                {
                    case NONE:
                        this.transformFirstPersonItem(f, 0.0F);
                        break;
                    case EAT:
                    case DRINK:
                        this.performDrinking(abstractclientplayer, partialTicks);
                        this.transformFirstPersonItem(f, 0.0F);
                        break;
                    case BLOCK:
                    	float var2 = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
                    	float var41 = this.mc.thePlayer.getSwingProgress(partialTicks);
                    	float var15 = MathHelper.sin(MathHelper.sqrt_float(var41) * 3.1415927f);
                        EntityPlayerSP var3 = this.mc.thePlayer;
                        float var4 = var3.getSwingProgress(partialTicks);
                        if(Animation.mode.isCurrentMode("Leakey")) {    
                        	this.transformFirstPersonItem(f, 0.0F);
                            this.doBlockTransformations();
                            float var14 = MathHelper.sin(f1 * f1 * 3.1415927F);
                            float var16 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.0F);
                            GlStateManager.translate(0.7F, -0.4F, 1.0F);
                            GlStateManager.rotate(-var16 * 0.0F, 0.0F, 0.0F, 0.0F);
                            GlStateManager.rotate(-var16 * 30.0F, 5.0F, 0.0F, 9.0F);
                        } else if (Animation.mode.isCurrentMode("Sigma")) {
                        	this.genCustom(var2 * 0.5f, 0);
                            GlStateManager.rotate(-var15 * 55 / 2.0F, -8.0F, -0.0F, 9.0F);
                            GlStateManager.rotate(-var15 * 45, 1.0F, var15 / 2, -0.0F);
                            this.func_178103_d2();
                            GL11.glTranslated(1.2, 0.3, 0.5);
                            GL11.glTranslatef(-1, this.mc.thePlayer.isSneaking() ? -0.1F : -0.2F, 0.2F);
                            GlStateManager.scale(1.2f,1.2f,1.2f);
                            break;
                        } else if(Animation.mode.isCurrentMode("Jello")) {
                        	transformFirstPersonItem(0.0F, 0.0F);
                        	doBlockTransformations();
                            int alpha = (int)Math.min(255L, ((System.currentTimeMillis() % 255L > 127L) ? Math.abs(Math.abs(System.currentTimeMillis()) % 255L - 255L) : (System.currentTimeMillis() % 255L)) * 2L);
                            float f5 = (var4 > 0.5D) ? (1.0F - var4) : var4;
                            GlStateManager.translate(0.3F, -0.0F, 0.4F);
                            GlStateManager.rotate(0.0F, 0.0F, 0.0F, 1.0F);
                            GlStateManager.translate(0.0F, 0.5F, 0.0F);
                            GlStateManager.rotate(90.0F, 1.0F, 0.0F, -1.0F);
                            GlStateManager.translate(0.6F, 0.5F, 0.0F);
                            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, -1.0F);
                            GlStateManager.rotate(-10.0F, 1.0F, 0.0F, -1.0F);
                            GlStateManager.rotate(this.mc.thePlayer.isSwingInProgress ? (-alpha / 5.0F) : 1.0F, 1.0F, -0.0F, 1.0F);
                            break;
                        } else if(Animation.mode.isCurrentMode("Meme")) {
                        	this.genCustom(0.0F, 0.0F);
                            this.doBlockTransformations();
                            float var111 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927F);
                            GlStateManager.translate(-0.5F, 0.4F, 0.0F);
                            GlStateManager.rotate(-var111 * 50.0F, -8.0F, -0.0F, 9.0F);
                            GlStateManager.rotate(-var111 * 70.0F, 1.0F, -0.4F, -0.0F);
                            break;
                        } else if(Animation.mode.isCurrentMode("1.7")) {
                        	this.genCustom(f, f1);
                            func_178103_d2();
                            break;
                        }
                        break;
                    case BOW:
                        this.transformFirstPersonItem(f, 0.0F);
                        this.doBowTransformations(partialTicks, abstractclientplayer);
                }
            }
            else
            {
                this.doItemUsedTransformations(f1);
                this.transformFirstPersonItem(f, f1);
            }

            this.renderItem(abstractclientplayer, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        }
        else if (!abstractclientplayer.isInvisible())
        {
            this.renderPlayerArm(abstractclientplayer, f, f1);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }
    
    private void genCustom(float p_178096_1_, float p_178096_2_) {
        GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178096_1_ * -0.6F, 0.0F);
        GlStateManager.rotate(25F, 0.0F, 1.0F, 0.0F);
        float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * 3.1415927F);
        float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * 3.1415927F);
        GlStateManager.rotate(var3 * -15F, 0.0F, 1.0F, 0.2F);
        GlStateManager.rotate(var4 * -10F, 0.2F, 0.1F, 1.0F);
        GlStateManager.rotate(var4 * -30F, 1.3F, 0.1F, 0.2F);
        GlStateManager.scale(0.4F, 0.4F, 0.4F);
    }
    
    private void func_178103_d2()
    {
        GlStateManager.translate(-0.5F, 0.2F, 0.0F);
        GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
    }
    
    @Shadow
    private void rotateArroundXAndY(float angle, float angleY) {}
    
    @Shadow
    private void setLightMapFromPlayer(AbstractClientPlayer clientPlayer) {}
    
    @Shadow
    private void rotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks) {}
    
    @Shadow
    private void renderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress) {}
    
    @Shadow
    private void transformFirstPersonItem(float equipProgress, float swingProgress) {}
    
    @Shadow
    private void performDrinking(AbstractClientPlayer clientPlayer, float partialTicks) {}
    
    @Shadow
    private void doBlockTransformations() {}
    
    @Shadow
    private void doBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer) {}
    
    @Shadow
    private void doItemUsedTransformations(float swingProgress) {}
    
    @Shadow
    public void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform) {}
    
    @Shadow
    private void renderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress) {}
    
}

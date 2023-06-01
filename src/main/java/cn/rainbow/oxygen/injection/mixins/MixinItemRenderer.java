package cn.rainbow.oxygen.injection.mixins;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.module.modules.render.Animation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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

    @Final
    @Shadow
    private Minecraft mc;

    @Shadow
    private float equippedProgress;

    @Shadow
    private float prevEquippedProgress;

    @Shadow
    private ItemStack itemToRender;

    @Final
    @Shadow
    private RenderManager renderManager;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void renderItemInFirstPerson(float partialTicks)
    {
        float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        EntityPlayerSP abstractclientplayer = this.mc.thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.rotateArroundXAndY(f2, f3);
        this.setLightMapFromPlayer(abstractclientplayer);
        this.rotateWithPlayerRotations(abstractclientplayer, partialTicks);
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
                        EntityPlayerSP var3 = this.mc.thePlayer;
                        float var4 = var3.getSwingProgress(partialTicks);
                        Animation animation = (Animation) Oxygen.INSTANCE.moduleManager.getModule(Animation.class);
                        switch (animation.getMode().getCurrentValue()) {
                            case "Normal" :
                                this.transformFirstPersonItem(f, 0.0F);
                                this.doBlockTransformations();
                                float ff1 = MathHelper.sin(f1 * f1 * 3.1415927F);
                                float ff2 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.0F);
                                GlStateManager.translate(0.7F, -0.4F, 1.0F);
                                GlStateManager.rotate(-ff1 * 0.0F, 0.0F, 0.0F, 0.0F);
                                GlStateManager.rotate(-ff2 * 30.0F, 5.0F, 0.0F, 9.0F);
                                break;
                            case "Sigma" :
                                float var15 =MathHelper.sin(f1 * f1 * 3.1415927F);
                                this.transformFirstPersonItem(var2 * 0.5f, 0.0F);
                                GlStateManager.rotate(-var15 * 55 / 2.0F, -8.0F, -0.0F, 9.0F);
                                GlStateManager.rotate(-var15 * 45, 1.0F, var15 / 2, -0.0F);
                                this.doBlockTransformations();
                                GL11.glTranslated(1.2, 0.3, 0.5);
                                GL11.glTranslatef(-1, this.mc.thePlayer.isSneaking() ? -0.1F : -0.2F, 0.2F);
                                break;
                            case "Exhi" :
                                transformFirstPersonItem(f, 0.83F);
                                float f4 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.83F);
                                GlStateManager.translate(-0.5F, 0.2F, 0.2F);
                                GlStateManager.rotate(-f4 * 0.0F, 0.0F, 0.0F, 0.0F);
                                GlStateManager.rotate(-f4 * 43.0F, 58.0F, 23.0F, 45.0F);
                                doBlockTransformations();
                                break;
                            case "1.7" :
                                this.transformFirstPersonItem(0.0F, 0.0F);
                                this.doBlockTransformations();
                                int alpha = (int) Math.min(255, ((System.currentTimeMillis() % 255) > 255/2 ? (Math.abs(Math.abs(System.currentTimeMillis()) % 255 - 255)) : System.currentTimeMillis() % 255)*2);
                                float f5 = (var4 > 0.5 ? 1-var4 : var4);
                                GlStateManager.translate(0.3f, -0.0f, 0.40f);
                                GlStateManager.rotate(0.0f, 0.0f, 0.0f, 1.0f);
                                GlStateManager.translate(0, 0.5f, 0);
                                GlStateManager.rotate(90, 1.0f, 0.0f, -1.0f);
                                GlStateManager.translate(0.6f, 0.5f, 0);
                                GlStateManager.rotate(-90, 1.0f, 0.0f, -1.0f);
                                GlStateManager.rotate(-10, 1.0f, 0.0f, -1.0f);
                                GlStateManager.rotate(mc.thePlayer.isSwingInProgress ? -alpha / 5f : 1, 1.0f, -0.0f, 1.0f);
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

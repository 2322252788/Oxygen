package cn.rainbow.oxygen.injection.mixins;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.module.modules.render.ChestESP;
import cn.rainbow.oxygen.utils.render.WorldRenderUtils;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TileEntityEnderChestRenderer.class)
public class MixinTileEntityEnderChestRenderer extends TileEntitySpecialRenderer<TileEntityEnderChest> {

    @Shadow
    @Final
    private static ResourceLocation ENDER_CHEST_TEXTURE;
    @Shadow
    private ModelChest field_147521_c;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void renderTileEntityAt(TileEntityEnderChest te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        int i = 0;

        if (te.hasWorldObj())
        {
            i = te.getBlockMetadata();
        }

        if (destroyStage >= 0)
        {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        }
        else
        {
            this.bindTexture(ENDER_CHEST_TEXTURE);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        int j = 0;

        if (i == 2)
        {
            j = 180;
        }

        if (i == 3)
        {
            j = 0;
        }

        if (i == 4)
        {
            j = 90;
        }

        if (i == 5)
        {
            j = -90;
        }

        GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
        f = 1.0F - f;
        f = 1.0F - f * f * f;
        this.field_147521_c.chestLid.rotateAngleX = -(f * (float)Math.PI / 2.0F);

        //START: Outlined Storage ESP
        ChestESP chestESP = (ChestESP) Oxygen.INSTANCE.moduleManager.getModule(ChestESP.class);
        boolean doOutline = chestESP.getEnabled() && chestESP.getModeValue().isCurrentMode("Box") && chestESP.getEnderChest().getCurrentValue();

        if(doOutline && te.hasWorldObj()) {
            field_147521_c.renderAll();
            WorldRenderUtils.renderOne();
            field_147521_c.renderAll();
            WorldRenderUtils.renderTwo();
            field_147521_c.renderAll();
            WorldRenderUtils.renderThree();
            WorldRenderUtils.renderFour(chestESP.getChestESPColours().getRGB());
            field_147521_c.renderAll();
            WorldRenderUtils.renderFive();
        }
        else{
            field_147521_c.renderAll();
        }
        //END: Outlined Storage ESP

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0)
        {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}

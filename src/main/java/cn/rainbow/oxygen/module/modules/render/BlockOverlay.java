package cn.rainbow.oxygen.module.modules.render;

import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.EventRender2D;
import cn.rainbow.oxygen.event.events.EventRender3D;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.setting.BooleanValue;
import cn.rainbow.oxygen.module.setting.NumberValue;
import cn.rainbow.oxygen.utils.render.ColorUtils;
import cn.rainbow.oxygen.utils.render.RenderUtil;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

public class BlockOverlay extends Module {
	
	private final NumberValue red = new NumberValue("Red", 255.0, 1.0, 255.0, 1.0);
	private final NumberValue green = new NumberValue("Green", 255.0, 1.0, 255.0, 1.0);
	private final NumberValue blue = new NumberValue("Blue", 255.0, 1.0, 255.0, 1.0);
	private final BooleanValue renderstring = new BooleanValue("RenderString", true);
	
	public BlockOverlay() {
		super("BlockOverlay", Category.Render);
	}
	
	public int getRed() {
        return (int)this.red.getCurrentValue();
    }
    
    public int getGreen() {
    	return (int)this.green.getCurrentValue();
    }
    
    public int getBlue() {
    	return (int)this.blue.getCurrentValue();
    }
    
    public boolean getRender() {
        return this.renderstring.getCurrentValue();
    }
    
    @EventTarget(events = {EventRender2D.class, EventRender3D.class})
    private void onRender(final Event event) {
	    if (event instanceof EventRender2D){
            if (this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                FontRenderer fr = this.mc.fontRendererObj;
                BlockPos pos = this.mc.objectMouseOver.getBlockPos();
                Block block = this.mc.theWorld.getBlockState(pos).getBlock();
                int id = Block.getIdFromBlock(block);
                String s = block.getLocalizedName() + " ID:" + id;
                String s2 = block.getLocalizedName();
                String s3 = " ID:" + id;
                if (this.mc.objectMouseOver != null && this.getRender()) {
                    ScaledResolution res = new ScaledResolution(this.mc);
                    int x = res.getScaledWidth() / 2 + 10;
                    int y = res.getScaledHeight() / 2 + 2;
                    RenderUtil.drawRoundedRect((float)x, (float)y, (float)(x + fr.getStringWidth(s) + 5), y + fr.FONT_HEIGHT + 0.5f, 2f, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.7f));
                    fr.drawString(s2, (x + 2), (y) + 1, ColorUtils.WHITE.c);
                    fr.drawString(s3, x + fr.getStringWidth(s2) + 2, (y) + 1, ColorUtils.GREY.c);
                }
            }
        }
        if (event instanceof EventRender3D) {
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos pos = this.mc.objectMouseOver.getBlockPos();
                Block block = this.mc.theWorld.getBlockState(pos).getBlock();
                this.mc.getRenderManager();
                double x = pos.getX() - mc.getRenderManager().viewerPosX;
                this.mc.getRenderManager();
                double y = pos.getY() - mc.getRenderManager().viewerPosY;
                this.mc.getRenderManager();
                double z = pos.getZ() - mc.getRenderManager().viewerPosZ;
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(3553);
                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GL11.glColor4f(this.getRed() / 255.0f, this.getGreen() / 255.0f, this.getBlue() / 255.0f, 0.15f);
                double minX = (block instanceof BlockStairs || Block.getIdFromBlock(block) == 134) ? 0.0 : block.getBlockBoundsMinX();
                double minY = (block instanceof BlockStairs || Block.getIdFromBlock(block) == 134) ? 0.0 : block.getBlockBoundsMinY();
                double minZ = (block instanceof BlockStairs || Block.getIdFromBlock(block) == 134) ? 0.0 : block.getBlockBoundsMinZ();
                RenderUtil.drawBoundingBox(new AxisAlignedBB(x + minX, y + minY, z + minZ, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
                GL11.glColor4f(this.getRed() / 255.0f, this.getGreen() / 255.0f, this.getBlue() / 255.0f, 1.0f);
                GL11.glLineWidth(0.5f);
                RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x + minX, y + minY, z + minZ, x + block.getBlockBoundsMaxX(), y + block.getBlockBoundsMaxY(), z + block.getBlockBoundsMaxZ()));
                GL11.glDisable(2848);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glPopMatrix();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

}


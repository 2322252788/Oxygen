package me.Oxygen.modules.render;

import java.awt.Color;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventRender3D;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.utils.render.RenderUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;

@ModuleRegister(name = "ChestESP", category = Category.RENDER)
public class ChestESP extends Module {

	@EventTarget(events = EventRender3D.class)
	private final void onRender(Event e) {
		if(e instanceof EventRender3D) {
        Iterator var3 = mc.theWorld.loadedTileEntityList.iterator();
        while (var3.hasNext()) {
            TileEntity ent = (TileEntity)var3.next();
            if (!(ent instanceof TileEntityChest) && !(ent instanceof TileEntityEnderChest)) continue;
            this.mc.getRenderManager();
            double x2 = (double)ent.getPos().getX() - mc.getRenderManager().viewerPosX;
            this.mc.getRenderManager();
            double y2 = (double)ent.getPos().getY() - mc.getRenderManager().viewerPosY;
            this.mc.getRenderManager();
            double z2 = (double)ent.getPos().getZ() - mc.getRenderManager().viewerPosZ;
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            RenderUtil.color((int)RenderUtil.reAlpha((int)new Color(-14848033).brighter().getRGB(), (float)0.35f));
            RenderUtil.drawBoundingBox(new AxisAlignedBB(x2 + ent.getBlockType().getBlockBoundsMinX(), y2 + ent.getBlockType().getBlockBoundsMinY(), z2 + ent.getBlockType().getBlockBoundsMinZ(), x2 + ent.getBlockType().getBlockBoundsMaxX(), y2 + ent.getBlockType().getBlockBoundsMaxY(), z2 + ent.getBlockType().getBlockBoundsMaxZ()));
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        }
        return;
		}
	}
    }


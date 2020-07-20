package me.Oxygen.utils.render;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;
import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import me.Oxygen.injection.interfaces.*;
import me.Oxygen.event.events.EventRender3D;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

public enum RenderUtil {
	
	INSTANCE;
	
	public static Minecraft mc = Minecraft.getMinecraft();
	public static float delta;
	protected static float zLevel;
	
	public static void enableGL3D(float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(lineWidth);
    }
	
    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
    }

    public static void post3D() {
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
    }
	
    public static void entityESPBox(Entity e, Color color, EventRender3D event) {
        double posX = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)event.getPartialTicks() - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double posY = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)event.getPartialTicks() - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double posZ = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)event.getPartialTicks() - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        AxisAlignedBB box = AxisAlignedBB.fromBounds(posX - (double)e.width, posY, posZ - (double)e.width, posX + (double)e.width, posY + (double)e.height + 0.2, posZ + (double)e.width);
        if (e instanceof EntityLivingBase) {
            box = AxisAlignedBB.fromBounds(posX - (double)e.width + 0.2, posY, posZ - (double)e.width + 0.2, posX + (double)e.width - 0.2, posY + (double)e.height + (e.isSneaking() ? 0.02 : 0.2), posZ + (double)e.width - 0.2);
        }
        GL11.glLineWidth(1.0f);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)1f);
        RenderUtil.drawOutlinedBoundingBox(box);
    }
	
	public static int rainbow(int delay) {
        double rainbow = Math.ceil((double)(System.currentTimeMillis() + (long)delay) / 10.0);
        return Color.getHSBColor((float)((rainbow %= 360.0) / 360.0), 0.5f, 1.0f).getRGB();
    }

    public static void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void drawCircle(double x, double y, double radius, int c) {
        float f2 = (float)(c >> 24 & 255) / 255.0f;
        float f22 = (float)(c >> 16 & 255) / 255.0f;
        float f3 = (float)(c >> 8 & 255) / 255.0f;
        float f4 = (float)(c & 255) / 255.0f;
        GlStateManager.alphaFunc(516, 0.001f);
        GlStateManager.color(f22, f3, f4, f2);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tes = Tessellator.getInstance();
        double i = 0.0;
        while (i < 360.0) {
            double f5 = Math.sin(i * 3.141592653589793 / 180.0) * radius;
            double f6 = Math.cos(i * 3.141592653589793 / 180.0) * radius;
            GL11.glVertex2d((double)((double)f3 + x), (double)((double)f4 + y));
            i += 1.0;
        }
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.alphaFunc(516, 0.1f);
    }
    
    public static void drawCircle(int xx, int yy, int radius, Color col)
    {
        int sections = 70;
        double dAngle = 6.283185307179586D / sections;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glBegin(2);
        for (int i = 0; i < sections; i++)
        {
            float x = (float)(radius * Math.cos(i * dAngle));
            float y = (float)(radius * Math.sin(i * dAngle));

            GL11.glColor4f(col.getRed() / 255.0F, col.getGreen() / 255.0F, col.getBlue() / 255.0F, col.getAlpha() / 255.0F);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    public static void drawGradient(double x, double y, double x2, double y2, int col1, int col2) {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;

        float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        float f7 = (col2 & 0xFF) / 255.0F;

        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);

        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);

        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }
    
    public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float l1, final int col1, final int col2) {
        RenderUtil.drawRect(x, y, x2, y2, col2);
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) x2, (double) y);
        GL11.glVertex2d((double) x, (double) y2);
        GL11.glVertex2d((double) x2, (double) y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;
        
        float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        float f7 = (col2 & 0xFF) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);

        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);

        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        GL11.glColor4d(255, 255, 255, 255);
    }
    
    public static void startDrawing() {
        GL11.glEnable((int)3042);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        ((IEntityRenderer)Minecraft.getMinecraft().entityRenderer).setupCameraTransform(((IMinecraft)Minecraft.getMinecraft()).getTimer().renderPartialTicks, 0);
    }

    public static void stopDrawing() {
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public void drawRombo(double x, double y, double z) {
    	((IEntityRenderer)Minecraft.getMinecraft().entityRenderer).setupCameraTransform(((IMinecraft)Minecraft.getMinecraft()).getTimer().renderPartialTicks, 0);
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)(x + 0.5), (double)(y += 1.0), (double)z);
        GL11.glVertex3d((double)x, (double)(y + 1.0), (double)z);
        GL11.glVertex3d((double)x, (double)y, (double)(z + 0.5));
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)x, (double)y, (double)(z + 0.5));
        GL11.glVertex3d((double)x, (double)(y + 1.0), (double)z);
        GL11.glVertex3d((double)(x + 0.5), (double)y, (double)z);
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)x, (double)y, (double)(z - 0.5));
        GL11.glVertex3d((double)x, (double)(y + 1.0), (double)z);
        GL11.glVertex3d((double)(x - 0.5), (double)y, (double)z);
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)(x - 0.5), (double)y, (double)z);
        GL11.glVertex3d((double)x, (double)(y + 1.0), (double)z);
        GL11.glVertex3d((double)x, (double)y, (double)(z - 0.5));
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)(x + 0.5), (double)y, (double)z);
        GL11.glVertex3d((double)x, (double)(y - 1.0), (double)z);
        GL11.glVertex3d((double)x, (double)y, (double)(z + 0.5));
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)x, (double)y, (double)(z + 0.5));
        GL11.glVertex3d((double)x, (double)(y - 1.0), (double)z);
        GL11.glVertex3d((double)(x + 0.5), (double)y, (double)z);
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)x, (double)y, (double)(z - 0.5));
        GL11.glVertex3d((double)x, (double)(y - 1.0), (double)z);
        GL11.glVertex3d((double)(x - 0.5), (double)y, (double)z);
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)(x - 0.5), (double)y, (double)z);
        GL11.glVertex3d((double)x, (double)(y - 1.0), (double)z);
        GL11.glVertex3d((double)x, (double)y, (double)(z - 0.5));
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)x, (double)y, (double)(z - 0.5));
        GL11.glVertex3d((double)x, (double)(y + 1.0), (double)z);
        GL11.glVertex3d((double)(x + 0.5), (double)y, (double)z);
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)(x + 0.5), (double)y, (double)z);
        GL11.glVertex3d((double)x, (double)(y + 1.0), (double)z);
        GL11.glVertex3d((double)x, (double)y, (double)(z - 0.5));
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)x, (double)y, (double)(z + 0.5));
        GL11.glVertex3d((double)x, (double)(y + 1.0), (double)z);
        GL11.glVertex3d((double)(x - 0.5), (double)y, (double)z);
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)(x - 0.5), (double)y, (double)z);
        GL11.glVertex3d((double)x, (double)(y + 1.0), (double)z);
        GL11.glVertex3d((double)x, (double)y, (double)(z + 0.5));
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)x, (double)y, (double)(z - 0.5));
        GL11.glVertex3d((double)x, (double)(y - 1.0), (double)z);
        GL11.glVertex3d((double)(x + 0.5), (double)y, (double)z);
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)(x + 0.5), (double)y, (double)z);
        GL11.glVertex3d((double)x, (double)(y - 1.0), (double)z);
        GL11.glVertex3d((double)x, (double)y, (double)(z - 0.5));
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)x, (double)y, (double)(z + 0.5));
        GL11.glVertex3d((double)x, (double)(y - 1.0), (double)z);
        GL11.glVertex3d((double)(x - 0.5), (double)y, (double)z);
        GL11.glEnd();
        GL11.glBegin((int)4);
        GL11.glVertex3d((double)(x - 0.5), (double)y, (double)z);
        GL11.glVertex3d((double)x, (double)(y - 1.0), (double)z);
        GL11.glVertex3d((double)x, (double)y, (double)(z + 0.5));
        GL11.glEnd();
    }
    
    public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, EntityLivingBase p_147046_5_) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(p_147046_0_, p_147046_1_, 40.0f);
        GlStateManager.scale(- p_147046_2_, p_147046_2_, p_147046_2_);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        float var6 = p_147046_5_.renderYawOffset;
        float var7 = p_147046_5_.rotationYaw;
        float var8 = p_147046_5_.rotationPitch;
        float var9 = p_147046_5_.prevRotationYawHead;
        float var10 = p_147046_5_.rotationYawHead;
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((- (float)Math.atan(p_147046_4_ / 40.0f)) * 20.0f, 1.0f, 0.0f, 0.0f);
        p_147046_5_.renderYawOffset = (float)Math.atan(p_147046_3_ / 40.0f) * -14.0f;
        p_147046_5_.rotationYaw = (float)Math.atan(p_147046_3_ / 40.0f) * -14.0f;
        p_147046_5_.rotationPitch = (- (float)Math.atan(p_147046_4_ / 40.0f)) * 15.0f;
        p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
        p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager var11 = Minecraft.getMinecraft().getRenderManager();
        var11.setPlayerViewY(180.0f);
        var11.setRenderShadow(false);
        var11.renderEntityWithPosYaw(p_147046_5_, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        var11.setRenderShadow(true);
        p_147046_5_.renderYawOffset = var6;
        p_147046_5_.rotationYaw = var7;
        p_147046_5_.rotationPitch = var8;
        p_147046_5_.prevRotationYawHead = var9;
        p_147046_5_.rotationYawHead = var10;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void drawOutlinedBox(AxisAlignedBB box) {
        if (box == null) {
            return;
        }
        ((IEntityRenderer)Minecraft.getMinecraft().entityRenderer).setupCameraTransform(((IMinecraft)Minecraft.getMinecraft()).getTimer().renderPartialTicks, 0);
        GL11.glBegin((int)3);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.minZ);
        GL11.glEnd();
        GL11.glBegin((int)3);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glEnd();
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.minZ);
        GL11.glVertex3d((double)box.maxX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.maxX, (double)box.maxY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.minY, (double)box.maxZ);
        GL11.glVertex3d((double)box.minX, (double)box.maxY, (double)box.maxZ);
        GL11.glEnd();
    }

    public static void drawTracerLine1(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth) {
        boolean temp = Minecraft.getMinecraft().gameSettings.viewBobbing;
        Minecraft.getMinecraft().gameSettings.viewBobbing = false;
        EntityRenderer var10000 = Minecraft.getMinecraft().entityRenderer;
        net.minecraft.util.Timer var10001 = ((IMinecraft)Minecraft.getMinecraft()).getTimer();
        ((IEntityRenderer)Minecraft.getMinecraft().entityRenderer).setupCameraTransform(((IMinecraft)Minecraft.getMinecraft()).getTimer().renderPartialTicks, 2);
        Minecraft.getMinecraft().gameSettings.viewBobbing = temp;
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)1.0f);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)2);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        GL11.glVertex3d((double)0.0, (double)(0.0 + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight()), (double)0.0);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glEnd();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static int getBlockColor(Block block) {
        int color = 0;
        switch (Block.getIdFromBlock(block)) {
            case 14: 
            case 41: {
                color = -1711477173;
                break;
            }
            case 15: 
            case 42: {
                color = -1715420992;
                break;
            }
            case 16: 
            case 173: {
                color = -1724434633;
                break;
            }
            case 21: 
            case 22: {
                color = -1726527803;
                break;
            }
            case 49: {
                color = -1724108714;
                break;
            }
            case 54: 
            case 146: {
                color = -1711292672;
                break;
            }
            case 56: 
            case 57: 
            case 138: {
                color = -1721897739;
                break;
            }
            case 61: 
            case 62: {
                color = -1711395081;
                break;
            }
            case 73: 
            case 74: 
            case 152: {
                color = -1711341568;
                break;
            }
            case 89: {
                color = -1712594866;
                break;
            }
            case 129: 
            case 133: {
                color = -1726489246;
                break;
            }
            case 130: {
                color = -1713438249;
                break;
            }
            case 52: {
                color = 805728308;
                break;
            }
            default: {
                color = -1711276033;
            }
        }
        return color == 0 ? 806752583 : color;
    }
    
    public static void drawFilledCircle(double x, double y, double r, int c, int id) {
		float f = (float) (c >> 24 & 0xff) / 255F;
		float f1 = (float) (c >> 16 & 0xff) / 255F;
		float f2 = (float) (c >> 8 & 0xff) / 255F;
		float f3 = (float) (c & 0xff) / 255F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glBegin(GL11.GL_POLYGON);
		if (id == 1) {
			GL11.glVertex2d(x, y);
			for (int i = 0; i <= 90; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2d(x - x2, y - y2);
			}
		} else if (id == 2) {
			GL11.glVertex2d(x, y);
			for (int i = 90; i <= 180; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2d(x - x2, y - y2);
			}
		} else if (id == 3) {
			GL11.glVertex2d(x, y);
			for (int i = 270; i <= 360; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2d(x - x2, y - y2);
			}
		} else if (id == 4) {
			GL11.glVertex2d(x, y);
			for (int i = 180; i <= 270; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2d(x - x2, y - y2);
			}
		} else {
			for (int i = 0; i <= 360; i++) {
				double x2 = Math.sin((i * 3.141526D / 180)) * r;
				double y2 = Math.cos((i * 3.141526D / 180)) * r;
				GL11.glVertex2f((float) (x - x2), (float) (y - y2));
			}
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}
    
    public static void drawFullCircle(int cx, int cy, double r, int segments, float lineWidth, int part, int c) {
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        r *= 2.0;
        cx *= 2;
        cy *= 2;
        float f2 = (float)(c >> 24 & 255) / 255.0f;
        float f22 = (float)(c >> 16 & 255) / 255.0f;
        float f3 = (float)(c >> 8 & 255) / 255.0f;
        float f4 = (float)(c & 255) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)lineWidth);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4f((float)f22, (float)f3, (float)f4, (float)f2);
        GL11.glBegin((int)3);
        int i = segments - part;
        while (i <= segments) {
            double x = Math.sin((double)i * 3.141592653589793 / 180.0) * r;
            double y = Math.cos((double)i * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d((double)((double)cx + x), (double)((double)cy + y));
            ++i;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
    }
    
	public static int reAlpha(int color, float alpha) {
		Color c = new Color(color);
		float r = ((float) 1 / 255) * c.getRed();
		float g = ((float) 1 / 255) * c.getGreen();
		float b = ((float) 1 / 255) * c.getBlue();
		return new Color(r, g, b, alpha).getRGB();
	}
    
    public static void drawBox(Box box) {
        if (box == null) {
            return;
        }
        // back
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        // left
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        // right
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glEnd();
        // front
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glEnd();
        // top
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.maxX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.minZ);
        GL11.glVertex3d(box.minX, box.maxY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.maxY, box.maxZ);
        GL11.glEnd();
        // bottom
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glEnd();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(box.maxX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.minZ);
        GL11.glVertex3d(box.minX, box.minY, box.maxZ);
        GL11.glVertex3d(box.maxX, box.minY, box.maxZ);
        GL11.glEnd();
    }
	
	public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }
	
	public static double interpolate(double newPos, double oldPos) {
		return oldPos + (newPos - oldPos) * (double)((IMinecraft)Minecraft.getMinecraft()).getTimer().renderPartialTicks;
	}
	
	public static Color rainbowEffect(int delay) {
        float hue = (float) (System.nanoTime() + delay) / 2.0E10F % 1.0F;
        Color color = new Color((int) Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()), 16));
        return new Color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
    }
    public static void drawFullscreenImage(ResourceLocation image) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.glBlendFunc((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDisable((int)3008);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture((int)0, (int)0, (float)0.0f, (float)0.0f, (int)scaledResolution.getScaledWidth(), (int)scaledResolution.getScaledHeight(), (float)scaledResolution.getScaledWidth(), (float)scaledResolution.getScaledHeight());
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3008);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawPlayerHead(String playerName, int x, int y, int width, int height) {
    	for (Object player : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
            if(player instanceof EntityPlayer) {
            	EntityPlayer ply = (EntityPlayer)player;
            	if (playerName.equalsIgnoreCase(ply.getName())) {
                	GameProfile profile = new GameProfile(ply.getUniqueID(), ply.getName());
                    NetworkPlayerInfo networkplayerinfo1 = new NetworkPlayerInfo(profile);
                    ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
                    GL11.glDisable((int)2929);
                    GL11.glEnable((int)3042);
                    GL11.glDepthMask((boolean)false);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
                    GL11.glDepthMask((boolean)true);
                    GL11.glDisable((int)3042);
                    GL11.glEnable((int)2929);
                }
            }
        }
    }

    public static double getAnimationState(double animation, double finalState, double speed) {
		float add = (float) (0.01 * speed);
		if (animation < finalState) {
			if (animation + add < finalState)
				animation += add;
			else
				animation = finalState;
		} else {
			if (animation - add > finalState)
				animation -= add;
			else
				animation = finalState;
		}
		return animation;
	}
    
    public static String getShaderCode(InputStreamReader file) {
        String shaderSource = "";
        try {
            String line;
            BufferedReader reader = new BufferedReader((Reader)file);
            while ((line = reader.readLine()) != null) {
                shaderSource = String.valueOf((Object)shaderSource) + line + "\n";
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit((int)-1);
        }
        return shaderSource.toString();
    } 

    public static void drawOutlinedRect(int x, int y, int width, int height, int lineSize, Color lineColor, Color backgroundColor) {
        RenderUtil.drawRect(x, y, width, height, backgroundColor.getRGB());
        RenderUtil.drawRect(x, y, width, y + lineSize, lineColor.getRGB());
        RenderUtil.drawRect(x, height - lineSize, width, height, lineColor.getRGB());
        RenderUtil.drawRect(x, y + lineSize, x + lineSize, height - lineSize, lineColor.getRGB());
        RenderUtil.drawRect(width - lineSize, y + lineSize, width, height - lineSize, lineColor.getRGB());
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.glBlendFunc((int)770, (int)771, (int)1, (int)0);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getRed() / 255.0f), (float)1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture((int)x, (int)y, (float)0.0f, (float)0.0f, (int)width, (int)height, (float)width, (float)height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void doGlScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        int scaleFactor = 1;
        int k = mc.gameSettings.guiScale;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glScissor((int)(x * scaleFactor), (int)(mc.displayHeight - (y + height) * scaleFactor), (int)(width * scaleFactor), (int)(height * scaleFactor));
    }

    public static void drawRect(float left, float top, float right, float bottom, int color) {
        if (left < right) {
            float i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
        	float j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawRect(int left, int top, int right, int bottom, int color)
    {
        if (left < right)
        {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
        	double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
        	double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos((double)left, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0D).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0D).endVertex();
        worldrenderer.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void color(int color) {
        float f = (float)(color >> 24 & 255) / 255.0f;
        float f1 = (float)(color >> 16 & 255) / 255.0f;
        float f2 = (float)(color >> 8 & 255) / 255.0f;
        float f3 = (float)(color & 255) / 255.0f;
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
    }

    public static int createShader(String shaderCode, int shaderType) throws Exception {
        int shader;
        block4 : {
            shader = 0;
            try {
                shader = ARBShaderObjects.glCreateShaderObjectARB((int)shaderType);
                if (shader != 0) break block4;
                return 0;
            }
            catch (Exception exc) {
                ARBShaderObjects.glDeleteObjectARB((int)shader);
                throw exc;
            }
        }
        ARBShaderObjects.glShaderSourceARB((int)shader, (CharSequence)shaderCode);
        ARBShaderObjects.glCompileShaderARB((int)shader);
        if (ARBShaderObjects.glGetObjectParameteriARB((int)shader, (int)35713) == 0) {
            throw new RuntimeException("Error creating shader:");
        }
        return shader;
    }

    public void drawCircle(int x, int y, float radius, int color) {
        float alpha = (float)(color >> 24 & 255) / 255.0f;
        float red = (float)(color >> 16 & 255) / 255.0f;
        float green = (float)(color >> 8 & 255) / 255.0f;
        float blue = (float)(color & 255) / 255.0f;
        boolean blend = GL11.glIsEnabled((int)3042);
        boolean line = GL11.glIsEnabled((int)2848);
        boolean texture = GL11.glIsEnabled((int)3553);
        if (!blend) {
            GL11.glEnable((int)3042);
        }
        if (!line) {
            GL11.glEnable((int)2848);
        }
        if (texture) {
            GL11.glDisable((int)3553);
        }
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)9);
        int i = 0;
        while (i <= 360) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)((double)i * 3.141526 / 180.0)) * (double)radius), (double)((double)y + Math.cos((double)((double)i * 3.141526 / 180.0)) * (double)radius));
            ++i;
        }
        GL11.glEnd();
        if (texture) {
            GL11.glEnable((int)3553);
        }
        if (!line) {
            GL11.glDisable((int)2848);
        }
        if (!blend) {
            GL11.glDisable((int)3042);
        }
    }
    
    public static void renderOne(float width) {
        checkSetupFBO();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(width);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glClearStencil(0xF);
        GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xF);
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    public static void renderTwo() {
        GL11.glStencilFunc(GL11.GL_NEVER, 0, 0xF);
        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    }

    public static void renderThree() {
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
    }

    public static void renderFour() {
        setColor(new Color(255, 255, 255));
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
        GL11.glPolygonOffset(1.0F, -2000000F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
    }

    public static void renderFive() {
        GL11.glPolygonOffset(1.0F, 2000000F);
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_STENCIL_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glPopAttrib();
    }

    public static void setColor(Color c) {
        GL11.glColor4d(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
    }

    public static void checkSetupFBO() {
        Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
        if (fbo != null) {
            if (fbo.depthBuffer > -1) {
                setupFBO(fbo);
                fbo.depthBuffer = -1;
            }
        }
    }
    public static void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
        int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
        EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)lineWidth);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)2896);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glLineWidth((float)lineWidth);
        GL11.glColor4f((float)lineRed, (float)lineGreen, (float)lineBlue, (float)lineAlpha);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawSolidBlockESP(double x, double y, double z, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glColor3f(1, 1, 1);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawOutlinedEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawSolidEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glLineWidth((float)lineWdith);
        GL11.glColor4f((float)lineRed, (float)lineGreen, (float)lineBlue, (float)lineAlpha);
        RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }
    
    public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glColor4f(red, green, blue, alpha);
		drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width , y + height, z + width));
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

    private static void glColor(Color color) {
        GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
    }

    public static void drawFilledBox(AxisAlignedBB mask) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(mask.minX, mask.minY, mask.minZ).endVertex();
        worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(mask.minX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.maxZ);
        worldRenderer.pos(mask.minX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.minX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.minZ);
        worldRenderer.pos(mask.maxX, mask.maxY, mask.maxZ);
        worldRenderer.pos(mask.maxX, mask.minY, mask.maxZ);
        tessellator.draw();
    }
    
    public static void circle(final float x, final float y, final float radius, final int fill) {
        arc(x, y, 0.0f, 360.0f, radius, fill);
    }
    
    public static void circle(final float x, final float y, final float radius, final Color fill) {
        arc(x, y, 0.0f, 360.0f, radius, fill);
    }
    
    public static void arc(final float x, final float y, final float start, final float end, final float radius, final int color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }
    
    public static void arc(final float x, final float y, final float start, final float end, final float radius, final Color color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }
    
    public static void arcEllipse(final float x, final float y, float start, float end, final float w, final float h, final int color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        final Tessellator var15 = Tessellator.getInstance();
        final WorldRenderer var16 = var15.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var12, var13, var14, var11);
        if (var11 > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (float i = end; i >= start; i -= 4.0f) {
                final float ldx = (float)Math.cos(i * 3.141592653589793 / 180.0) * w * 1.001f;
                final float ldy = (float)Math.sin(i * 3.141592653589793 / 180.0) * h * 1.001f;
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (float i = end; i >= start; i -= 4.0f) {
            final float ldx = (float)Math.cos(i * 3.141592653589793 / 180.0) * w;
            final float ldy = (float)Math.sin(i * 3.141592653589793 / 180.0) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void arcEllipse(final float x, final float y, float start, float end, final float w, final float h, final Color color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        final Tessellator var9 = Tessellator.getInstance();
        final WorldRenderer var10 = var9.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        if (color.getAlpha() > 0.5f) {
            GL11.glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (float i = end; i >= start; i -= 4.0f) {
                final float ldx = (float)Math.cos(i * 3.141592653589793 / 180.0) * w * 1.001f;
                final float ldy = (float)Math.sin(i * 3.141592653589793 / 180.0) * h * 1.001f;
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            GL11.glDisable(2848);
        }
        GL11.glBegin(6);
        for (float i = end; i >= start; i -= 4.0f) {
            final float ldx = (float)Math.cos(i * 3.141592653589793 / 180.0) * w;
            final float ldy = (float)Math.sin(i * 3.141592653589793 / 180.0) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public class Box {
    	
    	public double minX;
        public double minY;
        public double minZ;
        public double maxX;
        public double maxY;
        public double maxZ;

        public Box(double x, double y, double z, double x1, double y1, double z1) {
            minX = x;
            minY = y;
            minZ = z;
            maxX = x1;
            maxY = y1;
            maxZ = z1;
        }
    }

    public static void drawFilledCircle(float xx, float yy, float radius, Color col)
    {
        int sections = 50;
        double dAngle = 6.283185307179586D / sections;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (int i = 0; i < sections; i++)
        {
            float x = (float)(radius * Math.sin(i * dAngle));
            float y = (float)(radius * Math.cos(i * dAngle));

            GL11.glColor4f(col.getRed() / 255.0F, col.getGreen() / 255.0F, col.getBlue() / 255.0F, col.getAlpha() / 255.0F);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(int xx, int yy, float radius, int col)
    {
        float f = (col >> 24 & 0xFF) / 255.0F;
        float f1 = (col >> 16 & 0xFF) / 255.0F;
        float f2 = (col >> 8 & 0xFF) / 255.0F;
        float f3 = (col & 0xFF) / 255.0F;

        int sections = 50;
        double dAngle = 6.283185307179586D / sections;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glBegin(6);
        for (int i = 0; i < sections; i++)
        {
            float x = (float)(radius * Math.sin(i * dAngle));
            float y = (float)(radius * Math.cos(i * dAngle));

            GL11.glColor4f(f1, f2, f3, f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(float xx, float yy, float radius, int col)
    {
        float f = (col >> 24 & 0xFF) / 255.0F;
        float f1 = (col >> 16 & 0xFF) / 255.0F;
        float f2 = (col >> 8 & 0xFF) / 255.0F;
        float f3 = (col & 0xFF) / 255.0F;

        int sections = 50;
        double dAngle = 6.283185307179586D / sections;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (int i = 0; i < sections; i++)
        {
            float x = (float)(radius * Math.sin(i * dAngle));
            float y = (float)(radius * Math.cos(i * dAngle));

            GL11.glColor4f(f1, f2, f3, f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(int xx, int yy, float radius, int col, int xLeft, int yAbove, int xRight, int yUnder)
    {
        float f = (col >> 24 & 0xFF) / 255.0F;
        float f1 = (col >> 16 & 0xFF) / 255.0F;
        float f2 = (col >> 8 & 0xFF) / 255.0F;
        float f3 = (col & 0xFF) / 255.0F;

        int sections = 50;
        double dAngle = 6.283185307179586D / sections;

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (int i = 0; i < sections; i++)
        {
            float x = (float)(radius * Math.sin(i * dAngle));
            float y = (float)(radius * Math.cos(i * dAngle));

            float xEnd = xx + x;
            float yEnd = yy + y;
            if (xEnd < xLeft) {
                xEnd = xLeft;
            }
            if (xEnd > xRight) {
                xEnd = xRight;
            }
            if (yEnd < yAbove) {
                yEnd = yAbove;
            }
            if (yEnd > yUnder) {
                yEnd = yUnder;
            }
            GL11.glColor4f(f1, f2, f3, f);
            GL11.glVertex2f(xEnd, yEnd);
        }
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    public static void drawLine(float x, float y, float x2, float y2, Color color)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glEnable(2848);
        GL11.glLineWidth(1.0F);
        GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
        worldrenderer.begin(1, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, y, 0.0D).endVertex();
        worldrenderer.pos(x2, y2, 0.0D).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor) {
        rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x + width, y, x1 - width, y + width, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x, y, x + width, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x1 - width, y, x1, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
     }
    
    public static void rectangle(double left, double top, double right, double bottom, int color) {
	      double var5;
	      if(left < right) {
	         var5 = left;
	         left = right;
	         right = var5;
	      }

	      if(top < bottom) {
	         var5 = top;
	         top = bottom;
	         bottom = var5;
	      }

	      float var11 = (float)(color >> 24 & 255) / 255.0F;
	      float var6 = (float)(color >> 16 & 255) / 255.0F;
	      float var7 = (float)(color >> 8 & 255) / 255.0F;
	      float var8 = (float)(color & 255) / 255.0F;
	      Tessellator tessellator = Tessellator.getInstance();
	      WorldRenderer worldRenderer = tessellator.getWorldRenderer();
	      GlStateManager.enableBlend();
	      GlStateManager.disableTexture2D();
	      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	      GlStateManager.color(var6, var7, var8, var11);
	      worldRenderer.begin(7, DefaultVertexFormats.POSITION);
	      worldRenderer.pos(left, bottom, 0.0D).endVertex();
	      worldRenderer.pos(right, bottom, 0.0D).endVertex();
	      worldRenderer.pos(right, top, 0.0D).endVertex();
	      worldRenderer.pos(left, top, 0.0D).endVertex();
	      tessellator.draw();
	      GlStateManager.enableTexture2D();
	      GlStateManager.disableBlend();
	      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	   }

	public static void drawArc(float n, float n2, double n3, final int n4, final int n5, final double n6, final int n7) {
        n3 *= 2.0;
        n *= 2.0f;
        n2 *= 2.0f;
        final float n8 = (n4 >> 24 & 0xFF) / 255.0f;
        final float n9 = (n4 >> 16 & 0xFF) / 255.0f;
        final float n10 = (n4 >> 8 & 0xFF) / 255.0f;
        final float n11 = (n4 & 0xFF) / 255.0f;
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glLineWidth((float)n7);
        GL11.glEnable(2848);
        GL11.glColor4f(n9, n10, n11, n8);
        GL11.glBegin(3);
        int n12 = n5;
        while (n12 <= n6) {
            GL11.glVertex2d(n + Math.sin(n12 * 3.141592653589793 / 180.0) * n3, n2 + Math.cos(n12 * 3.141592653589793 / 180.0) * n3);
            ++n12;
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

	public static void drawBorderRect(float left, float top, float right, float bottom, int bcolor, int icolor,
            float f) {
        Gui.drawRect((int)left + (int)f, (int)top + (int)f, (int)right - (int)f, (int)bottom - (int)f, (int)icolor);
        Gui.drawRect((int)left,(int) top, (int)left + (int)f,(int) bottom,(int) bcolor);
        Gui.drawRect((int)left + (int)f,(int) top, (int)right, (int)top + (int)f,(int) bcolor);
        Gui.drawRect((int)left + (int)f, (int)bottom - (int)f,(int) right,(int) bottom, (int)bcolor);
        Gui.drawRect((int)right - (int)f,(int) top + (int)f, (int)right, (int)bottom - (int)f, (int)bcolor);
    }
	
	public static void drawBorderRect(double x, double y, double x1, double y1, int color, double lwidth) {
		drawHLine(x, y, x1, y, (float) lwidth, color);
		drawHLine(x1, y, x1, y1, (float) lwidth, color);
		drawHLine(x, y1, x1, y1, (float) lwidth, color);
		drawHLine(x, y1, x, y, (float) lwidth, color);
	}
	
	public static void drawHLine(double x, double y, double x1, double y1, float width, int color) {
		float var11 = (color >> 24 & 0xFF) / 255.0F;
		float var6 = (color >> 16 & 0xFF) / 255.0F;
		float var7 = (color >> 8 & 0xFF) / 255.0F;
		float var8 = (color & 0xFF) / 255.0F;
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(var6, var7, var8, var11);
		GL11.glPushMatrix();
		GL11.glLineWidth(width);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x1, y1);
		GL11.glEnd();

		GL11.glLineWidth(1);

		GL11.glPopMatrix();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.color(1, 1, 1, 1);

	}
	
	public static void whatTheFuckOpenGLThisFixesItemGlint() {
	      GlStateManager.disableLighting();
	      GlStateManager.disableDepth();
	      GlStateManager.disableBlend();
	      GlStateManager.enableLighting();
	      GlStateManager.enableDepth();
	      GlStateManager.disableLighting();
	      GlStateManager.disableDepth();
	      GlStateManager.disableTexture2D();
	      GlStateManager.disableAlpha();
	      GlStateManager.disableBlend();
	      GlStateManager.enableBlend();
	      GlStateManager.enableAlpha();
	      GlStateManager.enableTexture2D();
	      GlStateManager.enableLighting();
	      GlStateManager.enableDepth();
	   }
	
	public static void renderEnchantText(ItemStack stack, int x, int y) {
	      int encY = y - 24;
	      int sLevel;
	      int kLevel;
	      int fLevel;
	      if (stack.getItem() instanceof ItemArmor) {
	         sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack);
	         kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
	         fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
	         if (sLevel > 0) {
	            mc.fontRendererObj.drawString("p" + sLevel, (int)(x * 2), (int)encY, 16777215);
	            encY += 7;
	         }

	         if (kLevel > 0) {
	            mc.fontRendererObj.drawString("t" + kLevel, (int)(x * 2), (int)encY, 16777215);
	            encY += 7;
	         }

	         if (fLevel > 0) {
	            mc.fontRendererObj.drawString("u" + fLevel, (int)(x * 2), (int)encY, 16777215);
	            encY += 7;
	         }
	      }

	      int uLevel;
	      if (stack.getItem() instanceof ItemBow) {
	         sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
	         kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
	         fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
	         uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
	         if (sLevel > 0) {
	            mc.fontRendererObj.drawString("d" + sLevel, (int)(x * 2), (int)encY, 16777215);
	            encY += 7;
	         }

	         if (kLevel > 0) {
	            mc.fontRendererObj.drawString("k" + kLevel, (int)(x * 2), (int)encY, 16777215);
	            encY += 7;
	         }

	         if (fLevel > 0) {
	            mc.fontRendererObj.drawString("f" + fLevel, (int)(x * 2), (int)encY, 16777215);
	            encY += 7;
	         }

	         if (uLevel > 0) {
	            mc.fontRendererObj.drawString("u" + uLevel, (int)(x * 2), (int)encY, 16777215);
	            encY += 7;
	         }
	      }

	      if (stack.getItem() instanceof ItemSword) {
	         sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack);
	         kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack);
	         fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
	         uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
	         if (sLevel > 0) {
	            mc.fontRendererObj.drawString("s" + sLevel, (int)(x * 2), (int)encY, 16777215);
	            encY += 7;
	         }

	         if (kLevel > 0) {
	            mc.fontRendererObj.drawString("k" + kLevel, (int)(x * 2), (int)encY, 16777215);
	            encY += 7;
	         }

	         if (fLevel > 0) {
	            mc.fontRendererObj.drawString("f" + fLevel, (int)(x * 2), (int)encY, 16777215);
	            encY += 7;
	         }

	         if (uLevel > 0) {
	            mc.fontRendererObj.drawString("u" + uLevel, (int)(x * 2), (int)encY, 16777215);
	         }
	      }

	   }

	public static void renderItemStack(ItemStack stack, int x, int y) {
      GL11.glPushMatrix();
      GL11.glDepthMask(true);
      GlStateManager.clear(256);
      RenderHelper.enableStandardItemLighting();
      mc.getRenderItem().zLevel = -150.0F;
      whatTheFuckOpenGLThisFixesItemGlint();
      mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
      mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, y);
      mc.getRenderItem().zLevel = 0.0F;
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableCull();
      GlStateManager.enableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.disableLighting();
      GlStateManager.scale(0.5D, 0.5D, 0.5D);
      GlStateManager.disableDepth();
      renderEnchantText(stack, x, y);
      GlStateManager.enableDepth();
      GlStateManager.scale(2.0F, 2.0F, 2.0F);
      GL11.glPopMatrix();
   }
	
	public static void drawImage(final ResourceLocation resourceLocation, final int n, final int n2, final int n3, final int n4) {
        drawImage(resourceLocation, n, n2, n3, n4, 1.0f);
    }
    
    public static void drawImage(final ResourceLocation resourceLocation, final float n, final float n2, final float n3, final float n4) {
        drawImage(resourceLocation, (int)n, (int)n2, (int)n3, (int)n4, 1.0f);
    }
    
    public static void drawImage(final ResourceLocation resourceLocation, final int n, final int n2, final int n3, final int n4, final float n5) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, n5);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture(n, n2, 0.0f, 0.0f, n3, n4, (float)n3, (float)n4);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static void drawRoundedRect(float n, float n2, float n3, float n4, final int n5, final int n6) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(n *= 2.0f, (n2 *= 2.0f) + 1.0f, (n4 *= 2.0f) - 2.0f, n5);
        drawVLine((n3 *= 2.0f) - 1.0f, n2 + 1.0f, n4 - 2.0f, n5);
        drawHLine(n + 2.0f, n3 - 3.0f, n2, n5);
        drawHLine(n + 2.0f, n3 - 3.0f, n4 - 1.0f, n5);
        drawHLine(n + 1.0f, n + 1.0f, n2 + 1.0f, n5);
        drawHLine(n3 - 2.0f, n3 - 2.0f, n2 + 1.0f, n5);
        drawHLine(n3 - 2.0f, n3 - 2.0f, n4 - 2.0f, n5);
        drawHLine(n + 1.0f, n + 1.0f, n4 - 2.0f, n5);
        drawRect(n + 1.0f, n2 + 1.0f, n3 - 1.0f, n4 - 1.0f, n6);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void drawRoundedRect(float n, float n2, float n3, float n4, final float n5, final int n6) {
        n += (float)(n5 / 2.0f + 0.5);
        n2 += (float)(n5 / 2.0f + 0.5);
        n3 -= (float)(n5 / 2.0f + 0.5);
        n4 -= (float)(n5 / 2.0f + 0.5);
        drawRect(n, n2, n3, n4, n6);
        circle(n3 - n5 / 2.0f, n2 + n5 / 2.0f, n5, n6);
        circle(n + n5 / 2.0f, n4 - n5 / 2.0f, n5, n6);
        circle(n + n5 / 2.0f, n2 + n5 / 2.0f, n5, n6);
        circle(n3 - n5 / 2.0f, n4 - n5 / 2.0f, n5, n6);
        drawRect(n - n5 / 2.0f - 0.5f, n2 + n5 / 2.0f, n3, n4 - n5 / 2.0f, n6);
        drawRect(n, n2 + n5 / 2.0f, n3 + n5 / 2.0f + 0.5f, n4 - n5 / 2.0f, n6);
        drawRect(n + n5 / 2.0f, n2 - n5 / 2.0f - 0.5f, n3 - n5 / 2.0f, n4 - n5 / 2.0f, n6);
        drawRect(n + n5 / 2.0f, n2, n3 - n5 / 2.0f, n4 + n5 / 2.0f + 0.5f, n6);
    }

	public static void prepareScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        GL11.glScissor((int)((int)(x * (float)factor)), (int)((int)(((float)scale.getScaledHeight() - y2) * (float)factor)), (int)((int)((x2 - x) * (float)factor)), (int)((int)((y2 - y) * (float)factor)));
    }
	
	public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        RenderUtil.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }
	
	public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }
        RenderUtil.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }

	public static boolean isHovering(int n, int n2, int n3, int n4, int n5, int n6) {
        return (n > n3 && n < n5 && n2 > n4 && n2 < n6 ? 1 : 0) != 0;
    }

	public static boolean isHovering(int mouseX, int mouseY, float f, float c, float g, float h) {
		return (mouseX > f && mouseX < g && mouseY > c && mouseY < h ? 1 : 0) != 0;
	}

	public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)right, (double)top, (double)zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)left, (double)top, (double)zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)left, (double)bottom, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos((double)right, (double)bottom, (double)zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}

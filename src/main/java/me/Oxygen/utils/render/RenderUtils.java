package me.Oxygen.utils.render;

import java.awt.Color;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.List;

public class RenderUtils {
	//public static final Tessellation tessellator;
	private static final List<Integer> csBuffer;
	private static final Consumer<Integer> ENABLE_CLIENT_STATE;
	private static final Consumer<Integer> DISABLE_CLIENT_STATE;

	static {
		//tessellator = Tessellation.createExpanding(4, 1.0f, 2.0f);
		csBuffer = new ArrayList<Integer>();
		ENABLE_CLIENT_STATE = GL11::glEnableClientState;
		DISABLE_CLIENT_STATE = GL11::glEnableClientState;
	}

	/*public RenderUtil() {
		super();
	}*/

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

	public static int width() {
		return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
	}

	public static int height() {
		return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
	}

	/*public static double interpolation(final double newPos, final double oldPos) {
		return oldPos + (newPos - oldPos) * Helper.mc.timer.renderPartialTicks;
	}*/

	public static class R3DUtils {
		/*public static void startDrawing() {
			GL11.glEnable((int) 3042);
			GL11.glEnable((int) 3042);
			GL11.glBlendFunc((int) 770, (int) 771);
			GL11.glEnable((int) 2848);
			GL11.glDisable((int) 3553);
			GL11.glDisable((int) 2929);
			Minecraft.getMinecraft().entityRenderer
					.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
		}*/

		public static void stopDrawing() {
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 3553);
			GL11.glDisable((int) 2848);
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 2929);
		}

		/*public static void drawOutlinedBox(AxisAlignedBB box) {
			if (box == null) {
				return;
			}
			Minecraft.getMinecraft().entityRenderer
					.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
			GL11.glBegin((int) 3);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 3);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 1);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
			GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
			GL11.glEnd();
		}*/

		/*public static void drawBoundingBox(AxisAlignedBB aabb) {
			WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
			Tessellator tessellator = Tessellator.getInstance();
			Minecraft.getMinecraft().entityRenderer
					.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
			tessellator.draw();
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
			tessellator.draw();
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
			tessellator.draw();
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
			tessellator.draw();
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
			tessellator.draw();
			worldRenderer.startDrawingQuads();
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.maxZ);
			worldRenderer.addVertex(aabb.minX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.minX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.minZ);
			worldRenderer.addVertex(aabb.maxX, aabb.maxY, aabb.maxZ);
			worldRenderer.addVertex(aabb.maxX, aabb.minY, aabb.maxZ);
			tessellator.draw();
		}*/

		public static void drawFilledBox(AxisAlignedBB mask) {
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glEnd();
		}

		public static void drawOutlinedBoundingBox(AxisAlignedBB aabb) {
			GL11.glBegin((int) 3);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.minY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 3);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.maxY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 1);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.minY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.maxY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.maxZ);
			GL11.glEnd();
		}
	}

	public static class R2DUtils {
		public static void enableGL2D() {
			GL11.glDisable((int) 2929);
			GL11.glEnable((int) 3042);
			GL11.glDisable((int) 3553);
			GL11.glBlendFunc((int) 770, (int) 771);
			GL11.glDepthMask((boolean) true);
			GL11.glEnable((int) 2848);
			GL11.glHint((int) 3154, (int) 4354);
			GL11.glHint((int) 3155, (int) 4354);
		}

		public static void disableGL2D() {
			GL11.glEnable((int) 3553);
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 2929);
			GL11.glDisable((int) 2848);
			GL11.glHint((int) 3154, (int) 4352);
			GL11.glHint((int) 3155, (int) 4352);
		}

		/*public static void draw2DCorner(Entity e, double posX, double posY, double posZ, int color) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(posX, posY, posZ);
			GL11.glNormal3f((float) 0.0f, (float) 0.0f, (float) 0.0f);
			GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
			GlStateManager.scale(-0.1, -0.1, 0.1);
			GL11.glDisable((int) 2896);
			GL11.glDisable((int) 2929);
			GL11.glEnable((int) 3042);
			GL11.glBlendFunc((int) 770, (int) 771);
			GlStateManager.depthMask(true);
			R2DUtils.drawRect(7.0, -20.0, 7.300000190734863, -17.5, color);
			R2DUtils.drawRect(-7.300000190734863, -20.0, -7.0, -17.5, color);
			R2DUtils.drawRect(4.0, -20.299999237060547, 7.300000190734863, -20.0, color);
			R2DUtils.drawRect(-7.300000190734863, -20.299999237060547, -4.0, -20.0, color);
			R2DUtils.drawRect(-7.0, 3.0, -4.0, 3.299999952316284, color);
			R2DUtils.drawRect(4.0, 3.0, 7.0, 3.299999952316284, color);
			R2DUtils.drawRect(-7.300000190734863, 0.8, -7.0, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, 0.5, 7.300000190734863, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, -20.0, 7.300000190734863, -17.5, color);
			R2DUtils.drawRect(-7.300000190734863, -20.0, -7.0, -17.5, color);
			R2DUtils.drawRect(4.0, -20.299999237060547, 7.300000190734863, -20.0, color);
			R2DUtils.drawRect(-7.300000190734863, -20.299999237060547, -4.0, -20.0, color);
			R2DUtils.drawRect(-7.0, 3.0, -4.0, 3.299999952316284, color);
			R2DUtils.drawRect(4.0, 3.0, 7.0, 3.299999952316284, color);
			R2DUtils.drawRect(-7.300000190734863, 0.8, -7.0, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, 0.5, 7.300000190734863, 3.299999952316284, color);
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 2929);
			GlStateManager.popMatrix();
		}*/

		public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
			R2DUtils.enableGL2D();
			GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
			R2DUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
			R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
			R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
			R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
			R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
			R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
			R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
			R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
			R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
			GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
			R2DUtils.disableGL2D();
			Gui.drawRect(0, 0, 0, 0, 0);
		}

		public static void drawRect(double x2, double y2, double x1, double y1, int color) {
			R2DUtils.enableGL2D();
			R2DUtils.glColor(color);
			R2DUtils.drawRect(x2, y2, x1, y1);
			R2DUtils.disableGL2D();
		}

		private static void drawRect(double x2, double y2, double x1, double y1) {
			GL11.glBegin((int) 7);
			GL11.glVertex2d((double) x2, (double) y1);
			GL11.glVertex2d((double) x1, (double) y1);
			GL11.glVertex2d((double) x1, (double) y2);
			GL11.glVertex2d((double) x2, (double) y2);
			GL11.glEnd();
		}

		public static void glColor(int hex) {
			float alpha = (float) (hex >> 24 & 255) / 255.0f;
			float red = (float) (hex >> 16 & 255) / 255.0f;
			float green = (float) (hex >> 8 & 255) / 255.0f;
			float blue = (float) (hex & 255) / 255.0f;
			GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
		}

		public static void drawRect(float x, float y, float x1, float y1, int color) {
			R2DUtils.enableGL2D();
			glColor(color);
			R2DUtils.drawRect(x, y, x1, y1);
			R2DUtils.disableGL2D();
		}

		public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
			R2DUtils.enableGL2D();
			glColor(borderColor);
			R2DUtils.drawRect(x + width, y, x1 - width, y + width);
			R2DUtils.drawRect(x, y, x + width, y1);
			R2DUtils.drawRect(x1 - width, y, x1, y1);
			R2DUtils.drawRect(x + width, y1 - width, x1 - width, y1);
			R2DUtils.disableGL2D();
		}

		public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
			R2DUtils.enableGL2D();
			GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
			R2DUtils.drawVLine(x *= 2.0f, y *= 2.0f, y1 *= 2.0f, borderC);
			R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
			R2DUtils.drawHLine(x, x1 - 1.0f, y, borderC);
			R2DUtils.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
			R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
			GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
			R2DUtils.disableGL2D();
		}

		public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
			R2DUtils.enableGL2D();
			GL11.glShadeModel((int) 7425);
			GL11.glBegin((int) 7);
			glColor(topColor);
			GL11.glVertex2f((float) x, (float) y1);
			GL11.glVertex2f((float) x1, (float) y1);
			glColor(bottomColor);
			GL11.glVertex2f((float) x1, (float) y);
			GL11.glVertex2f((float) x, (float) y);
			GL11.glEnd();
			GL11.glShadeModel((int) 7424);
			R2DUtils.disableGL2D();
		}

		public static void drawHLine(float x, float y, float x1, int y1) {
			if (y < x) {
				float var5 = x;
				x = y;
				y = var5;
			}
			R2DUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
		}

		public static void drawVLine(float x, float y, float x1, int y1) {
			if (x1 < y) {
				float var5 = y;
				y = x1;
				x1 = var5;
			}
			R2DUtils.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
		}

		public static void drawHLine(float x, float y, float x1, int y1, int y2) {
			if (y < x) {
				float var5 = x;
				x = y;
				y = var5;
			}
			R2DUtils.drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
		}

		public static void drawRect(float x, float y, float x1, float y1) {
			GL11.glBegin((int) 7);
			GL11.glVertex2f((float) x, (float) y1);
			GL11.glVertex2f((float) x1, (float) y1);
			GL11.glVertex2f((float) x1, (float) y);
			GL11.glVertex2f((float) x, (float) y);
			GL11.glEnd();
		}
	}

	public static int getHexRGB(final int hex) {
		return 0xFF000000 | hex;
	}

	public static void drawCustomImage(int x, int y, int width, int height, ResourceLocation image) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc((int) 770, (int) 771, (int) 1, (int) 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0.0f, (float) 0.0f, (int) width, (int) height,
				(float) width, (float) height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
		Gui.drawRect(0, 0, 0, 0, 0);
	}

	/*public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float l1,
			final int col1, final int col2) {
		Gui.drawRect(x, y, x2, y2, col2);
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
	}*/

	public static void pre() {
		GL11.glDisable(2929);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
	}

	public static void post() {
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glColor3d(1.0, 1.0, 1.0);
	}

	/*public static void startDrawing() {
		GL11.glEnable(3042);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		Helper.mc.entityRenderer.setupCameraTransform(Helper.mc.timer.renderPartialTicks, 0);
	}*/

	public static void stopDrawing() {
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
	}

	public static Color blend(final Color color1, final Color color2, final double ratio) {
		final float r = (float) ratio;
		final float ir = 1.0f - r;
		final float[] rgb1 = new float[3];
		final float[] rgb2 = new float[3];
		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);
		final Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir,
				rgb1[2] * r + rgb2[2] * ir);
		return color3;
	}

	/*public static void drawLine(final Vec2f start, final Vec2f end, final float width) {
		drawLine(start.getX(), start.getY(), end.getX(), end.getY(), width);
	}

	public static void drawLine(final Vec3f start, final Vec3f end, final float width) {
		drawLine((float) start.getX(), (float) start.getY(), (float) start.getZ(), (float) end.getX(),
				(float) end.getY(), (float) end.getZ(), width);
	}*/

	/*public static void drawLine(final float x, final float y, final float x1, final float y1, final float width) {
		drawLine(x, y, 0.0f, x1, y1, 0.0f, width);
	}
*/
	/*public static void drawLine(final float x, final float y, final float z, final float x1, final float y1,
			final float z1, final float width) {
		GL11.glLineWidth(width);
		setupRender(true);
		setupClientState(GLClientState.VERTEX, true);
		RenderUtil.tessellator.addVertex(x, y, z).addVertex(x1, y1, z1).draw(3);
		setupClientState(GLClientState.VERTEX, false);
		setupRender(false);
	}

	public static void setupClientState(final GLClientState state, final boolean enabled) {
		RenderUtil.csBuffer.clear();
		if (state.ordinal() > 0) {
			RenderUtil.csBuffer.add(state.getCap());
		}
		RenderUtil.csBuffer.add(32884);
		RenderUtil.csBuffer.forEach(enabled ? RenderUtil.ENABLE_CLIENT_STATE : RenderUtil.DISABLE_CLIENT_STATE);
	}*/

	public static void setupRender(final boolean start) {
		if (start) {
			GlStateManager.enableBlend();
			GL11.glEnable(2848);
			GlStateManager.disableDepth();
			GlStateManager.disableTexture2D();
			GlStateManager.blendFunc(770, 771);
			GL11.glHint(3154, 4354);
		} else {
			GlStateManager.disableBlend();
			GlStateManager.enableTexture2D();
			GL11.glDisable(2848);
			GlStateManager.enableDepth();
		}
		GlStateManager.depthMask(!start);
	}

	public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
	}

	public static void layeredRect(double x1, double y1, double x2, double y2, int outline, int inline,
			int background) {
		R2DUtils.drawRect(x1, y1, x2, y2, outline);
		R2DUtils.drawRect(x1 + 1, y1 + 1, x2 - 1, y2 - 1, inline);
		R2DUtils.drawRect(x1 + 2, y1 + 2, x2 - 2, y2 - 2, background);
	}

	public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green,
			float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
		GL11.glPushMatrix();
		GL11.glEnable((int) 3042);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glDisable((int) 3553);
		GL11.glEnable((int) 2848);
		GL11.glDisable((int) 2929);
		GL11.glDepthMask((boolean) false);
		GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
		RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glLineWidth((float) lineWdith);
		GL11.glColor4f((float) lineRed, (float) lineGreen, (float) lineBlue, (float) lineAlpha);
		RenderUtil
				.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glDisable((int) 2848);
		GL11.glEnable((int) 3553);
		GL11.glEnable((int) 2929);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glPopMatrix();
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

	public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
		float red = 0.003921569F * redRGB;
		float green = 0.003921569F * greenRGB;
		float blue = 0.003921569F * blueRGB;
		GL11.glColor4f(red, green, blue, alpha);
	}
	
    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
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
}

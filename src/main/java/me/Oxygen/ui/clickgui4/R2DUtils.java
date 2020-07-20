package me.Oxygen.ui.clickgui4;

import org.lwjgl.opengl.GL11;

class R2DUtils {
   public static void enableGL2D() {
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glDepthMask(true);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
   }

   public static void disableGL2D() {
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
   }

   public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
      enableGL2D();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      float var6;
      float var7;
      float var9;
      drawVLine(var6 = x * 2.0F, (var7 = y * 2.0F) + 1.0F, (var9 = y1 * 2.0F) - 2.0F, borderC);
      float var8;
      drawVLine((var8 = x1 * 2.0F) - 1.0F, var7 + 1.0F, var9 - 2.0F, borderC);
      drawHLine(var6 + 2.0F, var8 - 3.0F, var7, borderC);
      drawHLine(var6 + 2.0F, var8 - 3.0F, var9 - 1.0F, borderC);
      drawHLine(var6 + 1.0F, var6 + 1.0F, var7 + 1.0F, borderC);
      drawHLine(var8 - 2.0F, var8 - 2.0F, var7 + 1.0F, borderC);
      drawHLine(var8 - 2.0F, var8 - 2.0F, var9 - 2.0F, borderC);
      drawHLine(var6 + 1.0F, var6 + 1.0F, var9 - 2.0F, borderC);
      drawRect(var6 + 1.0F, var7 + 1.0F, var8 - 1.0F, var9 - 1.0F, insideC);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
      RenderUtil.drawRect(0.0D, 0.0D, 0.0D, 0.0D, 0);
   }

   public static void drawRect(double x2, double y2, double x1, double y1, int color) {
      enableGL2D();
      glColor(color);
      drawRect(x2, y2, x1, y1);
      disableGL2D();
   }

   private static void drawRect(double x2, double y2, double x1, double y1) {
      GL11.glBegin(7);
      GL11.glVertex2d(x2, y1);
      GL11.glVertex2d(x1, y1);
      GL11.glVertex2d(x1, y2);
      GL11.glVertex2d(x2, y2);
      GL11.glEnd();
   }

   public static void glColor(int hex) {
      float alpha = (float)(hex >> 24 & 255) / 255.0F;
      float red = (float)(hex >> 16 & 255) / 255.0F;
      float green = (float)(hex >> 8 & 255) / 255.0F;
      float blue = (float)(hex & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
   }

   public static void drawRect(float x, float y, float x1, float y1, int color) {
      enableGL2D();
      glColor(color);
      drawRect(x, y, x1, y1);
      disableGL2D();
   }

   public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
      enableGL2D();
      glColor(borderColor);
      drawRect(x + width, y, x1 - width, y + width);
      drawRect(x, y, x + width, y1);
      drawRect(x1 - width, y, x1, y1);
      drawRect(x + width, y1 - width, x1 - width, y1);
      disableGL2D();
   }

   public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
      enableGL2D();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      float var6;
      float var7;
      float var9;
      drawVLine(var6 = x * 2.0F, var7 = y * 2.0F, var9 = y1 * 2.0F, borderC);
      float var8;
      drawVLine((var8 = x1 * 2.0F) - 1.0F, var7, var9, borderC);
      drawHLine(var6, var8 - 1.0F, var7, borderC);
      drawHLine(var6, var8 - 2.0F, var9 - 1.0F, borderC);
      drawRect(var6 + 1.0F, var7 + 1.0F, var8 - 1.0F, var9 - 1.0F, insideC);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
   }

   public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
      enableGL2D();
      GL11.glShadeModel(7425);
      GL11.glBegin(7);
      glColor(topColor);
      GL11.glVertex2f(x, y1);
      GL11.glVertex2f(x1, y1);
      glColor(bottomColor);
      GL11.glVertex2f(x1, y);
      GL11.glVertex2f(x, y);
      GL11.glEnd();
      GL11.glShadeModel(7424);
      disableGL2D();
   }

   public static void drawHLine(float x, float y, float x1, int y1) {
      if(y < x) {
         float var5 = x;
         x = y;
         y = var5;
      }

      drawRect(x, x1, y + 1.0F, x1 + 1.0F, y1);
   }

   public static void drawVLine(float x, float y, float x1, int y1) {
      if(x1 < y) {
         float var5 = y;
         y = x1;
         x1 = var5;
      }

      drawRect(x, y + 1.0F, x + 1.0F, x1, y1);
   }

   public static void drawHLine(float x, float y, float x1, int y1, int y2) {
      if(y < x) {
         float var5 = x;
         x = y;
         y = var5;
      }

      drawGradientRect(x, x1, y + 1.0F, x1 + 1.0F, y1, y2);
   }

   public static void drawRect(float x, float y, float x1, float y1) {
      GL11.glBegin(7);
      GL11.glVertex2f(x, y1);
      GL11.glVertex2f(x1, y1);
      GL11.glVertex2f(x1, y);
      GL11.glVertex2f(x, y);
      GL11.glEnd();
   }
}

package cn.rainbow.oxygen.gui.buttons;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.gui.font.cfont.CFontRenderer;
import cn.rainbow.oxygen.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class UIFlatButton extends GuiButton {
   private TimeHelper time = new TimeHelper();
   public String displayString;
   public int id;
   public boolean enabled;
   public boolean visible;
   protected boolean hovered;
   private int color;
   private float opacity;
   private CFontRenderer font;

   public UIFlatButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, int color) {
      super(buttonId, x, y, 10, 12, buttonText);
      this.width = widthIn;
      this.height = heightIn;
      this.enabled = true;
      this.visible = true;
      this.id = buttonId;
      this.xPosition = x;
      this.yPosition = y;
      this.displayString = buttonText;
      this.color = color;
      this.font = Oxygen.INSTANCE.fontmanager.wqy16;
   }

   public UIFlatButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, int color, CFontRenderer font) {
      super(buttonId, x, y, 10, 12, buttonText);
      this.width = widthIn;
      this.height = heightIn;
      this.enabled = true;
      this.visible = true;
      this.id = buttonId;
      this.xPosition = x;
      this.yPosition = y;
      this.displayString = buttonText;
      this.color = color;
      this.font = font;
   }

   protected int getHoverState(boolean mouseOver) {
      int i = 1;
      if (!this.enabled) {
         i = 0;
      } else if (mouseOver) {
         i = 2;
      }

      return i;
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if (this.visible) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
         int var5 = this.getHoverState(this.hovered);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.blendFunc(770, 771);
         if (!this.hovered) {
            this.time.reset();
            this.opacity = 0.0F;
         }

         if (this.hovered) {
            this.opacity += 0.5F;
            if (this.opacity > 1.0F) {
               this.opacity = 1.0F;
            }
         }

         float radius = (float)this.height / 2.0F;
         RenderUtil.drawFastRoundedRect((int)(this.xPosition - this.opacity * 0.1f), (float)this.yPosition - this.opacity, (int)((this.xPosition + this.width) + this.opacity * 0.1F), (float)this.yPosition + radius * 2.0F + this.opacity, 1.0F, this.color);
         GL11.glColor3f(2.55F, 2.55F, 2.55F);
         this.mouseDragged(mc, mouseX, mouseY);
         GL11.glPushMatrix();
         GL11.glPushAttrib(1048575);
         GL11.glScaled(1.0D, 1.0D, 1.0D);
         boolean var6 = true;
         float var10000 = (float)this.font.getStringWidth(StringUtils.stripControlCodes(this.displayString));
         this.font.drawString(this.displayString, (float)(this.xPosition + this.width / 2) - 6, (float)this.yPosition + (float)(this.height - this.font.getHeight()) / 2.0F + 0, this.hovered ? -1 : -1);
         GL11.glPopAttrib();
         GL11.glPopMatrix();
      }

   }

   private Color darkerColor(Color c, int step) {
      int red = c.getRed();
      int blue = c.getBlue();
      int green = c.getGreen();
      int var10000;
      if (red >= step) {
         var10000 = red - step;
      }

      if (blue >= step) {
         var10000 = blue - step;
      }

      if (green >= step) {
         var10000 = green - step;
      }

      return c.darker();
   }

   public void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
   }

   public void mouseReleased(int mouseX, int mouseY) {
   }

   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
      return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
   }

   public boolean isMouseOver() {
      return this.hovered;
   }

   public void drawButtonForegroundLayer(int mouseX, int mouseY) {
   }

   public void playPressSound(SoundHandler soundHandlerIn) {
      soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
   }

   public int getButtonWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   class TimeHelper {
      public long lastMs = 0L;

      public boolean isDelayComplete(long delay) {
         if (System.currentTimeMillis() - this.lastMs > delay) {
            return true;
         }
         return false;
      }

      public long getCurrentMS() {
         return System.nanoTime() / 1000000L;
      }

      public void reset() {
         this.lastMs = System.currentTimeMillis();
      }

      public long getLastMs() {
         return this.lastMs;
      }

      public void setLastMs(int i) {
         this.lastMs = System.currentTimeMillis() + (long)i;
      }

      public boolean hasReached(long milliseconds) {
         return this.getCurrentMS() - this.lastMs >= milliseconds;
      }

      public boolean hasReached(float timeLeft) {
         return (float)(this.getCurrentMS() - this.lastMs) >= timeLeft;
      }

      public boolean delay(double nextDelay) {
         return (double)(System.currentTimeMillis() - this.lastMs) >= nextDelay;
      }
   }
}

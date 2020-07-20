package me.Oxygen.ui.particle.particles.particle;

import java.util.Random;

import me.Oxygen.utils.render.Colors;
import me.Oxygen.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ParticleSnow extends Particle {
   private Random random = new Random();
   private ScaledResolution res;

   public void draw(int xAdd) {
      this.prepare();
      this.move();
      this.drawPixel(xAdd);
      this.resetPos();
   }

   private void prepare() {
      this.res = new ScaledResolution(Minecraft.getMinecraft());
   }

   private void drawPixel(int xAdd) {
      float size = 10.0F;

      for(int i = 0; i < 10; ++i) {
         int alpha = Math.min(0, 1 - i / 10);
         RenderUtil.drawFilledCircle(this.vector.x, this.vector.y, size + 1.0F + (float)i * 0.2F, RenderUtil.reAlpha(Colors.WHITE.c, (float)alpha));
      }

      RenderUtil.drawFilledCircle(this.vector.x + (float)xAdd, this.vector.y, 1.1F, RenderUtil.reAlpha(-1, 0.2F));
      RenderUtil.drawFilledCircle(this.vector.x + (float)xAdd, this.vector.y, 0.8F, RenderUtil.reAlpha(-1, 0.4F));
      RenderUtil.drawFilledCircle(this.vector.x + (float)xAdd, this.vector.y, 0.5F, RenderUtil.reAlpha(-1, 0.6F));
      RenderUtil.drawFilledCircle(this.vector.x + (float)xAdd, this.vector.y, 0.3F, RenderUtil.reAlpha(Colors.WHITE.c, 1.0F));
   }

   private void move() {
      float speed = 100.0F;
      this.vector.y += this.random.nextFloat() * 0.25F;
      this.vector.x -= this.random.nextFloat();
   }

   private void resetPos() {
      if (this.vector.x < 0.0F) {
         this.vector.x = (float)this.res.getScaledWidth();
      }

      if (this.vector.y > (float)this.res.getScaledHeight()) {
         this.vector.y = 0.0F;
      }

   }
}

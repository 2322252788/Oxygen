package me.Oxygen.ui.music;

import me.Oxygen.Oxygen;
import me.Oxygen.manager.MusicManager;
import me.Oxygen.modules.render.HUD;
import me.Oxygen.utils.fontRenderer.UnicodeFontRenderer;
import me.Oxygen.utils.render.Colors;
import me.Oxygen.utils.render.RenderUtil;
import me.Oxygen.utils.timer.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public enum Class287 {
   INSTANCE;

   public String ly = "";
   public String tly = "";
   public String downloadProgress = "0";
   public long readedSecs = 0L;
   public long totalSecs = 0L;
   public float animation = 0.0F;
   public TimeHelper timer = new TimeHelper();
   public boolean firstTime = true;
   public Oxygen hanaInstance;
   private static final Class287[] $VALUES = new Class287[]{INSTANCE};

   private Class287() {
      this.hanaInstance = Oxygen.INSTANCE;
   }

   public void renderOverlay() {
      int var1 = HUD.musicPosX.getValueState().intValue();
      int var2 = HUD.musicPosY.getValueState().intValue();
      ScaledResolution var3 = new ScaledResolution(Minecraft.getMinecraft());
      if(this.hanaInstance.MusicMgr.currentTrack != null && MusicManager.instance.mediaPlayer != null) {
         this.readedSecs = (long) MusicManager.instance.mediaPlayer.getCurrentTime().toSeconds();
         this.totalSecs = (long)MusicManager.instance.mediaPlayer.getStopTime().toSeconds();
      }

      if(this.hanaInstance.MusicMgr.currentTrack != null && MusicManager.instance.mediaPlayer != null) {
         this.hanaInstance.font.wqy18.drawString(this.hanaInstance.MusicMgr.currentTrack.getName() + " - " + this.hanaInstance.MusicMgr.currentTrack.getArtists(), 36.0F + var1, (10 + var2), Colors.WHITE.c);
         this.hanaInstance.font.wqy18.drawString(this.formatSeconds((int) this.readedSecs) + "/" + this.formatSeconds((int)this.totalSecs), 36.0F + var1, 20 + var2, -1);
         if(MusicManager.instance.trackImage.containsKey(Long.valueOf(this.hanaInstance.MusicMgr.currentTrack.getId()))) {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            ResourceLocation var4 = this.hanaInstance.MusicMgr.trackImage.get(Long.valueOf(this.hanaInstance.MusicMgr.currentTrack.getId()));
            RenderUtil.drawImage(var4, 4 + var1, 6 + var2, 28, 28);
            GL11.glPopMatrix();
         } else {
            MusicManager.instance.getTrackImage(this.hanaInstance.MusicMgr.currentTrack);
         }

         try {
            float var15 = (float)(MusicManager.instance.mediaPlayer.getCurrentTime().toSeconds() / Math.max(1.0D, MusicManager.instance.mediaPlayer.getStopTime().toSeconds())) * 100.0F;
            //RenderUtil.drawArc(18 + var1, 19 + var2, 14.0D, Colors.WHITE.c, 0, 360.0D, 4);
            //RenderUtil.drawArc(18 + var1, 19 + var2, 14.0D, Colors.BLUE.c, 180, 180.0F + var15 * 3.6F, 4);
         } catch (Exception var14) {
            ;
         }
      }

      UnicodeFontRenderer var16 = Oxygen.INSTANCE.font.wqy25;
      int var5 = ((Double)HUD.musicPosYlyr.getValueState()).intValue();
      int var6 = MusicManager.instance.lyric.isEmpty()?-7520544:-49273;
      int var7 = MusicManager.instance.lyric.isEmpty()?-1515777:-8481;
      var16.drawCenterOutlinedString(this.ly, (float)var3.getScaledWidth() / 2.0F - 0.5F, (float)(var3.getScaledHeight() - 120 - 80 + var5), var6, var7);
      var16.drawCenterOutlinedString(this.tly, (float)var3.getScaledWidth() / 2.0F, (float)(var3.getScaledHeight() - 100) + 0.5F - 80.0F + (float)var5, -49273, -8481);
      if(MusicManager.instance.showLyric) {
         if(this.firstTime) {
            this.timer.reset();
            this.firstTime = false;
         }

         UnicodeFontRenderer var8 = Oxygen.INSTANCE.font.wqy18;
         UnicodeFontRenderer var9 = Oxygen.INSTANCE.font.wqy25;
         float var10 = (float)var8.getStringWidth(this.hanaInstance.MusicMgr.currentTrack.getName());
         float var11 = (float)var9.getStringWidth("正在播放");
         float var12 = var10 > var11?var10:var11;
         RenderUtil.drawRect(var3.getScaledWidth() - this.animation - 50.0F, 5.0F, var3.getScaledWidth(), 40.0F, RenderUtil.reAlpha(Colors.BLACK.c, 0.7F));
         if(MusicManager.instance.trackImage.containsKey(Long.valueOf(this.hanaInstance.MusicMgr.currentTrack.getId()))) {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            ResourceLocation var13 = (ResourceLocation)this.hanaInstance.MusicMgr.trackImage.get(Long.valueOf(this.hanaInstance.MusicMgr.currentTrack.getId()));
            RenderUtil.drawImage(var13, var3.getScaledWidth() - this.animation - 45.0F, 8.0F, 28.0F, 28.0F);
            GL11.glPopMatrix();
         } else {
            MusicManager.instance.getTrackImage(this.hanaInstance.MusicMgr.currentTrack);
         }

         RenderUtil.drawArc(var3.getScaledWidth() - this.animation - 31.0F, 22.0F, 14.0D, Colors.WHITE.c, 0, 360.0D, 2);
         var9.drawString("正在播放", (float)var3.getScaledWidth() - this.animation - 12.0F, 8.0F, Colors.WHITE.c);
         var8.drawString(this.hanaInstance.MusicMgr.currentTrack.getName(), (float)var3.getScaledWidth() - this.animation - 12.0F, 26.0F, Colors.WHITE.c);
         if(this.timer.isDelayComplete(5000L)) {
            this.animation = (float)RenderUtil.getAnimationState(this.animation, (double)(-(var12 + 50.0F)), 300.0D);
            if(this.animation <= -(var12 + 50.0F)) {
            	MusicManager.instance.showLyric = false;
               this.firstTime = true;
            }
         } else {
            this.animation = (float)RenderUtil.getAnimationState(this.animation, (double)var12, 300.0D);
         }
      }

      GlStateManager.resetColor();
   }

   public String formatSeconds(int var1) {
      String var2 = "";
      int var3 = var1 / 60;
      if(var3 < 10) {
         var2 = var2 + "0";
      }

      var2 = var2 + var3 + ":";
      var1 %= 60;
      if(var1 < 10) {
         var2 = var2 + "0";
      }

      var2 = var2 + var1;
      return var2;
   }
}


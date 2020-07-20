package me.Oxygen.ui.music;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;

import java.awt.Color;

import org.lwjgl.opengl.*;

import me.Oxygen.Oxygen;
import me.Oxygen.manager.MusicManager;
import me.Oxygen.modules.render.HUD;
import me.Oxygen.utils.fontRenderer.UnicodeFontRenderer;
import me.Oxygen.utils.render.Colors;
import me.Oxygen.utils.render.RenderUtil;
import me.Oxygen.utils.timer.TimeHelper;
import net.minecraft.util.*;
import net.minecraft.client.renderer.*;

public enum Class344
{
    INSTANCE;
    
    public String ly;
    public String tly;
    public String downloadProgress;
    public long readedSecs;
    public long totalSecs;
    public float animation;
    public TimeHelper timer;
    public boolean firstTime;
    public Oxygen hanaInstance;
    private static final Class344[] $VALUES;
    
    private Class344() {
        this.ly = "";
        this.tly = "";
        this.downloadProgress = "0";
        this.readedSecs = 0L;
        this.totalSecs = 0L;
        this.animation = 0.0f;
        this.timer = new TimeHelper();
        this.firstTime = true;
        this.hanaInstance = Oxygen.INSTANCE;
    }
    
    public void renderOverlay() {
        final int intValue = HUD.musicPosX.getValueState().intValue();
        final int intValue2 = HUD.musicPosY.getValueState().intValue();
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        if (this.hanaInstance.MusicMgr.currentTrack != null && MusicManager.instance.mediaPlayer != null) {
            this.readedSecs = (int)MusicManager.instance.mediaPlayer.getCurrentTime().toSeconds();
            this.totalSecs = (int)MusicManager.instance.mediaPlayer.getStopTime().toSeconds();
        }
        if (this.hanaInstance.MusicMgr.currentTrack != null && MusicManager.instance.mediaPlayer != null) {
            this.hanaInstance.font.wqy18.drawString(this.hanaInstance.MusicMgr.currentTrack.getName() + " - " + this.hanaInstance.MusicMgr.currentTrack.getArtists(), 36.0f + intValue, 10 + intValue2, Colors.WHITE.c);
            this.hanaInstance.font.wqy18.drawString(this.formatSeconds((int)this.readedSecs) + "/" + this.formatSeconds((int)this.totalSecs), 36.0f + intValue, 20 + intValue2, -1);
            if (MusicManager.instance.trackImage.containsKey(this.hanaInstance.MusicMgr.currentTrack.getId())) {
                GL11.glPushMatrix();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                RenderUtil.drawImage(this.hanaInstance.MusicMgr.trackImage.get(this.hanaInstance.MusicMgr.currentTrack.getId()), 4 + intValue, 6 + intValue2, 28, 28);
                GL11.glPopMatrix();
            }
            else {
                MusicManager.instance.getTrackImage(this.hanaInstance.MusicMgr.currentTrack);
            }
            try {
                final float n = (float)(MusicManager.instance.mediaPlayer.getCurrentTime().toSeconds() / Math.max(1.0, MusicManager.instance.mediaPlayer.getStopTime().toSeconds())) * 100.0f;
                //RenderUtil.drawArc(18 + intValue, 19 + intValue2, 14.0, Colors.WHITE.c, 0, 360.0, 4);
                //RenderUtil.drawArc(18 + intValue, 19 + intValue2, 14.0, Colors.BLUE.c, 180, 180.0f + n * 3.6f, 4);
            }
            catch (Exception ex) {}
        }
        final UnicodeFontRenderer wqy25 = Oxygen.INSTANCE.font.wqy25;
        final int intValue3 = HUD.musicPosYlyr.getValueState().intValue();
        
        if(MusicManager.instance.displayLyric) {
        wqy25.drawCenterOutlinedString(MusicManager.instance.currentLyric, scaledResolution.getScaledWidth() / 2.0f - 0.5f, scaledResolution.getScaledHeight() - 120 - 80 + intValue3, MusicManager.instance.translateLyric.isEmpty() ? -7520544 : new Color(15,115,227).getRGB(), MusicManager.instance.translateLyric.isEmpty() ? -1515777 : -8481);
        wqy25.drawCenterOutlinedString(MusicManager.instance.currentTranslateLyric, scaledResolution.getScaledWidth() / 2.0f, scaledResolution.getScaledHeight() - 100 + 0.5f - 80.0f + intValue3, new Color(15,115,227).getRGB(), -8481);
        }
        
        if (MusicManager.instance.showLyric) {
            if (this.firstTime) {
                this.timer.reset();
                this.firstTime = false;
            }
            final UnicodeFontRenderer wqy26 = Oxygen.INSTANCE.font.wqy18;
            final UnicodeFontRenderer wqy27 = Oxygen.INSTANCE.font.wqy25;
            final float n2 = wqy26.getStringWidth(this.hanaInstance.MusicMgr.currentTrack.getName());
            final float n3 = wqy27.getStringWidth("正在播放");
            final float n4 = (n2 > n3) ? n2 : n3;
            RenderUtil.drawRect(scaledResolution.getScaledWidth() - this.animation - 50.0f, 5.0f, scaledResolution.getScaledWidth(), 40.0f, RenderUtil.reAlpha(Colors.BLACK.c, 0.7f));
            if (MusicManager.instance.trackImage.containsKey(this.hanaInstance.MusicMgr.currentTrack.getId())) {
                GL11.glPushMatrix();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                RenderUtil.drawImage(this.hanaInstance.MusicMgr.trackImage.get(this.hanaInstance.MusicMgr.currentTrack.getId()), scaledResolution.getScaledWidth() - this.animation - 45.0f, 8.0f, 28.0f, 28.0f);
                GL11.glPopMatrix();
            }
            else {
                MusicManager.instance.getTrackImage(this.hanaInstance.MusicMgr.currentTrack);
            }
            RenderUtil.drawArc(scaledResolution.getScaledWidth() - this.animation - 31.0f, 22.0f, 14.0, Colors.WHITE.c, 0, 360.0, 2);
            wqy27.drawString("正在播放", scaledResolution.getScaledWidth() - this.animation - 12.0f, 8.0f, Colors.WHITE.c);
            wqy26.drawString(this.hanaInstance.MusicMgr.currentTrack.getName(), scaledResolution.getScaledWidth() - this.animation - 12.0f, 26.0f, Colors.WHITE.c);
            if (this.timer.isDelayComplete(5000L)) {
                this.animation = (float)RenderUtil.getAnimationState(this.animation, -(n4 + 50.0f), 300.0);
                if (this.animation <= -(n4 + 50.0f)) {
                    MusicManager.instance.showLyric = false;
                    this.firstTime = true;
                }
            }
            else {
                this.animation = (float)RenderUtil.getAnimationState(this.animation, n4, 300.0);
            }
        }
        GlStateManager.resetColor();
    }
    
    public String formatSeconds(int n) {
        String string = "";
        final int n2 = n / 60;
        if (n2 < 10) {
            string += "0";
        }
        String s = string + n2 + ":";
        n %= 60;
        if (n < 10) {
            s += "0";
        }
        return s + n;
    }
    
    static {
        $VALUES = new Class344[] { Class344.INSTANCE };
    }
}

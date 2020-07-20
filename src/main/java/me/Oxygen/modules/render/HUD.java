package me.Oxygen.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.opengl.GL11;

import me.Oxygen.Oxygen;
import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventRenderGui;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.ui.music.Class287;
import me.Oxygen.ui.music.Class344;
import me.Oxygen.utils.render.Colors;
import me.Oxygen.utils.render.RenderUtil;
import me.Oxygen.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;

@ModuleRegister(name = "HUD", category = Category.RENDER, enabled = true)
public class HUD extends Module{
	public static Value<Double> musicPosX = new Value<Double>("HUD_MusicPlayerX", 70.0, 0.0, 400.0, 1.0);
	public static Value<Double> musicPosY = new Value<Double>("HUD_MusicPlayerY", 5.0, 0.0, 200.0, 1.0);
	public static Value<Double> musicPosYlyr = new Value<Double>("HUD_MusicPlayerLyricY", 120.0, 0.0, 200.0, 1.0);
	private Value<Boolean> music = new Value<Boolean>("HUD_Music", false);
	private Value<Boolean> rainbow = new Value<Boolean>("HUD_Rainbow", false);
	private Value<Boolean> array = new Value<Boolean>("HUD_ArrayList", true);
	public static Value<Boolean> hotbar = new Value<Boolean>("HUD_Hotbar", true);
	public static Value<Boolean> tttchat = new Value<Boolean>("HUD_ChatFont", true);
	public static Value<Boolean> toogleinfo = new Value<Boolean>("HUD_ToggleInfo", false);
	
	public Comparator<Module> comparator = (m, m1) -> {
        String mName = m.getDisplayName() == "" ? m.getName() : String.format("%s %s", m.getName(), m.getDisplayName());
        String m1Name = m1.getDisplayName() == "" ? m1.getName() : String.format("%s %s", m1.getName(), m1.getDisplayName());
        return Integer.compare(Oxygen.INSTANCE.font.tahoma18.getStringWidth(m1Name), Oxygen.INSTANCE.font.tahoma18.getStringWidth(mName));
    };
	
	@EventTarget(events = {EventRenderGui.class})
	private void onEvent(Event event) {
		if(event instanceof EventRenderGui) {
			if (!this.mc.gameSettings.showDebugInfo) {
				ScaledResolution sr = new ScaledResolution(mc);
				double x = mc.thePlayer.posX;
				double y = mc.thePlayer.posY;
				double z = mc.thePlayer.posZ;
				String x1 = String.format("%.0f", x);
				String y1 = String.format("%.0f", y);
				String z1 = String.format("%.0f", z);
				//if (!mc.isSingleplayer() && this.mc.thePlayer !=null && this.mc.theWorld !=null) {
				//	mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime();
				//}
				
				RenderUtil.drawRoundedRect(3, 3, 92, 63, new Color(255, 255, 255, 120).getRGB(), new Color(255, 255, 255, 120).getRGB());
				Oxygen.INSTANCE.font.tahoma40.drawString(Oxygen.INSTANCE.CLIENT_NAME, 14.0f, 1.0f, RenderUtil.reAlpha(Colors.BLACK.c, 0.75f));
				Oxygen.INSTANCE.font.tahoma16.drawString("By Rainbow", 49.0f, 25.0f, RenderUtil.reAlpha(Colors.BLACK.c, 0.75f));
				Oxygen.INSTANCE.font.tahoma40.drawString("________", 4.0f, +15.0f, RenderUtil.reAlpha(Colors.BLACK.c, 0.75f));
				Oxygen.INSTANCE.font.tahoma20.drawString("X:" + x1 + " " + "Y:" + y1 + " " + "Z:" + z1, 92.0f/2.0f - 40.0f, 38.0f, RenderUtil.reAlpha(Colors.BLACK.c, 0.75f));
				Oxygen.INSTANCE.font.tahoma20.drawString("FPS:" + Minecraft.getDebugFPS(), 92.0f/2.0f - 40.0f, 50.0f, RenderUtil.reAlpha(Colors.BLACK.c, 0.75f));
				
	        if(this.array.getValueState().booleanValue()) {
				ArrayList<Module> mods = new ArrayList<Module>(Oxygen.INSTANCE.ModMgr.getToggled());
				mods.sort(this.comparator);
				int yCount = 1;
				int rainbowTick = 0;
				sr.getScaledWidth();
				for(Module module : mods) {
					String modText = module.getName() + (module.getDisplayName() == "" ? "" : " " + module.getDisplayName());
					Color rainbow = new Color(Color.HSBtoRGB((float)((double)this.mc.thePlayer.ticksExisted / 50.0 + Math.sin((double)rainbowTick / 50.0 * 1.6)) % 1.0f, (float)0.5, 1.0f));
					Oxygen.INSTANCE.font.tahoma18.drawStringWithShadow(module.getName(), sr.getScaledWidth() - Oxygen.INSTANCE.font.tahoma18.getStringWidth(modText) - 6,yCount + 1,
							this.rainbow.getValueState() != false ? rainbow.getRGB() :new Color(255, 255, 255).getRGB());
					if (module.getDisplayName() != "") {
						Oxygen.INSTANCE.font.tahoma18.drawStringWithShadow(module.getDisplayName(),
								(float) sr.getScaledWidth() - Oxygen.INSTANCE.font.tahoma18.getStringWidth(module.getDisplayName()) - 6,
								yCount + 1, new Color(180, 180, 180).getRGB());
					}
					if (++rainbowTick > 50) {
                        rainbowTick = 0;
                    }
					yCount += Oxygen.INSTANCE.font.tahoma18.FONT_HEIGHT + 1;
			  }
	        }
	        
	        if(music.getValueState()) {
				Class344.INSTANCE.renderOverlay();
				Class287.INSTANCE.renderOverlay();
			}
	        
	        renderPotionStatus(sr);
		  }
		}
	}
	
	public void renderPotionStatus(ScaledResolution sr) {
		int x = 0;
		for (PotionEffect effect : mc.thePlayer.getActivePotionEffects()) {
			Potion potion = Potion.potionTypes[effect.getPotionID()];
			String PType = I18n.format(potion.getName(), new Object[0]);
			String d2 = "";
			switch (effect.getAmplifier()) {
			case 1: {
				PType = String.valueOf(PType) + (EnumChatFormatting.DARK_AQUA) + " II";
				break;
			}
			case 2: {
				PType = String.valueOf(PType) + (EnumChatFormatting.BLUE) + " III";
				break;
			}
			case 3: {
				PType = String.valueOf(PType) + (EnumChatFormatting.DARK_PURPLE) + " IV";
				break;
			}
			}
			if (effect.getDuration() < 600 && effect.getDuration() > 300) {
				d2 = (EnumChatFormatting.YELLOW) + Potion.getDurationString(effect);
			} else if (effect.getDuration() < 300) {
				d2 = (EnumChatFormatting.RED) + Potion.getDurationString(effect);
			} else if (effect.getDuration() > 600) {
				d2 = (EnumChatFormatting.WHITE) + Potion.getDurationString(effect);
			}
			int y2 = sr.getScaledHeight() - this.mc.fontRendererObj.FONT_HEIGHT + x - 5;
			int m2 = 25;
			this.mc.fontRendererObj.drawStringWithShadow(PType + " : " + d2,
					sr.getScaledWidth() - m2 - this.mc.fontRendererObj.getStringWidth(PType) - 2,
					y2 - this.mc.fontRendererObj.FONT_HEIGHT, Colors.DARKMAGENTA.c);

			x -= 12;
		}
	}
	
	public void drawRoundedRect(float n, float n2, float n3, float n4, final int n5, final int n6) {
        enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(n *= 2.0f, (n2 *= 2.0f) + 1.0f, (n4 *= 2.0f) - 2.0f, n5);
        drawVLine((n3 *= 2.0f) - 1.0f, n2 + 1.0f, n4 - 2.0f, n5);
        drawHLine(n + 2.0f, n3 - 3.0f, n2, n5);
        drawHLine(n + 2.0f, n3 - 3.0f, n4 - 1.0f, n5);
        drawHLine(n + 1.0f, n + 1.0f, n2 + 1.0f, n5);
        drawHLine(n3 - 2.0f, n3 - 2.0f, n2 + 1.0f, n5);
        drawHLine(n3 - 2.0f, n3 - 2.0f, n4 - 2.0f, n5);
        drawHLine(n + 1.0f, n + 1.0f, n4 - 2.0f, n5);
        RenderUtil.drawRect(n + 1.0f, n2 + 1.0f, n3 - 1.0f, n4 - 1.0f, n6);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }
	
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
    
    public static void drawHLine(float n, float n2, final float n3, final int n4) {
        if (n2 < n) {
            final float n5 = n;
            n = n2;
            n2 = n5;
        }
        RenderUtil.drawRect(n, n3, n2 + 1.0f, n3 + 1.0f, n4);
    }
    
    public static void drawVLine(final float n, float n2, float n3, final int n4) {
        if (n3 < n2) {
            final float n5 = n2;
            n2 = n3;
            n3 = n5;
        }
        RenderUtil.drawRect(n, n2 + 1.0f, n + 1.0f, n3, n4);
    }

}

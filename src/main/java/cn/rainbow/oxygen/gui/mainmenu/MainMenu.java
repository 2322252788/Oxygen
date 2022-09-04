package cn.rainbow.oxygen.gui.mainmenu;

import by.radioegor146.nativeobfuscator.Native;
import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.gui.GuiLogin;
import cn.rainbow.oxygen.gui.font.UnicodeFontRenderer;

import cn.rainbow.oxygen.utils.render.ColorUtils;
import cn.rainbow.oxygen.utils.render.GuiRenderUtils;
import cn.rainbow.oxygen.utils.render.RenderUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;

@Native
public class MainMenu
extends GuiScreen
implements GuiYesNoCallback {
    public static int anModule = 0;
    public static int anAlt = 0;
    public static int anSettings = 0;
    public static int anPlayer = 0;
    public static int anExit = 0;
    public static int anMenu = 0;
    public static int anSingle = 0;
    public UnicodeFontRenderer clientFont = Oxygen.INSTANCE.getFontmanager().wqy22;
    public UnicodeFontRenderer clientFont2 = Oxygen.INSTANCE.getFontmanager().wqy17;
    public static int anMulti = 0;
    public static int anPlayerLoader = 0;
    public static boolean playHovered;
    public static int bouncybounce;
    public static int clickeramount;
    public static int clicksec;
    public static boolean MenuOpen;
    public static int discordPlus;
    public static boolean isMees;
    boolean previousmouse = true;

    public static void doEffect() {
    }

    public void initGui() {
        /*try {
            Class clazz = Class.forName("cn.rainbow.Verify.C_NMD_HECK");
            if (clazz == null) {
                mc.displayGuiScreen(new GuiLogin());
                return;
            }
            Field field = clazz.getDeclaredField("CRACKKKKER_N_M_S_LLLLLLL");
            field.setAccessible(true);
            boolean get = field.getBoolean(Oxygen.INSTANCE.getClient().getObj());
            if (!get) mc.displayGuiScreen(new GuiLogin());
            field.setAccessible(false);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            mc.displayGuiScreen(new GuiLogin());
        }*/
        super.initGui();
    }

    public boolean isHovered(float f, int y, float g, int y2, int mouseX, int mouseY) {
        return (float)mouseX >= f && (float)mouseX <= g && mouseY >= y && mouseY <= y2;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        /*try {
            Class clazz = Class.forName("cn.rainbow.Verify.C_NMD_HECK");
            Field field = clazz.getDeclaredField("CRACKKKKER_N_M_S_LLLLLLL");
            field.setAccessible(true);
            boolean get = field.getBoolean(Oxygen.INSTANCE.getClient().getObj());
            if (!get) mc.displayGuiScreen(new GuiLogin());
            field.setAccessible(false);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            mc.displayGuiScreen(new GuiLogin());
        }*/
        ScaledResolution res = new ScaledResolution(mc);
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.renderImage(Resource.getMainMenu(), 0, 0, res.getScaledWidth(), res.getScaledHeight());
        Gui.drawRect(0, height - 25, width, height, new Color(0, 0, 0, 94).getRGB());
        Gui.drawRect(0, height - 25, width, height - 25 + 1, Color.white.getRGB());

        GuiRenderUtils.drawRoundedRect(width / 2 - 57, height / 2 - 30, 124f, 53f, 3, new Color(255, 255, 255, 120).getRGB(), 1, new Color(255, 255, 255, 120).getRGB());
        Oxygen.INSTANCE.getFontmanager().wqy60.drawString(Oxygen.name.toUpperCase(), width / 2 - 51, height / 2 - 28, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.75f));
        this.clientFont.drawString("By Rainbow", width / 2 - 23, height / 2 + 4, RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.75f));

        if (playHovered) {
            GuiRenderUtils.drawRoundedRect(15.0f, (float)(height - 25 - 15 - anPlayerLoader), 135f, 70f, 3, new Color(0, 0, 0, 150).getRGB(), 1, new Color(0, 0, 0, 150).getRGB());
            if (anPlayerLoader == 60) {
                Gui.drawRect(0, 0, 0, 0, 0);
                RenderUtil.renderImage("Oxygen/mainmenu/singleplayer.png", 30 - anSingle / 2, height - 25 - 5 - 35 - 25 - anSingle, 35 + anSingle, 35 + anSingle);
                this.clientFont.drawString("Single", 32, height - 25 - 5 - 20, Color.white.getRGB());
            }
            if (this.isHovered(30.0f, height - 25 - 5 - 35 - 25, 65.0f, height - 25 - 5 - 35 - 25 + 35, mouseX, mouseY)) {
                anSingle = Math.max(2, anSingle++);
                if (Mouse.isButtonDown(0)) {
                    mc.displayGuiScreen(new GuiSelectWorld(this));
                }
            } else {
                anSingle = Math.min(0, anSingle--);
            }
            if (anPlayerLoader == 60) {
            	RenderUtil.renderImage(new ResourceLocation("Oxygen/mainmenu/multiplayer.png"), 85 - anMulti / 2, height - 25 - 5 - 35 - 25 - anMulti, 52 + anMulti, 35 + anMulti);

                this.clientFont.drawString("Multi", 98, height - 25 - 5 - 20, Color.white.getRGB());
            }
            if (this.isHovered(85.0f, height - 25 - 5 - 35 - 25, 150.0f, height - 25 - 5 - 35 - 25 + height - 25 - 5 - 35 - 25 + 35, mouseX, mouseY)) {
                anMulti = Math.max(2, anMulti++);
                if (Mouse.isButtonDown(0)) {
                    mc.displayGuiScreen(new GuiMultiplayer(this));
                }
            } else {
                anMulti = Math.min(0, anMulti--);
            }
        }
        Oxygen.INSTANCE.getFontmanager().wqy14.drawString("Version - " + Oxygen.version, width - this.clientFont.getStringWidth("Version - " +  Oxygen.version) + 31, ((height + (height - 25)) / 2 - 8), Color.white.getRGB());
        String mcVer = "Minecraft(Forge) 1.8.9";
        Oxygen.INSTANCE.getFontmanager().wqy14.drawString(mcVer, width - this.clientFont.getStringWidth(mcVer) + 36, (int) ((height + (height - 25)) / 2 + 1), Color.white.getRGB());
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.drawIcon((float)(108 - anModule / 2), (float)((height - 25 + (height - 17)) / 2 - anModule), 17 + anModule, 17 + anModule, Resource.getModuleImg());
        RenderUtil.drawIcon((float)(78 - anAlt / 2), (float)((height - 25 + (height - 17)) / 2 - anAlt), 13 + anAlt, 17 + anAlt, Resource.getAlt());
        RenderUtil.drawIcon((float)(45 - anSettings / 2), (float)((height - 25 + (height - 15)) / 2 - anSettings), 15 + anSettings, 15 + anSettings, Resource.getSettings());
        RenderUtil.drawIcon((float)(15 - anPlayer / 2), (float)((height - 25 + (height - 15)) / 2 - anPlayer), 13 + anPlayer, 15 + anPlayer, Resource.getPlayer());
        RenderUtil.drawIcon((float)(width / 2 - 2 - anExit / 2), (float)((height - 25 + (height - 17)) / 2 - anExit), 16 + anExit, 17 + anExit, Resource.getExit());
        if (this.isHovered(108.0f, height - 25 + 4, 127.0f, height - 25 + 4 + 19, mouseX, mouseY)) {
            anModule = Math.max(2, anModule++);
            if (Mouse.isButtonDown(0)) {
            	mc.displayGuiScreen(new GuiModList(this));
            }
        } else {
            anModule = Math.min(0, anModule--);
        }
        if (this.isHovered(78.0f, height - 25 + 4, 93.0f, height - 25 + 4 + 19, mouseX, mouseY)) {
            anAlt = Math.max(2, anAlt++);
            if (Mouse.isButtonDown(0)) {
                //mc.displayGuiScreen((GuiScreen)new GuiAltManager());
            }
        } else {
            anAlt = Math.min(0, anAlt--);
        }
        if (this.isHovered(45.0f, height - 25 + 6, 62.0f, height - 25 + 6 + 17, mouseX, mouseY)) {
            anSettings = Math.max(2, anSettings++);
            if (Mouse.isButtonDown(0)) {
                mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
            }
        } else {
            anSettings = Math.min(0, anSettings--);
        }
        if (this.isHovered(15.0f, height - 25 + 6, 30.0f, height - 25 + 6 + 17, mouseX, mouseY) || playHovered && this.isHovered(15.0f, height - 25 - 85, 150.0f, height - 25 + 5, mouseX, mouseY)) {
            anPlayer = this.isHovered(15.0f, height - 25 + 6, 30.0f, height - 25 + 6 + 17, mouseX, mouseY) ? Math.max((int)2, (int)anPlayer++) : 0;
            if (anPlayerLoader < 60) {
                anPlayerLoader += 10;
            }
            playHovered = true;
        } else {
            playHovered = false;
            anPlayer = Math.min(0, anPlayer--);
            anPlayerLoader = 0;
        }
        if (this.isHovered(width / 2 - 2, height - 25 + 5, width / 2 + 18, height - 25 + 5 + 19, mouseX, mouseY)) {
            anExit = Math.max(2, anExit++);
            if (Mouse.isButtonDown(0)) {
                mc.shutdown();
            }
        } else {
            anExit = Math.min(0, anExit--);
        }
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    public static void drawScaledCustomSizeModalRect(double d, double e, float u, float v, double width, double height, double width2,
			double height2, float tileWidth, float tileHeight) {
		float f = 1.0F / tileWidth;
		float f1 = 1.0F / tileHeight;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(d, e + height2, 0.0D)
				.tex(u * f, (v + (float) height) * f1).endVertex();
		worldrenderer.pos(d + width2, e + height2, 0.0D)
				.tex((u + (float) width) * f, (v + (float) height) * f1).endVertex();
		worldrenderer.pos(d + width2, e, 0.0D)
				.tex((u + (float) width) * f, v * f1).endVertex();
		worldrenderer.pos(d, e, 0.0D).tex(u * f, v * f1).endVertex();
		tessellator.draw();
	}
    
    public static void drawRect(double d, double e, double g, double h, int color) {
		if (d < g) {
			int i = (int) d;
			d = g;
			g = i;
		}

		if (e < h) {
			int j = (int) e;
			e = h;
			h = j;
		}

		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(f, f1, f2, f3);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos(d, h, 0.0D).endVertex();
		worldrenderer.pos(g, h, 0.0D).endVertex();
		worldrenderer.pos(g, e, 0.0D).endVertex();
		worldrenderer.pos(d, e, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
    
    public static void drawHLine(float par1, float par2, float par3, int par4) {
	      if(par2 < par1) {
	         float var5 = par1;
	         par1 = par2;
	         par2 = var5;
	      }

	      drawRect(par1, par3, par2 + 3.0F, par3 + 3.0F, par4);
	   }
	
	public static void drawVLine(float par1, float par2, float par3, int par4) {
	      if(par3 < par2) {
	         float var5 = par2;
	         par2 = par3;
	         par3 = var5;
	      }

	      drawRect(par1, par2 + 1.0F, par1 + 1.0F, par3, par4);
	   }
    
    public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        x *= 2.0F;
        y *= 2.0F;
        x1 *= 2.0F;
        y1 *= 2.0F;
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        drawVLine(x, y + 1.0F, y1 - 2.0F, borderC);
        drawVLine(x1 - 1.0F, y + 1.0F, y1 - 2.0F, borderC);
        drawHLine(x + 2.0F, x1 - 3.0F, y, borderC);
        drawHLine(x + 2.0F, x1 - 3.0F, y1 - 1.0F, borderC);
        drawHLine(x + 1.0F, x + 1.0F, y + 1.0F, borderC);
        drawHLine(x1 - 2.0F, x1 - 2.0F, y + 1.0F, borderC);
        drawHLine(x1 - 2.0F, x1 - 2.0F, y1 - 2.0F, borderC);
        drawHLine(x + 1.0F, x + 1.0F, y1 - 2.0F, borderC);
        drawRect(x + 1.0F, y + 1.0F, x1 - 1.0F, y1 - 1.0F, insideC);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
     }

    static {
        bouncybounce = 0;
        clickeramount = 0;
        clicksec = 0;
        discordPlus = 0;
    }

    public static class Resource {

        private static final ResourceLocation background = new ResourceLocation("Oxygen/mainmenu/mainmenu.jpg");
        private static final ResourceLocation singleleplayer = new ResourceLocation("Oxygen/mainmenu/singleplayer.png");
        private static final ResourceLocation settings = new ResourceLocation("Oxygen/mainmenu/settings.png");
        private static final ResourceLocation play = new ResourceLocation("Oxygen/mainmenu/play.png");
        private static final ResourceLocation multiplayer = new ResourceLocation("Oxygen/mainmenu/multiplayer.png");
        private static final ResourceLocation exit = new ResourceLocation("Oxygen/mainmenu/exit.png");
        private static final ResourceLocation altmanager = new ResourceLocation("Oxygen/mainmenu/altmanager.png");
        private static final ResourceLocation vMenu = new ResourceLocation("Oxygen/mainmenu/vMenu.png");
        private static final ResourceLocation ModuleImg = new ResourceLocation("Oxygen/mainmenu/moduleManager.png");
        public int MainColor;

        public static ResourceLocation getMainMenu() {
            return background;
        }

        public static ResourceLocation getSinglePlayer() {
            return singleleplayer;
        }

        public static ResourceLocation getvMenu() {
            return vMenu;
        }

        public static ResourceLocation getModuleImg() {
            return ModuleImg;
        }

        public static ResourceLocation getSettings() {
            return settings;
        }

        public static ResourceLocation getPlayer() {
            return play;
        }

        public static ResourceLocation getMultiPlayer() {
            return multiplayer;
        }

        public static ResourceLocation getAlt() {
            return altmanager;
        }

        public static ResourceLocation getExit() {
            return exit;
        }
    }
}

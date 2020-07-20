package me.Oxygen.ui.clickgui3;

import java.util.Iterator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import me.Oxygen.Oxygen;
import me.Oxygen.manager.ModuleManager;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.ui.ClientNotification;
import me.Oxygen.utils.fontRenderer.UnicodeFontRenderer;
import me.Oxygen.utils.handler.MouseInputHandler;
import me.Oxygen.utils.render.Colors;
import me.Oxygen.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;

public class UIMenuMods
{
    private ArrayList<Module> modList;
    private MouseInputHandler handler;
    private MouseInputHandler rightCrink;
    public boolean open;
    public int x;
    public int y;
    public int width;
    public int tab_height;
    private Category c;
    public double yPos;
    private boolean opened;
    private boolean closed;
    private int valueYAdd;
    private float scrollY;
    private float scrollAmount;
    
    public UIMenuMods(Category c, MouseInputHandler handler) {
        super();
        this.modList = new ArrayList<Module>();
        this.rightCrink = new MouseInputHandler(1);
        this.valueYAdd = 0;
        this.c = c;
        this.handler = handler;
        this.addMods();
        this.yPos = -(this.y + this.tab_height + this.modList.size() * 20 + 10);
    }
    
    public void draw(int p_draw_1_, int p_draw_2_) {
        this.opened = true;
        int n = 160;
        if (p_draw_2_ > this.y + n) {
            p_draw_2_ = Integer.MAX_VALUE;
        }
        UnicodeFontRenderer tahoma16 = Oxygen.INSTANCE.font.tahoma16;
        new StringBuilder().append(this.c.name().substring(0, 1)).append(this.c.name().toLowerCase().substring(1, this.c.name().length())).toString();
        this.yPos = this.y + this.tab_height - 2;
        double n2 = this.yPos;
        RenderUtil.drawRect(this.x, n2, (this.x + this.width), (n2 + n - 23), -263429);
        int n3 = 15;
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        RenderUtil.doGlScissor(this.x, this.y + this.tab_height - 2, this.width, scaledResolution.getScaledHeight());
        double n4 = (this.modList.size() * n3 + n2 + this.valueYAdd);
        RenderUtil.doGlScissor(this.x, this.y + this.tab_height - 2, this.width, Math.min(n - (this.tab_height - 2), this.modList.size() * n3 + this.valueYAdd));
        GL11.glTranslated(0.0, (double)this.scrollY, 0.0);
        p_draw_2_ -= (int)this.scrollY;
        this.valueYAdd = 0;
        for (Module currentMod : this.modList) {
            if (currentMod.isEnabled()) {
            	RenderUtil.drawRect(this.x, n2, (this.x + this.width), (n2 + n3), -14704385);
            }
            boolean b;
            if (this.yPos == this.y + this.tab_height - 2 && p_draw_1_ >= this.x && p_draw_1_ <= this.x + this.width - 12 && p_draw_2_ >= n2 && p_draw_2_ < n2 + n3 && p_draw_2_ + this.scrollY >= this.y + this.tab_height) {
                b = true;
            }
            else {
                b = false;
            }
            boolean b2 = b;
            if (!Oxygen.INSTANCE.crink.menu.settingMode) {
            	Module mod = currentMod;
                float hoverOpacity;
                if (b2) {
                    hoverOpacity = (float) RenderUtil.getAnimationState(currentMod.hoverOpacity, 0.25f, 1.0f);
                }
                else {
                    hoverOpacity = (float) RenderUtil.getAnimationState(currentMod.hoverOpacity, 0.0f, 1.5f);
                }
                mod.hoverOpacity = hoverOpacity;
            }
            else {
                currentMod.hoverOpacity = 0.0f;
            }
            if (b2 && !Oxygen.INSTANCE.crink.menu.settingMode && this.handler.canExcecute()) {
            	Module mod2 = currentMod;
                boolean b3;
                if (!currentMod.isEnabled()) {
                    b3 = true;
                }
                else {
                    b3 = false;
                }
                mod2.set(b3);
            }
            if (b2 && this.rightCrink.canExcecute() && !Oxygen.INSTANCE.crink.menu.settingMode && Oxygen.INSTANCE.crink.menu.currentMod == null && currentMod.hasValues()) {
            	Oxygen.INSTANCE.crink.menu.settingMode = true;
                Oxygen.INSTANCE.crink.menu.currentMod = currentMod;
            }
            RenderUtil.drawRect(this.x, n2, (this.x + this.width), (n2 + n3), RenderUtil.reAlpha(Colors.BLACK.c, (float) currentMod.hoverOpacity));
            if (currentMod.isEnabled()) {
                tahoma16.drawString(currentMod.getName(), this.x + 12.0f, (float)(n2 + (n3 - tahoma16.FONT_HEIGHT) / 2), Colors.WHITE.c);
            }
            else {
                tahoma16.drawString(currentMod.getName(), this.x + 8.0f, (float)(n2 + (n3 - tahoma16.FONT_HEIGHT) / 2), Colors.BLACK.c);
            }
            n2 += n3;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        if (Oxygen.INSTANCE.crink.menu.settingMode && Oxygen.INSTANCE.crink.menu.currentMod != null) {
        }
        else {
            if (p_draw_1_ >= this.x && p_draw_1_ <= this.x + this.width && p_draw_2_ + this.scrollY >= this.y && p_draw_2_ + this.scrollY <= n2) {
                this.scrollY += Mouse.getDWheel() / 10.0f;
            }
            if (n2 - n3 - this.tab_height >= n && n2 - this.y + this.scrollY < (double)n) {
                this.scrollY = n - (float)n2 + this.y;
            }
            if (this.scrollY > 0.0f || n2 - n3 - this.tab_height < n) {
                this.scrollY = 0.0f;
            }
        }
    }
    
    public void mouseClick(int p_mouseClick_1_, int p_mouseClick_2_) {
        p_mouseClick_2_ -= (int)this.scrollY;
    }
    
    public void mouseRelease(int p_mouseRelease_1_, int p_mouseRelease_2_) {
    }
    
    private void addMods() {
        ModuleManager modMgr = Oxygen.INSTANCE.ModMgr;
        for (Module mod : ModuleManager.modules) {
            if (mod.getCategory() != this.c) {
                continue;
            }
            else {
                this.modList.add(mod);
                continue;
            }
        }
    }
}

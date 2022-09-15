package cn.rainbow.oxygen.gui.font;

import cn.rainbow.oxygen.Oxygen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class FontManager {

    public CFontRenderer wqy12 = getFont("wqy.ttf", 12, false);
    public CFontRenderer wqy14 = getFont("wqy.ttf", 14, false);
    public CFontRenderer wqy15 = getFont("wqy.ttf", 15, false);
    public CFontRenderer wqy16 = getFont("wqy.ttf", 16, true);
    public CFontRenderer wqy17 = getFont("wqy.ttf", 17, false);
    public CFontRenderer wqy18 = getFont("wqy.ttf", 18, true);
    public CFontRenderer wqy20 = getFont("wqy.ttf", 20, false);
    public CFontRenderer wqy22 = getFont("wqy.ttf", 22, false);
    public CFontRenderer wqy30 = getFont("wqy.ttf", 30, false);
    public CFontRenderer wqy35 = getFont("wqy.ttf", 35, false);
    public CFontRenderer wqy40 = getFont("wqy.ttf", 40, false);
    public CFontRenderer wqy60 = getFont("wqy.ttf", 60, false);

    public CFontRenderer segoe15 = getFont("segoe.ttf", 15, false);
    public CFontRenderer segoe17 = getFont("segoe.ttf", 17, false);
    public CFontRenderer segoe18 = getFont("segoe.ttf", 18, false);
    public CFontRenderer segoe20 = getFont("segoe.ttf", 20, false);

    public CFontRenderer tahoma13 = getFont("tahoma.ttf", 13, false);

    public CFontRenderer comfortaa10 = getFont("comfortaa.ttf", 10, false);
    public CFontRenderer comfortaa12 = getFont("comfortaa.ttf", 12, false);

    public CFontRenderer getFont(String name, int size, boolean allChars) {
        Oxygen.getLogger().info("[OxygenFont] " + name + " " + size + " Loading...");
        return new CFontRenderer(getFont(name, Font.PLAIN, size), true, true, allChars);
    }

    private Font getFont(String fontName, int fontType, int size) {
        Font font = null;
        try {
            InputStream ex = Minecraft.getMinecraft().getResourceManager().getResource(
                    new ResourceLocation("Oxygen/fonts/" + fontName)).getInputStream();
            font = Font.createFont(0, ex);
            font = font.deriveFont(fontType, size);
        } catch (Exception var3) {
            var3.printStackTrace();
            System.err.println("Failed to load custom font");
        }
        return font;
    }

}
package cn.rainbow.oxygen.gui.font;

import cn.rainbow.oxygen.Oxygen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

public class FontManager {

    private final HashMap<String, HashMap<Integer, OFontRenderer>> fonts = new HashMap<>();

    public OFontRenderer wqy12 = getFont("hmsans.ttf", 12, Font.PLAIN, false);
    public OFontRenderer wqy14 = getFont("hmsans.ttf", 14, Font.PLAIN, false);
    public OFontRenderer wqy15 = getFont("hmsans.ttf", 15, Font.PLAIN, false);
    public OFontRenderer wqy16 = getFont("hmsans.ttf", 16, Font.PLAIN, true);
    public OFontRenderer wqy17 = getFont("hmsans.ttf", 17, Font.PLAIN, false);
    public OFontRenderer wqy18 = getFont("hmsans.ttf", 18, Font.PLAIN, true);
    public OFontRenderer wqy20 = getFont("hmsans.ttf", 20, Font.PLAIN, false);
    public OFontRenderer wqy22 = getFont("hmsans.ttf", 22, Font.PLAIN, false);
    public OFontRenderer wqy30 = getFont("hmsans.ttf", 30, Font.PLAIN, false);
    public OFontRenderer wqy35 = getFont("hmsans.ttf", 35, Font.PLAIN, false);
    public OFontRenderer wqy40 = getFont("hmsans.ttf", 40, Font.PLAIN, false);
    public OFontRenderer wqy56 = getFont("hmsans.ttf", 56, Font.PLAIN, false);

    public OFontRenderer segoe15 = getFont("segoe.ttf", 15, Font.PLAIN, false);
    public OFontRenderer segoe17 = getFont("segoe.ttf", 17, Font.PLAIN, false);
    public OFontRenderer segoe18 = getFont("segoe.ttf", 18, Font.PLAIN, false);
    public OFontRenderer segoe20 = getFont("segoe.ttf", 20, Font.PLAIN, false);

    public OFontRenderer tahoma13 = getFont("tahoma.ttf", 13, Font.PLAIN, false);

    public OFontRenderer comfortaa10 = getFont("comfortaa.ttf", 10, Font.PLAIN, false);
    public OFontRenderer comfortaa12 = getFont("comfortaa.ttf", 12, Font.PLAIN, false);

    public FontManager() {
    }

    public OFontRenderer getFont(String name, int size, int fontType, boolean allChars) {
        if (this.fonts.containsKey(name) && this.fonts.get(name).containsKey(size)) {
            return this.fonts.get(name).get(size);
        }
        Oxygen.getLogger().info("[OxygenFont] " + name + " " + size + " Loading...");
        OFontRenderer cFontRenderer = new OFontRenderer(this.loadFont(name, fontType, size), true, true, allChars);
        HashMap<Integer, OFontRenderer> map = new HashMap<>();
        if (this.fonts.containsKey(name)) {
            map.putAll(this.fonts.get(name));
        }
        map.put(size, cFontRenderer);
        this.fonts.put(name, map);
        return cFontRenderer;
    }

    private Font loadFont(String fontName, int fontType, int size) {
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
package cn.rainbow.oxygen.gui.font;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.gui.font.fluxfont.FontUtils;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;

public class FontManager {

    private final HashMap<String, HashMap<Object, UnicodeFontRenderer>> fonts = new HashMap<>();

    public UnicodeFontRenderer wqy12 = getFont("wqy.ttf", 12, GlyphLoad.Normal);
    public UnicodeFontRenderer wqy14 = getFont("wqy.ttf", 14, GlyphLoad.Normal);
    public UnicodeFontRenderer wqy15 = getFont("wqy.ttf", 15, GlyphLoad.Normal);
    public FontUtils wqy16 = new FontUtils("wqy.ttf", Font.PLAIN, 16, 7, true);
    public UnicodeFontRenderer wqy17 = getFont("wqy.ttf", 17, GlyphLoad.Normal);
    public FontUtils owqy18 = new FontUtils("wqy.ttf", Font.PLAIN, 18, 7, true);
    public UnicodeFontRenderer wqy18 = getFont("wqy.ttf", 18, GlyphLoad.Normal);
    public UnicodeFontRenderer wqy20 = getFont("wqy.ttf", 20, GlyphLoad.Normal);
    public UnicodeFontRenderer wqy22 = getFont("wqy.ttf", 22, GlyphLoad.Normal);
    public UnicodeFontRenderer wqy30 = getFont("wqy.ttf", 30, GlyphLoad.Normal);
    public UnicodeFontRenderer wqy35 = getFont("wqy.ttf", 35, GlyphLoad.Normal);
    public UnicodeFontRenderer wqy40 = getFont("wqy.ttf", 40, GlyphLoad.Normal);
    public UnicodeFontRenderer wqy60 = getFont("wqy.ttf", 60, GlyphLoad.Normal);

    public UnicodeFontRenderer segoe15 = getFont("segoe.ttf", 15, GlyphLoad.Normal);
    public UnicodeFontRenderer segoe17 = getFont("segoe.ttf", 17, GlyphLoad.Normal);
    public UnicodeFontRenderer segoe18 = getFont("segoe.ttf", 18, GlyphLoad.Normal);
    public UnicodeFontRenderer segoe20 = getFont("segoe.ttf", 20, GlyphLoad.Normal);

    public UnicodeFontRenderer tahoma13 = getFont("tahoma.ttf", 13, GlyphLoad.Normal);

    public UnicodeFontRenderer comfortaa10 = getFont("comfortaa.ttf", 10, GlyphLoad.Normal);
    public UnicodeFontRenderer comfortaa12 = getFont("comfortaa.ttf", 12, GlyphLoad.Normal);

    public OxygenFont getFont(String name, int size) {
        Font font;
        try {
            InputStream inputStream = this.getClass()
                    .getResourceAsStream("/assets/minecraft/Oxygen/fonts/" + name);
            font = Font.createFont(0, inputStream);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception exception) {
            Oxygen.getLogger().error("Error loading font", exception);
            font = new Font("default", Font.PLAIN, size);
        }
        return new OxygenFont(font, size, true);
    }

    public UnicodeFontRenderer getFont(String name, float size, GlyphLoad loadType) {

        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && (this.fonts.get(name)).containsKey(size)) {
                return this.fonts.get(name).get(size);
            }
            InputStream inputStream = this.getClass()
                    .getResourceAsStream("/assets/minecraft/Oxygen/fonts/" + name);
            Font font = Font.createFont(0, inputStream);

            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size), loadType);

            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(
                    Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());
            HashMap<Object, UnicodeFontRenderer> map = new HashMap<>();
            if (this.fonts.containsKey(name)) {
                map.putAll(this.fonts.get(name));
            }
            map.put(size, unicodeFont);
            this.fonts.put(name, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unicodeFont;
    }
}
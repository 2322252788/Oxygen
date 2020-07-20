package me.Oxygen.utils.fontRenderer;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import me.Oxygen.Oxygen;
import me.Oxygen.injection.interfaces.IMinecraft;
import net.minecraft.client.Minecraft;

public class FontLoader {
	
	public FontLoader() {
		Oxygen.INSTANCE.logger.info("[" + Oxygen.INSTANCE.CLIENT_NAME + "]" + " " + "Font Loading...");
	}
	
	private HashMap<String, HashMap<Float, UnicodeFontRenderer>> fonts = new HashMap<String, HashMap<Float, UnicodeFontRenderer>>();
	
	public UnicodeFontRenderer segoe17 = this.getFontWithTTF("segoe", 17.0f);
	public UnicodeFontRenderer segoe40 = this.getFontWithTTF("segoe", 40.0f);
	
	public UnicodeFontRenderer simpleton13 = this.getFontOTF("simpleton", 13.0f);
	public UnicodeFontRenderer simpleton16 = this.getFontOTF("simpleton", 16.0f);
	public UnicodeFontRenderer simpleton17 = this.getFontOTF("simpleton", 17.0f);
	public UnicodeFontRenderer simpleton20 = this.getFontOTF("simpleton", 20.0f);
	public UnicodeFontRenderer simpleton25 = this.getFontOTF("simpleton", 25.0f);
	public UnicodeFontRenderer simpleton30 = this.getFontOTF("simpleton", 30.0f);
	public UnicodeFontRenderer simpleton70 = this.getFontOTF("simpleton", 70.0f);
	
	public UnicodeFontRenderer sansation15 = this.getFontWithTTF("sansation", 15f);
	public UnicodeFontRenderer sansation18 = this.getFontWithTTF("sansation", 18f);
	
	public UnicodeFontRenderer consolasbold12 = this.getFontWithTTF("consolasbold", 12.0f);
	
	public UnicodeFontRenderer tahoma12 = this.getFontWithTTF("tahoma", 12.0f);
	public UnicodeFontRenderer tahoma15 = this.getFontWithTTF("tahoma", 15.0f);
	public UnicodeFontRenderer tahoma16 = this.getFontWithTTF("tahoma", 16.0f);
	public UnicodeFontRenderer tahoma17 = this.getFontWithTTF("tahoma", 17.0f);
	public UnicodeFontRenderer tahoma18 = this.getFontWithTTF("tahoma", 18.0f);
	public UnicodeFontRenderer tahoma20 = this.getFontWithTTF("tahoma", 20.0f);
	public UnicodeFontRenderer tahoma25 = this.getFontWithTTF("tahoma", 25.0f);
	public UnicodeFontRenderer tahoma30 = this.getFontWithTTF("tahoma", 30.0f);
	public UnicodeFontRenderer tahoma40 = this.getFontWithTTF("tahoma", 40.0f);
	public UnicodeFontRenderer tahoma50 = this.getFontWithTTF("tahoma", 50.0f);

	public UnicodeFontRenderer comfortaa10 = this.getFontWithTTF("comfortaa", 10.0f);
	public UnicodeFontRenderer comfortaa12 = this.getFontWithTTF("comfortaa", 12.0f);
	public UnicodeFontRenderer comfortaa14 = this.getFontWithTTF("comfortaa", 14.0f);
	public UnicodeFontRenderer comfortaa15 = this.getFontWithTTF("comfortaa", 15.0f);
	public UnicodeFontRenderer comfortaa16 = this.getFontWithTTF("comfortaa", 16.0f);
	public UnicodeFontRenderer comfortaa18 = this.getFontWithTTF("comfortaa", 18.0f);
	public UnicodeFontRenderer comfortaa20 = this.getFontWithTTF("comfortaa", 20.0f);
	public UnicodeFontRenderer comfortaa34 = this.getFontWithTTF("comfortaa", 34.0f);
	
	public UnicodeFontRenderer icon20 = this.getFontWithCustomGlyph("icon", 20.0f, 59648, 59673);
	public UnicodeFontRenderer icon25 = this.getFontWithCustomGlyph("icon", 25.0f, 59648, 59673);
	public UnicodeFontRenderer icon40 = this.getFontWithCustomGlyph("icon", 40.0f, 59648, 59673);
	
	public UnicodeFontRenderer icon230 = this.getFontWithCustomGlyph("icon2", 30.0f, 59648, 59673);
	
	public UnicodeFontRenderer wqy14 = this.getFontWithCJK("wqy", 14.0f);
	public UnicodeFontRenderer wqy15 = this.getFontWithCJK("wqy", 15.0f);
	public UnicodeFontRenderer wqy16 = this.getFontWithCJK("wqy", 16.0f);
    public UnicodeFontRenderer wqy18 = this.getFontWithCJK("wqy", 18.0f);
    public UnicodeFontRenderer wqy25 = this.getFontWithCJK("wqy", 25.0f);
    
    public UnicodeFontRenderer getFontWithCustomGlyph(String name, float size, int fontPageStart, int fontPageEnd) {
        UnicodeFontRenderer unicodeFont = null;
        Oxygen.INSTANCE.logger.info("[" + Oxygen.INSTANCE.CLIENT_NAME + "]" + " 加载名为: " + name + "的字体");
        try {
            if (this.fonts.containsKey((Object)name) && (this.fonts.get(name)).containsKey(Float.valueOf((float)size))) {
                return (UnicodeFontRenderer)(this.fonts.get(name)).get(Float.valueOf((float)size));
            }
            InputStream inputStream = this.getClass().getResourceAsStream("/assets/minecraft/Oxygen/fonts/" + name + ".ttf");
            Font font = null;
            font = Font.createFont((int)0, (InputStream)inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size), fontPageStart, fontPageEnd);
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(((IMinecraft)Minecraft.getMinecraft()).getLanguageManager().isCurrentLanguageBidirectional());
            HashMap<Float, UnicodeFontRenderer> map = new HashMap<Float, UnicodeFontRenderer>();
            if (this.fonts.containsKey(name)) {
                map.putAll(this.fonts.get(name));
            }
            map.put(Float.valueOf((float)size), unicodeFont);
            this.fonts.put(name, map);
        }
        catch (Exception var7) {
            var7.printStackTrace();
        }
        return unicodeFont;
    }
	
    public UnicodeFontRenderer getFontWithCJK(final String name, final float size) {
        UnicodeFontRenderer unicodeFont = null;
        Oxygen.INSTANCE.logger.info("[" + Oxygen.INSTANCE.CLIENT_NAME + "]" + " 加载名为: " + name + "的字体");
        try {
            if (this.fonts.containsKey(name) && this.fonts.get(name).containsKey(size)) {
                return this.fonts.get(name).get(size);
            }
            final InputStream inputStream = this.getClass().getResourceAsStream("/assets/minecraft/Oxygen/fonts/" + name + ".ttf");
            Font font = null;
            font = Font.createFont(0, inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size), true);
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(((IMinecraft)Minecraft.getMinecraft()).getLanguageManager().isCurrentLanguageBidirectional());
            final HashMap<Float, UnicodeFontRenderer> map = new HashMap<Float, UnicodeFontRenderer>();
            if (this.fonts.containsKey(name)) {
                map.putAll(this.fonts.get(name));
            }
            map.put(size, unicodeFont);
            this.fonts.put(name, map);
        }
        catch (Exception var7) {
            var7.printStackTrace();
        }
        return unicodeFont;
    }
	
	public UnicodeFontRenderer getFontWithTTF(String name, float size) {
		UnicodeFontRenderer unicodeFont = null;
		Oxygen.INSTANCE.logger.info("[" + Oxygen.INSTANCE.CLIENT_NAME + "]" + " 加载名为: " + name + "的字体");
		try {
			if (this.fonts.containsKey(name) && (this.fonts.get(name)).containsKey(size)) {
				return (UnicodeFontRenderer)(this.fonts.get(name)).get(size);
			}
			InputStream inputStream = FontLoader.class.getResourceAsStream("/assets/minecraft/Oxygen/fonts/"+name+".ttf");
			//BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(FontLoader.class.getResource("/assets/minecraft/Leakey/fonts/" + name + ".ttf").getPath()));
			Font font = null;
			font = Font.createFont(0, inputStream);
			unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
			unicodeFont.setUnicodeFlag(true);
			unicodeFont.setBidiFlag(((IMinecraft)Minecraft.getMinecraft()).getLanguageManager().isCurrentLanguageBidirectional());
			HashMap<Float, UnicodeFontRenderer> map = new HashMap<Float, UnicodeFontRenderer>();
			if (this.fonts.containsKey(name)) {
				map.putAll(this.fonts.get(name));
			}
			map.put(size, unicodeFont);
	        this.fonts.put(name, map);
		} catch (Exception ex) { ex.printStackTrace(); }
		return unicodeFont;
	}
	
    public UnicodeFontRenderer getFontOTF(String name, float size) {
        UnicodeFontRenderer unicodeFont = null;
        Oxygen.INSTANCE.logger.info("[" + Oxygen.INSTANCE.CLIENT_NAME + "]" + " 加载名为: " + name + "的字体");
        try {
            if (this.fonts.containsKey(name) && this.fonts.get(name).containsKey(Float.valueOf(size))) {
                return this.fonts.get(name).get(Float.valueOf(size));
            }
            InputStream inputStream = FontLoader.class.getResourceAsStream("/assets/minecraft/Oxygen/fonts/"+name+".otf");
            Font font = null;
            font = Font.createFont(0, inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(((IMinecraft)Minecraft.getMinecraft()).getLanguageManager().isCurrentLanguageBidirectional());
            HashMap<Float, UnicodeFontRenderer> map = new HashMap<Float, UnicodeFontRenderer>();
            if (this.fonts.containsKey(name)) {
                map.putAll((Map<Float, UnicodeFontRenderer>)this.fonts.get(name));
            }
            map.put(Float.valueOf(size), unicodeFont);
            this.fonts.put(name, map);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return unicodeFont;
    }

}

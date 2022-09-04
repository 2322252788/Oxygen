package cn.rainbow.oxygen.utils.KeyStrokes;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.module.modules.render.KeyStrokes;
import cn.rainbow.oxygen.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Key {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final KeyBinding key;
    private final int xOffset;
    private final int yOffset;
    private boolean wasPressed = true;
    private long lastPress = 0L;
    private int color = 255;
    private double textBrightness = 1.0;

    public Key(KeyBinding key, int xOffset, int yOffset) {
        this.key = key;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void renderKey(int x, int y, int textColor) {
        boolean pressed = this.key.isKeyDown();
        String name = Keyboard.getKeyName((int)this.key.getKeyCode());
        if (pressed != this.wasPressed) {
            this.wasPressed = pressed;
            this.lastPress = System.currentTimeMillis();
        }
        if (pressed) {
            this.color = Math.min(255, (int)(2L * (System.currentTimeMillis() - this.lastPress)));
            this.textBrightness = Math.max(0.0, 1.0 - (double)(System.currentTimeMillis() - this.lastPress) / 20.0);
        } else {
            this.color = Math.max(0, 255 - (int)(2L * (System.currentTimeMillis() - this.lastPress)));
            this.textBrightness = Math.min(1.0, (double)(System.currentTimeMillis() - this.lastPress) / 20.0);
        }
        if (KeyStrokes.BackGroundRainbow.getCurrentValue()) {
            Gui.drawRect((int)(x + this.xOffset), y + this.yOffset, (int)(x + this.xOffset + (name.equals((Object)"SPACE") ? 70 : 22)), (int)(y + this.yOffset + (name.equals((Object)"SPACE") ? 11 : 22)), KeyStrokes.Rainbow.getColorValue((float)KeyStrokes.saturation.getCurrentValue(), (float)KeyStrokes.brightness.getCurrentValue(), (float)KeyStrokes.RainbowValue.getCurrentValue()).getRGB());
        }
        Gui.drawRect((int)(x + this.xOffset), (int)(y + this.yOffset), (int)(x + this.xOffset + (name.equals((Object)"SPACE") ? 70 : 22)), (int)(y + this.yOffset + (name.equals((Object)"SPACE") ? 11 : 22)), (int)(0x78000000 + (this.color << 16) + (this.color << 8) + this.color));
        if (KeyStrokes.Rect.getCurrentValue()) {
            Gui.drawRect((int)(x + this.xOffset), (int)(y + this.yOffset), (int)(x + this.xOffset + (name.equals("SPACE") ? 70 : 22)), (int)(y + this.yOffset + (1)), (int)(!KeyStrokes.RectRainbow.getCurrentValue() ? Color.WHITE.getRGB() : KeyStrokes.Rainbow.getColorValue((float) KeyStrokes.saturation.getCurrentValue(), (float)KeyStrokes.brightness.getCurrentValue(), (float)KeyStrokes.RainbowValue.getCurrentValue()).getRGB()));
            Gui.drawRect((int)(x + this.xOffset), (int)(y + this.yOffset), (int)(x + this.xOffset + (1)), y + this.yOffset + (name.equals("SPACE") ? 11 : 22), (int)(!KeyStrokes.RectRainbow.getCurrentValue() ? Color.WHITE.getRGB() : KeyStrokes.Rainbow.getColorValue((float)KeyStrokes.saturation.getCurrentValue(), (float)KeyStrokes.brightness.getCurrentValue(), (float)KeyStrokes.RainbowValue.getCurrentValue()).getRGB()));
            Gui.drawRect((int)(x + this.xOffset), (int)(y + this.yOffset + (name.equals("SPACE") ? 10 : 21)), (int)(x + this.xOffset + (name.equals("SPACE") ? 70 : 22)), (int)(y + this.yOffset + (name.equals("SPACE") ? 11 : 22)), (int)(!KeyStrokes.RectRainbow.getCurrentValue() ? Color.WHITE.getRGB() : KeyStrokes.Rainbow.getColorValue((float)KeyStrokes.saturation.getCurrentValue(), (float)KeyStrokes.brightness.getCurrentValue(), (float)KeyStrokes.RainbowValue.getCurrentValue()).getRGB()));
            Gui.drawRect((int)(x + this.xOffset + (name.equals("SPACE") ? 69 : 21)), (int)(y + this.yOffset), (int)(x + this.xOffset + (name.equals("SPACE") ? 70 : 22)), (int)(y + this.yOffset + (name.equals("SPACE") ? 11 : 22)), !KeyStrokes.RectRainbow.getCurrentValue() ? Color.WHITE.getRGB() : KeyStrokes.Rainbow.getColorValue((float)KeyStrokes.saturation.getCurrentValue(), (float)KeyStrokes.brightness.getCurrentValue(), (float)KeyStrokes.RainbowValue.getCurrentValue()).getRGB());
        }
        int red = textColor >> 16 & 0xFF;
        int green = textColor >> 8 & 0xFF;
        int blue = textColor & 0xFF;
        float nameWidth = Oxygen.INSTANCE.fontmanager.wqy18.getStringWidth(name);
        if (name.equals("SPACE")) {
            RenderUtil.drawHLine((x + this.xOffset + 18), y + this.yOffset + 5, x + this.xOffset + 72 - 18, y + this.yOffset + 5, 1.0f, textColor);
        } else {
        	Oxygen.INSTANCE.fontmanager.wqy18.drawString(name, (float)(x + this.xOffset + 22 / 2) - nameWidth / 2.0f, (float)(y + this.yOffset + 6), -16777216 + ((int)((double)red * this.textBrightness) << 16) + ((int)((double)green * this.textBrightness) << 8) + (int)((double)blue * this.textBrightness));
        }
    }
}

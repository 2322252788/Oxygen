package cn.rainbow.oxygen.utils.KeyStrokes;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.gui.font.UnicodeFontRenderer;
import cn.rainbow.oxygen.gui.font.cfont.CFontRenderer;
import cn.rainbow.oxygen.module.modules.render.KeyStrokes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MouseButton {
    private static final String[] BUTTONS = new String[]{"LMB", "RMB"};
    private final Minecraft mc = Minecraft.getMinecraft();
    private final int button;
    private final int xOffset;
    private final int yOffset;
    private List<Long> clicks = new ArrayList();
    private boolean wasPressed = true;
    private long lastPress = 0L;
    private int color = 255;
    private double textBrightness = 1.0;
    RainbowUtils Rainbow = new RainbowUtils();
    CFontRenderer font;
    UnicodeFontRenderer fontS;

    public MouseButton(int button, int xOffset, int yOffset) {
        this.font = Oxygen.INSTANCE.fontmanager.wqy18;
        this.fontS = Oxygen.INSTANCE.fontmanager.wqy12;
        this.button = button;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void addClick() {
        this.clicks.add(System.currentTimeMillis());
    }

    public int getClicks() {
        Iterator iterator = this.clicks.iterator();
        while (iterator.hasNext()) {
            if ((Long)iterator.next() >= System.currentTimeMillis() - 1000L) continue;
            iterator.remove();
        }
        return this.clicks.size();
    }

    public void renderMouseButton(int x, int y, int textColor) {
        boolean pressed = Mouse.isButtonDown((int)this.button);
        String name = BUTTONS[this.button];
        if (pressed != this.wasPressed) {
            this.wasPressed = pressed;
            this.lastPress = System.currentTimeMillis();
        }
        if (pressed) {
            this.color = Math.min(255, ((int)(2L * (System.currentTimeMillis() - this.lastPress))));
            this.textBrightness = Math.max(0.0, 1.0 - (double)(System.currentTimeMillis() - this.lastPress) / 20.0);
        } else {
            this.color = Math.max(0, 255 - (int)(2L * (System.currentTimeMillis() - this.lastPress)));
            this.textBrightness = Math.min(1.0, (double)(System.currentTimeMillis() - this.lastPress) / 20.0);
        }
        if (KeyStrokes.BackGroundRainbow.getCurrentValue()) {
            Gui.drawRect((int)(x + this.xOffset), (int)(y + this.yOffset), (int)(x + this.xOffset + 34), (int)(y + this.yOffset + 22), (int)KeyStrokes.Rainbow.getColorValue(((Double)KeyStrokes.saturation.getCurrentValue()).floatValue(), ((Double)KeyStrokes.brightness.getCurrentValue()).floatValue(), ((Double)KeyStrokes.RainbowValue.getCurrentValue()).floatValue()).getRGB());
        }
        Gui.drawRect((int)(x + this.xOffset), (int)(y + this.yOffset), (int)(x + this.xOffset + 34), (int)(y + this.yOffset + 22), (int)(0x78000000 + (this.color << 16) + (this.color << 8) + this.color));
        if (KeyStrokes.Rect.getCurrentValue()) {
            Gui.drawRect((int)(x + this.xOffset), (int)(y + this.yOffset), (int)(x + this.xOffset + 34), (int)(y + this.yOffset + 1), (int)(KeyStrokes.RectRainbow.getCurrentValue() == false ? Color.WHITE.getRGB() : KeyStrokes.Rainbow.getColorValue(((Double)KeyStrokes.saturation.getCurrentValue()).floatValue(), ((Double)KeyStrokes.brightness.getCurrentValue()).floatValue(), ((Double)KeyStrokes.RainbowValue.getCurrentValue()).floatValue()).getRGB()));
            Gui.drawRect((int)(x + this.xOffset), (int)(y + this.yOffset), (int)(x + this.xOffset + 1), (int)(y + this.yOffset + 22), (int)(KeyStrokes.RectRainbow.getCurrentValue() == false ? Color.WHITE.getRGB() : KeyStrokes.Rainbow.getColorValue(((Double)KeyStrokes.saturation.getCurrentValue()).floatValue(), ((Double)KeyStrokes.brightness.getCurrentValue()).floatValue(), ((Double)KeyStrokes.RainbowValue.getCurrentValue()).floatValue()).getRGB()));
            Gui.drawRect((int)(x + this.xOffset), (int)(y + this.yOffset + 21), (int)(x + this.xOffset + 34), (int)(y + this.yOffset + 22), (int)(KeyStrokes.RectRainbow.getCurrentValue() == false ? Color.WHITE.getRGB() : KeyStrokes.Rainbow.getColorValue(((Double)KeyStrokes.saturation.getCurrentValue()).floatValue(), ((Double)KeyStrokes.brightness.getCurrentValue()).floatValue(), ((Double)KeyStrokes.RainbowValue.getCurrentValue()).floatValue()).getRGB()));
            Gui.drawRect((int)(x + this.xOffset + 33), (int)(y + this.yOffset), (int)(x + this.xOffset + 34), (int)(y + this.yOffset + 22), (int)(KeyStrokes.RectRainbow.getCurrentValue() == false ? Color.WHITE.getRGB() : KeyStrokes.Rainbow.getColorValue(((Double)KeyStrokes.saturation.getCurrentValue()).floatValue(), ((Double)KeyStrokes.brightness.getCurrentValue()).floatValue(), ((Double)KeyStrokes.RainbowValue.getCurrentValue()).floatValue()).getRGB()));
        }
        int red = textColor >> 16 & 0xFF;
        int green = textColor >> 8 & 0xFF;
        int blue = textColor & 0xFF;
        if (KeyStrokes.mode.isCurrentMode("Low")) {
            float nameWidth = this.font.getStringWidth(name);
            this.font.drawString(name, (float)(x + this.xOffset + 16) - nameWidth / 2.0f, (float)(y + this.yOffset + 3), -16777216 + ((int)((double)red * this.textBrightness) << 16) + ((int)((double)green * this.textBrightness) << 8) + (int)((double)blue * this.textBrightness));
            String cpsText =this.getClicks() + " CPS";
            float cpsTextWidth = this.fontS.getStringWidth(cpsText);
            this.fontS.drawString(cpsText, (float)(x + this.xOffset + 16) - cpsTextWidth / 2.0f, (float)(y + this.yOffset + 12), -16777216 + ((int)(255.0 * this.textBrightness) << 16) + ((int)(255.0 * this.textBrightness) << 8) + (int)(255.0 * this.textBrightness));
        } else if (KeyStrokes.mode.isCurrentMode("Click")) {
            if (this.getClicks() == 0) {
                float nameWidth = this.font.getStringWidth(name);
                this.font.drawString(name, (float)(x + this.xOffset + 16) - nameWidth / 2.0f, (float)(y + this.yOffset + 10 - 4), -16777216 + ((int)((double)red * this.textBrightness) << 16) + ((int)((double)green * this.textBrightness) << 8) + (int)((double)blue * this.textBrightness));
            } else {
                String cpsText = this.getClicks() + " CPS";
                float cpsTextWidth = this.font.getStringWidth(cpsText);
                this.font.drawString(cpsText, (float)(x + this.xOffset + 16) - cpsTextWidth / 2.0f, (float)(y + this.yOffset + 10 - 4), -16777216 + ((int)((double)red * this.textBrightness) << 16) + ((int)((double)green * this.textBrightness) << 8) + (int)((double)blue * this.textBrightness));
            }
        }
    }
}

package cn.rainbow.oxygen.utils.KeyStrokes;

import java.awt.*;

public class RainbowUtils {
	
    private float hue = 0.0f;
    private float hue2 = 0.0f;

    public Color getColorValue(float saturation, float brightness, float value) {
        if (this.hue > 255.0f) {
            this.hue = 0.0f;
        }
        Color color = Color.getHSBColor(this.hue2 / 255.0f, saturation, brightness);
        this.hue2 += value;
        return color;
    }

    public void reset() {
        this.hue = 0.0f;
    }

    public void addValue(float value) {
        this.hue += value;
        this.hue2 = this.hue;
    }
    
}

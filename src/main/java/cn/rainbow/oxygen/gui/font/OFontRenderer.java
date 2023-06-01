package cn.rainbow.oxygen.gui.font;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class OFontRenderer extends OxygenFont {

    private final int[] colorCode = new int[32];

    public OFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics, boolean allChars) {
        super(font, antiAlias, fractionalMetrics, allChars);
        this.setupMinecraftColorcodes();
    }

    public float drawStringWithShadow(String text, double x2, double y2, int color) {
        float shadowWidth = this.drawString(text, x2 + 0.5, y2 + 0.5, color, true);
        return Math.max(shadowWidth, this.drawString(text, x2, y2, color, false));
    }

    public float drawString(String text, float x2, float y2, int color) {
        return this.drawString(text, x2, y2, color, false);
    }

    public float drawCenteredString(String text, float x2, float y2, int color) {
        return this.drawString(text, x2 - (float) (this.getStringWidth(text) / 2), y2, color);
    }

    public float drawString(String text, double x2, double y2, int color, boolean shadow) {
        text = processString(text);
        x2 -= 1.0;
        if (text.equals("")) {
            return 0.0f;
        }
        if (color == 553648127) {
            color = 16777215;
        }
        if ((color & -67108864) == 0) {
            color |= -16777216;
        }
        if (shadow) {
            color = (color & 16579836) >> 2 | color & -16777216;
        }

        OxygenFont.CharData[] currentData = this.charData;
        float alpha = (float) (color >> 24 & 255) / 255.0f;
        boolean strikethrough = false;
        boolean underline = false;
        x2 *= 2.0;
        y2 *= 2.0;

        GL11.glPushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.color((float) (color >> 16 & 255) / 255.0f, (float) (color >> 8 & 255) / 255.0f,
                (float) (color & 255) / 255.0f, alpha);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.texID);
        GL11.glBindTexture(3553, this.texID);

        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            if (character == '\247') {
                int colorIndex = 21;
                try {
                    colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                if (colorIndex < 16) {

                    underline = false;
                    strikethrough = false;

                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }

                    if (shadow) {
                        colorIndex += 16;
                    }

                    int colorcode = this.colorCode[colorIndex];
                    GlStateManager.color((float) (colorcode >> 16 & 255) / 255.0f,
                            (float) (colorcode >> 8 & 255) / 255.0f, (float) (colorcode & 255) / 255.0f, alpha);
                } else if (colorIndex == 18) {
                    strikethrough = true;
                } else if (colorIndex == 19) {
                    underline = true;
                } else if (colorIndex == 21) {
                    underline = false;
                    strikethrough = false;
                    GlStateManager.color((float) (color >> 16 & 255) / 255.0f, (float) (color >> 8 & 255) / 255.0f,
                            (float) (color & 255) / 255.0f, alpha);
                    currentData = this.charData;
                }
                ++i;
            } else if (character < currentData.length) {
                GlStateManager.bindTexture(this.texID);
                this.drawChar(currentData, character, (float) x2, (float) y2);
                if (strikethrough) {
                    this.drawLine(x2, y2 + (double) (currentData[character].height / 2),
                            x2 + (double) currentData[character].width - 8.0,
                            y2 + (double) (currentData[character].height / 2), 1.0f);
                }
                if (underline) {
                    this.drawLine(x2, y2 + (double) currentData[character].height - 2.0,
                            x2 + (double) currentData[character].width - 8.0,
                            y2 + (double) currentData[character].height - 2.0, 1.0f);
                }
                x2 += currentData[character].width - 9 + this.charOffset;
            }
        }
        GL11.glHint(3155, 4352);
        GL11.glPopMatrix();
        GlStateManager.bindTexture(0);
        return (float) x2 / 2.0f;
    }

    private String processString(String text) {
        String str = "";
        for (char c : text.toCharArray()) {
            if ((c < 50000 || c > 60000) && c != 9917) str += c;
        }
        text = str.replace('▬', '=').replace('❤', '♥').replace('⋆', '☆').replace('☠', '☆').replace('✰', '☆').replace("✫", "☆").replace("✙", "+");
        text = text.replace('⬅', '←').replace('⬆', '↑').replace('⬇', '↓').replace('➡', '→').replace('⬈', '↗').replace('⬋', '↙').replace('⬉', '↖').replace('⬊', '↘');
        text = text.replace('✦', '♦');
        return text;
    }

    @Override
    public int getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        int width = 0;
        int size = text.length();
        for (int i = 0; i < size; i++) {
            char character = text.charAt(i);
            if (character < this.charData.length) {
                width += this.charData[character].width - 9 + this.charOffset;
            }
        }
        return width / 2;
    }

    private void drawLine(double x2, double y2, double x1, double y1, float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    private void setupMinecraftColorcodes() {
        for (int index = 0; index < 32; index++) {
            int noClue = (index >> 3 & 1) * 85;
            int red = (index >> 2 & 1) * 170 + noClue;
            int green = (index >> 1 & 1) * 170 + noClue;
            int blue = (index >> 0 & 1) * 170 + noClue;
            if (index == 6) {
                red += 85;
            }
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[index] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
        }
    }
}
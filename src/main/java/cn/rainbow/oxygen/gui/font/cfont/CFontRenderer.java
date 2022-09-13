package cn.rainbow.oxygen.gui.font.cfont;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

public class CFontRenderer extends CFont {
	private final int[] colorCode = new int[32];

	public CFontRenderer(String font, int size, boolean allChar) {
		super(getFont(font, size), true, true, allChar);
		this.setupMinecraftColorcodes();
	}

	private static Font getFont(String name, int size) {
		Font font;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("Oxygen/fonts/" + name)).getInputStream();
			font = Font.createFont(0, is);
			font = font.deriveFont(0, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", 0, size);
		}
		return font;
	}

	public float drawStringWithShadow(String text, double x, double y, int color) {
		float shadowWidth = this.drawString(text, x + 0.5, y + 0.5, color, true);
		return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
	}

	public float drawStringWithShadowNew(String text, double x, double y, int color) {
		float shadowWidth = this.drawString(text, x + 0.5, y + 0.5, color, true);
		return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
	}

	public float drawString(String text, float x, float y, int color) {
		return this.drawString(text, x, y, color, false);
	}

	public float drawCenteredString(String text, float x, float y, int color) {
		return this.drawString(text, x - (float) (this.getStringWidth(text) / 2), y, color);
	}

	public float drawCenteredStringWithShadow(String text, float x, float y, int color) {
		return this.drawStringWithShadow(text, x - (float) (this.getStringWidth(text) / 2), y, color);
	}

	public float drawCenteredStringWithShadow(String text, double x, double y, int color) {
		return this.drawStringWithShadow(text, x - (double) (this.getStringWidth(text) / 2), y, color);
	}

	public void drawStringWithShadowForChat(String text, float x, float y, int color) {
		drawString(StringUtils.stripControlCodes(text), x + 1f, y + 1f, this.getShadowColor(color).getRGB());
		drawString(text, x, y, color);
	}

	private Color getShadowColor(int hex) {
		float a = (float) (hex >> 24 & 255) / 255.0f;
		float r = (float) (hex >> 16 & 255) / 255.0f;
		float g = (float) (hex >> 8 & 255) / 255.0f;
		float b = (float) (hex & 255) / 255.0f;
		return new Color(r * 0.2f, g * 0.2f, b * 0.2f, a * 0.9f);
	}

	public float drawString(String text, double x, double y, int color, boolean shadow) {
		text = processString(text);
		x -= 1;

		if (text.equals("")) {
			return 0.0F;
		}
		float alpha = (color >> 24 & 0xFF) / 255.0F;
		if (color == 553648127) {
			color = 16777215;
		}

		if ((color & 0xFC000000) == 0) {
			color |= -16777216;
		}

		if (shadow) {
			color = 0xFF000000;
		}

		CFont.CharData[] currentData = this.charData;

		boolean strikethrough = false;
		boolean underline = false;
		x *= 2.0;
		y *= 2.0;

		GL11.glPushMatrix();
		GlStateManager.scale(0.5D, 0.5D, 0.5D);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F,
				alpha);
		int size = text.length();
		GlStateManager.enableTexture2D();
		GlStateManager.bindTexture(texID);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);

		for (int i = 0; i < size; i++) {
			char character = text.charAt(i);

			if (character == '\247') {
				int colorIndex = 21;

				try {
					colorIndex = "0123456789abcdefklmnor".indexOf(text.charAt(i + 1));
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (colorIndex < 16) {
					underline = false;
					strikethrough = false;
					GlStateManager.bindTexture(texID);
					currentData = this.charData;

					if (colorIndex < 0) {
						colorIndex = 15;
					}

					if (shadow) {
						colorIndex += 16;
					}

					int colorcode = this.colorCode[colorIndex];
					GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0F, (colorcode >> 8 & 0xFF) / 255.0F,
							(colorcode & 0xFF) / 255.0F, alpha);
				} else if (colorIndex == 18) {
					strikethrough = true;
				} else if (colorIndex == 19) {
					underline = true;
				} else if (colorIndex == 21) {
					underline = false;
					strikethrough = false;
					GlStateManager.color((color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F,
							(color & 0xFF) / 255.0F, alpha);
					GlStateManager.bindTexture(texID);
					currentData = this.charData;
				}

				i++;
			} else if (character < currentData.length) {
				GL11.glBegin(GL11.GL_TRIANGLES);
				drawChar(currentData, character, (float) x, (float) y);
				GL11.glEnd();

				if (strikethrough) {
					drawLine(x, y + currentData[character].height / 2.0D, x + currentData[character].width - 8.0D,
							y + currentData[character].height / 2.0D, 1.0F);
				}

				if (underline) {
					drawLine(x, y + currentData[character].height - 2.0D, x + currentData[character].width - 8.0D,
							y + currentData[character].height - 2.0D, 1.0F);
				}

				x += currentData[character].width - 8 + this.charOffset;
			}
		}

		GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
		GL11.glPopMatrix();

		return (float) x / 2.0F;
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
		CFont.CharData[] currentData = this.charData;
		int size = text.length();
		int i = 0;
		while (i < size) {
			char character = text.charAt(i);
			if (character == '\247') {
				int colorIndex = "0123456789abcdefklmnor".indexOf(character);
				++i;
			} else if (character < currentData.length) {
				width += currentData[character].width - 8 + this.charOffset;
			}
			++i;
		}
		return width / 2;
	}

	private void drawLine(double x, double y, double x1, double y1, float width) {
		GL11.glDisable(3553);
		GL11.glLineWidth(width);
		GL11.glBegin(1);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x1, y1);
		GL11.glEnd();
		GL11.glEnable(3553);
	}

	public List<String> wrapWords(String text, double width) {
		ArrayList<String> finalWords = new ArrayList<String>();
		if ((double) this.getStringWidth(text) > width) {
			String[] words = text.split(" ");
			String currentWord = "";
			int lastColorCode = 65535;
			String[] arrstring = words;
			int n = arrstring.length;
			int n2 = 0;
			while (n2 < n) {
				String word = arrstring[n2];
				int i = 0;
				while (i < word.toCharArray().length) {
					char c = word.toCharArray()[i];
					if (c == '\247' && i < word.toCharArray().length - 1) {
						lastColorCode = word.toCharArray()[i + 1];
					}
					++i;
				}
				if ((double) this.getStringWidth(currentWord + word + " ") < width) {
					currentWord = currentWord + word + " ";
				} else {
					finalWords.add(currentWord);
					currentWord = 167 + lastColorCode + word + " ";
				}
				++n2;
			}
			if (currentWord.length() > 0) {
				if ((double) this.getStringWidth(currentWord) < width) {
					finalWords.add(167 + lastColorCode + currentWord + " ");
					currentWord = "";
				} else {
					for (String s : this.formatString(currentWord, width)) {
						finalWords.add(s);
					}
				}
			}
		} else {
			finalWords.add(text);
		}
		return finalWords;
	}

	public List<String> formatString(String string, double width) {
		ArrayList<String> finalWords = new ArrayList<String>();
		String currentWord = "";
		int lastColorCode = 65535;
		char[] chars = string.toCharArray();
		int i = 0;
		while (i < chars.length) {
			char c = chars[i];
			if (c == '\247' && i < chars.length - 1) {
				lastColorCode = chars[i + 1];
			}
			if ((double) this.getStringWidth(currentWord + c) < width) {
				currentWord = currentWord + c;
			} else {
				finalWords.add(currentWord);
				currentWord = 167 + lastColorCode + String.valueOf(c);
			}
			++i;
		}
		if (currentWord.length() > 0) {
			finalWords.add(currentWord);
		}
		return finalWords;
	}

	private void setupMinecraftColorcodes() {
		int index = 0;
		while (index < 32) {
			int noClue = (index >> 3 & 1) * 85;
			int red = (index >> 2 & 1) * 170 + noClue;
			int green = (index >> 1 & 1) * 170 + noClue;
			int blue = (index & 1) * 170 + noClue;
			if (index == 6) {
				red += 85;
			}
			if (index >= 16) {
				red /= 4;
				green /= 4;
				blue /= 4;
			}
			this.colorCode[index] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
			++index;
		}
	}

}

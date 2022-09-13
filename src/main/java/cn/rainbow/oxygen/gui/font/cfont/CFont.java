package cn.rainbow.oxygen.gui.font.cfont;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

public class CFont {
	private float imgSize = 1024;

	protected CharData[] charData = new CharData[2048];
	protected Font font;
	protected boolean antiAlias;
	protected boolean fractionalMetrics;
	protected int fontHeight = -1;
	protected int charOffset = 0;
	protected int texID;
	private boolean allChar = false;

	public CFont(Font font, boolean antiAlias, boolean fractionalMetrics) {
		this.font = font;
		this.antiAlias = antiAlias;
		this.fractionalMetrics = fractionalMetrics;
		texID = setupTexture(font, antiAlias, fractionalMetrics, this.charData);
	}

	public CFont(Font font, boolean antiAlias, boolean fractionalMetrics, boolean allChar) {
		this.font = font;
		this.antiAlias = antiAlias;
		this.fractionalMetrics = fractionalMetrics;
		this.allChar = allChar;
		if (this.allChar) {
			this.charData = new CharData[65535];
			this.imgSize = 8192;
		}
		texID = setupTexture(font, antiAlias, fractionalMetrics, this.charData);
	}

	protected int setupTexture(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
		BufferedImage img = generateFontImage(font, antiAlias, fractionalMetrics, chars);

		try {
			return TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), img, false, false);
		} catch (final NullPointerException e) {
			e.printStackTrace();
		}

		return 0;
	}

	protected BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics,
			CharData[] chars) {
		int imgSize = (int) this.imgSize;
		BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
		g.setFont(font);

		g.setColor(new Color(255, 255, 255, 0));
		g.fillRect(0, 0, imgSize, imgSize);
		g.setColor(Color.WHITE);

		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON
						: RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
		int charHeight = 0;
		int positionX = 0;
		int positionY = 0;

		for (int i = 0; i < chars.length; i++) {
			char ch = (char) i;
			final BufferedImage fontImage = this.getFontImage(ch, antiAlias, 0);

			CharData charData = new CharData();

			charData.width = fontImage.getWidth();
			charData.height = fontImage.getHeight();

			if (positionX + charData.width >= imgSize) {
				positionX = 0;
				positionY += charHeight;
				charHeight = 0;
			}

			charData.storedX = positionX;
			charData.storedY = positionY;

			if (charData.height > this.fontHeight) {
				this.fontHeight = charData.height;
			}
			if (charData.height > charHeight) {
				charHeight = charData.height;
			}

			chars[i] = charData;
			g.drawImage(fontImage, positionX, positionY, null);
			positionX += charData.width;
		}

		return bufferedImage;
	}

	private BufferedImage getFontImage(final char ch, final boolean antiAlias, int yAddon) {
		final BufferedImage tempfontImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = (Graphics2D) tempfontImage.getGraphics();

		if (antiAlias)
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		else
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

		g.setFont(font);
		final FontMetrics fontMetrics = g.getFontMetrics();
		int charwidth = fontMetrics.charWidth(ch) + 7;

		if (charwidth <= 0)
			charwidth = 7;
		int charheight = fontMetrics.getHeight() + 1 + yAddon;
		if (charheight <= 0)
			charheight = font.getSize();

		final BufferedImage fontImage = new BufferedImage(charwidth, charheight, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D gt = (Graphics2D) fontImage.getGraphics();
		if (antiAlias)
			gt.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		else
			gt.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		gt.setFont(font);
		gt.setColor(Color.WHITE);
		final int charx = 3;
		final int chary = 1;

		gt.drawString(String.valueOf(ch), charx, chary + fontMetrics.getAscent());

		return fontImage;

	}

	public void drawChar(CharData[] chars, char c, float x, float y) throws ArrayIndexOutOfBoundsException {
		try {
			drawQuad(x, y, chars[c].width, chars[c].height, chars[c].storedX,
					chars[c].storedY, chars[c].width, chars[c].height);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth,
			float srcHeight) {
		float renderSRCX = srcX / imgSize;
		float renderSRCY = srcY / imgSize;
		float renderSRCWidth = srcWidth / imgSize;
		float renderSRCHeight = srcHeight / imgSize;
		GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
		GL11.glVertex2d(x + width, y);
		GL11.glTexCoord2f(renderSRCX, renderSRCY);
		GL11.glVertex2d(x, y);
		GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
		GL11.glVertex2d(x, y + height);
		GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
		GL11.glVertex2d(x, y + height);
		GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
		GL11.glVertex2d(x + width, y + height);
		GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
		GL11.glVertex2d(x + width, y);
	}

	public int getStringHeight(String text) {
		return getHeight();
	}

	public int getHeight() {
		return (this.fontHeight - 8) / 2;
	}

	public int getStringWidth(String text) {
		int width = 0;

		for (char c : text.toCharArray()) {
			if (c < this.charData.length) {
				width += this.charData[c].width - 8 + this.charOffset;
			}
		}

		return width / 2;
	}

	public boolean isAntiAlias() {
		return this.antiAlias;
	}

	public void setAntiAlias(boolean antiAlias) {
		if (this.antiAlias != antiAlias) {
			this.antiAlias = antiAlias;
			texID = setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
		}
	}

	public boolean isFractionalMetrics() {
		return this.fractionalMetrics;
	}

	public void setFractionalMetrics(boolean fractionalMetrics) {
		if (this.fractionalMetrics != fractionalMetrics) {
			this.fractionalMetrics = fractionalMetrics;
			texID = setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
		}
	}

	public Font getFont() {
		return this.font;
	}

	public void setFont(Font font) {
		this.font = font;
		texID = setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
	}

	public class CharData {
		public int width;
		public int height;
		public int storedX;
		public int storedY;

		protected CharData() {
		}
	}
}
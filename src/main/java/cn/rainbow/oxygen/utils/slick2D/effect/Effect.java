
package cn.rainbow.oxygen.utils.slick2D.effect;

import cn.rainbow.oxygen.utils.slick2D.Glyph;
import cn.rainbow.oxygen.utils.slick2D.UnicodeFont;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Effect {
	public void draw (BufferedImage image, Graphics2D g, UnicodeFont unicodeFont, Glyph glyph);
}

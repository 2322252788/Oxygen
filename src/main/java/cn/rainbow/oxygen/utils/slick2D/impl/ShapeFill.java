package cn.rainbow.oxygen.utils.slick2D.impl;

import cn.rainbow.oxygen.utils.slick2D.Color;
import cn.rainbow.oxygen.utils.slick2D.geom.Shape;
import cn.rainbow.oxygen.utils.slick2D.geom.Vector2f;

public interface ShapeFill {
	
	public Color colorAt(Shape shape, float x, float y);
	
	public Vector2f getOffsetAt(Shape shape, float x, float y);
}

package cn.rainbow.oxygen.utils.slick2D.geom;


public interface GeomUtilListener {
	
	public void pointExcluded(float x, float y);

	
	public void pointIntersected(float x, float y);

	
	public void pointUsed(float x, float y);
}

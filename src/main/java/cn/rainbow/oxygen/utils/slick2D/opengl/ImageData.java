package cn.rainbow.oxygen.utils.slick2D.opengl;

import java.nio.ByteBuffer;

public interface ImageData {
	
	public int getDepth();
	
	public int getWidth();
	
	public int getHeight();
	
	public int getTexWidth();
	
	public int getTexHeight();
	
	public ByteBuffer getImageBufferData();

}
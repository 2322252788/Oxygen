package cn.rainbow.oxygen.utils.slick2D.util;

import java.io.IOException;


public interface DeferredResource {

	
	public void load() throws IOException;
	
	
	public String getDescription();
}

package cn.rainbow.oxygen.utils.slick2D.util;

import java.io.InputStream;
import java.net.URL;

public interface SlickResourceLocation {
	
	public InputStream getResourceAsStream(String ref);
	
	public URL getResource(String ref);
}

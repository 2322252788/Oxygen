package me.Oxygen.modules;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface ModuleRegister {
	
	public String name();
	
	public Category category();
	
	public boolean enabled() default false;
	
	public int keybind() default 0;

}

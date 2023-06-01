package cn.rainbow.oxygen.module;

import org.lwjgl.input.Keyboard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    String name();
    Category category();
    boolean toggle() default false;
    boolean noSetEnable() default false;
    int bindKey() default Keyboard.KEY_NONE;
}

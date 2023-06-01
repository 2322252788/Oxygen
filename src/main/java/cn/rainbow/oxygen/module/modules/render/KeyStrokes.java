package cn.rainbow.oxygen.module.modules.render;

import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.ClickMouseEvent;
import cn.rainbow.oxygen.event.events.Render2DEvent;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.ModuleInfo;
import cn.rainbow.oxygen.module.setting.BooleanValue;
import cn.rainbow.oxygen.module.setting.ModeValue;
import cn.rainbow.oxygen.module.setting.NumberValue;
import cn.rainbow.oxygen.utils.KeyStrokes.Key;
import cn.rainbow.oxygen.utils.KeyStrokes.MouseButton;
import cn.rainbow.oxygen.utils.KeyStrokes.RainbowUtils;
import org.lwjgl.input.Mouse;

import java.awt.*;

@ModuleInfo(name = "KeyStrokes", category = Category.Render)
public class KeyStrokes extends Module {

    private final Key[] movementKeys = new Key[5];
    private final MouseButton[] mouseButtons = new MouseButton[2];
    
    public static ModeValue mode = new ModeValue("Mode", "Low");
    public static BooleanValue BackGroundRainbow = new BooleanValue("BackGroundRainbow", false);
    public static BooleanValue RectRainbow = new BooleanValue("RectRainbow", true);
    public static BooleanValue Rect = new BooleanValue("Rect", true);
    public static NumberValue saturation = new NumberValue("Saturation", 0.5, 0.1, 1.0, 0.1);
    public static NumberValue brightness = new NumberValue("Brightness", 0.9, 0.1, 1.0, 0.1);
    public static NumberValue RainbowValue = new NumberValue("RainbowValue", 10.0, 1.0, 50.0, 1);
    public static NumberValue RainbowSpeed = new NumberValue("RainbowSpeed", 5.0, 1.0, 50.0, 1);
    public static NumberValue X = new NumberValue("X", 15.0, 0.0, 1000.0, 1);
    public static NumberValue Y = new NumberValue("Y", 170.0, 0.0, 1000.0, 1);
    public static RainbowUtils Rainbow = new RainbowUtils();

	public KeyStrokes() {
		mode.addValue("Low");
		mode.addValue("Click");
		this.movementKeys[0] = new Key(mc.gameSettings.keyBindForward, 26, 2);
        this.movementKeys[1] = new Key(mc.gameSettings.keyBindLeft, 2, 26);
        this.movementKeys[2] = new Key(mc.gameSettings.keyBindBack, 26, 26);
        this.movementKeys[3] = new Key(mc.gameSettings.keyBindRight, 50, 26);
        this.mouseButtons[0] = new MouseButton(0, 2, 50);
        this.mouseButtons[1] = new MouseButton(1, 38, 50);
        this.movementKeys[4] = new Key(mc.gameSettings.keyBindJump, 2, 74);
	}
	
	@EventTarget(events = {ClickMouseEvent.class, Render2DEvent.class})
    public void onMouse(Event event) {
		if(event instanceof ClickMouseEvent) {
			ClickMouseEvent ecm = (ClickMouseEvent) event;
			 if (ecm.button == 0 && Mouse.getEventButtonState()) {
		            this.mouseButtons[0].addClick();
		        }
		        if (ecm.button == 1 && Mouse.getEventButtonState()) {
		            this.mouseButtons[1].addClick();
		        }
		}
		if(event instanceof Render2DEvent) {
			int x = (int) X.getCurrentValue();
	        int y = (int)Y.getCurrentValue();
	        int textColor = Color.WHITE.getRGB();
	        this.draw(x, y, textColor);
	        Rainbow.addValue(0.2f * (float) RainbowSpeed.getCurrentValue());
		}
    }
	
	private void draw(int x, int y, int textColor) {
        this.movementKeys[0].renderKey(x, y, textColor);
        this.movementKeys[1].renderKey(x, y, textColor);
        this.movementKeys[2].renderKey(x, y, textColor);
        this.movementKeys[3].renderKey(x, y, textColor);
        this.mouseButtons[0].renderMouseButton(x, y, textColor);
        this.mouseButtons[1].renderMouseButton(x, y, textColor);
        this.movementKeys[4].renderKey(x, y, textColor);
    }
}

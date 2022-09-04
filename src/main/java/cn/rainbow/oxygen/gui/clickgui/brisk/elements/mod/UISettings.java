package cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod;

import cn.rainbow.oxygen.gui.clickgui.brisk.ClickUI;
import cn.rainbow.oxygen.module.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public abstract class UISettings {
	
	public Setting type;
	public static ClickUI clickUI;
	public static float allHeight;
	public static float startY;
	
	public UISettings(Setting type) {
		this.type = type;
	}
	
	public abstract float draw(int x, float y, int mouseX);
	
	public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton);
	
	public abstract void mouseReleased(int mouseX, int mouseY, int state);
	
	public static void handleMouseInput(int x, int y, int height) {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int mouseX = Mouse.getEventX() * sr.getScaledWidth() / Minecraft.getMinecraft().displayWidth;
        int mouseY = sr.getScaledHeight() - Mouse.getEventY() * sr.getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
		if (Mouse.getEventDWheel() != 0) {
	        if (isHovered(mouseX, mouseY, x, y, height)) {
	        	int height2 = y + height;
	        	startY += Mouse.getEventDWheel() / 15;
				if (startY < -allHeight + height2) {
					startY = -allHeight + height2;
				}
				if (startY > 0) {
					startY = 0;
				}
	        }
		}
	}
	
	private static boolean isHovered(int mouseX, int mouseY, int x, int y, int heightIn) {
		int x2 = x + 203;
		int y2 = y;
		int width = 195;
		int height = heightIn;
		return mouseX >= x2 && mouseX <= (x2 + width) && mouseY >= y2 && mouseY <= (y2 + height);
	}
}

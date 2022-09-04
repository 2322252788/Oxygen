package cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.gui.clickgui.brisk.ClickUI;
import cn.rainbow.oxygen.gui.font.UnicodeFontRenderer;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.modules.render.ClickGui;
import cn.rainbow.oxygen.utils.render.ColorUtils;
import cn.rainbow.oxygen.utils.Message;
import cn.rainbow.oxygen.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class UIMods {
	
	private ClickUI parent;
	public Module mod;
	private static Module setKeyMod;
	public static Module extended;
	public static Module currentMod;
	private static boolean listening;
	public static float allHeight;
	public static float startY;
	public float y;
	public int x;
	
	public UIMods(ClickUI parent, Module mod) {
		this.parent = parent;
		this.mod = mod;
	}
	
	public void draw() {
		UnicodeFontRenderer font = Oxygen.INSTANCE.fontmanager.segoe18;

		if (this.mod.getEnabled()) {
			RenderUtil.drawRect(this.parent.x + 102, this.y - 2, this.parent.x + 200, this.y + 16, ClickGui.getMode().isCurrentMode("Light") ? ColorUtils.AZURE.c : ColorUtils.GREY.c);
		}

		font.drawString((UIMods.setKeyMod == this.mod && UIMods.listening) ? "Listening..." : this.mod.getName(), this.x, (int) this.y, this.mod.getEnabled() ? ColorUtils.WHITE.c : ClickGui.getMode().isCurrentMode("Light") ? ColorUtils.AZURE.c : ColorUtils.WHITE.c);
		if (this.mod.getSettings().size() >= 1) {
			if (UIMods.extended == mod) {
				font.drawString("-", x + 81, (int) (y - 1), ClickGui.getMode().isCurrentMode("Light") ? ColorUtils.GREY.c : ColorUtils.WHITE.c);
			} else {
				font.drawString("+", x + 80, (int) (y - 1), ClickGui.getMode().isCurrentMode("Light") ? ColorUtils.GREY.c : ColorUtils.WHITE.c);
			}
		}
	}
	
	public void handleMouseInput() {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int mouseX = Mouse.getEventX() * sr.getScaledWidth() / Minecraft.getMinecraft().displayWidth;
        int mouseY = sr.getScaledHeight() - Mouse.getEventY() * sr.getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
		if (Mouse.getEventDWheel() != 0) {
			int height = this.parent.y + this.parent.heightIn;
	        if (this.isHovered(mouseX, mouseY)) {
	        	UIMods.startY += Mouse.getEventDWheel() / 15;
				if (startY < -allHeight + height) {
					startY = -allHeight + height;
				}
				if (startY > 0) {
					startY = 0;
				}
	        }
		}
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		int x = this.parent.x + 102;
		float y = this.y - 2;
		int width = 100;
		int height = 16;
		if (mouseX >= x && mouseX <= (x + width) && mouseY >= y && mouseY <= (y + height)) {
			if (mouseButton == 0) {
				this.mod.setEnabled(!mod.getEnabled());
			}else if (mouseButton == 1) {
				UIMods.extended = this.mod.getSettings().size() != 0 ? this.mod : null;
				UIMods.currentMod = this.mod;
				UISettings.startY = 0;
			}else if (mouseButton == 2) {
				if (mouseX >= x && mouseX <= (x + width) && mouseY >= y && mouseY <= (y + height)) {
					UIMods.setKeyMod = this.mod;
					UIMods.listening = true;
				}
			}
		}
	}
	
	public static boolean keyTyped(char typedChar, int keyCode) {
		if (listening) {
			if (keyCode != Keyboard.KEY_ESCAPE) {
				setKeyMod.setKeyCode(keyCode);
				Message.tellPlayer("Bind", setKeyMod.getName() + " has been bound to " + Keyboard.getKeyName(keyCode));
			} else {
				setKeyMod.setKeyCode(Keyboard.KEY_NONE);
				Message.tellPlayer("Bind", "Unbound " + setKeyMod.getName());
			}
			listening = false;
			return true;
		}
		return false;
	}
	
	public boolean isHovered(int mouseX, int mouseY) {
		int x = this.parent.x + 100;
		int y = this.parent.y;
		int width = 100;
		int height = this.parent.heightIn;
		return mouseX >= x && mouseX <= (x + width) && mouseY >= y && mouseY <= (y + height);
	}

}
package cn.rainbow.oxygen.gui.clickgui.brisk;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.gui.clickgui.brisk.elements.UICategory;
import cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.UIMods;
import cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.UISettings;
import cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.menu.UICheck;
import cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.menu.UICombo;
import cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.menu.UISlider;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.modules.render.ClickGui;
import cn.rainbow.oxygen.module.setting.BooleanValue;
import cn.rainbow.oxygen.module.setting.ModeValue;
import cn.rainbow.oxygen.module.setting.NumberValue;
import cn.rainbow.oxygen.module.setting.Setting;
import cn.rainbow.oxygen.utils.render.ColorUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class UIManager {
	
	public ArrayList<UICategory> category = new ArrayList<>();
	public ArrayList<UIMods> mods = new ArrayList<>();
	public Map<Module, ArrayList<UISettings>> settings = new HashMap<>();
	public ClickUI clickUI;
	
	public UIManager(ClickUI parent) {
		this.clickUI = parent;
		UIMods.extended = null;
		UICategory.currentCategory = Category.Combat;
		UISettings.clickUI = parent;
		for (Category cat : Category.values()) {
			this.category.add(new UICategory(this.clickUI, cat));
		}
		
		for (Module mod : Oxygen.INSTANCE.moduleManager.getModules()) {
			this.mods.add(new UIMods(this.clickUI, mod));
			this.initSettings(mod);
		}
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawCategory();
		this.drawModList();
		this.drawSettings(mouseX);
	}
	
	public void drawCategory() {
		int x = this.clickUI.x + 10;
		int y = this.clickUI.y + 35;
		for (UICategory cat : this.category) {
			cat.x = x;
			cat.y = y;
			cat.draw();
			y += 30;
		}
	}
	
	public void drawModList() {
		int x = this.clickUI.x + 110;
		float y = this.clickUI.y + UIMods.startY + 8;
		for (UIMods mod : this.mods) {
			if (mod.mod.getCategory().equals(UICategory.currentCategory)) {
				mod.x = x;
				mod.y = y;
				mod.draw();
				y += 20;
			}
		}
		UIMods.allHeight = y - UIMods.startY;
	}
	
	public void drawSettings(int mouseX) {
		if (UIMods.extended != null) {
			int x = this.clickUI.x + 210;
			float y = this.clickUI.y + UISettings.startY + 5;
			Oxygen.INSTANCE.fontmanager.segoe20.drawString(UIMods.extended.getName(), x, (int) y + 1, ClickGui.getMode().isCurrentMode("Light") ? ColorUtils.BLACK.c : ColorUtils.WHITE.c);
			y += 20;
			ArrayList<UISettings> booleanValue =
					this.settings.get(UIMods.extended).stream().filter(u -> u.type instanceof BooleanValue)
							.collect(Collectors.toCollection(ArrayList::new));
			ArrayList<UISettings> numberValue =
					this.settings.get(UIMods.extended).stream().filter(u -> u.type instanceof NumberValue)
							.collect(Collectors.toCollection(ArrayList::new));
			ArrayList<UISettings> modeValue =
					this.settings.get(UIMods.extended).stream().filter(u -> u.type instanceof ModeValue)
							.collect(Collectors.toCollection(ArrayList::new));
			for (UISettings s: modeValue) {
				y = s.draw(x, y, mouseX) + 15;
			}
			for (UISettings s: numberValue) {
				y = s.draw(x, y, mouseX) + 10;
			}
			for (UISettings s: booleanValue) {
				y = s.draw(x, y, mouseX) + 15;
			}
			UISettings.allHeight = y - UISettings.startY;
		} else {
			Oxygen.INSTANCE.fontmanager.segoe20.drawString("Module" + " No Settings.", this.clickUI.x + 210,
					this.clickUI.y + 5, ClickGui.getMode().isCurrentMode("Light") ? ColorUtils.BLACK.c : new Color(167, 167, 167).getRGB());
		}
	}
	
	public void initSettings(Module mod) {
		if (mod.getSettings().size() != 0) {
			ArrayList<UISettings> s = new ArrayList<>();
			for (Setting set : mod.getSettings()) {
				if (set instanceof ModeValue) {
					s.add(new UICombo(set));
				}else if (set instanceof NumberValue) {
					s.add(new UISlider(set));
				}else if (set instanceof BooleanValue) {
					s.add(new UICheck(set));
				}
			}
			
			this.settings.put(mod, s);
		}
	}
	
	public void handleMouseInput() {
		for (UIMods mod : this.mods) {
			mod.handleMouseInput();
		}
		
		UISettings.handleMouseInput(this.clickUI.x, this.clickUI.y, this.clickUI.heightIn);
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (UICategory cat : this.category) {
			cat.mouseClicked(mouseX, mouseY, mouseButton);
		}
		
		for (UIMods mod : this.mods) {
			if (mod.mod.getCategory().equals(UICategory.currentCategory)) {
				mod.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
		
		if (UIMods.extended != null) {
			for (UISettings s : this.settings.get(UIMods.extended)) {
				if (s.mouseClicked(mouseX, mouseY, mouseButton)) {
					return;
				}
			}
		}
	}
	
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (UIMods.extended != null) {
			for (UISettings s : this.settings.get(UIMods.extended)) {
				s.mouseReleased(mouseX, mouseY, state);
			}
		}
	}

	public void keyTyped(char typedChar, int keyCode) {
		
	}

}

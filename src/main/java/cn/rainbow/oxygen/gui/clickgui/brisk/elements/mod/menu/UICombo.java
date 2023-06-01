package cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.menu;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.UIMods;
import cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.UISettings;
import cn.rainbow.oxygen.gui.font.OFontRenderer;
import cn.rainbow.oxygen.module.modules.render.ClickGui;
import cn.rainbow.oxygen.module.setting.Setting;
import cn.rainbow.oxygen.utils.render.ColorUtils;
import cn.rainbow.oxygen.utils.render.RenderUtil;

public class UICombo extends UISettings {
	
	private final Setting set;
	private int width;
	private int x;
	private float y;
	
	public UICombo(Setting set) {
		super(set);
		this.set = set;
	}
	
	@Override
	public float draw(int x, float y, int mouseX) {
		this.x = x;
		this.y = y;
		this.width = 0;
		Oxygen.INSTANCE.fontmanager.wqy15.drawString(this.set.getName(), this.x, (int) this.y, ClickGui.getMode().isCurrentMode("Light") ? ColorUtils.BLACK.c : ColorUtils.WHITE.c);
		int height = 12;
		OFontRenderer font = Oxygen.INSTANCE.fontmanager.segoe15;
		
		for (int i = 0; i < this.set.getModeValue().getOptions().size(); i++) {
			String modName = this.set.getModeValue().getOptions().get(i);
			RenderUtil.drawRect(this.x + this.width, this.y + height + 4, this.x + 4 + this.width, this.y + height + 8, getComboColor(Oxygen.INSTANCE.settingManager.getSetting(UIMods.extended, this.set.getName()).getModeValue().isCurrentMode(modName)));
			font.drawString(modName, this.x + 7 + this.width, (int) (this.y + height), ClickGui.getMode().isCurrentMode("Light") ? ColorUtils.BLACK.c : ColorUtils.WHITE.c);
			
			if ((this.clickUI.x + this.clickUI.widthIn) > (this.x + this.width + (font.getStringWidth(modName) + 15) * 2.5)) {
				this.width += font.getStringWidth(modName) + 15;
			}else if (i != (this.set.getModeValue().getOptions().size() - 1)){
				this.width = 0;
				height += 12;
			}
		}
		
		return this.y + height;
	}

	private int getComboColor(boolean toggle) {
		int dark = toggle ? ColorUtils.WHITE.c : ColorUtils.GREY.c;
		int light = toggle ? ColorUtils.AZURE.c : ColorUtils.AQUA.c;
		return ClickGui.getMode().isCurrentMode("Dark") ? dark : light;
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0) {
			int width = 0;
			int height = 12;
			OFontRenderer font = Oxygen.INSTANCE.fontmanager.segoe15;
			for (String s : this.set.getModeValue().getOptions()) {
				if (mouseX >= x + width && mouseX <= x + width + 4 && mouseY >= (this.y + height + 4) && mouseY <= (this.y + height + 8)) {
					Oxygen.INSTANCE.settingManager.getSetting(UIMods.extended, this.set.getName()).getModeValue().setCurrentValue(s);
					return true;
				}				
				if ((this.clickUI.x + this.clickUI.widthIn) > (this.x + width + (font.getStringWidth(s) + 15) * 2.5)) {
					width += font.getStringWidth(s) + 15;
				}else {
					width = 0;
					height += 12;
				}
			}
		}		
		return false;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
	}

}

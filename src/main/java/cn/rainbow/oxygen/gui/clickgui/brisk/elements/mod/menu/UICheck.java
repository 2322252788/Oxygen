package cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.menu;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.UISettings;
import cn.rainbow.oxygen.gui.font.OFontRenderer;
import cn.rainbow.oxygen.module.modules.render.ClickGui;
import cn.rainbow.oxygen.module.setting.Setting;
import cn.rainbow.oxygen.utils.render.ColorUtils;
import cn.rainbow.oxygen.utils.render.RenderUtil;

public class UICheck extends UISettings {
	
	private final Setting set;
	private int x;
	private int y;
	
	public UICheck(Setting set) {
		super(set);
		this.set = set;
	}
	
	@Override
	public float draw(int x, float y, int mouseX) {
		this.x = x;
		this.y = (int) y;
		OFontRenderer font = Oxygen.INSTANCE.fontmanager.segoe15;
		font.drawString(this.set.getName(), this.x, this.y, ClickGui.getMode().isCurrentMode("Light") ? ColorUtils.BLACK.c : ColorUtils.WHITE.c);
		this.x += font.getStringWidth(this.set.getName()) + 10;
		RenderUtil.drawOutRoundRectLine(this.x, this.y + 6, this.x + 8, this.y + 6, 4, 2, ColorUtils.SILVER.c);
		RenderUtil.circle(this.set.getBooleanValue().getCurrentValue() ? this.x + 8 : this.x, this.y + 6, 3, circleColor(this.set.getBooleanValue().getCurrentValue()));
		return this.y;
	}

	private int circleColor(boolean toggle) {
		int dark = toggle ? ColorUtils.WHITE.c : ColorUtils.GREY.c;
		int light = toggle ? ColorUtils.AZURE.c : ColorUtils.AQUA.c;
		return ClickGui.getMode().isCurrentMode("Dark") ? dark : light;
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && (mouseX >= x - 5 && mouseX <= x + 13 && mouseY >= (y + 2) && mouseY <= (y + 10))) {
			this.set.getBooleanValue().setCurrentValue(!this.set.getBooleanValue().getCurrentValue());
		}
		return false;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
	}

}

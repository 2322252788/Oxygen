package cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.menu;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.UISettings;
import cn.rainbow.oxygen.module.modules.render.ClickGui;
import cn.rainbow.oxygen.module.setting.NumberValue;
import cn.rainbow.oxygen.module.setting.Setting;
import cn.rainbow.oxygen.utils.render.ColorUtils;
import cn.rainbow.oxygen.utils.render.RenderUtil;
import net.minecraft.util.MathHelper;

import java.text.DecimalFormat;

public class UISlider extends UISettings {

	private boolean dragging;
	private NumberValue set;
	private int width;
	private double animationX;
	private int y;
	private double x;

	private DecimalFormat df = null;

	public UISlider(Setting setting) {
		super(setting);
		this.width = 100;
		this.set = setting.getNumberValue();
		this.dragging = false;
		this.animationX = this.clickUI.x + 210;
	}

	@Override
	public float draw(int x, float y, int mouseX) {
		if (set.getStep() >= 1){
			df = new DecimalFormat("0");
		} else switch ((set.getMinValue() + "").length() - (set.getMinValue() + "").indexOf(".") - 1){
			case 1:
				df = new DecimalFormat("0.0");
				break;
			case 2:
				df = new DecimalFormat("0.00");
				break;
			case 3:
				df = new DecimalFormat("0.000");
				break;
		}
		//System.out.printf(Oxygen.INSTANCE.SetMgr.getModBySettings(set).getModName());
		String displayval = "" + Math.round(set.getCurrentValue() * 100D)/ 100D;
		String name = this.set.getName() + " " + set.getCurrentValue();
		Oxygen.INSTANCE.fontmanager.segoe15.drawString(name, x, (int) y, ClickGui.getMode().isCurrentMode("Light") ? ColorUtils.BLACK.c : ColorUtils.WHITE.c);
		this.y = (int) (y + 18);
		this.x = x;

		RenderUtil.drawLine(this.x, this.y, this.x + this.width, this.y, 3, ClickGui.getMode().isCurrentMode("Dark") ? ColorUtils.DARKGREY.c :ColorUtils.GREY.c);
		double percentBar = (set.getCurrentValue() - set.getMinValue()) / (set.getMaxValue() - set.getMinValue());
		this.animationX = RenderUtil.getAnimationState(this.animationX, this.x + (percentBar * this.width), ((this.x > this.animationX) ? (this.animationX + this.x) : Math.abs(this.animationX - this.x)) * 5);
		RenderUtil.drawLine(this.x, this.y, this.animationX, this.y, 3, ClickGui.getMode().isCurrentMode("Dark") ? ColorUtils.GREY.c : ColorUtils.AZURE.c);
		RenderUtil.circle((float) this.animationX, this.y, this.dragging ? 1.5F : 2, ClickGui.getMode().isCurrentMode("Dark") ? ColorUtils.WHITE.c : ColorUtils.AZURE.c);
		if (this.dragging) {
			double diff = set.getMaxValue() - set.getMinValue();
			double val = set.getMinValue() + (MathHelper.clamp_double((mouseX - x) / (double) width, 0, 1)) * diff;
			/*int longValue = 220 - 130;
			double valAbs = mouseX - (x + 130);
			double perc = valAbs / ((longValue) * Math.max(Math.min(set.getCurrentValue() / set.getMaxValue(), 0), 1));
			perc = Math.min(Math.max(0, perc), 1);
			double valRel = (set.getMaxValue() - set.getMinValue()) * perc;
			double val = set.getMinValue() + valRel;
			val = Math.round(val * (1 / set.getStep())) / (1 / set.getStep());*/
			set.setCurrentValue(Math.round(val * 100D)/ 100D);
			/*double pos = (mouseX - x + 155) / 140;
			if (pos < 0) pos = 0;
			if (pos > 1) pos = 1;
			double valueState = (set.getMaxValue() - set.getMinValue()) * pos + set.getMinValue();
			valueState = Math.round(valueState * (1.0 / set.getStep())) / (1.0 / set.getStep());
			set.setCurrentValue(valueState);*/
		}
		return this.y;
	}

	private double getDecimalFormat(double number){
		return Double.parseDouble(df.format(number));
	}

	private boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && (mouseX >= x && mouseX <= x + this.width && mouseY >= (y - 1.5) && mouseY <= (y + 1.5))) {
			this.dragging = true;
			return true;
		}
		return false;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		this.dragging = false;
	}

}

package cn.rainbow.oxygen.gui.clickgui.brisk.elements;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.gui.clickgui.brisk.ClickUI;
import cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.UIMods;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.modules.render.ClickGui;
import cn.rainbow.oxygen.utils.render.ColorUtils;
import cn.rainbow.oxygen.utils.render.RenderUtil;

import java.awt.*;

public class UICategory {
	
	public ClickUI parent;
	public static Category currentCategory;
	private Category category;
	private static double animationY;
	private int width;
	private int height;
	public int x;
	public double y;
	
	public UICategory(ClickUI parent, Category cat) {
		this.parent = parent;
		this.category = cat;
		this.width = 100;
		this.height = 23;
		UICategory.animationY = this.parent.y + 35;
	}
	
	public void draw() {
		if (this.category.equals(UICategory.currentCategory)) {
			UICategory.animationY = RenderUtil.getAnimationState(UICategory.animationY, this.y, ((this.y < UICategory.animationY) ? (UICategory.animationY - this.y) : Math.abs(UICategory.animationY - this.y)) * 20);
			RenderUtil.drawRect((float) this.parent.x, (float) (UICategory.animationY - 8), (float) (this.parent.x + this.width), (float) (animationY + this.height), ClickGui.getMode().isCurrentMode("Dark") ? ColorUtils.GREY.c :ColorUtils.AZURE.c);
		}

		Oxygen.INSTANCE.fontmanager.segoe20.drawString(this.category.name(), this.x, (int) this.y, ClickGui.getMode().isCurrentMode("Dark") ? ColorUtils.WHITE.c : ColorUtils.BLACK.c);
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && this.isHovered(mouseX, mouseY)) {
			UICategory.currentCategory = this.category;
			UIMods.startY = 0;
		}
	}
	
	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y - 6 && mouseY <= this.y + this.height;
	}

}

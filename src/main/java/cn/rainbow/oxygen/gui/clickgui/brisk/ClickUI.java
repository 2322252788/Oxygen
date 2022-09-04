package cn.rainbow.oxygen.gui.clickgui.brisk;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.file.files.ModuleConfig;
import cn.rainbow.oxygen.gui.clickgui.brisk.elements.mod.UIMods;
import cn.rainbow.oxygen.module.modules.render.ClickGui;
import cn.rainbow.oxygen.utils.render.ColorUtils;
import cn.rainbow.oxygen.utils.render.GuiRenderUtils;
import cn.rainbow.oxygen.utils.Message;
import cn.rainbow.oxygen.utils.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class ClickUI extends GuiScreen {
	
	public UIManager manager;
	public int widthIn;
	public int heightIn;
	private boolean dragging;
	public int x;
	public int y;
	private int x2;
	private int y2;
	
	public ClickUI() {	
		this.manager = new UIManager(this);
	}	
	
	@Override
	public void initGui() {
		ScaledResolution sr = new ScaledResolution(this.mc);
		int width = sr.getScaledWidth() / 2;
		int height = sr.getScaledHeight() / 2;
		this.x = width - 200;
		this.y = height - 120;
		this.widthIn = 400;
		this.heightIn = 240;
		try {
			if (this.mc.theWorld != null) {
				this.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
			}
		} catch (Throwable e) {
				Message.tellPlayer("[Oxygen]","加载Blur出现异常，建议关闭快速渲染。");
			}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (this.dragging) {
			this.x = this.x2 + mouseX;
			this.y = this.y2 + mouseY;
		}
		this.cutScreen(this.x, this.y, this.x + this.widthIn, this.y + this.heightIn);
		int color = ClickGui.getMode().isCurrentMode("Dark") ? new Color(27, 27, 27).getRGB() : ColorUtils.WHITE.c;
		RenderUtil.drawRoundedRect(this.x, this.y, this.x + this.widthIn, this.y + this.heightIn, 3f, color);
		RenderUtil.drawGradientSideways(this.x + 100, this.y, this.x + 102, this.y + this.heightIn, new Color(0, 0, 0, 180).getRGB(), new Color(0, 0, 0, 20).getRGB());
		RenderUtil.drawGradientSideways(this.x + 200, this.y, this.x + 202, this.y + this.widthIn, new Color(0, 0, 0, 20).getRGB(), new Color(0, 0, 0, 180).getRGB());
		Oxygen.INSTANCE.fontmanager.wqy30.drawString(Oxygen.name.toUpperCase(), this.x + 22, this.y + 2, ClickGui.getMode().isCurrentMode("Dark") ? ColorUtils.WHITE.c :ColorUtils.AZURE.c);
		Oxygen.INSTANCE.fontmanager.wqy12.drawString(Oxygen.version, this.x + 37, this.y + 18, ClickGui.getMode().isCurrentMode("Dark") ? ColorUtils.WHITE.c :ColorUtils.GREY.c);
		Oxygen.INSTANCE.fontmanager.wqy15.drawString("UI Made By Pipi", this.x + 5, this.y + 230, ColorUtils.GREY.c);
		this.manager.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		this.manager.handleMouseInput();
		super.handleMouseInput();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0 && this.isHovered(mouseX, mouseY)) {
			this.x2 = this.x - mouseX;
			this.y2 = this.y - mouseY;
			this.dragging = true;
		}
		this.manager.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (state == 0) {
			this.dragging = false;
		}
		this.manager.mouseReleased(mouseX, mouseY, state);
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		this.manager.keyTyped(typedChar, keyCode);
		if (!UIMods.keyTyped(typedChar, keyCode)) {
			super.keyTyped(typedChar, keyCode);
		}
	}
	
	@Override
	public void onGuiClosed() {
		try {
			this.mc.entityRenderer.stopUseShader();
		} catch (Throwable e) {
			Message.tellPlayer("[Oxygen]","加载Blur出现异常，建议关闭快速渲染。");
		}
		Oxygen.INSTANCE.filemanager.saveFile(new ModuleConfig());
	}
	
	private void cutScreen(float left, float top, float right, float bottom) {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		ScaledResolution sr = new ScaledResolution(this.mc);
		int mcwidth = sr.getScaledWidth();
		int mcheight = sr.getScaledHeight();
		int x = Math.round(mc.displayWidth * left / mcwidth);
		int width = Math.round(mc.displayWidth * (right - left) / mcwidth);
		int y = Math.round(mc.displayHeight - mc.displayHeight * bottom / mcheight);
		int height = Math.round(mc.displayHeight * (bottom - top) / mcheight);
		GL11.glScissor(x, y, width, height);
		// 记得搞完之后glDisable(GL_SCISSOR_TEST)哦;
	}
	
	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= this.x && mouseX <= (this.x + 100) && mouseY >= this.y && mouseY <= (this.y + 28);
	}

}

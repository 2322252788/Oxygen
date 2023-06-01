package cn.rainbow.oxygen.gui;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.utils.render.ColorUtils;
import cn.rainbow.oxygen.utils.render.RenderUtil;
import cn.rainbow.oxygen.utils.timer.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class ClientNotification {
	
	private String message;
	private TimerUtil timer;
	private double lastY, posY, width, height, animationX;
	private int color, imageWidth;
	private ResourceLocation image;
	private long stayTime;
	private static long lastMs;
	private long displayTime;
	private final int time;
	private final int animeTime;

	public ClientNotification(String message, Type type) {
		this.message = message;
		timer = new TimerUtil();
		timer.reset();
		width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(message) + 35;
		height = 20;
		animationX = width;
		stayTime = 1500;
		imageWidth = 13;
		posY = -1;
		time = 1500;
		animeTime = 340;
		displayTime = System.currentTimeMillis();
		image = new ResourceLocation("Oxygen/notification/" + type.name().toLowerCase() + ".png");
		if (type.equals(Type.INFO))
			color = RenderUtil.reAlpha(ColorUtils.DARKGREY.c, 0.55F);
		else if (type.equals(Type.ERROR))
			color = RenderUtil.reAlpha(ColorUtils.DARKGREY.c, 0.55F);
		else if (type.equals(Type.SUCCESS))
			color = RenderUtil.reAlpha(ColorUtils.DARKGREY.c, 0.55F);
		else if (type.equals(Type.WARNING))
			color = RenderUtil.reAlpha(ColorUtils.DARKGREY.c, 0.55F);
	}

	public void draw(double getY, double lastY) {
		this.lastY = lastY;
		long nowTime = System.currentTimeMillis();
		animationX = RenderUtil.getAnimationState(animationX, isFinished() ? width : 0, Math.max(isFinished() ? 200 : 30, Math.abs(animationX - (isFinished() ? width : 0)) * 5));
		if(posY == -1) {
			posY = getY;
		}else {
			posY = RenderUtil.getAnimationState(posY, getY, 200);
		}			
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		int x1 = (int) (res.getScaledWidth() - width + animationX), x2 = (int) (res.getScaledWidth() + animationX), y1 = (int) posY, y2 = (int) (y1 + height);
		Gui.drawRect(x1, y1, x2, y2, color);
		Gui.drawRect(x1, y2, x2, (int) (y2 + 0.5F), color);
		Gui.drawRect(x1, y2, x2, (int) (y2 + 0.5F), RenderUtil.reAlpha(1, 0.5F));
		
		Gui.drawRect(x1, y1, (int) (x1 + height), y2, RenderUtil.reAlpha(-1, 0.1F));
		RenderUtil.drawImage(image, (int)(x1 + (height - imageWidth) / 2F), y1 + (int)((height - imageWidth) / 2F), imageWidth, imageWidth);

		float f = (float)(x1 + width / 2F)+20f - (float)this.width * ((float)(nowTime - this.displayTime) / ((float)this.animeTime * 2.0f + (float)this.time));
		float f2 = 1.0f;
		float f3 = (float)this.height - 2.0f;
		float f4 = 0.0f;
		boolean bl = false;
		float f5 = Math.max((float)f, (float)f2);
		RenderUtil.drawRect((x1 + width / 2F) - 50.2f, (float)(y1 + height / 3.5F) +13f, (float)f5, (float)(y1 + height / 3.5F) + 15f, NotifyType.INFO.getRenderColor().getRGB());

		Oxygen.INSTANCE.fontmanager.wqy14.drawCenteredString(message, (float)(x1 + width / 2F) + 10, (float)(y1 + height / 3.5F), -1);
	}
	
	public boolean shouldDelete() {
		return isFinished() && animationX >= width;
	}

	private boolean isFinished() {
		return timer.delay(stayTime) && posY == lastY;
	}
	
	public double getHeight() {
		return height;
	}

	public enum Type {
		SUCCESS, INFO, WARNING, ERROR
	}

	enum NotifyType
	{
		SUCCESS(new Color(6348946)),
		ERROR(new Color(16723759)),
		WARNING(new Color(16121088)),
		INFO(new Color(6590631));

		private Color renderColor;

		public final Color getRenderColor() {
			return this.renderColor;
		}

		public final void setRenderColor(final Color set) {
			this.renderColor = set;
		}

		private NotifyType(final Color renderColor) {
			this.renderColor = renderColor;
		}
	}
}

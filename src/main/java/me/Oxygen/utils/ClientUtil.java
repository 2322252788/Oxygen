package me.Oxygen.utils;

import java.awt.Color;
import java.util.ArrayList;

import me.Oxygen.ui.ClientNotification;
import me.Oxygen.ui.ClientNotification.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public enum ClientUtil {
	
	INSTANCE;
	
	private static ArrayList<ClientNotification> notifications = new ArrayList<>();

	public void drawNotifications() {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		double startY = res.getScaledHeight() - 40;
		final double lastY = startY;
		for (int i = 0; i < notifications.size(); i++) {
			ClientNotification not = notifications.get(i);
			if (not.shouldDelete())
				notifications.remove(i);
			not.draw(startY, lastY);
			startY -= not.getHeight() + 1;
		}
	}
	
	public static void sendClientMessage(String message, Type type) {
		if (notifications.size() > 8) notifications.remove(0);
		notifications.add(new ClientNotification(message, type));
	}
	
	public static void tellPlayer(String string) {
        if (string != null && Minecraft.getMinecraft().thePlayer != null) {
        	Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText(string));
        }
    }
	
	public static int reAlpha(int color, float alpha) {
		Color c = new Color(color);
		float r = ((float) 1 / 255) * c.getRed();
		float g = ((float) 1 / 255) * c.getGreen();
		float b = ((float) 1 / 255) * c.getBlue();
		return new Color(r, g, b, alpha).getRGB();
	}
	
}

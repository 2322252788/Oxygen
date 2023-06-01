package cn.rainbow.oxygen.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Message {
	public static void tellPlayer(String title, String... text) {
		if (text.length > 1) {
			for (String s : text) {
				if (Minecraft.getMinecraft().thePlayer != null) {
					Minecraft.getMinecraft().thePlayer.addChatComponentMessage(
							new ChatComponentText("\247a\247l[" + title + "] " + "\247e" + s));
				} else {
					System.out.println("[" + title + "] " + s);
				}
			}
		} else {
			if (Minecraft.getMinecraft().thePlayer != null) {
				Minecraft.getMinecraft().thePlayer.addChatComponentMessage(
						new ChatComponentText("\247a\247l[" + title + "] " + "\247e" + text[0]));
			} else {
				System.out.println("[" + title + "] " + text[0]);
			}
		}
	}
}

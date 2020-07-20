package me.Oxygen.utils.world;

import me.Oxygen.Oxygen;
import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.modules.combat.Antibot;
import me.Oxygen.modules.player.Collective;
import me.Oxygen.modules.player.HYTBypass;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Check {

	private static Minecraft mc = Minecraft.getMinecraft();
	private static String serverIp;
	public static float pitch;

	public Check() {
		Oxygen.INSTANCE.EventMgr.register(this);
	}

	@EventTarget(events = {EventUpdate.class})
	private void onEvent(Event e) {
		if(e instanceof EventUpdate) {
		if (this.onServer("hypixel") && !Oxygen.INSTANCE.ModMgr.getModule(Antibot.class).isEnabled()) {
			ClientUtil.sendClientMessage("You can't close this module!", Type.ERROR);
			Oxygen.INSTANCE.ModMgr.getModule(Antibot.class).set(true);
		}
		
		if(this.onServer("59.111.137.99") && !Oxygen.INSTANCE.ModMgr.getModule(HYTBypass.class).isEnabled()) {
			mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e[Helper]§4警告! §f您未在花雨庭开启HYTBypass可能导致封号，已自动帮您开启!"));
			Oxygen.INSTANCE.ModMgr.getModule(HYTBypass.class).set(true);
		}
		
		if (!Oxygen.INSTANCE.ModMgr.getModule(Collective.class).isEnabled()) {
			Oxygen.INSTANCE.ModMgr.getModule(Collective.class).set(true);
			ClientUtil.sendClientMessage("You can't close this module!", Type.ERROR);
		}
		
        /*if (!MusicManager.instance.isLoop ) {
	        /*if(MusicManager.getInstance().getMediaPlayer().getCurrentTime().toSeconds() ==
	        		MusicManager.getInstance().getMediaPlayer().getStopTime().toSeconds()) {
	        	MusicManager.getInstance().next();
	        }*/
		 }
        }

	private boolean onServer(String server) {
		if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains(server)) {
			return true;
		}
		return false;
	}

	public static String setServerIp(String ip) {
		return serverIp = ip;
	}

	public static String getServerIp() {
		return serverIp;
	}
}

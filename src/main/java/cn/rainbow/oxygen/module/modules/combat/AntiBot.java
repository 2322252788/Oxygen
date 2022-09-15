package cn.rainbow.oxygen.module.modules.combat;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.PacketEvent;
import cn.rainbow.oxygen.event.events.UpdateEvent;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.setting.BooleanValue;
import cn.rainbow.oxygen.module.setting.ModeValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AntiBot extends Module {

	private static ModeValue mode = new ModeValue("Mode", "Hypixel");
	private BooleanValue debug = new BooleanValue("Debug", false);

	private static final ArrayList<String> players = new ArrayList<String>();

	public AntiBot() {
		super("AntiBot", Category.Combat);
		mode.addValue("Hypixel");
		mode.addValue("HuaYuTing");
		mode.addValue("Test");
	}

	@Override
	public void onEnable() {
		if (mode.isCurrentMode("HuaYuTing")) {
			mc.thePlayer.addChatMessage(new ChatComponentText("\247e[AntiBot]\247f该功能还在测试中(只适合花雨庭使用)"));
		}
		super.onEnable();
	}

	@Override
	public void onDisable() {
		players.clear();
		super.onDisable();
	}

	@EventTarget(events = {UpdateEvent.class, PacketEvent.class})
	public void onPacket(Event e) {
		if (e instanceof UpdateEvent) {
			this.setDisplayName(mode.getCurrentValue());
			if (this.mc.thePlayer == null || this.mc.theWorld == null) return;
			if (mode.isCurrentMode("Hypixel")) {
				if (!mc.theWorld.loadedEntityList.isEmpty()) {
					for (Entity entity : mc.theWorld.loadedEntityList) {
						if (entity instanceof EntityPlayer && entity != mc.thePlayer) {
							String displayName = entity.getDisplayName().getFormattedText();
							if (!getTabPlayerList().contains(entity)
									&& !displayName.toLowerCase().contains("[npc")
									&& displayName.startsWith("\u00a7")
									&& entity.isEntityAlive()) {
								if (!isHypixelNPC(entity) & entity.isInvisible()) {
									mc.theWorld.removeEntity(entity);
								}
							}
						}
					}
				}
			}
		}
		if (e instanceof PacketEvent) {
			PacketEvent eventPacket = (PacketEvent) e;
			if (mode.isCurrentMode("Test")) {
				if (eventPacket.getPacket() instanceof S18PacketEntityTeleport) {
					S18PacketEntityTeleport packet = (S18PacketEntityTeleport) eventPacket.getPacket();
					Entity entity = mc.theWorld.getEntityByID(packet.getEntityId());
					if (entity instanceof EntityPlayer) {
						if (entity.isInvisible()) {
							mc.theWorld.removeEntity(entity);
						}
					}
				}
			}
			if (mode.isCurrentMode("HuaYuTing")) {
				PacketEvent ep = (PacketEvent) e;
				if (mc.thePlayer == null || mc.theWorld == null)
					return;
				final Packet<?> packet = ep.getPacket();
				if (packet instanceof S02PacketChat) {
					S02PacketChat chatMessage = (S02PacketChat) packet;
					Matcher matchera = Pattern.compile(".*死了(!|！).*").matcher(chatMessage.getChatComponent().getUnformattedText());
					if (matchera.find()) {
						Matcher matcher = Pattern.compile("> (.*?)\\(").matcher(chatMessage.getChatComponent().getUnformattedText());
						if (matcher.find()) {
							String name = matcher.group(1);
							if (!name.equals("")) {
								if (!getPlayers().contains(name)) {
									getPlayers().add(name);
									if (this.debug.getCurrentValue()) {
										mc.thePlayer.addChatMessage(new ChatComponentText("\247e[AntiBot]\247f添加无敌人：" + name));
									}
									new Thread(() -> {
										try {
											Thread.sleep(6000);
											if (isPlayer(name)) {
												for (int i = 0; i < getPlayers().size(); ++i) {
													String f = getPlayers().get(i);
													if (f.equalsIgnoreCase(name)) {
														getPlayers().remove(i);
														if (debug.getCurrentValue()) {
															mc.thePlayer.addChatMessage(new ChatComponentText("\247e[AntiBot]\247f删除无敌人 " + name));
														}
													}
												}
											}

										} catch (InterruptedException ex) {
											ex.printStackTrace();
										}
									}).start();
								}
							}
						}
					} else {
						Matcher matcher = Pattern.compile("杀死了 (.*?)\\(").matcher(chatMessage.getChatComponent().getUnformattedText());
						if (matcher.find()) {
							String name = matcher.group(1);
							if (!name.equals("")) {
								if (!getPlayers().contains(name)) {
									getPlayers().add(name);
									if (this.debug.getCurrentValue()) {
										mc.thePlayer.addChatMessage(new ChatComponentText("\247e[AntiBot]\247f添加无敌人: " + name));
									}
									new Thread(() -> {
										try {
											Thread.sleep(6000);
											if (isPlayer(name)) {
												for (int i = 0; i < getPlayers().size(); ++i) {
													String f = getPlayers().get(i);
													if (f.equalsIgnoreCase(name)) {
														getPlayers().remove(i);
														if (debug.getCurrentValue()) {
															mc.thePlayer.addChatMessage(new ChatComponentText("\247e[AntiBot]\247f删除无敌人: " + name));
														}
													}
												}
											}
										} catch (InterruptedException ex) {
											ex.printStackTrace();
										}
									}).start();
								}
							}
						}
					}
				}
			}
		}
	}

	private ArrayList<String> getPlayers() {
		return players;
	}

	private static boolean isPlayer(final String player) {
		for (String bot : players) {
			if (bot.equalsIgnoreCase(player)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isBot(Entity e) {
		if (!(e instanceof EntityPlayer) || !Oxygen.INSTANCE.moduleManager.getModule(AntiBot.class).getEnabled())
			return false;
		EntityPlayer player = (EntityPlayer) e;

		if (mode.isCurrentMode("Hypixel")) {
			return !getTabPlayerList().contains(player) || isHypixelNPC(player);
		}

		if (mode.isCurrentMode("HuaYuTing")){
			return isPlayer(e.getName());
		}
		return false;
	}

	private static List<EntityPlayer> getTabPlayerList() {
		NetHandlerPlayClient nhpc = Minecraft.getMinecraft().thePlayer.sendQueue;
		List<EntityPlayer> list = new ArrayList<>();
		List<NetworkPlayerInfo> players = new GuiPlayerTabOverlay(Minecraft.getMinecraft(),
				Minecraft.getMinecraft().ingameGUI).field_175252_a.sortedCopy(nhpc.getPlayerInfoMap());
		for (final NetworkPlayerInfo info : players) {
			if (info == null) {
				continue;
			}
			list.add(Minecraft.getMinecraft().theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
		}
		return list;
	}

	private boolean isBotHypixel (EntityLivingBase entity){
		return (!entity.onGround && entity.isInvisible() && !entity.isPotionActive(14) && mc.thePlayer.ticksExisted < 40);
	}
	
	public static boolean isHypixelNPC(Entity entity) {
		String formattedName = entity.getDisplayName().getFormattedText();
		String customName = entity.getCustomNameTag();

		if (!formattedName.startsWith("\247") && formattedName.endsWith("\247r")) {
			return true;
		}

		if (formattedName.contains("[NPC]")) {
			return true;
		}
		return false;
	}
}

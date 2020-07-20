package me.Oxygen.modules.player;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.utils.other.Friend;
import me.Oxygen.value.Value;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;

@ModuleRegister(name = "PlayersCheck", category = Category.PLAYER)
public class PlayersCheck extends Module{
	
	private static ArrayList players = new ArrayList();
	
	private final Value<Boolean> info = new Value<Boolean>("PlayersCheck_Info", true);
	
	@Override
	public void onEnable() {
		mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(("§e[PlayersCheck]§f该功能还在测试中(只适合花语庭使用)")));
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		players.clear();
		super.onDisable();
	}
	
	@EventTarget(events = EventPacket.class)
	private final void onEvent(final Event event) {
		if(event instanceof EventPacket) {
			EventPacket ep = (EventPacket)event;
		if (mc.thePlayer == null || mc.theWorld == null)
            return;
        final Packet<?> packet = ep.getPacket();
        if (packet instanceof S02PacketChat) {
                S02PacketChat chatMessage = (S02PacketChat) packet;
                Matcher matchera = Pattern.compile(".*死了(!|！).*").matcher(chatMessage.getChatComponent().getUnformattedText());
                if(matchera.find()) {
                	Matcher matcher = Pattern.compile("> (.*?)\\(").matcher(chatMessage.getChatComponent().getUnformattedText());         
                    if (matcher.find()) {
                        String name = matcher.group(1);
                        if (!name.equals("")) {
                            if (!getPlayers().contains(name)) {
                            	//players.add(name);
                            	//FriendManager.getFriends().add(new Friend(name, name));
                            	getPlayers().add(new Friend(name, name));
                            	if(this.info.getValueState()) {
                            	mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e[PlayersCheck]§f添加无敌人：" + name));
                            	}
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                        	//if(FriendManager.getFriends().contains(name)) {
                                            Thread.sleep(6000);
                             	            if (isPlayer(name)) {
                             	               for(int i = 0; i < getPlayers().size(); ++i) {
                             	                  Friend f = (Friend)getPlayers().get(i);
                             	                  if (f.getName().equalsIgnoreCase(name)) {
                             	                	 getPlayers().remove(i);
                             	                	if(info.getValueState()) {
                             	                	mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e[PlayersCheck]§f删除无敌人 " + name));
                             	                	}
                             	                  }
                             	               }
                             	              }

                                       /*     boolean sss = FriendManager.getFriends().remove(name);
                                            FriendManager.getFriends().remove(name);
                                     
                                        	Mod.sendMessage("删除无敌人：");
                                        	//}*/
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }).start();
//                            mc.thePlayer.addChatMessage(new ChatComponentText("已加入列表: " + name));
                            }

                            //FileManager.saveFriends();
                        }
                    } 
                }else {
               	 Matcher matcher = Pattern.compile("杀死了 (.*?)\\(").matcher(chatMessage.getChatComponent().getUnformattedText());
                   if (matcher.find()) {
                       String name = matcher.group(1);
                       if (!name.equals("")) {
                           if (!getPlayers().contains(name)) {
                           	getPlayers().add(new Friend(name, name));
                           	if(this.info.getValueState()) {
                           	mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e[PlayersCheck]§f添加无敌人:" + name));
                           	}
                               new Thread(new Runnable() {
                                   @Override
                                   public void run() {
                                       try {
                                       	//if(FriendManager.getFriends().contains(name)) {
                                           Thread.sleep(6000);
                            	            if (isPlayer(name)) {
                            	               for(int i = 0; i < getPlayers().size(); ++i) {
                            	                  Friend f = (Friend)getPlayers().get(i);
                            	                  if (f.getName().equalsIgnoreCase(name)) {
                            	                	 getPlayers().remove(i);
                            	                	 if(info.getValueState()) {
                            	                	 mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§e[PlayersCheck]§f删除无敌人:" + name));
                            	                	 }
                            	                  }
                            	               }
                            	              }
                                       } catch (InterruptedException ex) {
                                           ex.printStackTrace();
                                       }
                                   }
                               }).start();
                           }

                       }
                   }
                	
                }    
        }
		}
	}
	
	public static ArrayList getPlayers() {
	      return players;
	   }

	   public static boolean isPlayer(final EntityPlayer player) {
	      Iterator var2 = players.iterator();

	      while(var2.hasNext()) {
	         Friend friend = (Friend)var2.next();
	         if (friend.getName().equalsIgnoreCase(player.getName())) {
	            return true;
	         }
	      }

	      return false;
	   }

	   public static boolean isPlayer(final String player) {
	      Iterator var2 = players.iterator();

	      while(var2.hasNext()) {
	         Friend friend = (Friend)var2.next();
	         if (friend.getName().equalsIgnoreCase(player)) {
	            return true;
	         }
	      }

	      return false;
	   }

	   public static Friend getPlayers(final String name) {
	      Iterator var2 = players.iterator();

	      while(var2.hasNext()) {
	         Friend friend = (Friend)var2.next();
	         if (friend.getName().equalsIgnoreCase(name)) {
	            return friend;
	         }
	      }

	      return null;
	   }

}

package me.Oxygen.modules.combat;

import java.util.ArrayList;
import java.util.List;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.injection.interfaces.IGuiPlayerTabOverlay;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.modules.Module;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;
import me.Oxygen.utils.timer.Timer;
import me.Oxygen.value.Value;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@ModuleRegister(name = "Antibot", category = Category.COMBAT)
public class Antibot extends Module {

	public static List<Entity> invalid = new ArrayList<>();
	private static List<Entity> removed = new ArrayList<>();
	private final Timer lastRemoved = new Timer();
	private final Timer timer = new Timer();
	
	private final static Value<String> mode = new Value<String>("Antibot" , "Mode", 0);
	private final Value<Boolean> cnhyp = new Value<Boolean>("Antibot_CNHypixel", false);

	public Antibot() {
		mode.addValue("WatchDog");
		mode.addValue("HuaYuTing");
	}

	public void onDisable() {
		invalid.clear();
		super.onDisable();
	}

	@EventTarget(events = EventUpdate.class)
	private final void onEvent(Event event) {	
		if(event instanceof EventUpdate) {
			this.setDisplayName(mode.getModeAt(mode.getCurrentMode()));
			if(this.mode.isCurrentMode("WatchDog")) {			
				//Clears the invalid player list after a second to prevent false positives staying permanent.
				if(!removed.isEmpty()){
					if(lastRemoved.delay(1000)){
						if(removed.size() == 1){
							ClientUtil.sendClientMessage("[AntiBot]" + removed.size() + " bot has been removed", Type.WARNING);
						}else{
							ClientUtil.sendClientMessage("[AntiBot]" + removed.size() + " bots have been removed", Type.WARNING);
						}
						lastRemoved.reset();
						removed.clear();
					}
				}

				if (!invalid.isEmpty() && timer.delay(1000)) {
					invalid.clear();
					timer.reset();
				}
				
				if (this.mode.isCurrentMode("WatchDog")) {
					for (Entity entity : mc.theWorld.getLoadedEntityList()) {
						if (entity instanceof EntityPlayer) {
							if (entity != mc.thePlayer && !invalid.contains(entity)) {

								String formated = entity.getDisplayName().getFormattedText();
								String custom = entity.getCustomNameTag();
								String name = entity.getName();

								if(entity.isInvisible() && !formated.startsWith("\247c") && formated.endsWith("\247r") && custom.equals(name)){
									double diffX = Math.abs(entity.posX - mc.thePlayer.posX);
									double diffY = Math.abs(entity.posY - mc.thePlayer.posY);
									double diffZ = Math.abs(entity.posZ - mc.thePlayer.posZ);
									double diffH = Math.sqrt(diffX * diffX + diffZ * diffZ);
									if(diffY < 13 && diffY > 10 && diffH < 3){
										List<EntityPlayer> list = this.getTabPlayerList();
										if(!list.contains(entity)){
											lastRemoved.reset();
											removed.add(entity);
											mc.theWorld.removeEntity(entity);
											invalid.add(entity);
										}

									}

								}
								
								//SHOP BEDWARS
								if(formated.startsWith("\247e\247r") && formated.endsWith("\247r")){
									invalid.add(entity);
								}
								
								if(entity.isInvisible()){
									//BOT INVISIBLES IN GAME
									if(!custom.equalsIgnoreCase("") && custom.toLowerCase().contains("\247c\247c") && name.contains("\247c")){
										lastRemoved.reset();
										removed.add(entity);
										mc.theWorld.removeEntity(entity);
										invalid.add(entity);
									}
								}
								
								//WATCHDOG BOT
								if(this.cnhyp.getValueState()) {
									if (!this.getTabPlayerList().contains(entity) && formated.startsWith("\247r\247c") && formated.endsWith("\247r")) {
										lastRemoved.reset();
										removed.add(entity);
										mc.theWorld.removeEntity(entity);
										invalid.add(entity);
									}
								} else {
									if (entity.onGround && mc.theWorld.getCollidingBoundingBoxes(entity, entity.getEntityBoundingBox().offset(0.0D, -0.001D, 0.0D)).isEmpty() && 
											!this.getTabPlayerList().contains(entity) && formated.startsWith("\247r\247c") && formated.endsWith("\247r")) {
										lastRemoved.reset();
										removed.add(entity);
										mc.theWorld.removeEntity(entity);
										invalid.add(entity);
									}
								}
								

								//LOBBY BOT
								if(formated.contains("\2478[NPC]")){
									invalid.add(entity);
								}
								if(!formated.contains("\247c") && !custom.equalsIgnoreCase("")){
									invalid.add(entity);
								}
							}
						}
					}
				}				
			}
		}
	}
	

	private final List<EntityPlayer> getTabPlayerList() {
		NetHandlerPlayClient nhpc = mc.thePlayer.sendQueue;
		List<EntityPlayer> list = new ArrayList<>();
		List<?> players = ((IGuiPlayerTabOverlay) new GuiPlayerTabOverlay(mc, mc.ingameGUI)).getField_175252_a().sortedCopy(nhpc.getPlayerInfoMap());
		for (final Object o : players) {
			final NetworkPlayerInfo info = (NetworkPlayerInfo) o;
			if (info == null) {
				continue;
			}
			list.add(mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
		}
		return list;
	}
	
	public static boolean isBot(Entity entity) {
		switch(mode.getModeAt(mode.getCurrentMode())) {
		case "WatchDog" :
			return invalid.contains(entity);
		case "HuaYuTing" :
			if((entity.getEntityBoundingBox().maxX - entity.getEntityBoundingBox().minX) < 0.21) {
				return true;
			}
			
		}
		return false;
	}
}

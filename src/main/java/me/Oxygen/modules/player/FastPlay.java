package me.Oxygen.modules.player;

import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.value.Value;

@ModuleRegister(name = "FastPlay", category = Category.PLAYER)
public class FastPlay extends Module{
	
	private Value<String> mode = new Value<String>("FastPlay", "Mode", 0);

	public FastPlay() {
	  //================Mode====================
		this.mode.addValue("BedWars_1v1");
		this.mode.addValue("BedWars_2v2");
		this.mode.addValue("BedWars_3v3");
		this.mode.addValue("BedWars_4v4");
		this.mode.addValue("SkyWars_Solo");
		this.mode.addValue("SkyWars_Solo_Insane");
		this.mode.addValue("SkyWars_Solo_LuckyBlock");
		this.mode.addValue("SkyWars_Team");
		this.mode.addValue("SkyWars_Team_Insane");
		this.mode.addValue("SkyWars_Team_LuckyBlock");
		this.mode.addValue("SurivialGames_Solo");
		this.mode.addValue("SurivialGames_Team");
		this.mode.addValue("MiniWalls");
	}
	
	@Override
	public void onEnable(){
		switch(mode.getModeAt(mode.getCurrentMode())) {
		
		case "BedWars_1v1" :
			mc.thePlayer.sendChatMessage("/play bedwars_eight_one");
			this.set(false);
			break;
			
		case "BedWars_2v2" :
			mc.thePlayer.sendChatMessage("/play bedwars_eight_two");
			this.set(false);
			break;
			
		case "BedWars_3v3" :
			mc.thePlayer.sendChatMessage("/play bedwars_four_three");
			this.set(false);
			break;
			
		case "BedWars_4v4" :
			mc.thePlayer.sendChatMessage("/play bedwars_four_four");
			this.set(false);
			break;
			
		case "SkyWars_Solo" :
			mc.thePlayer.sendChatMessage("/play solo_normal");
			this.set(false);
			break;
			
		case "SkyWars_Solo_Insane" :
			mc.thePlayer.sendChatMessage("/play solo_insane");
			this.set(false);
			break;
			
		case "SkyWars_Solo_LuckyBlock" :
			mc.thePlayer.sendChatMessage("/play solo_insane_lucky");
			this.set(false);
			break;
			
		case "SkyWars_Team" :
			mc.thePlayer.sendChatMessage("/play teams_normal");
			this.set(false);
			break;
			
		case "SkyWars_Team_Insane" :
			mc.thePlayer.sendChatMessage("/play teams_insane");
			this.set(false);
			break;
			
		case "SkyWars_Team_LuckyBlock" :
			mc.thePlayer.sendChatMessage("/play teams_insane_lucky");
			this.set(false);
			break;
			
		case "SurivialGames_Solo" :
			mc.thePlayer.sendChatMessage("/play blitz_solo_normal");
			this.set(false);
			break;
			
		case "SurivialGames_Team" :
			mc.thePlayer.sendChatMessage("/play blitz_teams_normal");
			this.set(false);
			break;
			
		case "MiniWalls" :
			mc.thePlayer.sendChatMessage("/play arcade_mini_walls");
			this.set(false);
			break;
		}
		}
	}

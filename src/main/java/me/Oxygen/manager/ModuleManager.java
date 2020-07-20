package me.Oxygen.manager;

import java.util.ArrayList;
import java.util.List;

import me.Oxygen.Oxygen;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.combat.Antibot;
import me.Oxygen.modules.combat.AutoClicker;
import me.Oxygen.modules.combat.AutoSword;
import me.Oxygen.modules.combat.Criticals;
import me.Oxygen.modules.combat.Hitbox;
import me.Oxygen.modules.combat.KeepSprint;
import me.Oxygen.modules.combat.KillAura;
import me.Oxygen.modules.combat.Reach;
import me.Oxygen.modules.combat.Velocity;
import me.Oxygen.modules.movement.Fly;
import me.Oxygen.modules.movement.Fly2;
import me.Oxygen.modules.movement.HYTHighJump;
import me.Oxygen.modules.movement.HYTLongJump;
import me.Oxygen.modules.movement.InvMove;
import me.Oxygen.modules.movement.NoFall;
import me.Oxygen.modules.movement.NoSlowdown;
import me.Oxygen.modules.movement.Phase;
import me.Oxygen.modules.movement.Speed;
import me.Oxygen.modules.movement.Sprint;
import me.Oxygen.modules.movement.TargetStrafe;
import me.Oxygen.modules.player.AutoArmor;
import me.Oxygen.modules.player.AutoRespawn;
import me.Oxygen.modules.player.AutoTool;
import me.Oxygen.modules.player.ChestStealer;
import me.Oxygen.modules.player.Collective;
import me.Oxygen.modules.player.FastPlay;
import me.Oxygen.modules.player.HYTBypass;
import me.Oxygen.modules.player.InvCleaner;
import me.Oxygen.modules.player.MCF;
import me.Oxygen.modules.player.MusicPlayer;
import me.Oxygen.modules.player.PlayersCheck;
import me.Oxygen.modules.player.Teams;
import me.Oxygen.modules.player.UseFast;
import me.Oxygen.modules.render.Animation;
import me.Oxygen.modules.render.ChestESP;
import me.Oxygen.modules.render.ClickGui;
import me.Oxygen.modules.render.ESP;
import me.Oxygen.modules.render.FullBright;
import me.Oxygen.modules.render.HUD;
import me.Oxygen.modules.render.ItemPhysic;
import me.Oxygen.modules.render.Nametags;
import me.Oxygen.modules.render.Projectiles;
import me.Oxygen.modules.render.TargetHUD;
import me.Oxygen.modules.world.AntiFall;
import me.Oxygen.modules.world.AntiSpammer;
import me.Oxygen.modules.world.CivBreak;
import me.Oxygen.modules.world.Crasher;
import me.Oxygen.modules.world.Eagle;
import me.Oxygen.modules.world.Fucker;
import me.Oxygen.modules.world.NoCommand;
import me.Oxygen.modules.world.Scaffold;
import me.Oxygen.modules.world.Scaffold2;
import me.Oxygen.modules.world.SpeedMine;

public class ModuleManager {
	
	public static ArrayList<Module> modules = new ArrayList<Module>();
	
	public ModuleManager() {
		
		Oxygen.INSTANCE.logger.info("[" + Oxygen.INSTANCE.CLIENT_NAME + "]" + " " + "Module Loading...");
		
		//Combat
		addModule(new Antibot());
		addModule(new AutoClicker());
		addModule(new AutoSword());
		addModule(new Criticals());
		addModule(new Hitbox());
		addModule(new KeepSprint());
		addModule(new KillAura());
		addModule(new Reach());
		addModule(new Velocity());
		
		//Movement
		addModule(new Fly());
		addModule(new Fly2());
		addModule(new HYTHighJump());
		addModule(new HYTLongJump());
		addModule(new InvMove());
		addModule(new NoFall());
		addModule(new NoSlowdown());
		addModule(new Phase());
		addModule(new Speed());
		addModule(new Sprint());
		addModule(new TargetStrafe());
		
		//Player
		addModule(new AutoArmor());
		addModule(new AutoRespawn());
		addModule(new AutoTool());
		addModule(new ChestStealer());
		addModule(new Collective());
		addModule(new FastPlay());
		addModule(new HYTBypass());
		addModule(new InvCleaner());
		addModule(new MCF());
		addModule(new MusicPlayer());
		addModule(new PlayersCheck());
		addModule(new Teams());
		addModule(new UseFast());
		
		//Render
		addModule(new Animation());
		addModule(new ChestESP());
		addModule(new ClickGui());
		addModule(new ESP());
		addModule(new FullBright());
		addModule(new HUD());
		addModule(new ItemPhysic());
		addModule(new Nametags());
		addModule(new Projectiles());
		addModule(new TargetHUD());
		
		//World
		addModule(new AntiFall());
		addModule(new AntiSpammer());
		addModule(new CivBreak());
		addModule(new Crasher());
		addModule(new Eagle());
		addModule(new Fucker());
		addModule(new NoCommand());
		addModule(new Scaffold());
		addModule(new Scaffold2());
		addModule(new SpeedMine());
	}
	
	public void addModule(Module module) {
		modules.add(module);
	}

	public List<Module> getModules() {
		return modules;
	}
	
	public ArrayList<Module> getToggled() {
		ArrayList<Module> toggled = new ArrayList<Module>();
		for(Module m : modules) {
			if(m.isEnabled()) {
				toggled.add(m);
			}
		}
		return toggled;
	}

	public Module getModuleByName(String moduleName) {
		for (Module mod : modules) {
			if ((mod.getName().trim().equalsIgnoreCase(moduleName))
					|| (mod.toString().trim().equalsIgnoreCase(moduleName.trim()))) {
				return mod;
			}
		}
		return null;
	}

	public Module getModule(Class<? extends Module> clazz) {
		for (Module m : modules) {
			if (m.getClass() == clazz) {
				return m;
			}
		}
		return null;
	}
	
	public static ArrayList<Module> getModulesInType(Category type) {
		ArrayList<Module> typed = new ArrayList<Module>();
		for(Module m : modules) {
			if(m.getCategory() == type) {
				typed.add(m);
			}
		}
		return typed;
	}

}

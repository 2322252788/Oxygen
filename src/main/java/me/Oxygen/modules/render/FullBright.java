package me.Oxygen.modules.render;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ModuleRegister(name = "FullBright", category = Category.RENDER)
public class FullBright extends Module {
	
	@EventTarget(events = EventUpdate.class)
	private final void onUpadte(Event e) {
		if(e instanceof EventUpdate) {
			mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 5200, 1));
		}
	}

	@Override
	public void onDisable() {
		this.mc.thePlayer.removePotionEffect(Potion.nightVision.getId());
	}

}

package me.Oxygen.modules.render;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.value.Value;

@ModuleRegister(name = "Animation", category = Category.RENDER)
public class Animation extends Module {
	
	public static Value<String> mode = new Value<String>("Animation", "Mode", 0);
	
	public Animation() {
		mode.addValue("Leakey");
		mode.addValue("Jello");
		mode.addValue("Sigma");
		mode.addValue("MeMe");
		mode.addValue("1.7");
	}
	
	@EventTarget(events = EventUpdate.class)
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
		this.setDisplayName(mode.getModeAt(mode.getCurrentMode()));
		}
	}
	

}

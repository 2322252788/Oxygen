package me.Oxygen.modules.render;

import org.lwjgl.input.Keyboard;

import me.Oxygen.Oxygen;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.ui.clickgui4.WorstClickGui;
import me.Oxygen.value.Value;

@ModuleRegister(name = "ClickGui", category = Category.RENDER, keybind = Keyboard.KEY_RSHIFT)
public class ClickGui extends Module{
	
	private Value<String> mode = new Value<String>("ClickGui", "Mode", 0);

	public ClickGui() {
		mode.addValue("Normal");
		mode.addValue("Jello");
		mode.addValue("Remix");
		mode.addValue("Power");
	}
	
	@Override
	public void onEnable() {
		switch(mode.getModeAt(mode.getCurrentMode())) {
		case "Normal" :
			this.mc.displayGuiScreen(Oxygen.INSTANCE.clickgui);
			this.set(false);
			break;
		case "Jello" :
			this.mc.displayGuiScreen(Oxygen.INSTANCE.crink);
			this.set(false);
			break;
		case "Remix" :
			this.mc.displayGuiScreen(new me.Oxygen.ui.clickgui2.ClickGui());
			this.set(false);
			break;
		case "Power" :
			this.mc.displayGuiScreen(new WorstClickGui());
			this.set(false);
			break;
		}
		super.onEnable();
	}

}

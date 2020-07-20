package me.Oxygen.modules.combat;

import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.value.Value;

@ModuleRegister(name = "Hitbox", category = Category.COMBAT)
public class Hitbox extends Module {

	public static Value<Double> size = new Value<Double>("Hitbox_Size",0.5d, 0.1d, 1.0d, 0.1d);
	

}

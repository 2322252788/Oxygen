package me.Oxygen.modules.combat;

import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.value.Value;

@ModuleRegister(name = "Reach", category = Category.COMBAT)
public class Reach extends Module {
	
	public static Value<Double> range = new Value<Double>("Reach_Range", 4.5d, 3d, 10.0d, 0.1d);

}

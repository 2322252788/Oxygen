package me.Oxygen.modules.player;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.utils.timer.Timer;
import me.Oxygen.value.Value;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;

@ModuleRegister(name = "ChestStealer", category = Category.PLAYER)
public class ChestStealer extends Module {

	private final Value<Double> delay = new Value<Double>("ChestStealer_Delay", 80.0, 0.0, 1000.0, 10.0);
	private final Timer timer = new Timer();

	@EventTarget(events = EventUpdate.class)
	private final void onEvent(Event event) {
		if(event instanceof EventUpdate) {
		/*
		if (StatCollector.translateToLocal("container.chest")
				.equalsIgnoreCase(this.lowerChestInventory.getDisplayName().getUnformattedText())
				|| StatCollector.translateToLocal("container.chestDouble")
						.equalsIgnoreCase(this.lowerChestInventory.getDisplayName().getUnformattedText())) {
			return;
		}
		*/
		
		if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
			ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
			int i2 = 0;
			while (i2 < container.getLowerChestInventory().getSizeInventory()) {
				if (container.getLowerChestInventory().getStackInSlot(i2) != null
						&& this.timer.delay((long) this.delay.getValueState().doubleValue())) {
					mc.playerController.windowClick(container.windowId, i2, 0, 1, mc.thePlayer);
					this.timer.reset();
				}
				++i2;
			}
			if (this.isEmpty()) {
				mc.thePlayer.closeScreen();
			}
		}
	}
}

	private final boolean isEmpty() {
		if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
			ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
			int i2 = 0;
			while (i2 < container.getLowerChestInventory().getSizeInventory()) {
				ItemStack itemStack = container.getLowerChestInventory().getStackInSlot(i2);
				if (itemStack != null && itemStack.getItem() != null) {
					return false;
				}
				++i2;
			}
		}
		return true;
	}
}

package cn.rainbow.oxygen.module.modules.player;

import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.UpdateEvent;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.setting.NumberValue;
import cn.rainbow.oxygen.utils.timer.TimerUtil;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;

public class ChestStealer extends Module {

	private final NumberValue delay = new NumberValue("Delay", 80.0, 0.0, 1000.0, 10.0);
	private final TimerUtil timer = new TimerUtil();

	public ChestStealer() {
		super("ChestStealer", Category.Player);
	}

	@EventTarget(events = {UpdateEvent.class})
	private void onUpdate(Event event) {
		if (event instanceof UpdateEvent){
			if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest) {
				ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
				int i2 = 0;
				while (i2 < container.getLowerChestInventory().getSizeInventory()) {
					if (container.getLowerChestInventory().getStackInSlot(i2) != null
							&& this.timer.delay((long) this.delay.getCurrentValue())) {
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

	private boolean isEmpty() {
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

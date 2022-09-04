package cn.rainbow.oxygen.module.modules.player;

import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.EventUpdate;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.setting.BooleanValue;
import cn.rainbow.oxygen.module.setting.NumberValue;
import cn.rainbow.oxygen.utils.InvUtils;
import cn.rainbow.oxygen.utils.timer.DelayTimer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class AutoArmor extends Module {
	long lastDelay = 0;
	public DelayTimer timer = new DelayTimer();
	private DelayTimer glitchFixer = new DelayTimer();
	public static NumberValue delay = new NumberValue("Delay", 150.0f, 100.0f, 500f, 50.0f);
	public static BooleanValue checkInv = new BooleanValue("OnlyInv", false);

	public static boolean isDone = false;

	public AutoArmor() {
		super("AutoArmor", Category.Combat);
	}

	public boolean isWearing() {
		return !timer.isDelayComplete(200);
	}

	@EventTarget(events = EventUpdate.class)
	public void onTick(Event event) {
		if (event instanceof EventUpdate) {
			if (mc.thePlayer == null) return;
			if (checkInv.getCurrentValue()) {
				if (!(mc.currentScreen instanceof GuiInventory))
					return;
			}

			if (mc.thePlayer.openContainer != null) {
				if (mc.thePlayer.openContainer instanceof ContainerChest) {
					return;
				}
			}

			if (!timer.hasPassed(lastDelay))
				return;

			lastDelay = (long) (delay.getCurrentValue() + getRandomDoubleInRange(-50, 150));

			if (mc.thePlayer.capabilities.isCreativeMode) {
				timer.reset();
				return;
			}

			int slot;

			for (ArmorType armorType : ArmorType.values()) {
				if ((slot = this.findArmor(armorType, InvUtils.getArmorScore(mc.thePlayer.inventory.armorItemInSlot(armorType.ordinal())))) != -1) {
					// set
					isDone = false;
					if (mc.thePlayer.inventory.armorItemInSlot(armorType.ordinal()) != null) {
						dropArmor(armorType.ordinal());
						timer.reset();
						return;
					}
					warmArmor(slot);
					timer.reset();
					return;
				} else {
					isDone = true;
				}
			}
		}
	}

	private int findArmor(ArmorType armorType, float minimum) {
		float best = 0;
		int result = -1;

		for (int i = 0; i < mc.thePlayer.inventory.mainInventory.length; i++) {
			ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
			if (InvUtils.getArmorScore(itemStack) < 0 || InvUtils.getArmorScore(itemStack) <= minimum
					|| InvUtils.getArmorScore(itemStack) < best || !isValid(armorType, itemStack))
				continue;

			best = InvUtils.getArmorScore(itemStack);
			result = i;
		}

		return result;
	}

	private double getRandomDoubleInRange(double minDouble, double maxDouble) {
		return minDouble >= maxDouble ? minDouble : new Random().nextDouble() * (maxDouble - minDouble) + minDouble;
	}

	private boolean isValid(ArmorType type, ItemStack itemStack) {
		if (!(itemStack.getItem() instanceof ItemArmor))
			return false;

		ItemArmor armor = (ItemArmor) itemStack.getItem();

		if (type == ArmorType.HELMET && armor.armorType == 0)
			return true;

		if (type == ArmorType.CHEST_PLATE && armor.armorType == 1)
			return true;

		if (type == ArmorType.LEGGINGS && armor.armorType == 2)
			return true;

		if (type == ArmorType.BOOTS && armor.armorType == 3)
			return true;

		return false;
	}


	private void warmArmor(int slot_In) {
		if (slot_In >= 0 && slot_In <= 8) { // 0-8 is hotbar
			clickSlot(slot_In + 36, 0, true);
		} else {
			clickSlot(slot_In, 0, true);
		}
	}


	private void clickSlot(int slot, int mouseButton, boolean shiftClick) {
		this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, mouseButton,
				shiftClick ? 1 : 0, this.mc.thePlayer);
	}


	private void dropArmor(int armorSlot) {
		int slot = InvUtils.armorSlotToNormalSlot(armorSlot);
		if (!InvUtils.isFull()) {

			this.clickSlot(slot, 0, true);
		} else {
			this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4,
					this.mc.thePlayer);
		}
	}
}

enum ArmorType {
	BOOTS, LEGGINGS, CHEST_PLATE, HELMET
}

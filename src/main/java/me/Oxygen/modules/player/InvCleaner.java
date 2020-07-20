package me.Oxygen.modules.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventMotion.MotionType;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.utils.timer.Timer;
import me.Oxygen.value.Value;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ModuleRegister(name = "InvCleaner", category = Category.PLAYER)
public class InvCleaner extends Module {
	private final Value<String> mode = new Value<String>("InvCleaner_Mode", "Mode", 0);
	private final Value<Double> delay = new Value<Double>("InvCleaner_Delay", 1.0, 0.0, 10.0, 1.0);
	private final Value<Double> blockcap = new Value<Double>("InvCleaner_BlockCap", 128.0, 0.0, 256.0, 1.0);
	private final Value<Boolean> food = new Value<Boolean>("InvCleaner_Food", false);
	private final Value<Boolean> archery = new Value<Boolean>("InvCleaner_Archery", false);
	private final Timer timer = new Timer();
	private final int weaponSlot = 36, pickaxeSlot = 37, axeSlot = 38, shovelSlot = 39;
	private ArrayList<Integer> whitelistedItems = new ArrayList<>();
	private static final List<Block> blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, 
    		Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, 
    		Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, 
    		Blocks.emerald_ore, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, 
    		Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, 
    		Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, 
    		Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, 
    		Blocks.wooden_button, Blocks.lever, Blocks.beacon, Blocks.ladder, Blocks.sapling, Blocks.oak_fence, 
    		Blocks.red_flower, Blocks.yellow_flower, Blocks.flower_pot, Blocks.red_mushroom, Blocks.brown_mushroom, 
    		Blocks.sand, Blocks.tallgrass, Blocks.tripwire_hook, Blocks.tripwire, Blocks.gravel, Blocks.dispenser, 
    		Blocks.dropper, Blocks.crafting_table, Blocks.furnace, Blocks.redstone_torch, Blocks.standing_sign, 
    		Blocks.wall_sign, Blocks.chest, Blocks.ender_chest, Blocks.trapped_chest, Blocks.web);

	public InvCleaner() {
		this.mode.mode.add("Basic");
		this.mode.mode.add("OpenInv");
	}


	@EventTarget(events = EventMotion.class)
	public void onEvent(Event event) {
		if(event instanceof EventMotion) {
			EventMotion em = (EventMotion)event;
		if(em.getMotionType() == MotionType.PRE) {
		long delay = ((long) this.delay.getValueState().intValue() * 50);
		this.setDisplayName(this.mode.getModeAt(this.mode.getCurrentMode()));

		if (this.mode.isCurrentMode("OpenInv") && !(mc.currentScreen instanceof GuiInventory)) {
			return;
		}

		if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory
				|| mc.currentScreen instanceof GuiChat) {
			if (timer.delay(delay) && weaponSlot >= 36) {

				if (!mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getHasStack()) {
					getBestWeapon(weaponSlot);

				} else {
					if (!isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) {
						getBestWeapon(weaponSlot);
					}
				}
			}
			if (timer.delay(delay) && pickaxeSlot >= 36) {
				getBestPickaxe(pickaxeSlot);
			}
			if (timer.delay(delay) && shovelSlot >= 36) {
				getBestShovel(shovelSlot);
			}
			if (timer.delay(delay) && axeSlot >= 36) {
				getBestAxe(axeSlot);
			}
			if (timer.delay(delay)) {
				for (int i = 9; i < 45; i++) {
					if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
						ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
						if (shouldDrop(is, i)) {
							drop(i);
							timer.reset();
							if (delay > 0)
								break;
						}
					}
				}
			}
		}
	}
}
}

	public void shiftClick(int slot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, mc.thePlayer);
	}

	public void swap(int slot1, int hotbarSlot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
	}

	public void drop(int slot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, mc.thePlayer);
	}

	public boolean isBestWeapon(ItemStack stack) {
		float damage = getDamage(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getDamage(is) > damage
						&& (is.getItem() instanceof ItemSword))
					return false;
			}
		}
		if ((stack.getItem() instanceof ItemSword )) {
			return true;
		} else {
			return false;
		}

	}

	public void getBestWeapon(int slot) {
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (isBestWeapon(is) && getDamage(is) > 0
						&& (is.getItem() instanceof ItemSword )) {
					swap(i, slot - 36);
					timer.reset();
					break;
				}
			}
		}
	}

	private float getDamage(ItemStack stack) {
		float damage = 0;
		Item item = stack.getItem();
		if (item instanceof ItemTool) {
			damage += getSpeed(stack);
		}
		if (item instanceof ItemSword) {
			ItemSword sword = (ItemSword) item;
			damage += sword.getDamageVsEntity();
		}
		damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f
				+ EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
		return damage;
	}
	
	private float getSpeed(ItemStack stack) {
		return ((ItemTool) stack.getItem()).getToolMaterial().getEfficiencyOnProperMaterial();
	}

	public boolean shouldDrop(ItemStack stack, int slot) {
		if (stack.getDisplayName().toLowerCase().contains("(right click)")) {
			return false;
		}
		if (stack.getDisplayName().toLowerCase().contains("§k||")) {
			return false;
		}
		if ((slot == weaponSlot && isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack()))
				|| (slot == pickaxeSlot
						&& isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())
						&& pickaxeSlot >= 0)
				|| (slot == axeSlot && isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())
						&& axeSlot >= 0)
				|| (slot == shovelSlot && isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())
						&& shovelSlot >= 0)) {
			return false;
		}
		if (stack.getItem() instanceof ItemArmor) {
			for (int type = 1; type < 5; type++) {
				if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
					ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
					if (isBestArmor(is, type)) {
						continue;
					}
				}
				if (isBestArmor(stack, type)) {
					return false;
				}
			}
		}
		if (stack.getItem() instanceof ItemBlock
				&& (getBlockCount() > this.blockcap.getValueState().intValue()
						|| getBlacklistedBlocks().contains(((ItemBlock) stack.getItem()).getBlock()))) {
			return true;
		}
		if (stack.getItem() instanceof ItemPotion) {
			if (isBadPotion(stack)) {
				return true;
			}
		}
		if (stack.getItem() instanceof ItemFood && this.food.getValueState()
				&& !(stack.getItem() instanceof ItemAppleGold)) {
			return true;
		}
		if (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool
				|| stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor) {
			return true;
		}
		if ((stack.getItem() instanceof ItemBow || stack.getItem().getUnlocalizedName().contains("arrow"))
				&& this.archery.getValueState()) {
			return true;
		}

		if (((stack.getItem().getUnlocalizedName().contains("tnt"))
				|| (stack.getItem().getUnlocalizedName().contains("stick"))
				|| (stack.getItem().getUnlocalizedName().contains("egg"))
				|| (stack.getItem().getUnlocalizedName().contains("string"))
				|| (stack.getItem().getUnlocalizedName().contains("cake"))
				|| (stack.getItem().getUnlocalizedName().contains("mushroom"))
				|| (stack.getItem().getUnlocalizedName().contains("flint"))
				|| (stack.getItem().getUnlocalizedName().contains("compass"))
				|| (stack.getItem().getUnlocalizedName().contains("dyePowder"))
				|| (stack.getItem().getUnlocalizedName().contains("feather"))
				|| (stack.getItem().getUnlocalizedName().contains("bucket"))
				|| (stack.getItem().getUnlocalizedName().contains("chest")
						&& !stack.getDisplayName().toLowerCase().contains("collect"))
				|| (stack.getItem().getUnlocalizedName().contains("snow"))
				|| (stack.getItem().getUnlocalizedName().contains("fish"))
				|| (stack.getItem().getUnlocalizedName().contains("enchant"))
				|| (stack.getItem().getUnlocalizedName().contains("exp"))
				|| (stack.getItem().getUnlocalizedName().contains("shears"))
				|| (stack.getItem().getUnlocalizedName().contains("anvil"))
				|| (stack.getItem().getUnlocalizedName().contains("torch"))
				|| (stack.getItem().getUnlocalizedName().contains("seeds"))
				|| (stack.getItem().getUnlocalizedName().contains("leather"))
				|| (stack.getItem().getUnlocalizedName().contains("reeds"))
				|| (stack.getItem().getUnlocalizedName().contains("skull"))
				|| (stack.getItem().getUnlocalizedName().contains("record"))
				|| (stack.getItem().getUnlocalizedName().contains("snowball"))
				|| (stack.getItem() instanceof ItemGlassBottle)
				|| (stack.getItem().getUnlocalizedName().contains("piston")))) {
			return true;
		}

		return false;
	}
	
	private static boolean isBestArmor(ItemStack stack, int type){
    	float prot = getProtection(stack);
    	String strType = "";
    	if(type == 1){
    		strType = "helmet";
    	}else if(type == 2){
    		strType = "chestplate";
    	}else if(type == 3){
    		strType = "leggings";
    	}else if(type == 4){
    		strType = "boots";
    	}
    	if(!stack.getUnlocalizedName().contains(strType)){
    		return false;
    	}
    	for (int i = 5; i < 45; i++) {
            if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
                if(getProtection(is) > prot && is.getUnlocalizedName().contains(strType))
                	return false;
            }
        }
    	return true;
    }
	
	private static float getProtection(ItemStack stack){
    	float prot = 0;
    	if ((stack.getItem() instanceof ItemArmor)) {
    		ItemArmor armor = (ItemArmor)stack.getItem();
    		prot += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075D;
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack)/100d;
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack)/100d;
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack)/100d;
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack)/50d;   	
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.featherFalling.effectId, stack)/100d;   	
    	}
	    return prot;
    }

	public ArrayList<Integer> getWhitelistedItem() {
		return whitelistedItems;
	}

	private int getBlockCount() {
		int blockCount = 0;
		for (int i = 0; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				Item item = is.getItem();
				if (is.getItem() instanceof ItemBlock
						&& !getBlacklistedBlocks().contains(((ItemBlock) item).getBlock())) {
					blockCount += is.stackSize;
				}
			}
		}
		return blockCount;
	}

	private void getBestPickaxe(int slot) {
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

				if (isBestPickaxe(is) && pickaxeSlot != i) {
					if (!isBestWeapon(is))
						if (!mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
							swap(i, pickaxeSlot - 36);
							timer.reset();
							if (this.delay.getValueState().intValue() > 0)
								return;
						} else if (!isBestPickaxe(mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())) {
							swap(i, pickaxeSlot - 36);
							timer.reset();
							if (this.delay.getValueState().intValue() > 0)
								return;
						}

				}
			}
		}
	}

	private void getBestShovel(int slot) {
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

				if (isBestShovel(is) && shovelSlot != i) {
					if (!isBestWeapon(is))
						if (!mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
							swap(i, shovelSlot - 36);
							timer.reset();
							if (this.delay.getValueState().intValue() > 0)
								return;
						} else if (!isBestShovel(mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())) {
							swap(i, shovelSlot - 36);
							timer.reset();
							if (this.delay.getValueState().intValue() > 0)
								return;
						}

				}
			}
		}
	}

	private void getBestAxe(int slot) {

		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

				if (isBestAxe(is) && axeSlot != i) {
					if (!isBestWeapon(is))
						if (!mc.thePlayer.inventoryContainer.getSlot(axeSlot).getHasStack()) {
							swap(i, axeSlot - 36);
							timer.reset();
							if (this.delay.getValueState().intValue() > 0)
								return;
						} else if (!isBestAxe(mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())) {
							swap(i, axeSlot - 36);
							timer.reset();
							if (this.delay.getValueState().intValue() > 0)
								return;
						}

				}
			}
		}
	}

	private boolean isBestPickaxe(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemPickaxe))
			return false;
		float value = getToolEffect(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe) {
					return false;
				}

			}
		}
		return true;
	}

	private boolean isBestShovel(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemSpade))
			return false;
		float value = getToolEffect(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getToolEffect(is) > value && is.getItem() instanceof ItemSpade) {
					return false;
				}

			}
		}
		return true;
	}

	private boolean isBestAxe(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemAxe))
			return false;
		float value = getToolEffect(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)) {
					return false;
				}

			}
		}
		return true;
	}

	private float getToolEffect(ItemStack stack) {
		Item item = stack.getItem();
		if (!(item instanceof ItemTool))
			return 0;
		String name = item.getUnlocalizedName();
		ItemTool tool = (ItemTool) item;
		float value = 1;
		if (item instanceof ItemPickaxe) {
			value = tool.getStrVsBlock(stack, Blocks.stone);
			if (name.toLowerCase().contains("gold")) {
				value -= 5;
			}
		} else if (item instanceof ItemSpade) {
			value = tool.getStrVsBlock(stack, Blocks.dirt);
			if (name.toLowerCase().contains("gold")) {
				value -= 5;
			}
		} else if (item instanceof ItemAxe) {
			value = tool.getStrVsBlock(stack, Blocks.log);
			if (name.toLowerCase().contains("gold")) {
				value -= 5;
			}
		} else
			return 1f;
		value += EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075D;
		value += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100d;
		return value;
	}

	private boolean isBadPotion(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ItemPotion) {
			final ItemPotion potion = (ItemPotion) stack.getItem();
			if (potion.getEffects(stack) == null)
				return true;
			for (final Object o : potion.getEffects(stack)) {
				final PotionEffect effect = (PotionEffect) o;
				if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId()
						|| effect.getPotionID() == Potion.moveSlowdown.getId()
						|| effect.getPotionID() == Potion.weakness.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	boolean invContainsType(int type) {

		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				Item item = is.getItem();
				if (item instanceof ItemArmor) {
					ItemArmor armor = (ItemArmor) item;
					if (type == armor.armorType) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void getBestArmor() {
		for (int type = 1; type < 5; type++) {
			if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
				if (isBestArmor(is, type)) {
					continue;
				} else {
					drop(4 + type);
				}
			}
			for (int i = 9; i < 45; i++) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
					ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
					if (isBestArmor(is, type) && getProtection(is) > 0) {
						shiftClick(i);
						timer.reset();
						if (this.delay.getValueState().intValue() > 0)
							return;
					}
				}
			}
		}
	}
	
	public static List<Block> getBlacklistedBlocks() {
        return blacklistedBlocks;
    }
	
	
}

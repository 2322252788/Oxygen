package cn.rainbow.oxygen.module.modules.combat;

import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.EventClickMouse;
import cn.rainbow.oxygen.event.events.EventUpdate;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.setting.BooleanValue;
import cn.rainbow.oxygen.module.setting.NumberValue;
import cn.rainbow.oxygen.utils.timer.TimeHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import java.util.Random;

public class AutoClicker extends Module {

	private TimeHelper left = new TimeHelper();
	private TimeHelper right = new TimeHelper();
	Random random = new Random();

	public static boolean isClicking = false;

	public boolean isDone = true;

	public int timer;

	public NumberValue maxCps = new NumberValue("MaxCPS", 12, 1, 20, 1);
	public NumberValue minCps = new NumberValue("MinCPS", 8, 1, 20, 1);
	public BooleanValue blockHit = new BooleanValue("BlockHit", false);
	public BooleanValue jitter = new BooleanValue("Jitter", false);

	public AutoClicker() {
		super("AutoClicker", Category.Combat);
	}

	@Override
	public void onEnable() {
		isDone = true;
		timer = 0;
	}

	@Override
	public void onDisable() {
		isDone = true;
	}

	private long getDelay() {
		return (long) (this.maxCps.getCurrentValue() + this.random.nextDouble()
				* (this.minCps.getCurrentValue() - this.maxCps.getCurrentValue()));
	}

	@EventTarget(events = {EventUpdate.class, EventClickMouse.class})
	public void onUpdate(Event event) {
		if(event instanceof EventUpdate) {
			if (mc.thePlayer != null) {
				isClicking = false;

				if (this.minCps.getCurrentValue() > this.maxCps.getCurrentValue()) {
					this.minCps.setCurrentValue(this.maxCps.getCurrentValue());
				}
				
				//YAY, Block Animation
				if(mc.gameSettings.keyBindAttack.pressed && mc.thePlayer.isUsingItem()) {
					this.swingItemNoPacket();
				}

				if (mc.gameSettings.keyBindAttack.pressed && !mc.thePlayer.isUsingItem()) {
					if (this.left.isDelayComplete(1000 / (double) this.getDelay())) {

						if (this.jitter.getCurrentValue()) {
							jitter(this.random);
						}

						mc.leftClickCounter = 0;
						mc.clickMouse();

						isClicking = true;
						left.reset();
					}
				}
			}

			if (!isDone) {
				switch (this.timer) {
				case 0: {
					mc.gameSettings.keyBindUseItem.pressed = false;
					break;
				}
				case 1:
				case 2: {
					mc.gameSettings.keyBindUseItem.pressed = true;
					break;
				}
				case 3: {
					mc.gameSettings.keyBindUseItem.pressed = false;
					isDone = true;
					this.timer = -1;
				}
				}
				++this.timer;
			}
		}
		if(event instanceof EventClickMouse) {
			ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
			if (stack != null && this.blockHit.getCurrentValue()) {
				if (stack.getItem() instanceof ItemSword && !mc.thePlayer.isUsingItem()) {
					if (!isDone || this.timer > 0)
						return;
					isDone = false;
				}
			}
		}
	}

	public void swingItemNoPacket() {
		if (!mc.thePlayer.isSwingInProgress || mc.thePlayer.swingProgressInt >= mc.thePlayer.getArmSwingAnimationEnd() / 2
				|| mc.thePlayer.swingProgressInt < 0) {
			mc.thePlayer.swingProgressInt = -1;
			mc.thePlayer.isSwingInProgress = true;
		}
	}

	public void jitter(Random rand) {
		if (rand.nextBoolean()) {
			if (rand.nextBoolean()) {
				mc.thePlayer.rotationPitch -= (float) (rand.nextFloat() * 0.6);
			} else {
				mc.thePlayer.rotationPitch += (float) (rand.nextFloat() * 0.6);
			}
		} else if (rand.nextBoolean()) {
			mc.thePlayer.rotationYaw -= (float) (rand.nextFloat() * 0.6);
		} else {
			mc.thePlayer.rotationYaw += (float) (rand.nextFloat() * 0.6);
		}
	}
}

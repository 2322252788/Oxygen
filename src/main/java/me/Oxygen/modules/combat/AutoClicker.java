package me.Oxygen.modules.combat;

import java.util.Random;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventClickMouse;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.injection.interfaces.IEntityLivingBase;
import me.Oxygen.injection.interfaces.IKeyBinding;
import me.Oxygen.injection.interfaces.IMinecraft;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.modules.Module;
import me.Oxygen.value.Value;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

@ModuleRegister(name = "AutoClicker", category = Category.COMBAT)
public class AutoClicker extends Module{
	private Timer left = new Timer();
    private Timer right = new Timer();
	Random random = new Random();
    private boolean isClicking = false;
    private boolean isDone = true;
    private int timer;
    
	private Value<Double> maxCps = new Value<Double>("AutoClicker_MaxCPS", 12.0, 1.0, 20.0, 1.0);
	private Value<Double> minCps = new Value<Double>("AutoClicker_MinCPS", 8.0, 1.0, 20.0, 1.0);
	private Value<Boolean> blockHit = new Value<Boolean>("AutoClicker_BlockHit", false);
	private Value<Boolean> jitter = new Value<Boolean>("AutoClicker_Jitter", false);
	
	@Override
	public void onEnable() {
        this.isDone = true;
        this.timer = 0;
        super.onEnable();
    }
    
	@Override
    public void onDisable() {
        this.isDone = true;
        super.onDisable();
    }
	
	private long getDelay() {
        return (long)(this.maxCps.getValueState().intValue() + this.random.nextDouble() * (this.minCps.getValueState().intValue() - this.maxCps.getValueState().intValue()));
    }
	
	@EventTarget(events = {EventUpdate.class, EventClickMouse.class})
	private void onEvent(Event event) {
		if(event instanceof EventUpdate) {
			if (mc.thePlayer != null) {
	            isClicking = false;
	            if (this.minCps.getValueState().intValue() > this.maxCps.getValueState().intValue()) {
	                this.minCps.setValueState(this.maxCps.getValueState());
	            }
	            if (((IKeyBinding)mc.gameSettings.keyBindAttack).getPress() && mc.thePlayer.isUsingItem()) {
	                this.swingItemNoPacket();
	            }
	            if (((IKeyBinding)mc.gameSettings.keyBindAttack).getPress() && !mc.thePlayer.isUsingItem() && this.left.isDelayComplete(Double.valueOf(1000.0 / this.getDelay()))) {
	                if (this.jitter.getValueState()) {
	                    this.jitter(this.random);
	                }
	                ((IMinecraft)mc).setClickCounter(0);
	                ((IMinecraft)mc).runCrinkMouse();
	                isClicking = true;
	                this.left.reset();
	            }
	        }
		}
		
		if(event instanceof EventClickMouse) {
			final ItemStack getCurrentEquippedItem = mc.thePlayer.getCurrentEquippedItem();
	        if (getCurrentEquippedItem != null && this.blockHit.getValueState() && getCurrentEquippedItem.getItem() instanceof ItemSword && !mc.thePlayer.isUsingItem()) {
	            if (!this.isDone || this.timer > 0) {
	                return;
	            }
	            this.isDone = false;
	        }
		}
	}
	
	public void swingItemNoPacket() {
        if (!mc.thePlayer.isSwingInProgress || mc.thePlayer.swingProgressInt >= ((IEntityLivingBase)mc.thePlayer).runGetArmSwingAnimationEnd() / 2 || mc.thePlayer.swingProgressInt < 0) {
            mc.thePlayer.swingProgressInt = -1;
            mc.thePlayer.isSwingInProgress = true;
        }
    }
	
	public void jitter(final Random random) {
        if (random.nextBoolean()) {
            if (random.nextBoolean()) {
                final EntityPlayerSP thePlayer = mc.thePlayer;
                thePlayer.rotationPitch -= (float)(random.nextFloat() * 0.6);
            }
            else {
                final EntityPlayerSP thePlayer2 = mc.thePlayer;
                thePlayer2.rotationPitch += (float)(random.nextFloat() * 0.6);
            }
        }
        else if (random.nextBoolean()) {
            final EntityPlayerSP thePlayer3 = mc.thePlayer;
            thePlayer3.rotationYaw -= (float)(random.nextFloat() * 0.6);
        }
        else {
            final EntityPlayerSP thePlayer4 = mc.thePlayer;
            thePlayer4.rotationYaw += (float)(random.nextFloat() * 0.6);
        }
    }
	
	private class Timer {
		private long lastMs;
		
		public Timer() {
	        this.lastMs = 0L;
	    }
	    
		private void reset() {
	        this.lastMs = System.currentTimeMillis();
	    }
	    
		private boolean isDelayComplete(final Double n) {
	        return System.currentTimeMillis() - this.lastMs > n;
	    }
	    
	}

}

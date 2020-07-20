package me.Oxygen.modules.movement;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventPriority;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventMotion.MotionType;
import me.Oxygen.event.events.EventSlowDown;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.modules.combat.KillAura;
import me.Oxygen.utils.Liquidbounce.time.MSTimer;
import me.Oxygen.utils.other.PlayerUtil;
import me.Oxygen.value.Value;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleRegister(name = "NoSlowdown", category = Category.MOVEMENT)
public class NoSlowdown extends Module{
	
	public Value<String> mode = new Value<String>("NoSlowDown","Mode",0); 
	private Value<Boolean> Packet = new Value<Boolean>("NoSlowDown_Packet", false);
	
	private Value<Double> blockForwardMultiplier = new Value<Double>("NoSlowDown_BlockForward", 1.0, 0.2, 1.0,0.01);
	private Value<Double> blockStrafeMultiplier = new Value<Double>("NoSlowDown_BlockStrafe", 1.0, 0.2, 1.0,0.01);

	private Value<Double> consumeForwardMultiplier = new Value<Double>("NoSlowDown_ConsumeForward", 0.45, 0.2, 1.0,0.01);
	private Value<Double> consumeStrafeMultiplier = new Value<Double>("NoSlowDown_ConsumeStrafe", 0.2, 0.2, 1.0,0.01);

	private Value<Double> bowForwardMultiplier = new Value<Double>("NoSlowDown_BowForward", 1.0, 0.2, 1.0,0.01);
	private Value<Double> bowStrafeMultiplier = new Value<Double>("NoSlowDown_BowStrafe", 1.0, 0.2, 1.0,0.01);
	
	MSTimer timer = new MSTimer();
	MSTimer timer2 = new MSTimer();

	public NoSlowdown() {
		mode.addValue("Normal");
		mode.addValue("Hypixel");
		mode.addValue("Custom");
	}
	
	@EventTarget(priority = EventPriority.LOW, 
			     events = {EventUpdate.class, EventSlowDown.class, EventMotion.class}
	)
	private final void onEvent(Event event) {
		if(event instanceof EventUpdate) {
			this.setDisplayName(mode.getModeAt(mode.getCurrentMode()));
		}
		if(event instanceof EventSlowDown) {
			EventSlowDown esd = (EventSlowDown)event;
			if(!mode.isCurrentMode("Custom")) {
				esd.setCancelled(true);
			}
			if(mode.isCurrentMode("Custom")) {
				Item heldItem = mc.thePlayer.getHeldItem() != null ? mc.thePlayer.getHeldItem().getItem():null;
				esd.setForward(this.getMultiplier(heldItem, true));
			    esd.setStrafe(this.getMultiplier(heldItem, false));
			}
		}
		if(event instanceof EventMotion) {
			EventMotion em = (EventMotion)event;
			if(em.getMotionType() == MotionType.PRE) {
				if(mode.isCurrentMode("Hypixel")) {
					if(mc.thePlayer.isBlocking() && PlayerUtil.isMoving2()){
		        		mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		        	}
				}
				if(Packet.getValueState() && mc.thePlayer.onGround && mc.thePlayer.isBlocking() && isMoving() && timer.hasTimePassed(50)) {
					timer.reset();
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				}
			}
            if(em.getMotionType() == MotionType.POST) {
            	if(mode.isCurrentMode("Hypixel")) {
    				if(mc.thePlayer.isBlocking() && PlayerUtil.isMoving2() && KillAura.target == null){
    					mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, this.mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));

    				}
    			}
            	
            	if(Packet.getValueState() && mc.thePlayer.onGround && mc.thePlayer.isBlocking() && isMoving() && timer2.hasTimePassed(50)) {
        			timer2.reset();
        			mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
        		}
			}
				}
			}
	
	private boolean isMoving() {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F);
    }
	
	private final float getMultiplier(Item item, boolean isForward) {
        Item item2 = item;
        return item2 instanceof ItemFood ||
        		item2 instanceof ItemPotion ||
        		item2 instanceof ItemBucketMilk ? (isForward ? this.consumeForwardMultiplier.getValueState().floatValue()
        				: this.consumeStrafeMultiplier.getValueState().floatValue()) 
        				: (item2 instanceof ItemSword ? (isForward ? this.blockForwardMultiplier.getValueState().floatValue() 
        						: this.blockStrafeMultiplier.getValueState().floatValue()) 
        						: (item2 instanceof ItemBow ? (isForward ? this.bowForwardMultiplier.getValueState().floatValue() 
        								: this.bowStrafeMultiplier.getValueState().floatValue()) 
        								: 0.2f));
    }

}

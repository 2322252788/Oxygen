package cn.rainbow.oxygen.module.modules.combat;

import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.EventMotion;
import cn.rainbow.oxygen.event.events.EventMove;
import cn.rainbow.oxygen.event.events.TickEvent;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.setting.BooleanValue;
import cn.rainbow.oxygen.module.setting.NumberValue;
import cn.rainbow.oxygen.utils.InvUtils;
import cn.rainbow.oxygen.utils.MoveUtils;
import cn.rainbow.oxygen.utils.block.BlockUtils;
import cn.rainbow.oxygen.utils.timer.DelayTimer;
import net.minecraft.block.BlockGlass;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.List;

public class AutoPot extends Module {
    private boolean jumping;
    private boolean rotated;
    public static NumberValue health = new NumberValue("Health", 13.0f, 1.0f, 20.0f, 1.0f);
    public static NumberValue delay = new NumberValue("Delay", 500.0f, 100.0f, 1500.0f, 50.0f);
    public static BooleanValue jump = new BooleanValue("Jump", false);
    public static BooleanValue regen = new BooleanValue("RegenPot", true);
    public static BooleanValue heal = new BooleanValue("HealPot", true);
    public static BooleanValue speed = new BooleanValue("SpeedPot", false);

    public static BooleanValue nofrog = new BooleanValue("NoFrog", true);
    public BooleanValue hvh = new BooleanValue("HVH", false);

    public AutoPot() {
        super("AutoPot", Category.Combat);
    }

    public static DelayTimer timer = new DelayTimer();
    private DelayTimer cooldown = new DelayTimer();

    private int lastPottedSlot;

    @EventTarget(events = {EventMove.class, EventMotion.class, TickEvent.class})
    private void onMove(final Event event) {
        if (event instanceof EventMove) {
            if (this.jumping) {
                this.mc.thePlayer.motionX = 0;
                this.mc.thePlayer.motionZ = 0;
                ((EventMove) event).setX(0);
                ((EventMove) event).setZ(0);

                if (cooldown.hasPassed(100) && this.mc.thePlayer.onGround) {
                    this.jumping = false;
                }
            }
        }
        if (event instanceof EventMotion) {
            EventMotion em = (EventMotion) event;
            if (em.getMotionType() == EventMotion.MotionType.PRE) {
                if (MoveUtils.getBlockUnderPlayer(mc.thePlayer, 0.01) instanceof BlockGlass || !MoveUtils.isOnGround(0.01))  {
                    timer.reset();
                    return;
                }

                if (mc.thePlayer.openContainer != null) {
                    if (mc.thePlayer.openContainer instanceof ContainerChest) {
                        timer.reset();
                        return;
                    }
                }

                //if (ModuleManager.scaffoldMod.isEnabled())
                //    return;

                if (KillAura.getTarget() != null && !this.hvh.getCurrentValue()) {
                    rotated = false;
                    timer.reset();
                    return;
                }

                final int potSlot = this.getPotFromInventory();
                if (potSlot != -1 && timer.hasPassed(delay.getCurrentValue())) {
                    if (jump.getCurrentValue() && !BlockUtils.isInLiquid()) {
                        ((EventMotion) event).setPitch(-89.5f);

                        this.jumping = true;
                        if (this.mc.thePlayer.onGround) {
                            this.mc.thePlayer.jump();
                            cooldown.reset();
                        }
                    } else {
                        ((EventMotion) event).setPitch(89.5f);
                    }

                    rotated = true;
                }
            }
            if (em.getMotionType() == EventMotion.MotionType.POST) {
                if (!rotated && !this.hvh.getCurrentValue())
                    return;

                rotated = false;

                final int potSlot = this.getPotFromInventory();
                if (potSlot != -1 && timer.hasPassed(delay.getCurrentValue()) && mc.thePlayer.isCollidedVertically) {
                    final int prevSlot = mc.thePlayer.inventory.currentItem;
                    if (potSlot < 9) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(potSlot));
                        mc.thePlayer.sendQueue.addToSendQueue(
                                new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(prevSlot));
                        mc.thePlayer.inventory.currentItem = prevSlot;
                        timer.reset();

                        this.lastPottedSlot = potSlot;
                    }
                }
            }
        }
        if (event instanceof TickEvent) {
            if (this.mc.currentScreen != null)
                return;

            final int potSlot = this.getPotFromInventory();
            if (potSlot != -1 && potSlot > 8 && this.mc.thePlayer.ticksExisted % 4 == 0) {
                this.swap(potSlot, InvUtils.findEmptySlot(this.lastPottedSlot));
            }
        }
    }

    private void swap(final int slot, final int hotbarNum) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
                this.mc.thePlayer);
    }

    private int getPotFromInventory() {
        // heals
        for (int i = 0; i < 36; ++i) {
            if (mc.thePlayer.inventory.mainInventory[i] != null) {
                final ItemStack is = mc.thePlayer.inventory.mainInventory[i];
                final Item item = is.getItem();

                if (!(item instanceof ItemPotion)) {
                    continue;
                }

                ItemPotion pot = (ItemPotion) item;

                if (!ItemPotion.isSplash(is.getMetadata())) {
                    continue;
                }

                List<PotionEffect> effects = pot.getEffects(is);

                for (PotionEffect effect : effects) {
                    if (mc.thePlayer.getHealth() < health.getCurrentValue() && ((heal.getCurrentValue() && effect.getPotionID() == Potion.heal.id) || (regen.getCurrentValue() && effect.getPotionID() == Potion.regeneration.id && !hasEffect(Potion.regeneration.id))))
                        return i;
                }
            }
        }

        // others
        for (int i = 0; i < 36; ++i) {
            if (this.mc.thePlayer.inventory.mainInventory[i] != null) {
                final ItemStack is = this.mc.thePlayer.inventory.mainInventory[i];
                final Item item = is.getItem();

                if (!(item instanceof ItemPotion)) {
                    continue;
                }

                List<PotionEffect> effects = ((ItemPotion) item).getEffects(is);

                for (PotionEffect effect : effects) {
                    if (effect.getPotionID() == Potion.moveSpeed.id && speed.getCurrentValue()
                            && !hasEffect(Potion.moveSpeed.id))
                        if (!is.getDisplayName().contains("\247a") || !nofrog.getCurrentValue())
                            return i;
                }
            }
        }

        return -1;
    }

    private boolean hasEffect(int potionId) {
        for (PotionEffect item : mc.thePlayer.getActivePotionEffects()) {
            if (item.getPotionID() == potionId)
                return true;
        }
        return false;
    }
}

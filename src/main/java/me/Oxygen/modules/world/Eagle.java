package me.Oxygen.modules.world;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

@ModuleRegister(name = "Eagle", category = Category.WORLD)
public class Eagle extends Module
{
    
    public Block getBlock(final BlockPos blockPos) {
        return mc.theWorld.getBlockState(blockPos).getBlock();
    }
    
    public Block getBlockUnderPlayer(final EntityPlayer entityPlayer) {
        return this.getBlock(new BlockPos(entityPlayer.posX, entityPlayer.posY - 1.0, entityPlayer.posZ));
    }
    
    @EventTarget(events = EventUpdate.class)
    public void onEvent(Event event) {
    	if(event instanceof EventUpdate) {
        if (this.getBlockUnderPlayer(mc.thePlayer) instanceof BlockAir) {
            if (mc.thePlayer.onGround) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
            }
        }
        else if (mc.thePlayer.onGround) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
    	}
    }
    
    public void onEnable() {
        mc.thePlayer.setSneaking(false);
        super.onEnable();
    }
    
    public void onDisable() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
        super.onDisable();
    }
}

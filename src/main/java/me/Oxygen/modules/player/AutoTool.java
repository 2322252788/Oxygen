package me.Oxygen.modules.player;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

@ModuleRegister(name = "AutoTool", category = Category.PLAYER)
public class AutoTool extends Module{
	
	@EventTarget(events = EventUpdate.class)
	private final void onEvent(Event event) {
		if(event instanceof EventUpdate) {
			if (mc.gameSettings.keyBindAttack.isKeyDown() && mc.objectMouseOver != null) {
				BlockPos pos = mc.objectMouseOver.getBlockPos();
				if (pos == null) {
					return;
				}
				updateTool(pos);
			}
  }
}
	
	private final void updateTool(BlockPos pos) {
	      Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
	      float strength = 1.0F;
	      int bestItemIndex = -1;

	      for(int i = 0; i < 9; ++i) {
	         ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i];
	         if(itemStack != null && itemStack.getStrVsBlock(block) > strength) {
	            strength = itemStack.getStrVsBlock(block);
	            bestItemIndex = i;
	         }
	      }

	      if(bestItemIndex != -1) {
	    	  Minecraft.getMinecraft().thePlayer.inventory.currentItem = bestItemIndex;
	      }

	   }

}

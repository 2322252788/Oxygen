package cn.rainbow.oxygen.module.modules.player

import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.UpdateEvent
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import net.minecraft.util.BlockPos

class AutoTool : Module("AutoTool", Category.Player) {
    @EventTarget(events = [UpdateEvent::class])
    private fun onEvent(event: Event) {
        if (event is UpdateEvent) {
            if (mc.gameSettings.keyBindAttack.isKeyDown && mc.objectMouseOver != null) {
                val pos = mc.objectMouseOver.blockPos ?: return
                updateTool(pos)
            }
        }
    }

    private fun updateTool(pos: BlockPos) {
        val block = mc.theWorld.getBlockState(pos).block
        var strength = 1.0f
        var bestItemIndex = -1
        for (i in 0..8) {
            val itemStack = mc.thePlayer.inventory.mainInventory[i]
            if (itemStack != null && itemStack.getStrVsBlock(block) > strength) {
                strength = itemStack.getStrVsBlock(block)
                bestItemIndex = i
            }
        }
        if (bestItemIndex != -1) {
            mc.thePlayer.inventory.currentItem = bestItemIndex
        }
    }
}
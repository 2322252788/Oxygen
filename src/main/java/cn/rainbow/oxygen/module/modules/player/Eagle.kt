package cn.rainbow.oxygen.module.modules.player

import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import net.minecraft.util.BlockPos
import net.minecraft.entity.player.EntityPlayer
import cn.rainbow.oxygen.event.events.UpdateEvent
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import net.minecraft.block.Block
import net.minecraft.block.BlockAir
import net.minecraft.client.settings.KeyBinding

class Eagle : Module("Eagle", Category.Player) {
    private fun getBlock(pos: BlockPos): Block {
        return mc.theWorld.getBlockState(pos).block
    }

    private fun getBlockUnderPlayer(player: EntityPlayer): Block {
        return getBlock(BlockPos(player.posX, player.posY - 1.0, player.posZ))
    }

    @EventTarget(events = [UpdateEvent::class])
    fun onUpdate(event: Event) {
        if (event is UpdateEvent) {
            if (getBlockUnderPlayer(mc.thePlayer) is BlockAir) {
                if (mc.thePlayer.onGround) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.keyCode, true)
                }
            } else {
                if (mc.thePlayer.onGround) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.keyCode, false)
                }
            }
        }
    }

    override fun onEnable() {
        mc.thePlayer.isSneaking = false
    }

    override fun onDisable() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.keyCode, false)
    }
}
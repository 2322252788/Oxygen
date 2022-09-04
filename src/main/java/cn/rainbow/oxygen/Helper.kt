package cn.rainbow.oxygen

import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.events.EventWorldChange
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient

object Helper {

    var worldChange: WorldClient? = null

    @JvmStatic
    fun onGameLoop() {
        val world = Minecraft.getMinecraft().theWorld
        if (worldChange == null) {
            worldChange = world
            return
        }

        if (world == null) {
            worldChange = null
            return
        }

        if (worldChange != world) {
            worldChange = world
            EventWorldChange().call()
        }
    }

    fun onEvent(event: Event) {

    }
}
package cn.rainbow.oxygen.module.modules.render

import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.module.setting.ModeValue
import cn.rainbow.oxygen.event.events.EventUpdate
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect

class Fullbright : Module("Fullbright", Category.Render) {
    private val mode = ModeValue("Mode", "Gamma")
    var oldGammaSetting = 0f

    init {
        mode.addValue("Gamma")
        mode.addValue("Potion")
    }

    @EventTarget(events = [EventUpdate::class])
    fun onUpdate(event: Event?) {
        if (event is EventUpdate) {
            displayName = mode.currentValue
            if (mode.isCurrentMode("Gamma")) {
                oldGammaSetting = mc.gameSettings.gammaSetting
                mc.gameSettings.gammaSetting = 1000.0f
            }
            if (mode.isCurrentMode("Potion")) {
                mc.thePlayer.addPotionEffect(PotionEffect(Potion.nightVision.getId(), 4000, 1))
            }
        }
    }

    override fun onEnable() {}
    override fun onDisable() {
        mc.gameSettings.gammaSetting = oldGammaSetting
    }
}
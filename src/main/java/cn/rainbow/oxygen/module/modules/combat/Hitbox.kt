package cn.rainbow.oxygen.module.modules.combat

import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.EventUpdate
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.setting.NumberValue

class Hitbox : Module("Hitbox", Category.Combat) {

    val siz = NumberValue("Size", 0.25, 0.1, 1.0, 0.05)

    @EventTarget(events = [EventUpdate::class])
    fun onUpdate(e: Event) {
        if (e is EventUpdate) {
            displayName = "Size:" + siz.currentValue
        }
    }

    companion object {
        @JvmStatic
		fun getSize(): Float {
            val hb = Oxygen.INSTANCE.moduleManager.getModule(Hitbox::class.java) as Hitbox
            return (if (hb.enabled) hb.siz.currentValue else 0.1f).toFloat()
        }
    }
}
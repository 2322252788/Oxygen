package cn.rainbow.oxygen.module.modules.combat

import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.UpdateEvent
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.ModuleInfo
import cn.rainbow.oxygen.module.setting.NumberValue

@ModuleInfo(name = "Hitbox", category = Category.Combat)
class Hitbox : Module() {

    val siz = NumberValue("Size", 0.25, 0.1, 1.0, 0.05)

    @EventTarget(events = [UpdateEvent::class])
    fun onUpdate(e: Event) {
        if (e is UpdateEvent) {
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
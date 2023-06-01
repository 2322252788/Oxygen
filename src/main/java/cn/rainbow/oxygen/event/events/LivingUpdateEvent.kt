package cn.rainbow.oxygen.event.events

import cn.rainbow.oxygen.event.Event
import net.minecraft.entity.Entity

class LivingUpdateEvent(val entity: Entity) : Event()
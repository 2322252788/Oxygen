package cn.rainbow.oxygen.event.events

import cn.rainbow.oxygen.event.Event
import net.minecraft.client.gui.ScaledResolution

class EventRender2D(val sr: ScaledResolution, val partialTicks: Float) : Event()
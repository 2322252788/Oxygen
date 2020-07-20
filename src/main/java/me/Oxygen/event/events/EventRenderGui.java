package me.Oxygen.event.events;

import me.Oxygen.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRenderGui extends Event {
	
	public ScaledResolution scaledRes;
	
	public EventRenderGui(ScaledResolution scaledRes) {
		this.scaledRes = scaledRes;
	}
	
}

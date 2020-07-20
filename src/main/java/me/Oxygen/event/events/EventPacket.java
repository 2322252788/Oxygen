package me.Oxygen.event.events;

import me.Oxygen.event.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event {
	private boolean cancelled;
	public Packet<?> packet;
	public boolean cancel;
	private PacketType type;

	public EventPacket(Packet<?> packet, PacketType type) {
		this.type = type;
		this.packet = packet;
		this.cancelled = false;
		this.cancel = false;
	}

	public Packet<?> getPacket() {
		return this.packet;	
	}

	public void setPacket(Packet<?> packet) {
		this.packet = packet;
	}
	
	public PacketType getPacketType() {
		return this.type;
	}
	
	public static enum PacketType {
        Send, 
        Recieve;
        
    private PacketType() {
      }
	}

	public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}


package cn.rainbow.oxygen.event.events;

import cn.rainbow.oxygen.event.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event {
	private boolean cancelled;
	public Packet<?> packet;
	private PacketType type;

	public EventPacket(Packet<?> packet, PacketType type) {
		this.type = type;
		this.packet = packet;
		this.cancelled = false;
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

	public enum PacketType {
		Send,
		Recieve;
	}
}


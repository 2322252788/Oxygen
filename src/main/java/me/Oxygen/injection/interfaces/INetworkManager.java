package me.Oxygen.injection.interfaces;

import net.minecraft.network.Packet;

public interface INetworkManager {

	void sendPacketNoEvent(Packet packetIn);

}

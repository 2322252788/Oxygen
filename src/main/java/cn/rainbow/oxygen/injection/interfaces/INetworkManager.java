package cn.rainbow.oxygen.injection.interfaces;

import net.minecraft.network.Packet;

public interface INetworkManager {
    void sendPacketNoEvent(Packet p);

    //void createNetworkManagerAndConnect(InetAddress address, int serverPort, boolean useNativeTransport);

}

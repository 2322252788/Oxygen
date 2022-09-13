package cn.rainbow.oxygen.injection.mixins;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import cn.rainbow.oxygen.event.events.PacketEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
	
	@Shadow
	@Final
	private ReentrantReadWriteLock readWriteLock;
	
	@Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void sendPacket(Packet packetIn, CallbackInfo ci) {
        final PacketEvent send = new PacketEvent(packetIn, PacketEvent.PacketType.Send);
        send.call();
        if (send.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = { "channelRead0" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V", shift = At.Shift.BEFORE) }, cancellable = true)
    private void packetReceived(ChannelHandlerContext p_channelRead0_1_, Packet packet, CallbackInfo ci) {
        final PacketEvent recieve = new PacketEvent(packet, PacketEvent.PacketType.Recieve);
        recieve.call();
        if (recieve.isCancelled()) {
            ci.cancel();
        }
    }
    
    @Shadow
    private void dispatchPacket(final Packet p0, final GenericFutureListener[] p1) {}
    
    @Shadow
    private void flushOutboundQueue() {}
    
    @Shadow
    public boolean isChannelOpen() {
    	return false;
    }
}
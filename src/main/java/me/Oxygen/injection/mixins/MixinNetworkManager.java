package me.Oxygen.injection.mixins;

import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.event.events.EventPacket.PacketType;
import me.Oxygen.injection.interfaces.INetworkManager;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

@Mixin(NetworkManager.class)
public class MixinNetworkManager implements INetworkManager {
	
	@Shadow
	@Final
	private ReentrantReadWriteLock readWriteLock;
	
	@Shadow
	@Final
	private Queue<InboundHandlerTuplePacketListener> outboundPacketsQueue;
	
	@Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void sendPacket(Packet packetIn, CallbackInfo ci) {
        final EventPacket send = new EventPacket(packetIn, PacketType.Send);
        send.call();
        if (send.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = { "channelRead0" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V", shift = At.Shift.BEFORE) }, cancellable = true)
    private void packetReceived(ChannelHandlerContext p_channelRead0_1_, Packet packet, CallbackInfo ci) {
        final EventPacket recieve = new EventPacket(packet, PacketType.Recieve);
        recieve.call();
        if (recieve.isCancelled()) {
            ci.cancel();
        }
    }

    @Override
    public void sendPacketNoEvent(Packet packetIn) {
		if (this.isChannelOpen()) {
			this.flushOutboundQueue();
			this.dispatchPacket(packetIn, (GenericFutureListener<? extends Future<? super Void>>[]) null);
		} else {
			this.readWriteLock.writeLock().lock();

			try {
				this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener[]) null));
			} finally {
				this.readWriteLock.writeLock().unlock();
			}
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
    
    static class InboundHandlerTuplePacketListener
    {
        private final Packet packet;
        private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;
        
        public InboundHandlerTuplePacketListener(final Packet inPacket, final GenericFutureListener<? extends Future<? super Void>>... inFutureListeners) {
            this.packet = inPacket;
            this.futureListeners = inFutureListeners;
        }
    }
}
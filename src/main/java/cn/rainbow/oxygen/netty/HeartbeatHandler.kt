package cn.rainbow.oxygen.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent

class HeartbeatHandler: ChannelInboundHandlerAdapter() {

    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        if (evt is IdleStateEvent) {
            val state = evt.state()
            if (state == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(byteArrayOf(0))
            }
        } else {
            super.userEventTriggered(ctx, evt)
        }
    }
}
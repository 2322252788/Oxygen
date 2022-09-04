package cn.rainbow.oxygen.netty

import by.radioegor146.nativeobfuscator.Native
import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.utils.CheckUtils
import cn.rainbow.oxygen.utils.other.RC4Utils
import com.google.gson.JsonObject
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.bytes.ByteArrayDecoder
import io.netty.handler.codec.bytes.ByteArrayEncoder
import io.netty.handler.timeout.IdleStateHandler
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

@Native
class Client {

    lateinit var channel: Channel
    var obj: Any? = null
    var running = false
    lateinit var ct: Thread
    lateinit var checkThread: Thread
    var login = false
    var account: String = ""
    var password: String = ""

    init {
        init()
    }

    private fun init() {
        //M D 5 C H E C K
        /*val oxygen = MD5Utils.getMD5(Oxygen::class.java)
        val client = MD5Utils.getMD5(Client::class.java)
        val mD5Utils = MD5Utils.getMD5(MD5Utils::class.java)
        val forgeMain = MD5Utils.getMD5(MixinLoader::class.java)
        if (!oxygen.equals(client, ignoreCase = true) ||
            !oxygen.equals(mD5Utils, ignoreCase = true) ||
            !oxygen.equals(forgeMain, ignoreCase = true)) {
            val fml = FMLCommonHandler.instance()
            val method = fml.javaClass.getMethod("exitJava", Int::class.java, Boolean::class.java)
            method.invoke(fml, 0, true)
        }*/
        //C H E C K E N D
        running = true
        /*ct = Thread(this::connect)
        ct.name = "IRCConnect"
        ct.isDaemon = true
        ct.start()
        checkThread = Thread(this::aliveCheck)
        checkThread.name = "CheckThread"
        checkThread.isDaemon = true
        checkThread.start()
        System.gc()*/
    }

    private fun connect() {
        val group: EventLoopGroup = NioEventLoopGroup()
        try {
            val bootstrap = Bootstrap()
                .group(group)
                .channel(NioSocketChannel::class.java)
                .handler(object : ChannelInitializer<SocketChannel>() {
                    @Throws(Exception::class)
                    override fun initChannel(ch: SocketChannel) {
                        val pipeline = ch.pipeline()
                        pipeline.addLast(IdleStateHandler(0, 8, 0, TimeUnit.SECONDS))
                        pipeline.addLast(LengthFieldBasedFrameDecoder(Int.MAX_VALUE, 0, 4, 0, 4))
                        pipeline.addLast(LengthFieldPrepender(4))
                        pipeline.addLast(ByteArrayDecoder())
                        pipeline.addLast(ByteArrayEncoder())
                        pipeline.addLast(HeartbeatHandler())
                        pipeline.addLast(ClientHandler())
                    }
                })
            Oxygen.logger.info("[Oxygen] 正在连接服务器...")
            this.channel = bootstrap.connect("170.178.217.203", 2333).sync().channel()//连接至目标主机
            if (this.account != "" && this.password != "" && login) {
                login(this.account, this.password)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun login(account: String, password: String) {
        this.account = account
        this.password = password
        if (!this.channel.isActive && running) {
            Oxygen.logger.warn("正在连接服务器...")
            return
        }
        println("sendLogin")
        val obj = JsonObject()
        obj.addProperty("username", account)
        obj.addProperty("password", RC4Utils.encrypt(password, CheckUtils.getHWID()))
        obj.addProperty("md5", RC4Utils.encrypt("2c57eb484dec5edce17f728f78d02ec3", CheckUtils.getHWID()))
        println(obj.toString())
        sendNoEncrypt(obj.toString())
    }

    private fun aliveCheck() {
        var error = 0
        Thread.sleep(5000L)
        while (running) {
            if (!this.channel.isActive) {
                if (error >= 20) {
                    Oxygen.logger.warn("服务器连接失败,请检查网络通畅后再启动游戏!")
                    Oxygen.INSTANCE.exit()
                }
                //曹尼玛的为什么总是断开啊啊啊啊啊
                ct = Thread(this::connect)
                ct.name = "IRCConnect"
                ct.isDaemon = true
                ct.start()
                error++
            }
            Thread.sleep(1000)
        }
    }

    fun send(string: String) {
        channel.writeAndFlush(RC4Utils.encrypt(string, CheckUtils.getHWID()))
    }

    private fun sendNoEncrypt(string: String) {
        channel.writeAndFlush(string.toByteArray(StandardCharsets.UTF_8))
    }
}
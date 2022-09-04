package cn.rainbow.oxygen.netty

import by.radioegor146.nativeobfuscator.Native
import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.gui.mainmenu.MainMenu
import cn.rainbow.oxygen.utils.CheckUtils
import cn.rainbow.oxygen.utils.Message
import cn.rainbow.oxygen.utils.other.RC4Utils
import com.google.gson.JsonParser
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import javassist.ClassPool
import javassist.CtClass
import javassist.CtField
import javassist.Modifier
import net.minecraft.client.Minecraft
import javax.swing.JOptionPane

@Native
class ClientHandler: SimpleChannelInboundHandler<ByteArray>() {

    var err = 0

    override fun channelRead0(ctx: ChannelHandlerContext, msg: ByteArray) {
        try {
            val raw = String(msg)
            Oxygen.logger.info(raw)
            if (!Oxygen.INSTANCE.client.login) {
                when (raw) {
                    "MD5_ERROR" -> {
                        Oxygen.INSTANCE.showMessageDialog("MD5校验失败,请使用最新版客户端！", "Oxygen", JOptionPane.ERROR_MESSAGE)
                        Oxygen.INSTANCE.exit()
                        return
                    }
                    "LOGIN_FAIL" -> {
                        err++
                        Oxygen.INSTANCE.showMessageDialog("密码错误！", "Oxygen", JOptionPane.ERROR_MESSAGE)
                        return
                    }
                    "NULL_HWID" -> {
                        err++
                        CheckUtils.setSysClipboardText(CheckUtils.getHWID())
                        Oxygen.INSTANCE.showMessageDialog("HWID已复制到剪贴板，请联系管理员设置后登录！", "Oxygen", JOptionPane.ERROR_MESSAGE)
                        Oxygen.INSTANCE.exit()
                        return
                    }
                    "NULL_USER" -> {
                        err++
                        Oxygen.INSTANCE.showMessageDialog("用户不存在！", "Oxygen", JOptionPane.ERROR_MESSAGE)
                        return
                    }
                    "BAN" -> {
                        Oxygen.INSTANCE.showMessageDialog("你已被封禁！", "Oxygen", JOptionPane.ERROR_MESSAGE)
                        Oxygen.INSTANCE.exit()
                        return
                    }
                }
            }
            if (err >= 10) {
                Oxygen.INSTANCE.exit()
            }
            val parser = JsonParser().parse(RC4Utils.decrypt(raw, CheckUtils.getHWID()))
            if (parser == null || !parser.isJsonObject) return
            val jsonObject = parser.asJsonObject
            if (jsonObject.has("Type")) {
                when (jsonObject.get("Type").asString) {
                    "IRC" -> {
                        val text = jsonObject.get("Data").asString
                        if (!Oxygen.INSTANCE.client.login) {
                            if (text.equals("LOGIN_SUCCESS")) {
                                Oxygen.INSTANCE.client.login = true
                                try {
                                    try {
                                        Class.forName("cn.rainbow.Verify.C_NMD_HECK")
                                    } catch (e: Exception) {
                                        //新建class
                                        val classPool = ClassPool.getDefault()
                                        val ctClass = classPool.makeClass("cn.rainbow.Verify.C_NMD_HECK")
                                        //写private boolean
                                        val ctField = CtField(CtClass.booleanType, "CRACKKKKER_N_M_S_LLLLLLL", ctClass)
                                        ctField.modifiers = Modifier.PRIVATE
                                        ctClass.addField(ctField)
                                        //置入
                                        Oxygen.INSTANCE.client.obj = ctClass.toClass().newInstance()
                                    }
                                    val field = Oxygen.INSTANCE.client.obj?.javaClass!!.getDeclaredField("CRACKKKKER_N_M_S_LLLLLLL")
                                    field.isAccessible = true
                                    field.set(Oxygen.INSTANCE.client.obj, true)
                                    val f = field.get(Oxygen.INSTANCE.client.obj) as Boolean
                                    if (f) {
                                        Minecraft.getMinecraft().displayGuiScreen(MainMenu())
                                    }
                                    Message.tellPlayer("Check", f.toString())
                                    field.isAccessible = false
                                } catch (e: Exception) {
                                    Oxygen.logger.error("VerifyError!!!", e)
                                }
                            }
                        } else Message.tellPlayer("IRC", text)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.channel().close()
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        Oxygen.logger.error("与服务器的连接断开...")
        ctx.channel().close()
        ctx.channel().eventLoop()
    }

}
package cn.rainbow.oxygen

import by.radioegor146.nativeobfuscator.Native
import cn.rainbow.oxygen.command.CommandManager
import cn.rainbow.oxygen.event.EventManager
import cn.rainbow.oxygen.file.FileManager
import cn.rainbow.oxygen.gui.clickgui.brisk.ClickUI
import cn.rainbow.oxygen.gui.font.FontManager
import cn.rainbow.oxygen.netty.Client
import cn.rainbow.oxygen.module.ModuleManager
import cn.rainbow.oxygen.module.setting.SettingManager
import cn.rainbow.oxygen.utils.CheckUtils
import cn.rainbow.oxygen.utils.RandomUtils
import cn.rainbow.oxygen.utils.other.MD5Utils
import cn.rainbow.oxygen.utils.WebUtils
import cn.rainbow.oxygen.utils.other.ServerUtils
import cn.rainbow.oxygen.utils.rotation.RotationUtils
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.FMLCommonHandler
import org.apache.logging.log4j.LogManager
import org.lwjgl.opengl.Display
import java.lang.reflect.Field
import java.util.*
import javax.swing.JOptionPane

@Native
class Oxygen {

    companion object {
        lateinit var INSTANCE: Oxygen
        const val name = "Oxygen"
        const val version = "20220628"
        const val DEV_MODE = true
        @JvmStatic
        val logger = LogManager.getLogger("Oxygen")
    }

    lateinit var fontmanager: FontManager
    lateinit var settingManager: SettingManager
    lateinit var moduleManager: ModuleManager
    lateinit var commandManager: CommandManager
    lateinit var filemanager: FileManager
    lateinit var briskGUI: ClickUI
    lateinit var client: Client

    var isLoad = false

    init {
        INSTANCE = this
        logger.info("[Oxygen] Loading...")
        start()
    }

    private fun start() {
        this.fontmanager = FontManager()
        this.moduleManager = ModuleManager()
        this.settingManager = SettingManager()
        this.commandManager = CommandManager()
        isLoad = true
        this.filemanager = FileManager()
        INSTANCE.filemanager.loadAllFile()
        this.briskGUI = ClickUI()
        logger.info(CheckUtils.getHWID())
        logger.info(MD5Utils.getMD5(this::class.java))
        Display.setTitle(getHitokoto())
        registerEvent()
        client = Client()
        Runtime.getRuntime().addShutdownHook(Thread(this::shutdown))
        //onHelp()
    }

    fun shutdown() {
        this.filemanager.saveAllFile()
        client.running = false
    }

    fun exit() {
        try {
            shutdown()
            Minecraft.getMinecraft().shutdown()
            val fml = FMLCommonHandler.instance()
            val method = fml.javaClass.getMethod("exitJava", Int::class.java, Boolean::class.java)
            method.invoke(fml, 0, true)
        } catch (e: Exception) {
            val runtime = Runtime.getRuntime()
            val method = runtime.javaClass.getMethod("exit", Int::class.java)
            method.invoke(runtime, 0)
        }
    }

    private fun registerEvent() {
        EventManager.register(RotationUtils())
        EventManager.register(ServerUtils())
    }

    private fun getHitokoto(): String {
        var hitokoto = "null"
        var from = ""
        var success = false
        var failed = 0
        while (!success) {
            try {
                if (failed > 5) break
                val json = WebUtils.sendGet("https://v1.hitokoto.cn/?c=a&c=b&c=d&charset=gbk")
                val obj = JsonParser().parse(json) as JsonObject
                hitokoto = obj["hitokoto"].asString
                from = obj["from"].asString
                success = true
            } catch (e: Exception) {
                failed ++
            }
        }
        return if (hitokoto.equals("null", ignoreCase = true)) {
            "$name $version"
        } else {
            "${RandomUtils.randomString(5 + Random().nextInt(5))} | $hitokoto --- $from"
        }
    }

    private fun onHelp() {
        val task = Thread {
            while (client.running) {
                if (client.login) {
                    try {
                        val clazz = Class.forName("cn.rainbow.Verify.C_NMD_HECK")
                        val field: Field = clazz.getDeclaredField("CRACKKKKER_N_M_S_LLLLLLL")
                        field.isAccessible = true
                        val get = field.getBoolean(client.obj)
                        if (!get) {
                            val fml = FMLCommonHandler.instance()
                            val method = fml.javaClass.getMethod("exitJava", Int::class.java, Boolean::class.java)
                            method.invoke(fml, 0, true)
                        } else {
                            field.isAccessible = false
                        }
                        Thread.sleep(1000 * 60 * 5)
                    } catch (e: Exception) {
                        val fml = FMLCommonHandler.instance()
                        val method = fml.javaClass.getMethod("exitJava", Int::class.java, Boolean::class.java)
                        method.invoke(fml, 0, true)
                    }
                }
            }
        }
        task.isDaemon = true
        task.start()
    }

    fun showMessageDialog(text: String, title: String, type: Int) {
        JOptionPane.showMessageDialog(null, text, title, type)
    }
}
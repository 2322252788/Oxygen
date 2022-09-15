package cn.rainbow.oxygen.module.modules.render

import by.radioegor146.nativeobfuscator.Native
import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.Render2DEvent
import cn.rainbow.oxygen.module.Category.Client
import cn.rainbow.oxygen.module.Category.Render
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.setting.BooleanValue
import cn.rainbow.oxygen.module.setting.ModeValue
import cn.rainbow.oxygen.utils.render.BlurBuffer
import cn.rainbow.oxygen.utils.render.ColorUtils
import cn.rainbow.oxygen.utils.render.RenderUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.resources.I18n
import net.minecraft.potion.Potion
import net.minecraft.util.EnumChatFormatting
import java.awt.Color

@Native
class HUD: Module("HUD", Render) {

    private val logoMode = ModeValue("Logo", "Blur", arrayOf("Blur", "Classic", "None"))
    private val array = BooleanValue("ModList", true)
    val chatFont = BooleanValue("ChatFont", true)
    val chatCombine = BooleanValue("ChatCombine", true)
    val chatAnim = BooleanValue("ChatAnimation", true)
    val chatRect = BooleanValue("ChatRect", false)

    @EventTarget(events = [Render2DEvent::class])
    private fun onEvent(event: Event) {
        if (event is Render2DEvent && !this.mc.gameSettings.showDebugInfo) {
            val sr = event.sr
            val x = mc.thePlayer.posX
            val y = mc.thePlayer.posY
            val z = mc.thePlayer.posZ
            val x1 = String.format("%.0f", x)
            val y1 = String.format("%.0f", y)
            val z1 = String.format("%.0f", z)
            if (!logoMode.isCurrentMode("None")) {
                if (logoMode.isCurrentMode("Blur")) {
                    if (BlurBuffer.blurEnabled()) {
                        BlurBuffer.updateBlurBuffer(true)
                        BlurBuffer.blurArea(3F, 3F, 98F, 66F, true)
                    }
                } else if (logoMode.isCurrentMode("Classic")) {
                    RenderUtil.drawRoundedRect2(3F, 3F, 101F, 68F, Color(255, 255, 255, 120).rgb, Color(255, 255, 255, 120).rgb)
                }
                val logoColor = if (logoMode.isCurrentMode("Blur"))
                    RenderUtil.reAlpha(ColorUtils.WHITE.c, 0.75f)
                else
                    RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.75f)

                Oxygen.INSTANCE.fontmanager.wqy40.drawString(Oxygen.name.uppercase(), 14.0f, 6.0f, logoColor)
                Oxygen.INSTANCE.fontmanager.wqy15.drawString("By Rainbow", 56.0f, 28.0f, logoColor)
                RenderUtil.drawRect(4.0, 41.0, 100.0, 42.0, logoColor)
                Oxygen.INSTANCE.fontmanager.wqy20.drawString("X:$x1 Y:$y1 Z:$z1", 92.0f / 2.0f - 39.0f, 44.0f, logoColor)
                Oxygen.INSTANCE.fontmanager.wqy20.drawString("FPS:" + Minecraft.getDebugFPS(), 92.0f / 2.0f - 39.0f, 55.0f, logoColor)
            }
            if (this.array.currentValue) {
                val font = Oxygen.INSTANCE.fontmanager.segoe17

                var yCount = 2
                var counter = 0

                val mods = Oxygen.INSTANCE.moduleManager.getEnableMods()

                mods.sortWith { m1: Module, m2: Module ->
                    val modText1: String =
                        m1.name + if (m1.displayName == "") "" else " " + m1.displayName
                    val modText2: String =
                        m2.name + if (m2.displayName == "") "" else " " + m2.displayName
                    val width1 = font.getStringWidth(modText1).toFloat()
                    val width2 = font.getStringWidth(modText2).toFloat()
                    -width1.compareTo(width2)
                }

                for (m in mods) {
                    if (!isBlacklisted(m)) {
                        val modText: String =
                            m.name + if (m.displayName == "") "" else " " + m.displayName
                        Gui.drawRect((sr.scaledWidth - font.getStringWidth(modText) - 4f).toInt(),
                            yCount - 2, sr.scaledWidth + 6,
                            yCount + 10,
                            RenderUtil.reAlpha(ColorUtils.BLACK.c, 0.55f)
                        )
                        font.drawStringWithShadow(
                            m.name, (sr.scaledWidth - font.getStringWidth(modText) - 3).toDouble(),
                            (yCount - 3).toDouble(), ColorUtils.WHITE.c
                        )
                        if (m.displayName != "") {
                            font.drawStringWithShadow(
                                " " + m.displayName,
                                sr.scaledWidth.toDouble() - font.getStringWidth(" " + m.displayName) - 3,
                                (yCount - 3).toDouble(), Color(180, 180, 180).rgb
                            )
                        }
                        yCount += font.height
                        counter ++
                    }
                }
            }
            renderPotionStatus(sr)
        }
    }

    private fun renderPotionStatus(sr: ScaledResolution) {
        var x = 0
        for (effect in mc.thePlayer.activePotionEffects) {
            val potion = Potion.potionTypes[effect.potionID]
            var PType: String = I18n.format(potion.name, arrayOfNulls<Any>(0))
            var d2 = ""
            when (effect.amplifier) {
                1 -> {
                    PType = PType + EnumChatFormatting.DARK_AQUA + " II"
                }
                2 -> {
                    PType = PType + EnumChatFormatting.BLUE + " III"
                }
                3 -> {
                    PType = PType + EnumChatFormatting.DARK_PURPLE + " IV"
                }
            }
            if (effect.duration < 600 && effect.duration > 300) {
                d2 = EnumChatFormatting.YELLOW.toString() + Potion.getDurationString(effect)
            } else if (effect.duration < 300) {
                d2 = EnumChatFormatting.RED.toString() + Potion.getDurationString(effect)
            } else if (effect.duration > 600) {
                d2 = EnumChatFormatting.WHITE.toString() + Potion.getDurationString(effect)
            }
            val y2 = sr.scaledHeight - mc.fontRendererObj.FONT_HEIGHT + x - 5
            val m2 = 25
            mc.fontRendererObj.drawStringWithShadow("$PType : $d2", (
                    sr.scaledWidth - m2 - mc.fontRendererObj.getStringWidth(PType) - 2).toFloat(), (
                    y2 - mc.fontRendererObj.FONT_HEIGHT).toFloat(), ColorUtils.DARKMAGENTA.c)
            x -= 12
        }
    }

    private fun isBlacklisted(mod: Module): Boolean {
        var black = false
        black = when(mod.category) {
            Render -> true
            Client -> true
            else -> false
        }
        return black
    }

    override fun onEnable() {
        /*try {
            val clazz = Class.forName("cn.rainbow.Verify.C_NMD_HECK")
            val field: Field = clazz.getDeclaredField("CRACKKKKER_N_M_S_LLLLLLL")
            field.isAccessible = true
            val get = field.getBoolean(Oxygen.INSTANCE.client.obj)
            if (!get) {
                val fml = FMLCommonHandler.instance()
                val method = fml.javaClass.getMethod("exitJava", Int::class.java, Boolean::class.java)
                method.invoke(fml, 0, true)
            } else {
                field.isAccessible = false
            }
        } catch (e: Exception) {
            val fml = FMLCommonHandler.instance()
            val method = fml.javaClass.getMethod("exitJava", Int::class.java, Boolean::class.java)
            method.invoke(fml, 0, true)
        }*/
    }
}

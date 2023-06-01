package cn.rainbow.oxygen.module.modules.combat

import cn.rainbow.oxygen.Oxygen
import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.*
import cn.rainbow.oxygen.event.events.MotionEvent.MotionType
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.ModuleInfo
import cn.rainbow.oxygen.module.setting.BooleanValue
import cn.rainbow.oxygen.module.setting.ModeValue
import cn.rainbow.oxygen.module.setting.NumberValue
import cn.rainbow.oxygen.utils.EntityUtils
import cn.rainbow.oxygen.utils.Message
import cn.rainbow.oxygen.utils.RandomUtils
import cn.rainbow.oxygen.utils.extensions.getDistanceToEntityBox
import cn.rainbow.oxygen.utils.other.ServerUtils
import cn.rainbow.oxygen.utils.rotation.RotationUtils
import cn.rainbow.oxygen.utils.timer.DelayTimer
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemSword
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

@ModuleInfo(name = "KillAura", category = Category.Combat)
class KillAura: Module() {

    private val aps = NumberValue("APS", 10.0, 1.0, 20.0, 1.0)
    private val fov =  NumberValue("FOV", 360.0, 10.0, 360.0, 10.0)
    private val range = NumberValue("Range", 4.2, 1.0, 8.0, 0.1)
    private val blockRange = NumberValue("BlockRange", 2.0, 0.0, 4.0, 0.1)
    private val switchDelay = NumberValue("SwitchDelay", 1000.0, 1.0, 2000.0, 100.0)
    private val minTurnSpeed = NumberValue("MinTurnSpeed", 80.0, 1.0, 120.0, 1.0)
    private val maxTurnSpeed = NumberValue("MaxTurnSpeed", 120.0, 1.0, 120.0, 1.0)
    private val mode = ModeValue("Mode", "Normal", arrayOf("Normal", "AAC"))
    private val attackMode = ModeValue("Attack", "Switch", arrayOf("Switch", "Single", "Multi"))
    private val attackTiming = ModeValue("AttackTiming", "Post", arrayOf("Pre", "Post"))
    private val priority = ModeValue("Priority", "Range", arrayOf("Health", "Fov", "Range", "Armor", "Distance"))
    private val autoblock = BooleanValue("AutoBlock", true)
    private val targetHud = BooleanValue("TargetHUD", true)
    private val autoDisable = BooleanValue("AutoDisable", true)

    companion object {
        @JvmStatic
        var target: EntityLivingBase? = null
        @JvmStatic
        var blocking = false
    }

    private var index = 0
    private val switchTimer = DelayTimer()
    private val delayTimer = DelayTimer()
    private val targets = CopyOnWriteArrayList<EntityLivingBase>()

    override fun onEnable() {
        this.index = 0
        super.onEnable()
    }

    override fun onDisable() {
        this.index = 0
        target = null
        this.targets.clear()
        if (mc.thePlayer != null) {
            unBlock(true)
        }
        super.onDisable()
    }

    @EventTarget(events = [MotionEvent::class, Render3DEvent::class, Render2DEvent::class, WorldChangeEvent::class])
    fun onEvent(event: Event) {
        if (event is MotionEvent) {
            this.displayName = this.mode.currentValue
            if (event.motionType.equals(MotionType.PRE)) {
                if(Oxygen.INSTANCE.moduleManager.getModule("Scaffold")!!.enabled && ServerUtils.INSTANCE.isOnHypixel) {
                    Message.tellPlayer("KillAura", "检测到Scaffold开启，已自动关闭")
                    this.enabled = false
                }
                loadTargets()

                if (index >= targets.size) {
                    index = 0
                }

                if (targets.size == 0) {
                    target = null
                } else {
                    target = targets[index]
                    if (mc.thePlayer.getDistanceToEntity(target) > range.currentValue) {
                        target = targets[0]
                    }
                }

                if (target != null) {
                    if (targets.size > 1 && this.switchTimer.hasPassed(switchDelay.currentValue)) {
                        this.index++
                        this.switchTimer.reset()
                    }

                    updateRotation(target!!)

                    if (mc.thePlayer.isBlocking
                        || mc.thePlayer.heldItem != null && mc.thePlayer.heldItem.item is ItemSword
                        && autoblock.currentValue && blocking) { // 格挡
                        unBlock(!mc.thePlayer.isBlocking && !autoblock.currentValue
                                && mc.thePlayer.getItemInUseCount() > 0)
                    }
                    if (attackTiming.isCurrentMode("Pre")) {
                        doAttack()
                    }
                } else {
                    targets.clear()
                    if (mc.thePlayer.heldItem != null && mc.thePlayer.heldItem.item is ItemSword
                        && autoblock.currentValue && blocking) {
                        unBlock(true)
                    }
                }
            }
            if (event.motionType.equals(MotionType.POST)) {
                if (attackTiming.isCurrentMode("Post") && target != null) {
                    doAttack()
                }
                if (target != null
                    && (mc.thePlayer.heldItem != null && mc.thePlayer.heldItem.item is ItemSword
                            && autoblock.currentValue || mc.thePlayer.isBlocking)
                    && !blocking) { // 格挡
                    doBlock(true)
                }
            }
        }
        if (event is WorldChangeEvent) {
            if (this.autoDisable.currentValue) {
                enabled = false
            }
        }
        if (event is Render2DEvent) {
            val sr = ScaledResolution(mc)
            val font2 = mc.fontRendererObj
            if (target != null && targetHud.currentValue) {
                val name = target!!.name
                font2.drawStringWithShadow(name, (sr.scaledWidth / 2).toFloat() - font2.getStringWidth(name) / 2,
                    (sr.scaledHeight / 2 - 30).toFloat(), -1)
                mc.textureManager.bindTexture(ResourceLocation("textures/gui/icons.png"))
                var i = 0
                while (i.toFloat() < target!!.maxHealth / 2.0f) {
                    mc.ingameGUI.drawTexturedModalRect(
                        (sr.scaledWidth / 2).toFloat() - target!!.maxHealth / 2.0f * 10.0f / 2.0f + (i * 10).toFloat(),
                        (sr.scaledHeight / 2 - 16).toFloat(),
                        16,
                        0,
                        9,
                        9
                    )
                    ++i
                }
                i = 0
                while (i.toFloat() < target!!.health / 2.0f) {
                    mc.ingameGUI.drawTexturedModalRect(
                        (sr.scaledWidth / 2).toFloat() - target!!.maxHealth / 2.0f * 10.0f / 2.0f + (i * 10).toFloat(),
                        (sr.scaledHeight / 2 - 16).toFloat(),
                        52,
                        0,
                        9,
                        9
                    )
                    ++i
                }
            }
        }
        if (event is Render3DEvent) {
            if (target != null && targetHud.currentValue) {
                if(target!!.isDead) {
                    return
                }
                //can mark
                val drawTime = (System.currentTimeMillis() % 2000).toInt()
                val drawMode = drawTime > 1000
                var drawPercent = drawTime / 1000.0
                //true when goes up
                if(!drawMode) {
                    drawPercent = 1 - drawPercent
                }else{
                    drawPercent -= 1
                }
                drawPercent = easeInOutQuad(drawPercent)
                mc.entityRenderer.disableLightmap()
                GL11.glPushMatrix()
                GL11.glDisable(GL11.GL_TEXTURE_2D)
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                GL11.glEnable(GL11.GL_LINE_SMOOTH)
                GL11.glEnable(GL11.GL_BLEND)
                GL11.glDisable(GL11.GL_DEPTH_TEST)
                GL11.glDisable(GL11.GL_CULL_FACE)
                GL11.glShadeModel(7425)
                mc.entityRenderer.disableLightmap()

                val it = target!!
                val bb = it.entityBoundingBox
                val radius = ((bb.maxX - bb.minX) + (bb.maxZ - bb.minZ)) * 0.5f
                val height = bb.maxY - bb.minY
                val x =
                    it.lastTickPosX + (it.posX - it.lastTickPosX) * event.partialTicks - mc.renderManager.viewerPosX
                val y =
                    (it.lastTickPosY + (it.posY - it.lastTickPosY) * event.partialTicks - mc.renderManager.viewerPosY) + height * drawPercent
                val z =
                    it.lastTickPosZ + (it.posZ - it.lastTickPosZ) * event.partialTicks - mc.renderManager.viewerPosZ
                val eased = (height / 3) * (if (drawPercent > 0.5) {
                    1 - drawPercent
                } else {
                    drawPercent
                }) * (if (drawMode) {
                    -1
                } else {
                    1
                })
                for (i in 5..360 step 5) {
                    val x1 = x - sin(i * Math.PI / 180F) * radius
                    val z1 = z + cos(i * Math.PI / 180F) * radius
                    val x2 = x - sin((i - 5) * Math.PI / 180F) * radius
                    val z2 = z + cos((i - 5) * Math.PI / 180F) * radius
                    GL11.glBegin(GL11.GL_QUADS)
                    GL11.glColor4f(1F, 1F, 1F, 0.7F * (i / 360F))
                    GL11.glVertex3d(x1, y + eased, z1)
                    GL11.glVertex3d(x2, y + eased, z2)
                    GL11.glColor4f(0F, 0F, 0F, 0F)
                    GL11.glVertex3d(x2, y, z2)
                    GL11.glVertex3d(x1, y, z1)
                    GL11.glEnd()
                }

                GL11.glEnable(GL11.GL_CULL_FACE)
                GL11.glShadeModel(7424)
                GL11.glColor4f(1f, 1f, 1f, 1f)
                GL11.glEnable(GL11.GL_DEPTH_TEST)
                GL11.glDisable(GL11.GL_LINE_SMOOTH)
                GL11.glDisable(GL11.GL_BLEND)
                GL11.glEnable(GL11.GL_TEXTURE_2D)
                GL11.glPopMatrix()
            }
        }
    }

    private fun easeInOutQuad(x: Double): Double {
        return if(x < 0.5){2 * x * x}else{1 - (-2 * x + 2).pow(2) / 2}
    }

    private fun doBlock(setItemUseInCount: Boolean) {
        if (setItemUseInCount)
            mc.thePlayer.itemInUseCount = mc.thePlayer.heldItem.maxItemUseDuration
        mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.heldItem))
        blocking = true
    }

    private fun unBlock(setItemUseInCount: Boolean) {
        if (setItemUseInCount)
            mc.thePlayer.itemInUseCount = 0
        C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos(-1, -1, -1), EnumFacing.DOWN)
        blocking = false
    }

    private fun doAttack() {
        val aps: Int = this.aps.currentValue.toInt()
        val delayValue = 1000.0F / (aps + getRandomDoubleInRange(-1.0, 1.0))

        if (this.delayTimer.hasPassed(delayValue) || (aps == 15 && this.mc.thePlayer.ticksExisted % 3 == 0)) {
            val isInRange = mc.thePlayer.getDistanceToEntity(target) <= range.currentValue

            if (isInRange) {
                delayTimer.reset()
            }

            if (mc.thePlayer.isBlocking || mc.thePlayer.heldItem != null
                && mc.thePlayer.heldItem.item is ItemSword && autoblock.currentValue) {
                unBlock(!mc.thePlayer.isBlocking && !autoblock.currentValue && mc.thePlayer.getItemInUseCount() > 0)
            }

            if (this.attackMode.isCurrentMode("Multi")) {
                targets.forEach { ent ->
                    if (ent.hurtTime < 7) {
                        attackEntity(ent)
                    }
                }
            } else if (isInRange) {
                attackEntity(target!!)
            }
        }
    }

    private fun attackEntity(entity: EntityLivingBase) {
        mc.thePlayer.swingItem()
        AttackEvent(entity).call()
        mc.thePlayer.sendQueue.addToSendQueue(C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK))
    }

    private fun updateRotation(entity: EntityLivingBase) {
        var boundingBox = entity.entityBoundingBox
        val minPredict = 0.5F
        val maxPredict = 1.0F
        boundingBox = boundingBox.offset(
            (entity.posX - entity.prevPosX) * RandomUtils.nextFloat(
                minPredict,
                maxPredict
            ),
            (entity.posY - entity.prevPosY) * RandomUtils.nextFloat(
                minPredict,
                maxPredict
            ),
            (entity.posZ - entity.prevPosZ) * RandomUtils.nextFloat(
                minPredict,
                maxPredict
            )
        )
        val (_, rotation) = RotationUtils.calculateCenter(
            "LiquidBounce",
            "Off",
            0.0,
            boundingBox,
            true,
            mc.thePlayer.canEntityBeSeen(entity)
        ) ?: return
        val limitedRotation = RotationUtils.limitAngleChange(RotationUtils.serverRotation, rotation,
            (Math.random() * (maxTurnSpeed.currentValue - minTurnSpeed.currentValue) + minTurnSpeed.currentValue).toFloat())
        try {
            RotationUtils.setTargetRotation(limitedRotation, if (mode.isCurrentMode("AAC")) 15 else 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadTargets() {
        val cache = CopyOnWriteArrayList<EntityLivingBase>()
        val size = when(attackMode.currentValue) {
            "Switch" -> 4
            "Multi" -> Int.MAX_VALUE
            else -> 1
        }
        for (ent in mc.theWorld.loadedEntityList) {
            if (ent !is EntityLivingBase || !EntityUtils.isSelected(ent, true) || !mc.thePlayer.canEntityBeSeen(ent)) {
                continue
            }

            val distance = mc.thePlayer.getDistanceToEntityBox(ent)
            val entFov = RotationUtils.getRotationDifference(ent)
            val f = fov.currentValue

            if (distance <= (range.currentValue + this.blockRange.currentValue) && (f == 180.0 || entFov <= f)) {
                cache.add(ent)
                if (cache.size >= size) break
            }
        }
        when (priority.currentValue) {
            "Health" -> cache.sortBy { it.health }
            "Fov" -> cache.sortBy { RotationUtils.getRotationDifference(it) }
            "Range" -> cache.sortBy { it.getDistanceToEntity(mc.thePlayer) }
            "Armor" -> cache.sortBy { it.totalArmorValue }
            "Distance" -> cache.sortBy { mc.thePlayer.getDistanceToEntityBox(it) }
        }
        this.targets.clear()
        this.targets.addAll(cache)
    }

    private fun getRandomDoubleInRange(minDouble: Double, maxDouble: Double): Double {
        return if (minDouble >= maxDouble) minDouble else Random().nextDouble() * (maxDouble - minDouble) + minDouble
    }
}
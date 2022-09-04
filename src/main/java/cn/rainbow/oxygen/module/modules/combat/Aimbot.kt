package cn.rainbow.oxygen.module.modules.combat

import cn.rainbow.oxygen.event.Event
import cn.rainbow.oxygen.event.EventTarget
import cn.rainbow.oxygen.event.events.EventStrafe
import cn.rainbow.oxygen.module.Category
import cn.rainbow.oxygen.module.Module
import cn.rainbow.oxygen.module.setting.BooleanValue
import cn.rainbow.oxygen.module.setting.NumberValue
import cn.rainbow.oxygen.utils.EntityUtils
import cn.rainbow.oxygen.utils.RandomUtils
import cn.rainbow.oxygen.utils.extensions.getDistanceToEntityBox
import cn.rainbow.oxygen.utils.rotation.RotationUtils
import cn.rainbow.oxygen.utils.timer.MSTimer
import cn.rainbow.oxygen.utils.rotation.Rotation
import kotlin.random.Random

class Aimbot : Module("Aimbot", Category.Combat) {

    private val rangeValue = NumberValue("Range", 4.4, 1.0, 8.0, 0.1)
    private val turnSpeedValue = NumberValue("TurnSpeed", 2.0, 1.0, 180.0, 1.0)
    private val fovValue = NumberValue("FOV", 180.0, 1.0, 180.0, 1.0)
    private val centerValue = BooleanValue("Center", false)
    private val lockValue = BooleanValue("Lock", true)
    private val onClickValue = BooleanValue("OnClick", false)
    private val jitterValue = BooleanValue("Jitter", false)

    private val clickTimer = MSTimer()

    @EventTarget(events = [EventStrafe::class])
    fun onStrafe(event: Event) {
        if (event is EventStrafe) {
            if (mc.gameSettings.keyBindAttack.isKeyDown) {
                clickTimer.reset()
            }

            if (onClickValue.currentValue && clickTimer.hasTimePassed(500L)) {
                return
            }

            val range = rangeValue.currentValue
            val entity = mc.theWorld.loadedEntityList
                .filter {
                    EntityUtils.isSelected(it, true) && mc.thePlayer.canEntityBeSeen(it) &&
                            mc.thePlayer.getDistanceToEntityBox(it) <= range && RotationUtils.getRotationDifference(it) <= fovValue.currentValue
                }
                .minByOrNull { RotationUtils.getRotationDifference(it) } ?: return

            if (!lockValue.currentValue && RotationUtils.isFaced(entity, range)) {
                return
            }

            val rotation = RotationUtils.limitAngleChange(
                Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch),
                if (centerValue.currentValue) {
                    RotationUtils.toRotation(RotationUtils.getCenter(entity.entityBoundingBox), true)
                } else {
                    RotationUtils.searchCenter(entity.entityBoundingBox, false, false, true,
                        false).rotation
                },
                (turnSpeedValue.currentValue + Math.random()).toFloat()
            )

            rotation.toPlayer(mc.thePlayer)

            if (jitterValue.currentValue) {
                val yaw = Random.nextBoolean()
                val pitch = Random.nextBoolean()
                val yawNegative = Random.nextBoolean()
                val pitchNegative = Random.nextBoolean()

                if (yaw) {
                    mc.thePlayer.rotationYaw += if (yawNegative) -RandomUtils.nextFloat(0F, 1F) else RandomUtils.nextFloat(0F, 1F)
                }

                if (pitch) {
                    mc.thePlayer.rotationPitch += if (pitchNegative) -RandomUtils.nextFloat(0F, 1F) else RandomUtils.nextFloat(0F, 1F)
                    if (mc.thePlayer.rotationPitch > 90) {
                        mc.thePlayer.rotationPitch = 90F
                    } else if (mc.thePlayer.rotationPitch < -90) {
                        mc.thePlayer.rotationPitch = -90F
                    }
                }
            }
        }
    }
}
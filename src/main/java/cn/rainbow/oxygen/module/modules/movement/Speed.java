package cn.rainbow.oxygen.module.modules.movement;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.MotionEvent;
import cn.rainbow.oxygen.event.events.MoveEvent;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.ModuleInfo;
import cn.rainbow.oxygen.module.setting.ModeValue;
import cn.rainbow.oxygen.utils.PlayerUtils;
import cn.rainbow.oxygen.utils.other.MathUtils;
import net.minecraft.potion.Potion;

@ModuleInfo(name = "Speed", category = Category.Movement)
public class Speed extends Module {

    public ModeValue modeValue = new ModeValue("Mode", "NCPBhop", new String[] {"NCPBhop"});

    private double lastDist;
    private int stage;
    private double moveSpeed;
    private final double boostSpeed = 2.1D;

    public void onEnable() {
        super.onEnable();

        mc.timer.timerSpeed = 1;
        this.lastDist = 0;
        this.stage = 2;
        this.moveSpeed = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        mc.timer.timerSpeed = 1;
        this.moveSpeed = getBaseMoveSpeed();
    }

    @EventTarget(events = {MotionEvent.class, MoveEvent.class})
    public void onEvent(Event event) {
        if (event instanceof MotionEvent) {
            MotionEvent em = (MotionEvent) event;
            if (em.getMotionType() == MotionEvent.MotionType.POST) {
                if (this.mc.thePlayer.isInWater() || this.mc.thePlayer.isInLava()) {
                    mc.timer.timerSpeed = 1.0F;
                    this.lastDist = 0.0D;
                    return;
                }

                double movementInput = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                double strafe = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                this.lastDist = Math.sqrt(movementInput * movementInput + strafe * strafe);
            }
        }
        if (event instanceof MoveEvent) {
            MoveEvent emm = (MoveEvent) event;
            if (modeValue.isCurrentMode("NCPBhop")) {
                if (this.mc.thePlayer.isInWater() || this.mc.thePlayer.isInLava() || this.mc.thePlayer.capabilities.isFlying) {
                    mc.timer.timerSpeed = 1;
                    return;
                }

                if (this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically) {
                    moveSpeed = PlayerUtils.getBaseMoveSpeed();
                    this.stage = 2;
                    mc.timer.timerSpeed = 1;
                }

                if (MathUtils.round(this.mc.thePlayer.posY - (double) ((int) this.mc.thePlayer.posY), 3) == MathUtils.round(0.138D, 3)) {
                    --mc.thePlayer.motionY;
                    emm.setY(emm.getY() - 0.0931D);
                    mc.thePlayer.posY -= 0.0931D;
                }

                if (this.stage != 2 || this.mc.thePlayer.moveForward == 0.0F && this.mc.thePlayer.moveStrafing == 0.0F) {
                    if (this.stage == 3) {
                        double forward = 0.66D * (this.lastDist - getBaseMoveSpeed());
                        this.moveSpeed = this.lastDist - forward;

                        if(this.moveSpeed < 0.3)
                            this.moveSpeed = 0.3;
                    } else {
                        if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0D, this.mc.thePlayer.motionY, 0.0D)).size() > 0 || this.mc.thePlayer.onGround) {
                            this.stage = PlayerUtils.isMoving() ? 1 : 0;
                        }

                        this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
                    }
                } else if(this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically){
                    emm.setY(this.mc.thePlayer.motionY = 0.415D);
                    this.moveSpeed *= 1.9;
                    mc.timer.timerSpeed = 1.08f;
                }

                this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
                TargetStrafe ts = (TargetStrafe) Oxygen.INSTANCE.moduleManager.getModule(TargetStrafe.class);
                if (ts.getEnabled()) {
                    ts.doStrafeAtSpeed(emm, this.moveSpeed);
                } else {
                    setMoveSpeed(emm, this.moveSpeed);
                }

                ++this.stage;
            }
        }
    }

    public void setMoveSpeed(final MoveEvent event, final double speed) {
        double forward = mc.thePlayer.moveForward;
        double strafe = mc.thePlayer.moveStrafing;
        float yaw = mc.thePlayer.rotationYaw;

        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f))
                    + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f))
                    - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    public double getBaseMoveSpeed() {
        double baseSpeed = 0.2872D;
        if (mc.thePlayer != null && mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0D + 0.2D * (double) (amplifier + 1);
        }

        return baseSpeed;
    }

}

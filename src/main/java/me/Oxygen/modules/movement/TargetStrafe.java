package me.Oxygen.modules.movement;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import me.Oxygen.Oxygen;
import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventMotion.MotionType;
import me.Oxygen.event.events.EventMove;
import me.Oxygen.event.events.EventRender3D;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.modules.combat.KillAura;
import me.Oxygen.utils.other.MathUtil;
import me.Oxygen.utils.other.MoveUtil;
import me.Oxygen.utils.other.PlayerUtil;
import me.Oxygen.utils.rotation.RotationUtils;
import me.Oxygen.value.Value;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

@ModuleRegister(name = "TargetStrafe", category = Category.MOVEMENT)
public final class TargetStrafe
extends Module {
    private final Value<Double> radius = new Value<Double>("TargetStrafe_Radius", 2.0, 0.1, 4.0, 0.1);
    private final Value<Boolean> render = new Value<Boolean>("TargetStrafe_Render", true);
    private final Value<Boolean> space = new Value<Boolean>("TargetStrafe_HoldSpace", false);
    private int direction = -1;

    @EventTarget(events = {EventMotion.class, EventRender3D.class})
    public final void onEvent(Event event) {
    	if(event instanceof EventMotion) {
    		EventMotion em = (EventMotion)event;
        if (em.getMotionType() == MotionType.PRE) {
            if (mc.thePlayer.isCollidedHorizontally) {
                this.switchDirection();
            }
            if (mc.gameSettings.keyBindLeft.isPressed()) {
                this.direction = 1;
            }
            if (mc.gameSettings.keyBindRight.isPressed()) {
                this.direction = -1;
            }
        }
    	}
    	if(event instanceof EventRender3D) {
    		EventRender3D er3 = (EventRender3D)event;
    		 if (this.canStrafe() && this.render.getValue().booleanValue()) {
    	            this.drawCircle(KillAura.target, er3.getPartialTicks(), this.radius.getValue().doubleValue());
    	        }
    	}
    }

    private void switchDirection() {
        this.direction = this.direction == 1 ? -1 : 1;
    }

    public void strafe(EventMove event, double moveSpeed) {
        EntityLivingBase target = KillAura.target;
        float[] rotations = getRotationsEntity(target);
        if ((double)mc.thePlayer.getDistanceToEntity(target) <= (Double)this.radius.getValue()) {
            MoveUtil.setSpeed(event, moveSpeed, rotations[0], this.direction, 0.0);
        } else {
        	MoveUtil.setSpeed(event, moveSpeed, rotations[0], this.direction, 1.0);
        }
    }

    private void drawCircle(Entity entity, float partialTicks, double rad) {
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        startSmooth();
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)3);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - mc.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - mc.getRenderManager().viewerPosZ;
        float r = 0.003921569f * (float)Color.WHITE.getRed();
        float g = 0.003921569f * (float)Color.WHITE.getGreen();
        float b = 0.003921569f * (float)Color.WHITE.getBlue();
        double pix2 = Math.PI * 2;
        for (int i = 0; i <= 90; ++i) {
            GL11.glColor3f((float)r, (float)g, (float)b);
            GL11.glVertex3d((double)(x + rad * Math.cos((double)i * (Math.PI * 2) / 45.0)), (double)y, (double)(z + rad * Math.sin((double)i * (Math.PI * 2) / 45.0)));
        }
        GL11.glEnd();
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        endSmooth();
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }
    
    private float[] getRotationsEntity(EntityLivingBase entity) {
        if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.contains("hypixel") && PlayerUtil.isMoving2()) {
            return RotationUtils.getRotations(entity.posX + MathUtil.randomNumber(0.03, -0.03), entity.posY + (double)entity.getEyeHeight() - 0.4 + MathUtil.randomNumber(0.07, -0.07), entity.posZ + MathUtil.randomNumber(0.03, -0.03));
        }
        return RotationUtils.getRotations(entity.posX, entity.posY + (double)entity.getEyeHeight() - 0.4, entity.posZ);
    }
    
    private final void startSmooth() {
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2881);
        GL11.glEnable((int)2832);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glHint((int)3153, (int)4354);
    }

    private final void endSmooth() {
        GL11.glDisable((int)2848);
        GL11.glDisable((int)2881);
        GL11.glEnable((int)2832);
    }

    public boolean canStrafe() {
        return Oxygen.INSTANCE.ModMgr.getModule(KillAura.class).isEnabled() && KillAura.target != null && this.isEnabled() && KillAura.target instanceof EntityPlayer && (this.space.getValue() == false || mc.gameSettings.keyBindJump.isKeyDown());
    }
}

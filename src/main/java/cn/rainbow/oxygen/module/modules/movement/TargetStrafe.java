package cn.rainbow.oxygen.module.modules.movement;

import java.awt.Color;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.MotionEvent;
import cn.rainbow.oxygen.event.events.MoveEvent;
import cn.rainbow.oxygen.event.events.Render3DEvent;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.ModuleInfo;
import cn.rainbow.oxygen.module.modules.combat.KillAura;
import cn.rainbow.oxygen.module.setting.BooleanValue;
import cn.rainbow.oxygen.module.setting.NumberValue;
import cn.rainbow.oxygen.utils.render.RenderUtil;
import org.lwjgl.opengl.GL11;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

@ModuleInfo(name = "TargetStrafe", category = Category.Movement)
public class TargetStrafe extends Module {
	private final NumberValue Radius = new NumberValue("Radius", 1.0D, 0.0D, 6.0D, 0.01D);
	private final BooleanValue ESP = new BooleanValue("ESP", true);
	private final BooleanValue WallCheck = new BooleanValue("WallCheck", true);
	private int direction = -1;

	private void switchDirection() {
		if (this.direction == 1) {
			this.direction = -1;
		} else {
			this.direction = 1;
		}
	}

	public final boolean doStrafeAtSpeed(MoveEvent event, double moveSpeed) {
		boolean strafe = this.canStrafe();
		if (strafe) {
			float[] rotations = getRotations(KillAura.getTarget());
			if ((double) mc.thePlayer.getDistanceToEntity(KillAura.getTarget()) <= Radius.getCurrentValue()) {
				setSpeed(event, moveSpeed, rotations[0], this.direction, 0.0D);
			} else {
				setSpeed(event, moveSpeed, rotations[0], this.direction, 1.0D);
			}
		}

		return strafe;
	}

	@EventTarget(events = {MotionEvent.class, Render3DEvent.class})
	private void onUpdate(Event event) {
		if (event instanceof MotionEvent) {
			MotionEvent em = (MotionEvent) event;
			if (em.getMotionType() == MotionEvent.MotionType.PRE) {
				if (mc.thePlayer.isCollidedHorizontally) {
					this.switchDirection();
				}
			}
		}
		if (event instanceof Render3DEvent) {
			Render3DEvent er3 = (Render3DEvent) event;
			if (ESP.getCurrentValue()) {
				for (Entity entity : mc.theWorld.getLoadedEntityList()) {
					if (this.Check(entity) && entity == KillAura.getTarget()) {
						this.drawCircle(entity, er3.getPartialTicks(), Radius.getCurrentValue());
					}
				}
			}
		}
	}

	public static void setSpeed(MoveEvent moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
		double forward = pseudoForward;
		double strafe = pseudoStrafe;
		float yaw = pseudoYaw;
		if (pseudoForward == 0.0D && pseudoStrafe == 0.0D) {
			moveEvent.setZ(0.0D);
			moveEvent.setX(0.0D);
		} else {
			if (pseudoForward != 0.0D) {
				if (pseudoStrafe > 0.0D) {
					yaw = pseudoYaw + (float) (pseudoForward > 0.0D ? -45 : 45);
				} else if (pseudoStrafe < 0.0D) {
					yaw = pseudoYaw + (float) (pseudoForward > 0.0D ? 45 : -45);
				}

				strafe = 0.0D;
				if (pseudoForward > 0.0D) {
					forward = 1.0D;
				} else if (pseudoForward < 0.0D) {
					forward = -1.0D;
				}
			}

			double cos = Math.cos(Math.toRadians((double) (yaw + 90.0F)));
			double sin = Math.sin(Math.toRadians((double) (yaw + 90.0F)));
			moveEvent.setX(forward * moveSpeed * cos + strafe * moveSpeed * sin);
			moveEvent.setZ(forward * moveSpeed * sin - strafe * moveSpeed * cos);
		}

	}

	public float[] getRotations(final Entity entity) {
		return getRotationFromPosition(entity.posX, entity.posZ, entity.posY + entity.getEyeHeight() / 2.0f);
	}

	public float[] getRotationFromPosition(final double n, final double n2, final double n3) {
		final double n4 = n - mc.thePlayer.posX;
		final double n5 = n2 - mc.thePlayer.posZ;
		return new float[]{(float) (Math.atan2(n5, n4) * 180.0 / 3.141592653589793) - 90.0f, (float) (-Math.atan2(n3 - mc.thePlayer.posY - 1.2, MathHelper.sqrt_double(n4 * n4 + n5 * n5)) * 180.0 / 3.141592653589793)};
	}

	private boolean Check(Entity e2) {
		if (!e2.isEntityAlive()) {
			return false;
		} else {
			return e2 != mc.thePlayer && e2 instanceof EntityPlayer;
		}
	}

	private void drawCircle(Entity entity, float partialTicks, double rad) {
		GL11.glPushMatrix();
		GL11.glDisable(3553);
		RenderUtil.startDrawing();
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		GL11.glLineWidth(1.0F);
		GL11.glBegin(3);
		double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) partialTicks - mc.getRenderManager().viewerPosX;
		double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) partialTicks - mc.getRenderManager().viewerPosY;
		Color color = Color.WHITE;
		double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) partialTicks - mc.getRenderManager().viewerPosZ;
		if (entity == KillAura.getTarget() && Oxygen.INSTANCE.moduleManager.getModule("Speed").getEnabled()) {
			color = Color.GREEN;
		}

		float r = 0.003921569F * (float) color.getRed();
		float g = 0.003921569F * (float) color.getGreen();
		float b = 0.003921569F * (float) color.getBlue();
		for (int i = 0; i <= 90; ++i) {
			GL11.glColor3f(r, g, b);
			GL11.glVertex3d(x + rad * Math.cos((double) i * 6.283185307179586D / 45.0D), y, z + rad * Math.sin((double) i * 6.283185307179586D / 45.0D));
		}

		GL11.glEnd();
		GL11.glDepthMask(true);
		GL11.glEnable(2929);
		RenderUtil.stopDrawing();
		GL11.glEnable(3553);
		GL11.glPopMatrix();
	}

	public boolean canStrafe() {
		return Oxygen.INSTANCE.moduleManager.getModule("KillAura").getEnabled() && KillAura.getTarget() != null && this.getEnabled();
	}

}


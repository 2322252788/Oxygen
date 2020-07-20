package me.Oxygen.modules.movement;

import java.util.ArrayList;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventMove;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.injection.interfaces.*;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.modules.player.Collective;
import me.Oxygen.ui.ClientNotification.Type;
import me.Oxygen.utils.ClientUtil;
import me.Oxygen.utils.other.MathUtil;
import me.Oxygen.utils.other.PlayerUtil;
import me.Oxygen.utils.timer.TimeHelper;
import me.Oxygen.value.Value;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

@ModuleRegister(name = "Fly", category = Category.MOVEMENT)
public class Fly extends Module {
	public ArrayList<String> options = new ArrayList<String>();
	private final Value<String> mode = new Value<String>("Fly","Mode",0);
	private final Value<Double> speed = new Value<Double>("Fly_Speed",0.5,0.5,5.0,0.5);
	int counter;
	double moveSpeed, lastDist;
	int level = 1;
	boolean b2;
	double luls;
	boolean decreasing;
	
	private final TickTimer cubecraftTeleportTickTimer = new TickTimer();

	public Fly() {
		mode.addValue("Motion");
		mode.addValue("Hypixel");
		mode.addValue("CubeCraft");
		mode.addValue("Boat");
	}

	@EventTarget(events = {EventMotion.class, EventUpdate.class, EventMove.class, EventPacket.class})
	private void onUpdate(Event e) {
		if(e instanceof EventMotion) {
			EventMotion em = (EventMotion)e;
		
		double speed = Math.max(this.speed.getValueState().doubleValue(), getBaseMoveSpeed());
		switch (this.mode.getModeAt(this.mode.getCurrentMode())) {
		case "Hypixel":
			TimeHelper timer = new TimeHelper();
			if (timer.delay(1000F)) {
				mc.thePlayer.onGround = true;
			}
			luls += decreasing ? -0.1 : 0.1;
			if (luls >= 3) {
				decreasing = true;
				// b2 = false;
				// level = -1;
			} else if (luls <= 0.1) {
				decreasing = false;
			}

			em.setGround(true);
			mc.thePlayer.cameraYaw = b2 ? 0.001F : 0.105F;
			if (!((IEntityPlayerSP) mc.thePlayer).moving()) {
				mc.thePlayer.motionX = 0.0D;
				mc.thePlayer.motionZ = 0.0D;
			}
			if (!b2) {
				if (((IEntityPlayerSP) mc.thePlayer).moving()) {
					PlayerUtil.setSpeed(PlayerUtil.getSpeed());
				}
			}
			if ((mc.thePlayer.ticksExisted % 2) == 0) {
				mc.thePlayer.setPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + MathUtil.getRandomInRange(1.0E-9D, 1.29E-13D),
						mc.thePlayer.posZ);
			}
			mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown()
					&& !mc.gameSettings.keyBindSneak.isKeyDown() ? 0.2
							: mc.gameSettings.keyBindSneak.isKeyDown() ? -0.2 : 0;
			break;
		case "CubeCraft" :
			mc.thePlayer.motionY = 0;
			mc.thePlayer.onGround= true;
			break;
		case "Motion":
			mc.thePlayer.motionY = 0;
			mc.thePlayer.onGround= true;
			if (PlayerUtil.MovementInput()) {
				PlayerUtil.setSpeed(speed);
			}
			if (mc.thePlayer.movementInput.jump) {
				mc.thePlayer.motionY = 0.42 * speed;
			} else if (mc.thePlayer.movementInput.sneak) {
				mc.thePlayer.motionY = -0.42 * speed;
			}
		}
	}
		if(e instanceof EventUpdate) {
			double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
			
			this.setDisplayName(this.mode.getModeAt(this.mode.getCurrentMode()));
			if (this.mode.isCurrentMode("Boat")) {
				if(mc.thePlayer != null) {
					if(mc.gameSettings.keyBindJump.isKeyDown()) {
						mc.thePlayer.motionY = 0.1;
					}
				}
			}
		if(this.mode.isCurrentMode("CubeCraft")) {
			((IMinecraft)mc).getTimer().timerSpeed = 0.6F;

            cubecraftTeleportTickTimer.update();
		}
		}
		if(e instanceof EventMove) {
			EventMove em = (EventMove)e;
			if (this.mode.isCurrentMode("Hypixel")) {
				double speed = Math.max(this.speed.getValueState().doubleValue(), getBaseMoveSpeed());
				float forward = mc.thePlayer.movementInput.moveForward;
				float strafe = mc.thePlayer.movementInput.moveStrafe;
				float yaw = mc.thePlayer.rotationYaw;
				double mx = Math.cos(Math.toRadians(yaw + 90.0F));
				double mz = Math.sin(Math.toRadians(yaw + 90.0F));
				if ((forward == 0.0F) && (strafe == 0.0F)) {
					em.setX(0.0D);
					em.setZ(0.0D);
				} else if (forward != 0.0F) {
					if (strafe >= 1.0F) {
						yaw += forward > 0.0F ? -35 : 35;
						strafe = 0.0F;
					} else if (strafe <= -1.0F) {
						yaw += forward > 0.0F ? 35 : -35;
						strafe = 0.0F;
					}

					if (forward > 0.0F) {
						forward = 1.0F;
					} else if (forward < 0.0F) {
						forward = -1.0F;
					}
				}
				if (b2) {
					if ((level != 1) || ((mc.thePlayer.moveForward == 0.0F)
							&& (mc.thePlayer.moveStrafing == 0.0F))) {
						if (level == 2) {
							level = 3;
							moveSpeed *= 2.1499999D;
						} else if (level == 3) {
							level = 4;
							double difference = ((mc.thePlayer.ticksExisted % 2) == 0 ? 0.0103D : 0.0123D)
									* (lastDist - this.getBaseMoveSpeed());
							moveSpeed = lastDist - difference;
						} else {
							if ((mc.theWorld
									.getCollidingBoundingBoxes(mc.thePlayer,
											((IEntity)mc.thePlayer).getBoundingBox().offset(0.0D,
													mc.thePlayer.motionY, 0.0D))
									.size() > 0) || mc.thePlayer.isCollidedVertically) {
								level = 1;
							}
							moveSpeed = lastDist - (lastDist / 159.0D);
						}
					} else {
						level = 2;
						double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.6
								: 2.099999;
						moveSpeed = boost * this.getBaseMoveSpeed();
					}
					moveSpeed = Math.max(moveSpeed, this.getBaseMoveSpeed());
					em.setX((forward * moveSpeed * mx) + (strafe * moveSpeed * mz));
					em.setZ((forward * moveSpeed * mz) - (strafe * moveSpeed * mx));
					if ((forward == 0.0F) && (strafe == 0.0F)) {
						em.setX(0.0D);
						em.setZ(0.0D);
					}
				}
			}
			if(this.mode.isCurrentMode("CubeCraft")) {
				final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);

                if (cubecraftTeleportTickTimer.hasTimePassed(2)) {
                    em.setX(-Math.sin(yaw) * 2.4D);
                    em.setZ(Math.cos(yaw) * 2.4D);

                    cubecraftTeleportTickTimer.reset();
                } else {
                    em.setX(-Math.sin(yaw) * 0.2D);
                    em.setZ(Math.cos(yaw) * 0.2D);
                }
			}
		}
		if(e instanceof EventPacket) {
			EventPacket ep = (EventPacket)e;
			if (ep.getPacket() instanceof S08PacketPlayerPosLook) {
				if(Collective.lagcheck) {
					this.set(false);
					mc.thePlayer.onGround = false;
					mc.thePlayer.motionX *= 0;
					mc.thePlayer.motionZ *= 0;
					mc.thePlayer.jumpMovementFactor = 0;
					ClientUtil.sendClientMessage("[LagBackCheck] AutoDisable!", Type.WARNING);
				}
			}
		}
	}

	


	private double getBaseMoveSpeed() {
		double baseSpeed = 0.2903D;
		if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
			baseSpeed *= 1.0D + (0.2D * (amplifier + 1));
		}

		return baseSpeed;
	}

	public void damagePlayer(int damage) {
		if (damage < 1)
			damage = 1;
		if (damage > MathHelper.floor_double(mc.thePlayer.getMaxHealth()))
			damage = MathHelper.floor_double(mc.thePlayer.getMaxHealth());

		double offset = 0.0625;
		if (mc.thePlayer != null && mc.getNetHandler() != null && mc.thePlayer.onGround) {
			for (int i = 0; i <= ((3 + damage) / offset); i++) {
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
				mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY, mc.thePlayer.posZ, (i == ((3 + damage) / offset))));
			}
		}
	}

	public void onEnable() {
		((IMinecraft)mc).getTimer().timerSpeed = 1.0f;
		if (this.mode.isCurrentMode("Hypixel")) {
			double x2 = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
			double z2 = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
			double lul = 1;
			double xOffset = (mc.thePlayer.movementInput.moveForward * lul * x2) + (mc.thePlayer.movementInput.moveStrafe * lul * z2);
			double zOffset = (mc.thePlayer.movementInput.moveForward * lul * z2) + (mc.thePlayer.movementInput.moveStrafe * lul * x2);
			double x = mc.thePlayer.posX + xOffset;
			double y = mc.thePlayer.posY + 0.42;
			double z = mc.thePlayer.posZ + zOffset;
			PlayerUtil.damage();
			level = 1;
			moveSpeed = 0.1D;
			b2 = true;
			lastDist = 0.0D;
			if (mc.thePlayer.onGround) {
				mc.thePlayer.motionY = 0.4189999999f;
			}
		}
		super.onEnable();
	}

	public void onDisable() {
		((IMinecraft)mc).getTimer().timerSpeed = 1.0f;
		level = 1;
		moveSpeed = 0.1D;
		b2 = false;
		lastDist = 0.0D;
		super.onDisable();
	}
	
	private class TickTimer {
		private int tick;

	    public void update() {
	        tick++;
	    }

	    public void reset() {
	        tick = 0;
	    }

	    public boolean hasTimePassed(final int ticks) {
	        return tick >= ticks;
	    }
	}
}


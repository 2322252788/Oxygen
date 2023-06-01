package cn.rainbow.oxygen.injection.mixins;

import java.util.List;

import cn.rainbow.oxygen.Oxygen;
import cn.rainbow.oxygen.command.sender.CommandSender;
import cn.rainbow.oxygen.event.events.*;
import cn.rainbow.oxygen.injection.interfaces.IEntity;
import cn.rainbow.oxygen.module.modules.client.NoCommand;
import cn.rainbow.oxygen.module.modules.movement.NoSlow;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

	public MixinEntityPlayerSP() {
		super(null, null);
	}

	@Shadow
	@Final
	public NetHandlerPlayClient sendQueue;

	@Shadow
	public int sprintingTicksLeft;

	@Shadow
	protected int sprintToggleTimer;

	@Shadow
	public float timeInPortal;

	@Shadow
	public float prevTimeInPortal;

	@Shadow
	protected Minecraft mc;

	@Shadow
	public MovementInput movementInput;

	@Shadow
	public float horseJumpPower;

	@Shadow
	public int horseJumpPowerCounter;

	@Shadow
	protected abstract void sendHorseJump();

	@Shadow
	public abstract boolean isRidingHorse();

	@Shadow
	private double lastReportedPosX;

	@Shadow
	private double lastReportedPosY;

	@Shadow
	private double lastReportedPosZ;

	@Shadow
	private boolean serverSneakState;

	@Shadow
	private boolean serverSprintState;

	@Shadow
	private float lastReportedYaw;

	@Shadow
	private float lastReportedPitch;

	@Shadow
	private int positionUpdateTicks;

	@Inject(method = "onUpdate", at = @At("HEAD"))
	public void onUpdate(CallbackInfo callbackInfo) {
		new UpdateEvent().call();
	}

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public void onLivingUpdate() {

		if (this.sprintingTicksLeft > 0) {
			--this.sprintingTicksLeft;

			if (this.sprintingTicksLeft == 0) {
				this.setSprinting(false);
			}
		}

		if (this.sprintToggleTimer > 0) {
			--this.sprintToggleTimer;
		}

		this.prevTimeInPortal = this.timeInPortal;

		if (this.inPortal) {

			if (this.timeInPortal == 0.0F) {
				this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("portal.trigger"), this.rand.nextFloat() * 0.4F + 0.8F));
			}

			this.timeInPortal += 0.0125F;

			if (this.timeInPortal >= 1.0F) {
				this.timeInPortal = 1.0F;
			}

			this.inPortal = false;
		} else if (this.isPotionActive(Potion.confusion) && this.getActivePotionEffect(Potion.confusion).getDuration() > 60) {
			this.timeInPortal += 0.006666667F;

			if (this.timeInPortal > 1.0F) {
				this.timeInPortal = 1.0F;
			}
		} else {
			if (this.timeInPortal > 0.0F) {
				this.timeInPortal -= 0.05F;
			}

			if (this.timeInPortal < 0.0F) {
				this.timeInPortal = 0.0F;
			}
		}

		if (this.timeUntilPortal > 0) {
			--this.timeUntilPortal;
		}

		boolean flag = this.movementInput.jump;
		boolean flag1 = this.movementInput.sneak;
		float f = 0.8F;
		boolean flag2 = this.movementInput.moveForward >= f;
		this.movementInput.updatePlayerMoveState();

		if (this.isUsingItem() && !this.isRiding() && !Oxygen.INSTANCE.moduleManager.getModule(NoSlow.class).getEnabled()) {
			this.movementInput.moveStrafe *= 0.2F;
			this.movementInput.moveForward *= 0.2F;
			this.sprintToggleTimer = 0;
		}

		/*final NoSlow noSlow = (NoSlow) LiquidBounce.moduleManager.getModule(NoSlow.class);
		final KillAura killAura = (KillAura) LiquidBounce.moduleManager.getModule(KillAura.class);

		if (getHeldItem() != null && (this.isUsingItem() || (getHeldItem().getItem() instanceof ItemSword && killAura.getBlockingStatus())) && !this.isRiding()) {
			final SlowDownEvent slowDownEvent = new SlowDownEvent(0.2F, 0.2F);
			LiquidBounce.eventManager.callEvent(slowDownEvent);
			this.movementInput.moveStrafe *= slowDownEvent.getStrafe();
			this.movementInput.moveForward *= slowDownEvent.getForward();
			this.sprintToggleTimer = 0;
		}*/

		this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double) this.width * 0.35D);
		this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double) this.width * 0.35D);
		this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ - (double) this.width * 0.35D);
		this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D, this.posZ + (double) this.width * 0.35D);

		//final Sprint sprint = (Sprint) LiquidBounce.moduleManager.getModule(Sprint.class);

		boolean flag3 = /*!sprint.foodValue.get() ||*/ (float) this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;

		if (this.onGround && !flag1 && !flag2 && this.movementInput.moveForward >= f && !this.isSprinting() && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness)) {
			if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
				this.sprintToggleTimer = 7;
			} else {
				this.setSprinting(true);
			}
		}

		if (!this.isSprinting() && this.movementInput.moveForward >= f && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
			this.setSprinting(true);
		}

		if (!this.isSprinting() && this.movementInput.moveForward >= f && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
			this.setSprinting(true);
		}

		/*final Scaffold scaffold = (Scaffold) Oxygen.INSTANCE.ModMgr.getModule(Scaffold.class);
		if ((scaffold.isEnabled() && !scaffold.sprintValue.getCurrentValue()) || ((RotationUtils.targetRotation != null && RotationUtils.getRotationDifference(new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)) > 30)))
			this.setSprinting(false);*/

		if (this.isSprinting() && (this.movementInput.moveForward < f || this.isCollidedHorizontally || !flag3)) {
			this.setSprinting(false);
		}

		if (this.capabilities.allowFlying) {
			if (this.mc.playerController.isSpectatorMode()) {
				if (!this.capabilities.isFlying) {
					this.capabilities.isFlying = true;
					this.sendPlayerAbilities();
				}
			} else if (!flag && this.movementInput.jump) {
				if (this.flyToggleTimer == 0) {
					this.flyToggleTimer = 7;
				} else {
					this.capabilities.isFlying = !this.capabilities.isFlying;
					this.sendPlayerAbilities();
					this.flyToggleTimer = 0;
				}
			}
		}

		if (this.capabilities.isFlying && this.isCurrentViewEntity()) {
			if (this.movementInput.sneak) {
				this.motionY -= this.capabilities.getFlySpeed() * 3.0F;
			}

			if (this.movementInput.jump) {
				this.motionY += this.capabilities.getFlySpeed() * 3.0F;
			}
		}

		if (this.isRidingHorse()) {
			if (this.horseJumpPowerCounter < 0) {
				++this.horseJumpPowerCounter;

				if (this.horseJumpPowerCounter == 0) {
					this.horseJumpPower = 0.0F;
				}
			}

			if (flag && !this.movementInput.jump) {
				this.horseJumpPowerCounter = -10;
				this.sendHorseJump();
			} else if (!flag && this.movementInput.jump) {
				this.horseJumpPowerCounter = 0;
				this.horseJumpPower = 0.0F;
			} else if (flag) {
				++this.horseJumpPowerCounter;

				if (this.horseJumpPowerCounter < 10) {
					this.horseJumpPower = (float) this.horseJumpPowerCounter * 0.1F;
				} else {
					this.horseJumpPower = 0.8F + 2.0F / (float) (this.horseJumpPowerCounter - 9) * 0.1F;
				}
			}
		} else {
			this.horseJumpPower = 0.0F;
		}

		super.onLivingUpdate();

		if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
			this.capabilities.isFlying = false;
			this.sendPlayerAbilities();
		}
	}

	/**
	 * @author Rainbow
	 * @reason null
	 */
	@Overwrite
	public void onUpdateWalkingPlayer() {

		MotionEvent pre = new MotionEvent(MotionEvent.MotionType.PRE, this.rotationYaw, this.rotationPitch, this.onGround,
				this.posX, this.posY, this.posZ);
		pre.call();

		boolean flag = this.isSprinting();

		if (flag != this.serverSprintState) {
			if (flag) {
				this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
			} else {
				this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
			}

			this.serverSprintState = flag;
		}

		boolean flag1 = this.isSneaking();

		if (flag1 != this.serverSneakState) {
			if (flag1) {
				this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
			} else {
				this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
			}

			this.serverSneakState = flag1;
		}

		if (this.isCurrentViewEntity()) {
			double d0 = pre.getX() - this.lastReportedPosX;
			double d1 = pre.getY() - this.lastReportedPosY;
			double d2 = pre.getZ() - this.lastReportedPosZ;
			double d3 = pre.getYaw() - this.lastReportedYaw;
			double d4 = pre.getPitch() - this.lastReportedPitch;
			boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || this.positionUpdateTicks >= 20;
			boolean flag3 = d3 != 0.0D || d4 != 0.0D;

			if (this.ridingEntity == null) {
				if (flag2 && flag3) {
					this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(pre.getX(), pre.getY(),
							pre.getZ(), pre.getYaw(), pre.getPitch(), pre.isGround()));
				} else if (flag2) {
					this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(pre.getX(), pre.getY(),
							pre.getZ(), pre.isGround()));
				} else if (flag3) {
					this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(pre.getYaw(), pre.getPitch(), pre.isGround()));
				} else {
					this.sendQueue.addToSendQueue(new C03PacketPlayer(pre.isGround()));
				}
			} else {
				this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D,
						this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
				flag2 = false;
			}

			++this.positionUpdateTicks;

			if (flag2) {
				this.lastReportedPosX = this.posX;
				this.lastReportedPosY = this.getEntityBoundingBox().minY;
				this.lastReportedPosZ = this.posZ;
				this.positionUpdateTicks = 0;
			}

			if (flag3) {
				this.lastReportedYaw = pre.getYaw();
				this.lastReportedPitch = pre.getPitch();
			}
		}

		MotionEvent post = new MotionEvent(MotionEvent.MotionType.POST, this.rotationYaw, this.rotationPitch, this.onGround,
				this.posX, this.posY, this.posZ);
		post.call();
	}


	/**
	 * @author Rainbow
	 * @reason null
	 */
	@Overwrite
	public void sendChatMessage(String message) {
		if (message.startsWith(".") && !Oxygen.INSTANCE.moduleManager.getModule(NoCommand.class).getEnabled()) {
			CommandSender.INSTANCE.forMinecraft(message.substring(1));
			return;
		}
		this.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
	}

	@Shadow
	public boolean isCurrentViewEntity() {
		return false;
	}

	@Override
	public void moveEntity(double x, double y, double z) {
		final MoveEvent event = new MoveEvent(x, y, z);
		event.call();
		x = event.getX();
		y = event.getY();
		z = event.getZ();

		if (this.noClip) {
			this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
			this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
	        this.posY = this.getEntityBoundingBox().minY;
	        this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;
		} else {
			this.worldObj.theProfiler.startSection("move");
			double d0 = this.posX;
			double d1 = this.posY;
			double d2 = this.posZ;

			if (this.isInWeb) {
				this.isInWeb = false;
				x *= 0.25D;
				y *= 0.05000000074505806D;
				z *= 0.25D;
				this.motionX = 0.0D;
				this.motionY = 0.0D;
				this.motionZ = 0.0D;
			}
			double d3 = x;
			double d4 = y;
			double d5 = z;
			boolean flag = this.onGround && (this.isSneaking() && !Oxygen.INSTANCE.moduleManager.getModule("Scaffold").getEnabled()) && this instanceof EntityPlayer;
			if (flag || event.isSafeWalk()) {
				double d6;

				for (d6 = 0.05D; x != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0D, 0.0D)).isEmpty(); d3 = x) {
					if (x < d6 && x >= -d6) {
						x = 0.0D;
					} else if (x > 0.0D) {
						x -= d6;
					} else {
						x += d6;
					}
				}

				for (; z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(0.0D, -1.0D, z)).isEmpty(); d5 = z) {
					if (z < d6 && z >= -d6) {
						z = 0.0D;
					} else if (z > 0.0D) {
						z -= d6;
					} else {
						z += d6;
					}
				}

				for (; x != 0.0D && z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0D, z)).isEmpty(); d5 = z) {
					if (x < d6 && x >= -d6) {
						x = 0.0D;
					} else if (x > 0.0D) {
						x -= d6;
					} else {
						x += d6;
					}

					d3 = x;

					if (z < d6 && z >= -d6) {
						z = 0.0D;
					} else if (z > 0.0D) {
						z -= d6;
					} else {
						z += d6;
					}
				}
			}

			List<AxisAlignedBB> list1 = this.worldObj.getCollidingBoundingBoxes(this,
					this.getEntityBoundingBox().addCoord(x, y, z));
			AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();

			for (AxisAlignedBB axisalignedbb1 : list1) {
				y = axisalignedbb1.calculateYOffset(this.getEntityBoundingBox(), y);
			}

			this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));
			boolean flag1 = this.onGround || d4 != y && d4 < 0.0D;

			for (AxisAlignedBB axisalignedbb2 : list1) {
				x = axisalignedbb2.calculateXOffset(this.getEntityBoundingBox(), x);
			}

			this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0D, 0.0D));

			for (AxisAlignedBB axisalignedbb13 : list1) {
				z = axisalignedbb13.calculateZOffset(this.getEntityBoundingBox(), z);
			}

			this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, 0.0D, z));

			StepEvent eventStep = new StepEvent(true, this.stepHeight);
			eventStep.call();
			if (this.stepHeight > 0.0F && flag1 && (d3 != x || d5 != z)) {
				
				double d11 = x;
				double d7 = y;
				double d8 = z;
				AxisAlignedBB axisalignedbb3 = this.getEntityBoundingBox();
				this.setEntityBoundingBox(axisalignedbb);
				y = eventStep.getStepHeight();
				List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this,
						this.getEntityBoundingBox().addCoord(d3, y, d5));
				AxisAlignedBB axisalignedbb4 = this.getEntityBoundingBox();
				AxisAlignedBB axisalignedbb5 = axisalignedbb4.addCoord(d3, 0.0D, d5);
				double d9 = y;

				for (AxisAlignedBB axisalignedbb6 : list) {
					d9 = axisalignedbb6.calculateYOffset(axisalignedbb5, d9);
				}

				axisalignedbb4 = axisalignedbb4.offset(0.0D, d9, 0.0D);
				double d15 = d3;

				for (AxisAlignedBB axisalignedbb7 : list) {
					d15 = axisalignedbb7.calculateXOffset(axisalignedbb4, d15);
				}

				axisalignedbb4 = axisalignedbb4.offset(d15, 0.0D, 0.0D);
				double d16 = d5;

				for (AxisAlignedBB axisalignedbb8 : list) {
					d16 = axisalignedbb8.calculateZOffset(axisalignedbb4, d16);
				}

				axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d16);
				AxisAlignedBB axisalignedbb14 = this.getEntityBoundingBox();
				double d17 = y;

				for (AxisAlignedBB axisalignedbb9 : list) {
					d17 = axisalignedbb9.calculateYOffset(axisalignedbb14, d17);
				}

				axisalignedbb14 = axisalignedbb14.offset(0.0D, d17, 0.0D);
				double d18 = d3;

				for (AxisAlignedBB axisalignedbb10 : list) {
					d18 = axisalignedbb10.calculateXOffset(axisalignedbb14, d18);
				}

				axisalignedbb14 = axisalignedbb14.offset(d18, 0.0D, 0.0D);
				double d19 = d5;

				for (AxisAlignedBB axisalignedbb11 : list) {
					d19 = axisalignedbb11.calculateZOffset(axisalignedbb14, d19);
				}

				axisalignedbb14 = axisalignedbb14.offset(0.0D, 0.0D, d19);
				double d20 = d15 * d15 + d16 * d16;
				double d10 = d18 * d18 + d19 * d19;

				if (d20 > d10) {
					x = d15;
					z = d16;
					y = -d9;
					this.setEntityBoundingBox(axisalignedbb4);
				} else {
					x = d18;
					z = d19;
					y = -d17;
					this.setEntityBoundingBox(axisalignedbb14);
				}

				for (AxisAlignedBB axisalignedbb12 : list) {
					y = axisalignedbb12.calculateYOffset(this.getEntityBoundingBox(), y);
				}

				this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));

				if (d11 * d11 + d8 * d8 >= x * x + z * z) {
					x = d11;
					y = d7;
					z = d8;
					this.setEntityBoundingBox(axisalignedbb3);
				}
				
				StepEvent eventStep2 = new StepEvent(false, 1D + y);
				eventStep2.call();
			}

			this.worldObj.theProfiler.endSection();
			this.worldObj.theProfiler.startSection("rest");
			this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
	        this.posY = this.getEntityBoundingBox().minY;
	        this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;
			this.isCollidedHorizontally = d3 != x || d5 != z;
			this.isCollidedVertically = d4 != y;
			this.onGround = this.isCollidedVertically && d4 < 0.0D;
			this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
			int i = MathHelper.floor_double(this.posX);
			int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
			int k = MathHelper.floor_double(this.posZ);
			BlockPos blockpos = new BlockPos(i, j, k);
			Block block1 = this.worldObj.getBlockState(blockpos).getBlock();

			if (block1.getMaterial() == Material.air) {
				Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();

				if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate) {
					block1 = block;
					blockpos = blockpos.down();
				}
			}

			this.updateFallState(y, this.onGround, block1, blockpos);

			if (d3 != x) {
				this.motionX = 0.0D;
			}

			if (d5 != z) {
				this.motionZ = 0.0D;
			}

			if (d4 != y) {
				block1.onLanded(this.worldObj, this);
			}

			if (this.canTriggerWalking() && !flag && this.ridingEntity == null) {
				double d12 = this.posX - d0;
				double d13 = this.posY - d1;
				double d14 = this.posZ - d2;

				if (block1 != Blocks.ladder) {
					d13 = 0.0D;
				}

				if (block1 != null && this.onGround) {
					block1.onEntityCollidedWithBlock(this.worldObj, blockpos, this);
				}

				this.distanceWalkedModified = (float) ((double) this.distanceWalkedModified
						+ (double) MathHelper.sqrt_double(d12 * d12 + d14 * d14) * 0.6D);
				this.distanceWalkedOnStepModified = (float) ((double) this.distanceWalkedOnStepModified
						+ (double) MathHelper.sqrt_double(d12 * d12 + d13 * d13 + d14 * d14) * 0.6D);

				if (this.distanceWalkedOnStepModified > (float) ((IEntity) this).getNextStepDistance()
						&& block1.getMaterial() != Material.air) {
					((IEntity) this).setNextStepDistance((int) this.distanceWalkedOnStepModified + 1);

					if (this.isInWater()) {
						float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D
								+ this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D)
								* 0.35F;

						if (f > 1.0F) {
							f = 1.0F;
						}

						this.playSound(this.getSwimSound(), f,
								1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
					}

					this.playStepSound(blockpos, block1);
				}
			}

			try {
				this.doBlockCollisions();
			} catch (Throwable throwable) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
				CrashReportCategory crashreportcategory = crashreport
						.makeCategory("Entity being checked for collision");
				this.addEntityCrashInfo(crashreportcategory);
				throw new ReportedException(crashreport);
			}

			boolean flag2 = this.isWet();

			if (this.worldObj.isFlammableWithin(this.getEntityBoundingBox().contract(0.001D, 0.001D, 0.001D))) {
				this.dealFireDamage(1);

				if (!flag2) {
					((IEntity) this).setFire(((IEntity) this).getFire() + 1);

					if (((IEntity) this).getFire() == 0) {
						this.setFire(8);
					}
				}
			} else if (((IEntity) this).getFire() <= 0) {
				((IEntity) this).setFire(-this.fireResistance);				
			}

			if (flag2 && ((IEntity) this).getFire() > 0) {
				this.playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				((IEntity) this).setFire(-this.fireResistance);
			}

			this.worldObj.theProfiler.endSection();
		}
	}

	public boolean moving() {
        return Minecraft.getMinecraft().thePlayer.movementInput.moveForward > 0.0 | Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe > 0.0;
    }

	public void setMoveSpeed(final MoveEvent event, final double speed) {
        double forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward;
        double strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe;
        float yaw = this.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
}

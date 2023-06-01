package cn.rainbow.oxygen.module.modules.render;

import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.Render3DEvent;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.ModuleInfo;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name = "Projectiles", category = Category.Render)
public class Projectiles extends Module {

	private EntityLivingBase entity;
	private MovingObjectPosition blockCollision;
	private MovingObjectPosition entityCollision;
	private static AxisAlignedBB aim;

	@EventTarget(events = Render3DEvent.class)
	public void onRender(Event event) {
		if(event instanceof Render3DEvent) {
			if (this.mc.thePlayer.inventory.getCurrentItem() != null) {
				EntityPlayerSP player = this.mc.thePlayer;
				ItemStack stack = player.inventory.getCurrentItem();
				int item = Item.getIdFromItem(this.mc.thePlayer.getHeldItem().getItem());
				if (item == 261 || item == 368 || item == 332 || item == 344) {
					double posX = player.lastTickPosX
							+ (player.posX - player.lastTickPosX) * (double) this.mc.timer.renderPartialTicks
							- Math.cos(Math.toRadians((double) player.rotationYaw)) * 0.1599999964237213D;
					double posY = player.lastTickPosY
							+ (player.posY - player.lastTickPosY) * (double) this.mc.timer.renderPartialTicks
							+ (double) player.getEyeHeight() - 0.1D;
					double posZ = player.lastTickPosZ
							+ (player.posZ - player.lastTickPosZ) * (double) this.mc.timer.renderPartialTicks
							- Math.sin(Math.toRadians((double) player.rotationYaw)) * 0.1599999964237213D;
					double itemBow = (double) (stack.getItem() instanceof ItemBow ? 1.0F : 0.4F);
					double yaw = Math.toRadians((double) player.rotationYaw);
					double pitch = Math.toRadians((double) player.rotationPitch);
					double trajectoryX = -Math.sin(yaw) * Math.cos(pitch) * itemBow;
					double trajectoryY = -Math.sin(pitch) * itemBow;
					double trajectoryZ = Math.cos(yaw) * Math.cos(pitch) * itemBow;
					double trajectory = Math
							.sqrt(trajectoryX * trajectoryX + trajectoryY * trajectoryY + trajectoryZ * trajectoryZ);
					trajectoryX /= trajectory;
					trajectoryY /= trajectory;
					trajectoryZ /= trajectory;
					if (stack.getItem() instanceof ItemBow) {
						float bowPower = (float) (72000 - player.getItemInUseCount()) / 20.0F;
						bowPower = (bowPower * bowPower + bowPower * 2.0F) / 3.0F;
						if (bowPower > 1.0F) {
							bowPower = 1.0F;
						}

						bowPower *= 3.0F;
						trajectoryX *= bowPower;
						trajectoryY *= bowPower;
						trajectoryZ *= bowPower;
					} else {
						trajectoryX *= 1.5D;
						trajectoryY *= 1.5D;
						trajectoryZ *= 1.5D;
					}

					GL11.glPushMatrix();
					GL11.glDisable(3553);
					GL11.glEnable(3042);
					GL11.glBlendFunc(770, 771);
					GL11.glDisable(2929);
					GL11.glDepthMask(false);
					GL11.glEnable(2848);
					GL11.glLineWidth(2.0F);
					double gravity = stack.getItem() instanceof ItemBow ? 0.05D : 0.03D;
					GL11.glColor4f(0.0F, 1.0F, 0.2F, 0.5F);
					GL11.glBegin(3);

					for (int i = 0; i < 2000; ++i) {
						this.mc.getRenderManager();
						double var10000 = posX - this.mc.getRenderManager().renderPosX;
						this.mc.getRenderManager();
						double var10001 = posY - this.mc.getRenderManager().renderPosY;
						this.mc.getRenderManager();
						GL11.glVertex3d(var10000, var10001, posZ - this.mc.getRenderManager().renderPosZ);
						posX += trajectoryX * 0.1D;
						posY += trajectoryY * 0.1D;
						posZ += trajectoryZ * 0.1D;
						trajectoryX *= 0.999D;
						trajectoryY *= 0.999D;
						trajectoryZ *= 0.999D;
						trajectoryY -= gravity * 0.1D;
						Vec3 vec = new Vec3(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ);
						this.blockCollision = this.mc.theWorld.rayTraceBlocks(vec, new Vec3(posX, posY, posZ));

						for (Entity o : this.mc.theWorld.getLoadedEntityList()) {
							if (o instanceof EntityLivingBase && !(o instanceof EntityPlayerSP)) {
								this.entity = (EntityLivingBase) o;
								AxisAlignedBB entityBoundingBox = this.entity.getEntityBoundingBox().expand(0.3D, 0.3D,
										0.3D);
								this.entityCollision = entityBoundingBox.calculateIntercept(vec,
										new Vec3(posX, posY, posZ));
								if (this.entityCollision != null) {
									this.blockCollision = this.entityCollision;
								}

								if (this.entityCollision != null) {
									GL11.glColor4f(1.0F, 0.0F, 0.2F, 0.5F);
								}

								if (this.entityCollision != null) {
									this.blockCollision = this.entityCollision;
								}
							}
						}

						if (this.blockCollision != null) {
							break;
						}
					}

					GL11.glEnd();
					this.mc.getRenderManager();
					double renderX = posX - this.mc.getRenderManager().renderPosX;
					this.mc.getRenderManager();
					double renderY = posY - this.mc.getRenderManager().renderPosY;
					this.mc.getRenderManager();
					double renderZ = posZ - this.mc.getRenderManager().renderPosZ;
					GL11.glPushMatrix();
					GL11.glTranslated(renderX - 0.5D, renderY - 0.5D, renderZ - 0.5D);
					switch (this.blockCollision.sideHit.getIndex()) {
					case 2:
					case 3:
						GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
						aim = new AxisAlignedBB(0.0D, 0.5D, -1.0D, 1.0D, 0.45D, 0.0D);
						break;
					case 4:
					case 5:
						GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
						aim = new AxisAlignedBB(0.0D, -0.5D, 0.0D, 1.0D, -0.45D, 1.0D);
						break;
					default:
						aim = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 0.45D, 1.0D);
					}

					drawBox(aim);
					drawSelectionBoundingBox(aim);
					GL11.glPopMatrix();
					GL11.glDisable(3042);
					GL11.glEnable(3553);
					GL11.glEnable(2929);
					GL11.glDepthMask(true);
					GL11.glDisable(2848);
					GL11.glPopMatrix();
				}
			}
		}
	}

	public static void drawSelectionBoundingBox(AxisAlignedBB p_181561_0_) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(3, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(3, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		tessellator.draw();
		worldrenderer.begin(1, DefaultVertexFormats.POSITION);
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.minZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.maxX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.minY, p_181561_0_.maxZ).endVertex();
		worldrenderer.pos(p_181561_0_.minX, p_181561_0_.maxY, p_181561_0_.maxZ).endVertex();
		tessellator.draw();
	}

	public static void drawBox(AxisAlignedBB bb) {
		GL11.glBegin(7);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glEnd();
	}
}
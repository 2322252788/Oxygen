package cn.rainbow.oxygen.module.modules.render;

import cn.rainbow.oxygen.event.Event;
import cn.rainbow.oxygen.event.EventTarget;
import cn.rainbow.oxygen.event.events.LivingUpdateEvent;
import cn.rainbow.oxygen.event.events.Render3DEvent;
import cn.rainbow.oxygen.module.Category;
import cn.rainbow.oxygen.module.Module;
import cn.rainbow.oxygen.module.ModuleInfo;
import cn.rainbow.oxygen.utils.Location;
import cn.rainbow.oxygen.utils.other.MathUtils;
import cn.rainbow.oxygen.utils.Particles;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@ModuleInfo(name = "DMGParticle", category = Category.Render)
public class DMGParticle extends Module {
	
	private final HashMap<EntityLivingBase, Float> healthMap = new HashMap<>();
	private final ArrayList<Particles> particles = new ArrayList<>();

	@EventTarget(events = { LivingUpdateEvent.class, Render3DEvent.class })
	public void onLivingUpdate(Event event) {
		
		if (event instanceof LivingUpdateEvent) {
			LivingUpdateEvent elu = (LivingUpdateEvent) event;
			
			EntityLivingBase entity = (EntityLivingBase) elu.getEntity();
			if (entity == this.mc.thePlayer) {
				return;
			}
			if (!this.healthMap.containsKey(entity)) {
				this.healthMap.put(entity, entity.getHealth());
			}
			float floatValue = this.healthMap.get(entity);
			float health = entity.getHealth();
			if (floatValue != health) {
				String text;
				if (floatValue - health < 0.0f) {
					text = "\247a" + MathUtils.roundToPlace((floatValue - health) * -1.0f, 1);
				} else {
					text = "\247e" + MathUtils.roundToPlace(floatValue - health, 1);
				}
				Location location = new Location(entity);
				location.setY(entity.getEntityBoundingBox().minY
						+ (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2.0);
				location.setX(location.getX() - 0.5 + new Random(System.currentTimeMillis()).nextInt(5) * 0.1);
				location.setZ(location.getZ() - 0.5
						+ new Random(System.currentTimeMillis() + (0x203FF36645D9EA2EL ^ 0x203FF36645D9EA2FL)).nextInt(5)
								* 0.1);
				this.particles.add(new Particles(location, text));
				this.healthMap.remove(entity);
				this.healthMap.put(entity, entity.getHealth());
			}
		}
		
		if (event instanceof Render3DEvent) {
			for (Particles update : this.particles) {
				++update.ticks;
				if (update.ticks <= 10) {
					update.location.setY(update.location.getY() + update.ticks * 0.005);
				}
				if (update.ticks > 50) {
					this.particles.remove(update);
					break;
				}
				
				double x = update.location.getX();
				double n = x - mc.getRenderManager().viewerPosX;
				double y = update.location.getY();
				double n2 = y - mc.getRenderManager().viewerPosY;
				double z = update.location.getZ();
				double n3 = z - mc.getRenderManager().viewerPosZ;
				GlStateManager.pushMatrix();
				GlStateManager.enablePolygonOffset();
				GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
				GlStateManager.translate((float) n, (float) n2, (float) n3);
				GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
				float textY;
				if (this.mc.gameSettings.thirdPersonView == 2) {
					textY = -1.0f;
				} else {
					textY = 1.0f;
				}
				GlStateManager.rotate(this.mc.getRenderManager().playerViewX, textY, 0.0f, 0.0f);
				final double size = 0.03;
				GlStateManager.scale(-size, -size, size);
				enableGL2D();
				disableGL2D();
				GL11.glDepthMask(false);
				mc.fontRendererObj.drawStringWithShadow(update.text,
						(float) (-(mc.fontRendererObj.getStringWidth(update.text))),
						(float) (-(mc.fontRendererObj.FONT_HEIGHT - 1)), 0);
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				GL11.glDepthMask(true);
				GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
				GlStateManager.disablePolygonOffset();
				GlStateManager.popMatrix();
			}
		}
	}

	public static void enableGL2D() {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glDepthMask(true);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
	}

	public static void disableGL2D() {
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}
	
	@Override
	public void onEnable() {
	
	}
	
	@Override
	public void onDisable() {
	
	}
}

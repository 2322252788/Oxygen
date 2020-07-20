package me.Oxygen.utils.rotation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class RotationUtils {
	
	public static Minecraft mc = Minecraft.getMinecraft();

	public static float[] getRotationFromPosition(double x, double z, double y) {
		Minecraft.getMinecraft();
		double xDiff = x - mc.thePlayer.posX;
		Minecraft.getMinecraft();
		double zDiff = z - mc.thePlayer.posZ;
		Minecraft.getMinecraft();
		double yDiff = y - mc.thePlayer.posY - 0.6;
		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
		float pitch = (float) (-Math.atan2(yDiff, dist) * 180.0 / Math.PI);
		return new float[] { yaw, pitch };
	}
	
	public static float[] getRotationFromPosition2(double x, double z, double y) {
        Minecraft.getMinecraft();
        double xDiff = x - mc.thePlayer.posX;
        Minecraft.getMinecraft();
        double zDiff = z - mc.thePlayer.posZ;
        Minecraft.getMinecraft();
        double yDiff = y - mc.thePlayer.posY - 1.2;
        double dist = MathHelper.sqrt_double((double)(xDiff * xDiff + zDiff * zDiff));
        float yaw = (float)(Math.atan2((double)zDiff, (double)xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(-(Math.atan2((double)yDiff, (double)dist) * 180.0 / 3.141592653589793));
        return new float[]{yaw, pitch};
    }
	
	public static float[] getRotationNeededHypixelBetter(Entity p) {
	      double d1 = p.posX - mc.thePlayer.posX;
	      double d2 = p.posY + (double)p.getEyeHeight() - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
	      double d3 = p.posZ - mc.thePlayer.posZ;
	      double d4 = Math.sqrt(d1 * d1 + d3 * d3);
	      float f1 = (float)(Math.atan2(d3, d1) * 180.0D / 3.141592653589793D) - 90.0F;
	      float f2 = (float)(-Math.atan2(d2, d4) * 180.0D / 3.141592653589793D);
	      return new float[]{f1, f2};
	   }
	
	public static float[] getRotations(EntityLivingBase ent) {
        double x = ent.posX + (ent.posX - ent.lastTickPosX);
        double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.1f);
        return getRotationFromPosition2(x, z, y);
    }
	
	public static float[] getRotations(double posX, double posY, double posZ) {
        EntityPlayerSP player = mc.thePlayer;
        double x = posX - player.posX;
        double y = posY - (player.posY + (double)player.getEyeHeight());
        double z = posZ - player.posZ;
        double dist = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(y, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }
	
	public static float getYawChange(double posX, double posZ) {
		double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
		double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double yawToEntity;
		if (deltaZ < 0.0D && deltaX < 0.0D) {
			yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else if (deltaZ < 0.0D && deltaX > 0.0D) {
			yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
		} else {
			yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
		}

		return MathHelper.wrapAngleTo180_float(-(Minecraft.getMinecraft().thePlayer.rotationYaw - (float) yawToEntity));
	}

	public static float getYawChange(EntityLivingBase ent, float yaw) {
		double deltaX = ent.posX - mc.thePlayer.posX;
		double deltaZ = ent.posZ - mc.thePlayer.posZ;
		float yawToEntity = (float) (Math.atan2(deltaZ, deltaX) * 180.0 / 3.141592653589793) - 90.0f;

		return MathHelper.wrapAngleTo180_float(-(yaw - yawToEntity));
	}
	
	public static float getPitchChange(Entity entity, double posY) {
		double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
		double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
		double deltaY = posY - 2.2D + (double) entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
		double distanceXZ = (double) MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
		return -MathHelper
				.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch - (float) pitchToEntity) - 2.5F;
	}
	
}

package me.Oxygen.utils.rotation;

import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtil
{
    /*public static float[] getNeededRotationsToEntity(final Entity entity) {
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        final EntityPlayerSP player = Minecraft.getMinecraft();
        final double x = entity.posX - player.posX;
        double y = entity.posY - player.posY;
        final double z = entity.posZ - player.posZ;
        final double yaw = -Math.atan2(x, z) * 57.0;
        y /= player.getDistanceToEntity(entity);
        double pitch = Math.asin(y) * 57.0 - 20.0;
        pitch = -pitch;
        return new float[] { (float)yaw, (float)((pitch > 90.0) ? 90.0 : ((pitch < -90.0) ? -90.0 : pitch)) };
    }*/
    
    public static int wrapAngleToDirection(final float yaw, final int zones) {
        int angle = (int)(yaw + 360 / (2 * zones) + 0.5) % 360;
        if (angle < 0) {
            angle += 360;
        }
        return angle / (360 / zones);
    }
    
    public static float[] getRotations(final BlockPos pos, final EnumFacing facing) {
        return getRotations(pos.getX(), pos.getY(), pos.getZ(), facing);
    }
    
    public static Vec3 getVectorForRotation(final Rotation rotation) {
        final float yawCos = MathHelper.cos(-rotation.getYaw() * 0.017453292f - 3.1415927f);
        final float yawSin = MathHelper.sin(-rotation.getYaw() * 0.017453292f - 3.1415927f);
        final float pitchCos = -MathHelper.cos(-rotation.getPitch() * 0.017453292f);
        final float pitchSin = MathHelper.sin(-rotation.getPitch() * 0.017453292f);
        return new Vec3((double)(yawSin * pitchCos), (double)pitchSin, (double)(yawCos * pitchCos));
    }
    
    public static float[] getRotations(final double x, final double y, final double z, final EnumFacing facing) {
        final EntityPig temp = new EntityPig(Minecraft.getMinecraft().theWorld);
        temp.posX = x + 0.5;
        temp.posY = y + 0.5;
        temp.posZ = z + 0.5;
        final EntityPig entityPig = temp;
        entityPig.posX += facing.getDirectionVec().getX() * 0.5;
        final EntityPig entityPig2 = temp;
        entityPig2.posY += facing.getDirectionVec().getY() * 0.5;
        final EntityPig entityPig3 = temp;
        entityPig3.posZ += facing.getDirectionVec().getZ() * 0.5;
        return getRotations((Entity)temp);
    }
    
    public static float[] getRotations(final Entity ent) {
        final double x = ent.posX;
        final double z = ent.posZ;
        final double y = ent.posY + ent.getEyeHeight() / 2.0f;
        return getRotationFromPosition(x, z, y);
    }
    
    public static float[] getAverageRotations(final List<Entity> targetList) {
        double posX = 0.0;
        double posY = 0.0;
        double posZ = 0.0;
        for (final Entity ent : targetList) {
            posX += ent.posX;
            posY += ent.getEntityBoundingBox().maxY - 2.0;
            posZ += ent.posZ;
        }
        final float[] array = new float[2];
        final int n = 0;
        posX /= targetList.size();
        posZ /= targetList.size();
        array[n] = getRotationFromPosition(posX, posZ, posY /= targetList.size())[0];
        array[1] = getRotationFromPosition(posX, posZ, posY)[1];
        return array;
    }
    
    public static double getRotationDifference(final Rotation a, final Rotation b) {
        return Math.hypot(getAngleDifference(a.getYaw(), b.getYaw()), a.getPitch() - b.getPitch());
    }
    
    private static float getAngleDifference(final float a, final float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }
    
    public static float[] getBowAngles(final Entity entity) {
        final double xDelta = entity.posX - entity.lastTickPosX;
        final double zDelta = entity.posZ - entity.lastTickPosZ;
        Minecraft.getMinecraft();
        double d = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        d -= d % 0.8;
        double xMulti = 1.0;
        double zMulti = 1.0;
        final boolean sprint = entity.isSprinting();
        xMulti = d / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
        zMulti = d / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
        final double n = entity.posX + xMulti;
        Minecraft.getMinecraft();
        final double x = n - Minecraft.getMinecraft().thePlayer.posX;
        final double n2 = entity.posZ + zMulti;
        Minecraft.getMinecraft();
        final double z = n2 - Minecraft.getMinecraft().thePlayer.posZ;
        Minecraft.getMinecraft();
        final double posY = Minecraft.getMinecraft().thePlayer.posY;
        Minecraft.getMinecraft();
        final double y = posY + Minecraft.getMinecraft().thePlayer.getEyeHeight() - (entity.posY + entity.getEyeHeight());
        Minecraft.getMinecraft();
        final double dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        final float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        final float pitch = (float)Math.toDegrees(Math.atan2(y, dist));
        return new float[] { yaw, pitch };
    }
    
    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        Minecraft.getMinecraft();
        final double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        Minecraft.getMinecraft();
        final double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        Minecraft.getMinecraft();
        final double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);
        return new float[] { yaw, pitch };
    }
    
    public static float getTrajAngleSolutionLow(final float d3, final float d1, final float velocity) {
        final float g = 0.006f;
        final float sqrt = velocity * velocity * velocity * velocity - g * (g * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
        return (float)Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(sqrt)) / (g * d3)));
    }
    
    public static float getYawChange(final double posX, final double posZ) {
        Minecraft.getMinecraft();
        final double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
        Minecraft.getMinecraft();
        final double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        final double yawToEntity = (deltaZ < 0.0 && deltaX < 0.0) ? (90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX))) : ((deltaZ < 0.0 && deltaX > 0.0) ? (-90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX))) : Math.toDegrees(-Math.atan(deltaX / deltaZ)));
        Minecraft.getMinecraft();
        return MathHelper.wrapAngleTo180_float(-Minecraft.getMinecraft().thePlayer.rotationYaw - (float)yawToEntity);
    }
    
    public static float getYawChangeByHead(final double posX, final double posZ) {
        Minecraft.getMinecraft();
        final double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
        Minecraft.getMinecraft();
        final double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        final double yawToEntity = (deltaZ < 0.0 && deltaX < 0.0) ? (90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX))) : ((deltaZ < 0.0 && deltaX > 0.0) ? (-90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX))) : Math.toDegrees(-Math.atan(deltaX / deltaZ)));
        Minecraft.getMinecraft();
        return MathHelper.wrapAngleTo180_float(-Minecraft.getMinecraft().thePlayer.rotationYawHead - (float)yawToEntity);
    }
    
    public static float getPitchChange(final Entity entity, final double posY) {
        final double posX = entity.posX;
        Minecraft.getMinecraft();
        final double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
        final double posZ = entity.posZ;
        Minecraft.getMinecraft();
        final double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        final double n = posY - 2.2 + entity.getEyeHeight();
        Minecraft.getMinecraft();
        final double deltaY = n - Minecraft.getMinecraft().thePlayer.posY;
        final double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        final double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        Minecraft.getMinecraft();
        return -MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch - (float)pitchToEntity) - 2.5f;
    }
    
    public static float getNewAngle(float angle) {
        if ((angle %= 360.0f) >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }
    
    public static float getDistanceBetweenAngles(final float angle1, final float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 360.0f - angle3;
        }
        return angle3;
    }
    
    /*public static float[] grabBlockRotations(final BlockPos pos) {
        Minecraft.getMinecraft();
        final Vec3 positionVector = Minecraft.getMinecraft().thePlayer.getPositionVector();
        final double n = 0.0;
        Minecraft.getMinecraft();
        return getVecRotation(positionVector.addVector(n, (double)Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0), new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }
    
    public static float[] getVecRotation(final Vec3 position) {
        Minecraft.getMinecraft();
        final Vec3 positionVector = Minecraft.getMinecraft().thePlayer.getPositionVector();
        final double n = 0.0;
        Minecraft.getMinecraft();
        return getVecRotation(positionVector.addVector(n, (double)Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0), position);
    }
    
    public static float[] getVecRotation(final Vec3 origin, final Vec3 position) {
        final Vec3 difference = position.subtract(origin);
        final double distance = difference.flat().lengthVector();
        final float yaw = (float)Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(difference.yCoord, distance)));
        return new float[] { yaw, pitch };
    }*/
    
    public static float[] faceTarget(final Entity target, final float p_706252, final float p_706253, final boolean miss) {
        final double posX = target.posX;
        Minecraft.getMinecraft();
        final double var4 = posX - Minecraft.getMinecraft().thePlayer.posX;
        final double posZ = target.posZ;
        Minecraft.getMinecraft();
        final double var5 = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double var7;
        if (target instanceof EntityLivingBase) {
            final EntityLivingBase var6 = (EntityLivingBase)target;
            final double n = var6.posY + var6.getEyeHeight();
            Minecraft.getMinecraft();
            final double posY = Minecraft.getMinecraft().thePlayer.posY;
            Minecraft.getMinecraft();
            var7 = n - (posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        else {
            final double n2 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0;
            Minecraft.getMinecraft();
            final double posY2 = Minecraft.getMinecraft().thePlayer.posY;
            Minecraft.getMinecraft();
            var7 = n2 - (posY2 + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        }
        final Random rnd = new Random();
        final double var8 = MathHelper.sqrt_double(var4 * var4 + var5 * var5);
        final float var9 = (float)(Math.atan2(var5, var4) * 180.0 / 3.141592653589793) - 90.0f;
        final float var10 = (float)(-Math.atan2(var7 - ((target instanceof EntityPlayer) ? 0.25 : 0.0), var8) * 180.0 / 3.141592653589793);
        Minecraft.getMinecraft();
        final float pitch = changeRotation(Minecraft.getMinecraft().thePlayer.rotationPitch, var10, p_706253);
        Minecraft.getMinecraft();
        final float yaw = changeRotation(Minecraft.getMinecraft().thePlayer.rotationYaw, var9, p_706252);
        return new float[] { yaw, pitch };
    }
    
    public static float changeRotation(final float p_706631, final float p_706632, final float p_706633) {
        float var4 = MathHelper.wrapAngleTo180_float(p_706632 - p_706631);
        if (var4 > p_706633) {
            var4 = p_706633;
        }
        if (var4 < -p_706633) {
            var4 = -p_706633;
        }
        return p_706631 + var4;
    }
    
    public static float getYawChangeGiven(final double posX, final double posZ, final float yaw) {
        Minecraft.getMinecraft();
        final double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
        Minecraft.getMinecraft();
        final double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else if (deltaZ < 0.0 && deltaX > 0.0) {
            yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(yaw - (float)yawToEntity));
    }
    
    public static float[] getRotationToLocation(final Vec3 loc) {
        final double xDiff = loc.xCoord - Minecraft.getMinecraft().thePlayer.posX;
        final double yDiff = loc.yCoord - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        final double zDiff = loc.zCoord - Minecraft.getMinecraft().thePlayer.posZ;
        final double distance = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, distance) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
}

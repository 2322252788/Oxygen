// 
// Decompiled by Procyon v0.5.36
// 

package me.Oxygen.utils.Liquidbounce.block;

import net.minecraft.util.MathHelper;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class BlockUtils
{
    private static final Minecraft mc;
    
    public static Block getBlock(final BlockPos blockPos) {
        return BlockUtils.mc.theWorld.getBlockState(blockPos).getBlock();
    }
    
    public static Material getMaterial(final BlockPos blockPos) {
        return getBlock(blockPos).getMaterial();
    }
    
    public static boolean isReplaceable(final BlockPos blockPos) {
        return getMaterial(blockPos).isReplaceable();
    }
    
    public static IBlockState getState(final BlockPos pos) {
        return BlockUtils.mc.theWorld.getBlockState(pos);
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }
    
    public static String getBlockName(final int id) {
        return Block.getBlockById(id).getLocalizedName();
    }
    
    public static boolean isFullBlock(final BlockPos blockPos) {
        final AxisAlignedBB axisAlignedBB = getBlock(blockPos).getCollisionBoundingBox((World)BlockUtils.mc.theWorld, blockPos, getState(blockPos));
        return axisAlignedBB != null && axisAlignedBB.maxX - axisAlignedBB.minX == 1.0 && axisAlignedBB.maxY - axisAlignedBB.minY == 1.0 && axisAlignedBB.maxZ - axisAlignedBB.minZ == 1.0;
    }
    
    public static double getCenterDistance(final BlockPos blockPos) {
        return BlockUtils.mc.thePlayer.getDistance(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
    }
    
    public static Map<BlockPos, Block> searchBlocks(final int radius) {
        final Map<BlockPos, Block> blocks = new HashMap<BlockPos, Block>();
        for (int x = radius; x > -radius; --x) {
            for (int y = radius; y > -radius; --y) {
                for (int z = radius; z > -radius; --z) {
                    final BlockPos blockPos = new BlockPos((int)BlockUtils.mc.thePlayer.posX + x, (int)BlockUtils.mc.thePlayer.posY + y, (int)BlockUtils.mc.thePlayer.posZ + z);
                    final Block block = getBlock(blockPos);
                    blocks.put(blockPos, block);
                }
            }
        }
        return blocks;
    }
    
    public static boolean collideBlock(final AxisAlignedBB axisAlignedBB, final ICollide collide) {
        for (int x = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = getBlock(new BlockPos((double)x, axisAlignedBB.minY, (double)z));
                if (block != null && !collide.collideBlock(block)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean collideBlockIntersects(final AxisAlignedBB axisAlignedBB, final ICollide collide) {
        for (int x = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                final BlockPos blockPos = new BlockPos((double)x, axisAlignedBB.minY, (double)z);
                final Block block = getBlock(blockPos);
                if (block != null && collide.collideBlock(block)) {
                    final AxisAlignedBB boundingBox = block.getCollisionBoundingBox((World)BlockUtils.mc.theWorld, blockPos, getState(blockPos));
                    if (boundingBox != null && BlockUtils.mc.thePlayer.getEntityBoundingBox().intersectsWith(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public interface ICollide
    {
        boolean collideBlock(final Block p0);
    }
}

package me.Oxygen.modules.world;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.input.Keyboard;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventMotion.MotionType;
import me.Oxygen.event.events.EventSafeWalk;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.utils.other.MoveUtil;
import me.Oxygen.utils.rotation.RotationUtil;
import me.Oxygen.value.Value;
import me.Oxygen.injection.interfaces.IKeyBinding;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.client.C0APacketAnimation;

@ModuleRegister(name = "Scaffold", category = Category.WORLD)
public class Scaffold
extends Module {
    private BlockData blockData;
    private timeHelper time = new timeHelper();
    private timeHelper delay = new timeHelper();
    private timeHelper timer2 = new timeHelper();
    private Value<Boolean> tower = new Value<Boolean>("Scaffold_Tower", true);
    private Value<Boolean> movetower = new Value<Boolean>("Scaffold_MoveTower", false);
    private Value<Boolean> noSwing = new Value<Boolean>("Scaffold_NoSwing", true);
    private double olddelay;
    private BlockPos blockpos;
    private EnumFacing facing;
    private List<Block> blacklisted = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.ender_chest, Blocks.yellow_flower, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.crafting_table, Blocks.snow_layer, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.cactus, Blocks.lever, Blocks.activator_rail, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.furnace, Blocks.ladder, Blocks.oak_fence, Blocks.redstone_torch, Blocks.iron_trapdoor, Blocks.trapdoor, Blocks.tripwire_hook, Blocks.hopper, Blocks.acacia_fence_gate, Blocks.birch_fence_gate, Blocks.dark_oak_fence_gate, Blocks.jungle_fence_gate, Blocks.spruce_fence_gate, Blocks.oak_fence_gate, Blocks.dispenser, Blocks.sapling, Blocks.tallgrass, Blocks.deadbush, Blocks.web, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.nether_brick_fence, Blocks.vine, Blocks.double_plant, Blocks.flower_pot, Blocks.beacon, Blocks.pumpkin, Blocks.lit_pumpkin);
    public static List<Block> blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.ender_chest, Blocks.enchanting_table, Blocks.stone_button, Blocks.wooden_button, Blocks.crafting_table, Blocks.beacon);
    private boolean rotated = false;
    private boolean should = false;
    int slot;
    int theSlot;
    boolean sneaking;
    boolean isSneaking;
    public static ItemStack currentlyHolding;
    static final int[] $SwitchMap$net$minecraft$util$EnumFacing = new int[EnumFacing.values().length];

    public Scaffold() {
        this.currentlyHolding = null;
        this.theSlot = -1;
    }

    @EventTarget(events = {EventMotion.class, EventSafeWalk.class})
    private final void onEvent(Event event) {
    	if(event instanceof EventMotion) {
    		EventMotion em = (EventMotion)event;
    	if(em.getMotionType() == MotionType.PRE) {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY - 1.0;
        double z = mc.thePlayer.posZ;
        BlockPos blockBelow = new BlockPos(x, y, z);
        if (this.sneaking && !mc.thePlayer.isSneaking()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            this.sneaking = !this.sneaking;
        }
        if (((IKeyBinding)mc.gameSettings.keyBindSneak).getPress() && MoveUtil.MovementInput()) {
        	MoveUtil.setSpeed(MoveUtil.getBaseMoveSpeed() * 0.6);
            this.isSneaking = true;
        }
        else {
            this.isSneaking = false;
        }
        if(blockData != null) {
        BlockPos tempPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0 - (this.isSneaking ? 0.01 : 0.0), mc.thePlayer.posZ);
        this.blockData = this.getBlockData(tempPos, blacklistedBlocks);
        float[] rotate = RotationUtil.getRotationFromPosition(blockData.position.getX(), this.blockData.position.getZ(), this.blockData.position.getY());
        em.yaw = rotate[0];
        em.pitch = rotate[1];
        mc.thePlayer.rotationYawHead = em.yaw;
        }
        if (mc.thePlayer != null) {
            this.blockData = this.getBlockData(blockBelow, blacklistedBlocks);
            if (this.blockData == null) {
                this.blockData = this.getBlockData(blockBelow.offset(EnumFacing.DOWN), blacklistedBlocks);
            }
            if (this.mc.theWorld.getBlockState(blockBelow = new BlockPos(x, y, z)).getBlock() == Blocks.air) {
                if (this.tower.getValueState().booleanValue()) {
                    if (this.movetower.getValueState().booleanValue()) {
                        if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
                            if (this.isMoving2()) {
                                if (this.isOnGround(0.76) && !this.isOnGround(0.75) && mc.thePlayer.motionY > 0.23 && mc.thePlayer.motionY < 0.25) {
                                    mc.thePlayer.motionY = (double)Math.round(mc.thePlayer.posY) - mc.thePlayer.posY;
                                }
                                if (this.isOnGround(1.0E-4)) {
                                    mc.thePlayer.motionY = 0.42;
                                    mc.thePlayer.motionX *= 0.9;
                                    mc.thePlayer.motionZ *= 0.9;
                                } else if (mc.thePlayer.posY >= (double)Math.round(mc.thePlayer.posY) - 1.0E-4 && mc.thePlayer.posY <= (double)Math.round(mc.thePlayer.posY) + 1.0E-4) {
                                    mc.thePlayer.motionY = 0.0;
                                }
                            } else {
                                mc.thePlayer.motionX = 0.0;
                                mc.thePlayer.motionZ = 0.0;
                                mc.thePlayer.jumpMovementFactor = 0.0f;
                                blockBelow = new BlockPos(x, y, z);
                                if (this.mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air && this.blockData != null) {
                                    mc.thePlayer.motionY = 0.4196;
                                    mc.thePlayer.motionX *= 0.75;
                                    mc.thePlayer.motionZ *= 0.75;
                                }
                            }
                        }
                    } else if (!this.isMoving2() && this.mc.gameSettings.keyBindJump.isKeyDown()) {
                        mc.thePlayer.motionX = 0.0;
                        mc.thePlayer.motionZ = 0.0;
                        mc.thePlayer.jumpMovementFactor = 0.0f;
                        blockBelow = new BlockPos(x, y, z);
                        if (this.mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air && this.blockData != null) {
                            mc.thePlayer.motionY = 0.4196;
                            mc.thePlayer.motionX *= 0.75;
                            mc.thePlayer.motionZ *= 0.75;
                        }
                    }
                }
            }
        
        }
    	} else if(em.getMotionType() == MotionType.POST) {
    		int i;
    		this.theSlot = -1;
            for (i = 36; i < 45; ++i) {
            	this.theSlot = i - 36;
                ItemStack is;
                Item item;
                if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = (is = mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem()) instanceof ItemBlock) || this.blacklisted.contains(((ItemBlock)item).getBlock()) || ((ItemBlock)item).getBlock().getLocalizedName().toLowerCase().contains("chest") || this.blockData == null) continue;
                int currentItem = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i - 36));
                mc.thePlayer.inventory.currentItem = i - 36;
                mc.playerController.updateController();
                try {
                    this.currentlyHolding = this.mc.thePlayer.inventory.getStackInSlot(i - 36);
                    mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(this.theSlot), blockData.position, blockData.face, getVec3(blockData.position, blockData.face));
                    if (this.noSwing.getValueState().booleanValue()) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    } else {
                        mc.thePlayer.swingItem();
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
                mc.thePlayer.inventory.currentItem = currentItem;
                mc.playerController.updateController();
                return;
            }
            if (this.invCheck()) {
                for (i = 9; i < 36; ++i) {
                    Item item;
                    if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock) || this.blacklisted.contains(((ItemBlock)item).getBlock()) || ((ItemBlock)item).getBlock().getLocalizedName().toLowerCase().contains("chest")) continue;
                    this.swap(i, 7);
                    break;
                }
            }
    	}
    	}
    	if(event instanceof EventSafeWalk) {
    		EventSafeWalk esw = (EventSafeWalk)event;
    		if (!this.isSneaking) {
    			esw.setSafe(true);
            }
    	}
    }

    private final static Vec3 getVec3(final BlockPos blockPos, final EnumFacing enumFacing) {
        final double n = blockPos.getX() + 0.5;
        final double n2 = blockPos.getY() + 0.5;
        final double n3 = blockPos.getZ() + 0.5;
        double n4 = n + enumFacing.getFrontOffsetX() / 2.0;
        double n5 = n3 + enumFacing.getFrontOffsetZ() / 2.0;
        double n6 = n2 + enumFacing.getFrontOffsetY() / 2.0;
        if (enumFacing == EnumFacing.UP || enumFacing == EnumFacing.DOWN) {
            n4 += randomNumber(0.3, -0.3);
            n5 += randomNumber(0.3, -0.3);
        }
        else {
            n6 += randomNumber(0.3, -0.3);
        }
        if (enumFacing == EnumFacing.WEST || enumFacing == EnumFacing.EAST) {
            n5 += randomNumber(0.3, -0.3);
        }
        if (enumFacing == EnumFacing.SOUTH || enumFacing == EnumFacing.NORTH) {
            n4 += randomNumber(0.3, -0.3);
        }
        return new Vec3(n4, n6, n5);
    }

    private final static double randomNumber(final double n, final double n2) {
        return Math.random() * (n - n2) + n2;
    }

    private final boolean isOnGround(double height) {
        if (!this.mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, - height, 0.0)).isEmpty()) {
            return true;
        }
        return false;
    }

    private final boolean isMoving2() {
        return mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f;
    }

    private final float[] getRotationsBlock(BlockPos block, EnumFacing face) {
        double x = (double)block.getX() + 0.5 - mc.thePlayer.posX + (double)face.getFrontOffsetX() / 2.0;
        double z = (double)block.getZ() + 0.5 - mc.thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0;
        double y = (double)block.getY() + 0.5;
        double d1 = mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight() - y;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }

    private final static float randomFloat(long seed) {
        seed = System.currentTimeMillis() + seed;
        return 0.3f + (float)new Random(seed).nextInt(70000000) / 1.0E8f + 1.458745E-8f;
    }

    protected void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }

    private final boolean invCheck() {
        for (int i = 36; i < 45; ++i) {
            Item item;
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock) || this.blacklisted.contains(((ItemBlock)item).getBlock())) continue;
            return false;
        }
        return true;
    }

    private final double getDoubleRandom(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    private final boolean canPlace(EntityPlayerSP player, WorldClient worldIn, ItemStack heldStack, BlockPos hitPos, EnumFacing side, Vec3 vec3) {
        if (heldStack.getItem() instanceof ItemBlock) {
            return ((ItemBlock)heldStack.getItem()).canPlaceBlockOnSide(worldIn, hitPos, side, player, heldStack);
        }
        return false;
    }

    private final void setBlockAndFacing(BlockPos bp) {
        if (this.mc.theWorld.getBlockState(bp.add(0, -1, 0)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(0, -1, 0);
            this.facing = EnumFacing.UP;
        } else if (this.mc.theWorld.getBlockState(bp.add(-1, 0, 0)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(-1, 0, 0);
            this.facing = EnumFacing.EAST;
        } else if (this.mc.theWorld.getBlockState(bp.add(1, 0, 0)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(1, 0, 0);
            this.facing = EnumFacing.WEST;
        } else if (this.mc.theWorld.getBlockState(bp.add(0, 0, -1)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(0, 0, -1);
            this.facing = EnumFacing.SOUTH;
        } else if (this.mc.theWorld.getBlockState(bp.add(0, 0, 1)).getBlock() != Blocks.air) {
            this.blockpos = bp.add(0, 0, 1);
            this.facing = EnumFacing.NORTH;
        } else {
            bp = null;
            this.facing = null;
        }
    }

    private final int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock) || this.blacklisted.contains(((ItemBlock)item).getBlock())) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    private final int getBlockSlot() {
        for (int i = 36; i < 45; ++i) {
            ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack == null || !(itemStack.getItem() instanceof ItemBlock) || itemStack.stackSize <= 0 || this.blacklisted.stream().anyMatch(e -> e.equals(((ItemBlock)itemStack.getItem()).getBlock()))) continue;
            return i - 36;
        }
        return -1;
    }

    private final BlockData getBlockData(BlockPos pos, List list) {
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock())) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(-1, 0, 0), Keyboard.isKeyDown((int)42) && mc.thePlayer.onGround && mc.thePlayer.fallDistance == 0.0f && this.mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock())) {
            return new BlockData(pos.add(1, 0, 0), Keyboard.isKeyDown((int)42) && mc.thePlayer.onGround && mc.thePlayer.fallDistance == 0.0f && this.mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock())) {
            return new BlockData(pos.add(0, 0, -1), Keyboard.isKeyDown((int)42) && mc.thePlayer.onGround && mc.thePlayer.fallDistance == 0.0f && this.mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock())) {
            return new BlockData(pos.add(0, 0, 1), Keyboard.isKeyDown((int)42) && mc.thePlayer.onGround && mc.thePlayer.fallDistance == 0.0f && this.mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock() == Blocks.air ? EnumFacing.DOWN : EnumFacing.NORTH, this.blockData);
        }
        BlockPos add = pos.add(-1, 0, 0);
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add.add(1, 0, 0)).getBlock())) {
            return new BlockData(add.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add.add(0, 0, -1)).getBlock())) {
            return new BlockData(add.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add.add(0, 0, 1)).getBlock())) {
            return new BlockData(add.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        BlockPos add2 = pos.add(1, 0, 0);
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add2.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add2.add(1, 0, 0)).getBlock())) {
            return new BlockData(add2.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add2.add(0, 0, -1)).getBlock())) {
            return new BlockData(add2.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add2.add(0, 0, 1)).getBlock())) {
            return new BlockData(add2.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        BlockPos add3 = pos.add(0, 0, -1);
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add3.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add3.add(1, 0, 0)).getBlock())) {
            return new BlockData(add3.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add3.add(0, 0, -1)).getBlock())) {
            return new BlockData(add3.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add3.add(0, 0, 1)).getBlock())) {
            return new BlockData(add3.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        BlockPos add4 = pos.add(0, 0, 1);
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add4.add(-1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(-1, 0, 0), EnumFacing.EAST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add4.add(1, 0, 0)).getBlock())) {
            return new BlockData(add4.add(1, 0, 0), EnumFacing.WEST, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add4.add(0, 0, -1)).getBlock())) {
            return new BlockData(add4.add(0, 0, -1), EnumFacing.SOUTH, this.blockData);
        }
        if (!blacklistedBlocks.contains(this.mc.theWorld.getBlockState(add4.add(0, 0, 1)).getBlock())) {
            return new BlockData(add4.add(0, 0, 1), EnumFacing.NORTH, this.blockData);
        }
        return null;
    }

    private final boolean isAirBlock(Block block) {
        return block.getMaterial().isReplaceable() && (!(block instanceof BlockSnow) || block.getBlockBoundsMaxY() <= 0.125);
    }

    private final Vec3 getBlockSide(BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.NORTH) {
            return new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() - 0.5);
        }
        if (face == EnumFacing.EAST) {
            return new Vec3((double)pos.getX() + 0.5, pos.getY(), pos.getZ());
        }
        if (face == EnumFacing.SOUTH) {
            return new Vec3(pos.getX(), pos.getY(), (double)pos.getZ() + 0.5);
        }
        if (face == EnumFacing.WEST) {
            return new Vec3((double)pos.getX() - 0.5, pos.getY(), pos.getZ());
        }
        return new Vec3(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.timer2.reset();
        this.sneaking = true;
        this.currentlyHolding = null;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (this.sneaking && !mc.thePlayer.isSneaking()) {
            mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    }

    static {
        try {
            Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.UP.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.SOUTH.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.NORTH.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.EAST.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            Scaffold.$SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.WEST.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }

    public class timeHelper {
        private long prevMS = 0L;

        public boolean delay(float milliSec) {
            return (float)(this.getTime() - this.prevMS) >= milliSec;
        }

        public void reset() {
            this.prevMS = this.getTime();
        }

        public long getTime() {
            return System.nanoTime() / 1000000L;
        }

        public long getDifference() {
            return this.getTime() - this.prevMS;
        }

        public void setDifference(long difference) {
            this.prevMS = this.getTime() - difference;
        }
    }

    private static class BlockData {
        public static BlockPos position;
        public static EnumFacing face;

        public BlockData(BlockPos position, EnumFacing face, BlockData blockData) {
            BlockData.position = position;
            BlockData.face = face;
        }

        private BlockPos getPosition() {
            return position;
        }

        private EnumFacing getFacing() {
            return face;
        }

        static BlockPos access$0(BlockData var0) {
            return var0.getPosition();
        }

        static EnumFacing access$1(BlockData var0) {
            return var0.getFacing();
        }

        static BlockPos access$2(BlockData var0) {
            return position;
        }

        static EnumFacing access$3(BlockData var0) {
            return face;
        }
    }

}

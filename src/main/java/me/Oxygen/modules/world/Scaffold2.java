/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package me.Oxygen.modules.world;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.*;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventMotion;
import me.Oxygen.event.events.EventMove;
import me.Oxygen.event.events.EventMotion.MotionType;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.event.events.EventSafeWalk;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.injection.interfaces.*;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.utils.Liquidbounce.InventoryUtils;
import me.Oxygen.utils.Liquidbounce.block.BlockUtils;
import me.Oxygen.utils.Liquidbounce.block.PlaceInfo;
import me.Oxygen.utils.Liquidbounce.rotation.PlaceRotation;
import me.Oxygen.utils.Liquidbounce.rotation.Rotation;
import me.Oxygen.utils.Liquidbounce.rotation.RotationUtils;
import me.Oxygen.utils.Liquidbounce.time.MSTimer;
import me.Oxygen.utils.Liquidbounce.time.TimeUtils;
import me.Oxygen.utils.other.MoveUtil;
import me.Oxygen.value.Value;


@ModuleRegister(name = "Scaffold2",  category = Category.WORLD)
public class Scaffold2 extends Module {

    /**
     * OPTIONS
     */

    // Mode
    public final Value<String> modeValue = new Value<String>("Scaffold2", "Mode", 0);

    // Delay
    private final Value<Double> maxDelayValue = new Value<Double>("Scaffold2_MaxDelay", 0.0, 0.0, 1000.0, 1.0);

    private final Value<Double> minDelayValue = new Value<Double>("Scaffold2_MinDelay", 0.0, 0.0, 1000.0, 1.0);

    private final Value<Boolean> placeableDelay = new Value<Boolean>("Scaffold2_PlaceableDelay", false);

    // AutoBlock
    private final Value<Boolean> autoBlockValue = new Value<Boolean>("Scaffold2_AutoBlock", true);
    private final Value<Boolean> stayAutoBlock = new Value<Boolean>("Scaffold2_StayAutoBlock", false);

    // Basic stuff
    public final Value<Boolean> sprintValue = new Value<Boolean>("Scaffold2_Sprint", true);
    private final Value<Boolean> swingValue = new Value<Boolean>("Scaffold2_Swing", true);
    private final Value<Boolean> searchValue = new Value<Boolean>("Scaffold2_Search", true);
    private final Value<Boolean> downValue = new Value<Boolean>("Scaffold2_Down", true);
    private final Value<String> placeModeValue = new Value<String>("Scaffold2", "PlaceTiming", 0);

    // Eagle
    private final Value<Boolean> eagleValue = new Value<Boolean>("Scaffold2_Eagle", false);
    private final Value<Boolean> eagleSilentValue = new Value<Boolean>("Scaffold2_EagleSilent", false);
    private final Value<Double> blocksToEagleValue = new Value<Double>("Scaffold2_BlocksToEagle", 0.0, 0.0, 10.0, 1.0);

    // Expand
    private final Value<Double> expandLengthValue = new Value<Double>("Scaffold2_ExpandLength", 5.0, 1.0, 6.0, 1.0);

    // Rotations
    private final Value<Boolean> rotationsValue = new Value<Boolean>("Scaffold2_Rotations", true);
    private final Value<Double> keepLengthValue = new Value<Double>("Scaffold2_KeepRotationLength", 0.0, 0.0, 20.0, 1.0);
    private final Value<Boolean> keepRotationValue = new Value<Boolean>("Scaffold2_KeepRotation", false);

    // Zitter
    private final Value<Boolean> zitterValue = new Value<Boolean>("Scaffold2_Zitter", false);
    private final Value<String> zitterModeValue = new Value<String>("Scaffold2", "ZitterMode", 0);
    private final Value<Double> zitterSpeed = new Value<Double>("Scaffold2_ZitterSpeed", 0.13, 0.1, 0.3, 0.01);
    private final Value<Double> zitterStrength = new Value<Double>("Scaffold2_ZitterStrength", 0.072, 0.05, 0.2, 0.001);

    // Game
    private final Value<Double> timerValue = new Value<Double>("Timer", 1.0, 0.1, 10.0, 0.1);
    private final Value<Double> speedModifierValue = new Value<Double>("SpeedModifier", 1.0, 0.0, 2.0, 1.0);

    // Safety
    private final Value<Boolean> sameYValue = new Value<Boolean>("Scaffold2_SameY", false);
    private final Value<Boolean> safeWalkValue = new Value<Boolean>("Scaffold2_SafeWalk", true);
    private final Value<Boolean> airSafeValue = new Value<Boolean>("Scaffold2_AirSafe", false);

    // Visuals
    private final Value<Boolean> counterDisplayValue = new Value<Boolean>("Scaffold2_Counter", true);
    private final Value<Boolean> markValue = new Value<Boolean>("Scaffold2_Mark", false);

    /**
     * MODULE
     */

    // Target block
    private PlaceInfo targetPlace;


    // Launch position
    private int launchY;

    // Rotation lock
    private Rotation lockRotation;

    // Auto block slot
    private int slot;

    // Zitter Smooth
    private boolean zitterDirection;

    // Delay
    private final MSTimer delayTimer = new MSTimer();
    private final MSTimer zitterTimer = new MSTimer();
    private long delay;

    // Eagle
    private int placedBlocksWithoutEagle = 0;
    private boolean eagleSneaking;

    // Down
    private boolean shouldGoDown = false;
    
    public Scaffold2() {
    	modeValue.addValue("Normal");
    	modeValue.addValue("Rewinside");
    	modeValue.addValue("Expand");
    	placeModeValue.addValue("Pre");
    	placeModeValue.addValue("Post");
    	zitterModeValue.addValue("Teleport");
    	zitterModeValue.addValue("Smooth");
    }

    /**
     * Enable module
     */
    @Override
    public void onEnable() {
        if (mc.thePlayer == null) return;

        launchY = (int) mc.thePlayer.posY;
    }

    /**
     * Update event
     *
     * @param event
     */
    @EventTarget(events = {EventUpdate.class, EventPacket.class, EventMotion.class, EventMove.class})
    public void onUpdate(Event event) {
    	if(event instanceof EventUpdate) {
        ((IMinecraft)mc).getTimer().timerSpeed = timerValue.getValueState().floatValue();

        shouldGoDown = downValue.getValueState() && GameSettings.isKeyDown(mc.gameSettings.keyBindSneak) && getBlocksAmount() > 1;
        if (shouldGoDown)
            ((IKeyBinding)mc.gameSettings.keyBindSneak).setPress(false);

        if (mc.thePlayer.onGround) {

            // Rewinside scaffold mode
            if (modeValue.isCurrentMode("Rewinside")) {
                MoveUtil.strafe(0.2F);
                mc.thePlayer.motionY = 0D;
            }

            // Smooth Zitter
            if (zitterValue.getValueState() && zitterModeValue.getValueState().equalsIgnoreCase("smooth")) {
                if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight))
                    ((IKeyBinding)mc.gameSettings.keyBindRight).setPress(false);

                if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft))
                    ((IKeyBinding)mc.gameSettings.keyBindLeft).setPress(false);

                if (zitterTimer.hasTimePassed(100)) {
                    zitterDirection = !zitterDirection;
                    zitterTimer.reset();
                }

                if (zitterDirection) {
                	((IKeyBinding)mc.gameSettings.keyBindRight).setPress(true);
                	((IKeyBinding)mc.gameSettings.keyBindLeft).setPress(false);
                } else {
                	((IKeyBinding)mc.gameSettings.keyBindRight).setPress(false);
                	((IKeyBinding)mc.gameSettings.keyBindLeft).setPress(true);
                }
            }

            // Eagle
            if (eagleValue.getValueState() && !shouldGoDown) {
                if (placedBlocksWithoutEagle >= blocksToEagleValue.getValueState()) {
                    final boolean shouldEagle = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX,
                            mc.thePlayer.posY - 1D, mc.thePlayer.posZ)).getBlock() == Blocks.air;

                    if (eagleSilentValue.getValueState()) {
                        if (eagleSneaking != shouldEagle) {
                            mc.getNetHandler().addToSendQueue(
                                    new C0BPacketEntityAction(mc.thePlayer, shouldEagle ?
                                            C0BPacketEntityAction.Action.START_SNEAKING :
                                            C0BPacketEntityAction.Action.STOP_SNEAKING)
                            );
                        }

                        eagleSneaking = shouldEagle;
                    } else
                        ((IKeyBinding)mc.gameSettings.keyBindSneak).setPress(shouldEagle);

                    placedBlocksWithoutEagle = 0;
                } else
                    placedBlocksWithoutEagle++;
            }

            // Zitter
            if (zitterValue.getValueState() && zitterModeValue.isCurrentMode("Teleport")) {
                MoveUtil.strafe(zitterSpeed.getValueState().floatValue());

                final double yaw = Math.toRadians(mc.thePlayer.rotationYaw + (zitterDirection ? 90D : -90D));
                mc.thePlayer.motionX -= Math.sin(yaw) * zitterStrength.getValueState();
                mc.thePlayer.motionZ += Math.cos(yaw) * zitterStrength.getValueState();
                zitterDirection = !zitterDirection;
            }
        }
    }
    	if(event instanceof EventPacket) {
    		EventPacket ep = (EventPacket)event;
    		if (mc.thePlayer == null)
                return;

            final Packet<?> packet = ep.getPacket();

            // AutoBlock
            if (packet instanceof C09PacketHeldItemChange) {
                final C09PacketHeldItemChange packetHeldItemChange = (C09PacketHeldItemChange) packet;

                slot = packetHeldItemChange.getSlotId();
            }
    	}
    	if(event instanceof EventMotion) {
    		EventMotion em = (EventMotion)event;
    		if (rotationsValue.getValueState() && keepRotationValue.getValueState() && lockRotation != null)
                RotationUtils.setTargetRotation(lockRotation);

            // Place block
    		if(em.getMotionType() == MotionType.PRE && placeModeValue.isCurrentMode("Pre")) {
    			place();
    		}
    		if(em.getMotionType() == MotionType.POST && placeModeValue.isCurrentMode("Post")) {
    			place();
    		}

            // Update and search for new block
            if (em.getMotionType() == MotionType.PRE)
                update();

            // Reset placeable delay
            if (targetPlace == null && placeableDelay.getValueState())
                delayTimer.reset();
    	}
    	if(event instanceof EventSafeWalk) {
    		EventSafeWalk em = (EventSafeWalk)event;
    		if (!safeWalkValue.getValueState() || shouldGoDown)
                return;

            if (airSafeValue.getValueState() || mc.thePlayer.onGround)
                em.setSafe(true);
    	}
    }

    private void update() {
        final boolean isHeldItemBlock = mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock;
        if (autoBlockValue.getValueState() ? InventoryUtils.findAutoBlockBlock() == -1 && !isHeldItemBlock : !isHeldItemBlock)
            return;

        findBlock(modeValue.isCurrentMode("Expand"));
    }

    /**
     * Search for new target block
     */
    private void findBlock(final boolean expand) {
        final BlockPos blockPosition = shouldGoDown ? (mc.thePlayer.posY == (int) mc.thePlayer.posY + 0.5D ?
                new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.6D, mc.thePlayer.posZ)
                : new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.6, mc.thePlayer.posZ).down()) :
                (mc.thePlayer.posY == (int) mc.thePlayer.posY + 0.5D ? new BlockPos(mc.thePlayer)
                        : new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ).down());

        if (!expand && (!isReplaceable(blockPosition) || search(blockPosition, !shouldGoDown)))
            return;

        if (expand) {
            for (int i = 0; i < expandLengthValue.getValueState(); i++) {
                if (search(blockPosition.add(
                        mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST ? -i : mc.thePlayer.getHorizontalFacing() == EnumFacing.EAST ? i : 0,
                        0,
                        mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH ? -i : mc.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH ? i : 0
                ), false))

                    return;
            }
        } else if (searchValue.getValueState()) {
            for (int x = -1; x <= 1; x++)
                for (int z = -1; z <= 1; z++)
                    if (search(blockPosition.add(x, 0, z), !shouldGoDown))
                        return;
        }
    }

    /**
     * Place target block
     */
    private void place() {
        if (targetPlace == null) {
            if (placeableDelay.getValueState())
                delayTimer.reset();
            return;
        }

        if (!delayTimer.hasTimePassed(delay) || (sameYValue.getValueState() && launchY - 1 != (int) targetPlace.getVec3().yCoord))
            return;

        int blockSlot = -1;
        ItemStack itemStack = mc.thePlayer.getHeldItem();

        if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) || mc.thePlayer.getHeldItem().stackSize <= 0) {
            if (!autoBlockValue.getValueState())
                return;

            blockSlot = InventoryUtils.findAutoBlockBlock();

            if (blockSlot == -1)
                return;

            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(blockSlot - 36));
            itemStack = mc.thePlayer.inventoryContainer.getSlot(blockSlot).getStack();
        }


        if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, targetPlace.getBlockPos(), targetPlace.getEnumFacing(), targetPlace.getVec3())) {
            delayTimer.reset();
            delay = TimeUtils.randomDelay(minDelayValue.getValueState().intValue(), maxDelayValue.getValueState().intValue());

            if (mc.thePlayer.onGround) {
                final float modifier = speedModifierValue.getValueState().floatValue();

                mc.thePlayer.motionX *= modifier;
                mc.thePlayer.motionZ *= modifier;
            }

            if (swingValue.getValueState())
                mc.thePlayer.swingItem();
            else
                mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
        }

        if (!stayAutoBlock.getValueState() && blockSlot >= 0)
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));

        // Reset
        this.targetPlace = null;
    }

    /**
     * Disable scaffold module
     */
    @Override
    public void onDisable() {
        if (mc.thePlayer == null) return;

        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
            ((IKeyBinding)mc.gameSettings.keyBindSneak).setPress(false);

            if (eagleSneaking)
                mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }

        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight))
            ((IKeyBinding)mc.gameSettings.keyBindRight).setPress(false);

        if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft))
            ((IKeyBinding)mc.gameSettings.keyBindLeft).setPress(false);

        lockRotation = null;
        ((IMinecraft)mc).getTimer().timerSpeed = 1F;
        shouldGoDown = false;

        if (slot != mc.thePlayer.inventory.currentItem)
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    }

    /**
     * Scaffold visuals
     *
     * @param event
     */
    /*@EventTarget
    public void onRender2D(final Render2DEvent event) {
        if (counterDisplayValue.getValueState()) {
            GlStateManager.pushMatrix();

            final BlockOverlay blockOverlay = (BlockOverlay) LiquidBounce.moduleManager.getModule(BlockOverlay.class);
            if (blockOverlay.getState() && blockOverlay.getInfoValue().get() && blockOverlay.getCurrentBlock() != null)
                GlStateManager.translate(0, 15F, 0);

            final String info = "Blocks: ยง7" + getBlocksAmount();
            final ScaledResolution scaledResolution = new ScaledResolution(mc);

            RenderUtils.drawBorderedRect((scaledResolution.getScaledWidth() / 2) - 2,
                    (scaledResolution.getScaledHeight() / 2) + 5,
                    (scaledResolution.getScaledWidth() / 2) + Fonts.font40.getStringWidth(info) + 2,
                    (scaledResolution.getScaledHeight() / 2) + 16, 3, Color.BLACK.getRGB(), Color.BLACK.getRGB());
            GlStateManager.resetColor();
            Fonts.font40.drawString(info, scaledResolution.getScaledWidth() / 2,
                    scaledResolution.getScaledHeight() / 2 + 7, Color.WHITE.getRGB());

            GlStateManager.popMatrix();
        }
    }*/

    /**
     * Scaffold visuals
     *
     * @param event
     */
    /*@EventTarget
    public void onRender3D(final Render3DEvent event) {
        if (!markValue.getValueState())
            return;

        for (int i = 0; i < (modeValue.isCurrentMode("Expand") ? expandLengthValue.getValueState() + 1 : 2); i++) {
            final BlockPos blockPos = new BlockPos(mc.thePlayer.posX + (mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST ? -i : mc.thePlayer.getHorizontalFacing() == EnumFacing.EAST ? i : 0), mc.thePlayer.posY - (mc.thePlayer.posY == (int) mc.thePlayer.posY + 0.5D ? 0D : 1.0D) - (shouldGoDown ? 1D : 0), mc.thePlayer.posZ + (mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH ? -i : mc.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH ? i : 0));
            final PlaceInfo placeInfo = PlaceInfo.get(blockPos);

            if (BlockUtils.isReplaceable(blockPos) && placeInfo != null) {
                RenderUtils.drawBlockBox(blockPos, new Color(68, 117, 255, 100), false);
                break;
            }
        }
    }*/

    /**
     * Search for placeable block
     *
     * @param blockPosition pos
     * @param checks        visible
     * @return
     */
    private boolean search(final BlockPos blockPosition, final boolean checks) {
        if (!isReplaceable(blockPosition))
            return false;

        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        PlaceRotation placeRotation = null;

        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = blockPosition.offset(side);

            if (!BlockUtils.canBeClicked(neighbor))
                continue;

            final Vec3 dirVec = new Vec3(side.getDirectionVec());

            for (double xSearch = 0.1D; xSearch < 0.9D; xSearch += 0.1D) {
                for (double ySearch = 0.1D; ySearch < 0.9D; ySearch += 0.1D) {
                    for (double zSearch = 0.1D; zSearch < 0.9D; zSearch += 0.1D) {
                        final Vec3 posVec = new Vec3(blockPosition).addVector(xSearch, ySearch, zSearch);
                        final double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
                        final Vec3 hitVec = posVec.add(new Vec3(dirVec.xCoord * 0.5, dirVec.yCoord * 0.5, dirVec.zCoord * 0.5));

                        if (checks && (eyesPos.squareDistanceTo(hitVec) > 18D || distanceSqPosVec > eyesPos.squareDistanceTo(posVec.add(dirVec)) || mc.theWorld.rayTraceBlocks(eyesPos, hitVec, false, true, false) != null))
                            continue;

                        // face block
                        final double diffX = hitVec.xCoord - eyesPos.xCoord;
                        final double diffY = hitVec.yCoord - eyesPos.yCoord;
                        final double diffZ = hitVec.zCoord - eyesPos.zCoord;

                        final double diffXZ = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);

                        final Rotation rotation = new Rotation(
                                MathHelper.wrapAngleTo180_float((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F),
                                MathHelper.wrapAngleTo180_float((float) -Math.toDegrees(Math.atan2(diffY, diffXZ)))
                        );

                        final Vec3 rotationVector = RotationUtils.getVectorForRotation(rotation);
                        final Vec3 vector = eyesPos.addVector(rotationVector.xCoord * 4, rotationVector.yCoord * 4, rotationVector.zCoord * 4);
                        final MovingObjectPosition obj = mc.theWorld.rayTraceBlocks(eyesPos, vector, false, false, true);

                        if (!(obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && obj.getBlockPos().equals(neighbor)))
                            continue;

                        if (placeRotation == null || RotationUtils.getRotationDifference(rotation) < RotationUtils.getRotationDifference(placeRotation.getRotation()))
                            placeRotation = new PlaceRotation(new PlaceInfo(neighbor, side.getOpposite(), hitVec), rotation);
                    }
                }
            }
        }

        if (placeRotation == null) return false;

        if (rotationsValue.getValueState()) {
            RotationUtils.setTargetRotation(placeRotation.getRotation(), keepLengthValue.getValueState().intValue());
            lockRotation = placeRotation.getRotation();
        }
        targetPlace = placeRotation.getPlaceInfo();
        return true;
    }

    /**
     * @return hotbar blocks amount
     */
    private int getBlocksAmount() {
        int amount = 0;

        for (int i = 36; i < 45; i++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock) itemStack.getItem()).getBlock();
                if (mc.thePlayer.getHeldItem() == itemStack || !InventoryUtils.BLOCK_BLACKLIST.contains(block))
                    amount += itemStack.stackSize;
            }
        }

        return amount;
    }
    
    public static final Block getBlock(final BlockPos blockPos) {
        final WorldClient field_71441_e = Minecraft.getMinecraft().theWorld;
        if (field_71441_e != null) {
            final IBlockState func_180495_p = field_71441_e.getBlockState(blockPos);
            if (func_180495_p != null) {
                return func_180495_p.getBlock();
            }
        }
        return null;
    }
    
    public static final Material getMaterial(final BlockPos blockPos) {
        final Block block = getBlock(blockPos);
        return (block != null) ? block.getMaterial() : null;
    }
    
    public static final boolean isReplaceable(final BlockPos blockPos) {
        final Material material = getMaterial(blockPos);
        return material != null && material.isReplaceable();
    }

    }

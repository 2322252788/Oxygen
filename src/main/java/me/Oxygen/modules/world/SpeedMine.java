package me.Oxygen.modules.world;

import me.Oxygen.event.Event;
import me.Oxygen.event.EventTarget;
import me.Oxygen.event.events.EventDamageBlock;
import me.Oxygen.event.events.EventPacket;
import me.Oxygen.event.events.EventUpdate;
import me.Oxygen.injection.interfaces.IC07PacketPlayerDigging;
import me.Oxygen.injection.interfaces.IPlayerControllerMP;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.value.Value;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

@ModuleRegister(name = "SpeedMine", category = Category.WORLD)
public class SpeedMine extends Module
{
	private final Value<String> mode = new Value<String>("SpeedMine", "Mode", 0);
	private final Value<Double> speed = new Value<Double>("SpeedMine_Speed", 0.3, 0.1, 1.1, 0.01);;

    boolean bzs = false;
    double bzx = 0.0;
    BlockPos pos;
    EnumFacing face;

    public SpeedMine() {
        mode.addValue("Normal");
        mode.addValue("HuaYuTing");
        mode.addValue("New");
    }
    
    public Block getBlock(double x,double y,double z){
		BlockPos bp = new BlockPos(x, y, z);
		return mc.theWorld.getBlockState(bp).getBlock();
	}
    
    @EventTarget(events = {EventDamageBlock.class, EventPacket.class, EventUpdate.class})
	private final void onEvent(final Event e) {
        if(e instanceof EventDamageBlock) {

            EventDamageBlock edb = (EventDamageBlock) e;
            if (mode.isCurrentMode("Normal")) {
                final IPlayerControllerMP playerControllerMP = (IPlayerControllerMP) mc.playerController;
                playerControllerMP.setBlockHitDelay(0);
                if (playerControllerMP.getCurBlockDamageMP() >= 1.0 - this.speed.getValueState()) {
                    playerControllerMP.setCurBlockDamageMP(1.0f);
                }
            }
            if (mode.isCurrentMode("New")) {
                final IPlayerControllerMP playerControllerMP = (IPlayerControllerMP) mc.playerController;
                mc.thePlayer.swingItem();
                playerControllerMP.setCurBlockDamageMP(playerControllerMP.getCurBlockDamageMP() + getBlock(edb.getPos().getX(), edb.getPos().getY(), edb.getPos().getZ()).getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, edb.getPos()) * speed.getValueState().floatValue());
                if (playerControllerMP.getCurBlockDamageMP() >= 0.7f) {
                    playerControllerMP.setCurBlockDamageMP(1.0f);
                    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, edb.getPos(), edb.getFac()));
                }
            }
        }
        if (e instanceof EventPacket){
            EventPacket ep = (EventPacket)e;
            Packet packet = ep.getPacket();
            if (mode.isCurrentMode("HuaYuTing") && packet instanceof C07PacketPlayerDigging && mc.playerController != null){
                if (((IC07PacketPlayerDigging) packet).getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK){
                    bzs = true;
                    pos = ((IC07PacketPlayerDigging) packet).getPosition();
                    face = ((IC07PacketPlayerDigging) packet).getFacing();
                    bzx = 0.0;
                }
                else if(((IC07PacketPlayerDigging) packet).getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK || ((IC07PacketPlayerDigging) packet).getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                    bzs = false;
                    pos = null;
                    face = null;
                }
            }
        }
        if (e instanceof EventUpdate && mode.isCurrentMode("HuaYuTing")){
            if(mc.playerController.extendedReach()) {
                ((IPlayerControllerMP)mc.playerController).setBlockHitDelay(0);
            }
            else if(bzs) {
                bzx += (mc.theWorld.getBlockState(pos).getBlock().getPlayerRelativeBlockHardness(mc.thePlayer, mc.theWorld, pos) * 1.4);
                if (bzx >= 1.0) {
                    mc.theWorld.setBlockState(pos, Blocks.air.getDefaultState(), 11);
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, face));
                    bzx = 0.0;
                    bzs = false;
                }
            }
        }
    }
}


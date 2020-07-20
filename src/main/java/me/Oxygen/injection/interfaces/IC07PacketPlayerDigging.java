package me.Oxygen.injection.interfaces;

import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public interface IC07PacketPlayerDigging {

    C07PacketPlayerDigging.Action getStatus();

    BlockPos getPosition();

    EnumFacing getFacing();
}

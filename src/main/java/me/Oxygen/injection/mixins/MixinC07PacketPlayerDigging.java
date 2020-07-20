package me.Oxygen.injection.mixins;

import me.Oxygen.injection.interfaces.IC07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(C07PacketPlayerDigging.class)
public class MixinC07PacketPlayerDigging implements IC07PacketPlayerDigging {

    @Shadow
    private BlockPos position;

    @Shadow
    private EnumFacing facing;

    @Shadow
    private C07PacketPlayerDigging.Action status;

    @Override
    public C07PacketPlayerDigging.Action getStatus() {
        return status;
    }

    @Override
    public BlockPos getPosition() {
        return position;
    }

    @Override
    public EnumFacing getFacing() {
        return facing;
    }
}

package me.Oxygen.utils.Liquidbounce.rotation;

import net.minecraft.util.Vec3;
import me.Oxygen.utils.Liquidbounce.rotation.Rotation;

public class VecRotation {

    private final Vec3 vec;

    private final Rotation rotation;

    public final Vec3 getVec() {
        return this.vec;
    }

    public final Rotation getRotation() {
        return this.rotation;
    }

    public VecRotation(final Vec3 vec, final Rotation rotation) {
        this.vec = vec;
        this.rotation = rotation;
    }

    public final Vec3 component1() {
        return this.vec;
    }

    public final Rotation component2() {
        return this.rotation;
    }

    public final VecRotation copy(final Vec3 vec, final Rotation rotation) {
        return new VecRotation(vec, rotation);
    }

}

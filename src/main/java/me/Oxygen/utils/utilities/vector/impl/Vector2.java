package me.Oxygen.utils.utilities.vector.impl;

import me.Oxygen.utils.utilities.vector.Vector;

public class Vector2<T extends Number>
extends Vector<Number> {
    public Vector2(T x, T y) {
        super(x, y, (Number)0);
    }

    public Vector3<T> toVector3() {
        return new Vector3(this.getX(), this.getY(), this.getZ());
    }
}

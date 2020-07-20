package me.Oxygen.utils.utilities.vector.impl;

import me.Oxygen.utils.utilities.vector.Vector;
import me.Oxygen.utils.utilities.vector.impl.Vector2;

public class Vector3<T extends Number>
extends Vector<Number> {
    public Vector3(T x, T y, T z) {
        super(x, y, z);
    }

    public Vector2<T> toVector2() {
        return (Vector2<T>)new Vector2(this.getX(), this.getY());
    }
}

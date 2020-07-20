package me.Oxygen.utils.Liquidbounce.rotation;

import me.Oxygen.utils.Liquidbounce.block.PlaceInfo;

public final class PlaceRotation {
	
	private final PlaceInfo placeInfo;
	
	private final Rotation rotation;
	
    public final PlaceInfo getPlaceInfo() {
        return this.placeInfo;
    }

    public final Rotation getRotation() {
        return this.rotation;
    }
    
    public PlaceRotation(PlaceInfo placeInfo, Rotation rotation) {
        this.placeInfo = placeInfo;
        this.rotation = rotation;
    }
    
    public final PlaceRotation copy(PlaceInfo placeInfo, Rotation rotation) {
        return new PlaceRotation(placeInfo, rotation);
    }

}

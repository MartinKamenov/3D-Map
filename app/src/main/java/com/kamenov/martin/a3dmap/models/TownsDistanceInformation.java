package com.kamenov.martin.a3dmap.models;

public class TownsDistanceInformation {
    private final Town[] townsBetween;
    private final float distance;

    public TownsDistanceInformation(float distance, Town[] townsBetween) {
        this.distance = distance;
        this.townsBetween = townsBetween;
    }
}

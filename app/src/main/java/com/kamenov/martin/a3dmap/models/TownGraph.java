package com.kamenov.martin.a3dmap.models;

public class TownGraph {
    private final Town firstTown;
    private final Town secondTown;
    private final float distance;
    private int latDegreeToKm = 111;

    public TownGraph(Town firstTown, Town secondTown) {
        this.firstTown = firstTown;
        this.secondTown = secondTown;
        this.distance = getDistance();
    }

    public float getDistance() {
        return latDegreeToKm * (float)getDifferenceUsingPythagoras(firstTown.lng, secondTown.lng, firstTown.lat, secondTown.lat);
    }

    private double getDifferenceUsingPythagoras(double x1, double x2, double y1, double y2) {
        return Math.sqrt(
                (x1 - x2)*(x1 - x2) +
                        (y1 - y2) * (y1 - y2)
        );
    }
}

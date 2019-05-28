package com.kamenov.martin.a3dmap.models;

public class Town {
    public String name;
    public float x;
    public float y;

    public Town(String name, double y, double x) {
        this.name = name;
        this.x = (float) x;
        this.y = (float) y;
    }
}

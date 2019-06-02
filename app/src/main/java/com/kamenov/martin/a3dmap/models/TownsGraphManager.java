package com.kamenov.martin.a3dmap.models;

import java.util.ArrayList;

public class TownsGraphManager {
    private final Town[] towns;
    private final ArrayList<TownGraph> allGraphs;

    public TownsGraphManager(Town[] towns) {
        this.towns = towns;
        this.allGraphs = new ArrayList<>();
        setGraphs();
    }

    public void createGraph(String firstTownName, String secondTownName) {
        int firstTownIndex = -1;
        int secondTownIndex = -1;
        firstTownName = firstTownName.toLowerCase();
        secondTownName = secondTownName.toLowerCase();

        int index = 0;
        while (firstTownIndex == -1 || secondTownIndex == -1 || index < towns.length) {
            String townName = towns[index].city.toLowerCase();
            if(townName.contains(firstTownName)) {
                firstTownIndex = index;
            }

            if(townName.contains(secondTownName)) {
                secondTownIndex = index;
            }

            index++;
        }
    }

    public void setGraphs() {
        createGraph("Vidin", "Montana");
    }
}

package com.kamenov.martin.a3dmap.models;

import java.util.ArrayList;

public class TownsGraphManager {
    private final Town[] towns;
    private final ArrayList<TownGraph> allGraphs;

    private static TownsGraphManager instance;

    private TownsGraphManager(Town[] towns) {
        this.towns = towns;
        this.allGraphs = new ArrayList<>();
        setGraphs();
    }

    public static TownsGraphManager getInstance(Town[] towns) {
        if(instance == null) {
            instance = new TownsGraphManager(towns);
        }

        return instance;
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

        allGraphs.add(new TownGraph(towns[firstTownIndex], towns[secondTownIndex]));
    }

    public void setGraphs() {
        createGraph("Vidin", "Montana");
        createGraph("Montana", "Vratsa");
        createGraph("Vratsa", "Sofia");
    }

    public TownsDistanceInformation findClosesRouth(Town firstTown,
                                                    Town secondTown,
                                                    float distance,
                                                    ArrayList<Town> townsRoute) {
        if(firstTown.city.equals(secondTown.city)) {
            return new TownsDistanceInformation(distance, townsRoute.toArray(new Town[townsRoute.size()]));
        }

        townsRoute.add(secondTown);

        ArrayList<Town> nextAvailableTowns = new ArrayList<>();

        for(int i = 0; i < allGraphs.size(); i++) {
            Town graphFirstTown = allGraphs.get(i).getFirstTown();
            Town graphSecondTown = allGraphs.get(i).getSecondTown();

            if(graphFirstTown.city.equals(firstTown.city) && isTownAvailable(townsRoute, graphSecondTown)) {
                nextAvailableTowns.add(graphSecondTown);
            } else if(graphSecondTown.city.equals(firstTown.city) && isTownAvailable(townsRoute, graphFirstTown)) {
                nextAvailableTowns.add(graphFirstTown);
            }
        }

        for(int i = 0; i < nextAvailableTowns.size(); i++) {
            findClosesRouth(secondTown, nextAvailableTowns.get(i), distance, townsRoute);
        }

        return new TownsDistanceInformation(distance, new Town[townsRoute.size()]);
    }

    private boolean isTownAvailable(ArrayList<Town> townsRoute, Town town) {
        for(int i = 0; i < townsRoute.size(); i++) {
            if(townsRoute.get(i).city.equals(town.city)) {
                return false;
            }
        }

        return true;
    }
}

package com.kamenov.martin.a3dmap.models;

import java.util.ArrayList;

public class TownsGraphManager {
    public ArrayList<Town> minRouthe;
    public float minimalDistance;
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
        createGraph("Montana", "Vidin");
        createGraph("Montana", "Vratsa");
        createGraph("Vratsa", "Montana");
        createGraph("Vratsa", "Sofia");
        createGraph("Sofia", "Vratsa");
    }

    public void findClosestRouthe(Town fromTown,
                                                    Town toTown,
                                                    float distance,
                                                    ArrayList<Town> townsRoute) {
        if(fromTown.city.equals(toTown.city)) {
            if(distance < minimalDistance) {
                minimalDistance = distance;
                minRouthe = townsRoute;
            }
            return;
        }

        townsRoute.add(fromTown);

        ArrayList<TownGraph> nextAvailableTowns = new ArrayList<>();

        for(int i = 0; i < allGraphs.size(); i++) {
            Town graphFirstTown = allGraphs.get(i).getFirstTown();
            Town graphSecondTown = allGraphs.get(i).getSecondTown();

            if(graphFirstTown.city.equals(fromTown.city) && isTownAvailable(townsRoute, graphSecondTown)) {
                nextAvailableTowns.add(allGraphs.get(i));
            }
        }

        for(int i = 0; i < nextAvailableTowns.size(); i++) {
            TownGraph graph = nextAvailableTowns.get(i);
            findClosestRouthe(graph.getSecondTown(), toTown,
                    distance + graph.getDistance(), townsRoute);
        }
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

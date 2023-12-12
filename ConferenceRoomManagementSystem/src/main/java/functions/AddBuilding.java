package functions;

import models.Building;

import java.util.function.Function;

import db.Storage;

public class AddBuilding implements Function<String, Building> {
    @Override
    public Building apply(String buildingId) {
        Building building = new Building(buildingId);
        Storage.addBuilding(building);
        return building;
    }
}

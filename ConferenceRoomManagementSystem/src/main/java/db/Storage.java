package db;

import models.Booking;
import models.Building;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Storage {

    private static Map<String, Booking> bookings;
    private static Map<String, List<Booking>> userBookings;
    private static Map<String,Building> buildings;

    private Storage() {
    }

    public static Map<String, Booking> getBookings() {
        if (Objects.isNull(bookings)) {
            bookings = new HashMap<>();
        }
        return bookings;
    }
    public static Map<String, List<Booking>> getUserBookings() {
        if (Objects.isNull(userBookings)) {
            userBookings = new HashMap<>();
        }
        return userBookings;
    }

    public static void addBuilding(Building building){
        if (Objects.isNull(buildings)) {
            buildings = new HashMap<>();
        }
        buildings.putIfAbsent(building.getId(), building);
    }

    public static Building getBuilding(String buildingId){
        return buildings.get(buildingId);
    }
}

package functions;

import lombok.val;
import models.Building;
import models.Room;
import org.apache.commons.lang3.tuple.Pair;

import db.Storage;

import java.util.function.Function;

public class AddRoom implements Function<Pair<String, Pair<String, Integer>>, Room> {

    @Override
    public Room apply(Pair<String, Pair<String, Integer>> input) {
        val roomId = input.getLeft();
        val buildingId = input.getRight().getLeft();
        Building building = Storage.getBuilding(buildingId);
        val floorNo = input.getRight().getRight();
        val floor = building.getFloors().get(floorNo);
        val room = new Room(roomId, floorNo, building.getId());
        floor.addRoom(room);
        return room;
    }
}

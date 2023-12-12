package functions;

import lombok.val;
import models.Building;
import models.Floor;
import org.apache.commons.lang3.tuple.Pair;

import db.Storage;

import java.util.function.Function;

public class AddFloor implements Function<Pair<String, Integer>, Floor> {
    @Override
    public Floor apply(Pair<String, Integer> buildingIntegerPair){
        val buildingId = buildingIntegerPair.getLeft();
        Building building = Storage.getBuilding(buildingId);
        val floorNo = buildingIntegerPair.getRight();
        val floor = new Floor(floorNo);
        building.addFloor(floor);
        return floor;
    }
}

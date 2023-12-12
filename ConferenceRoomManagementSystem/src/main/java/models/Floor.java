package models;


import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Data
public class Floor {
    private final Integer number;
    private Map<String, Room> rooms = new HashMap<>();

    public void addRoom(@NonNull final Room room) {
        rooms.putIfAbsent(room.getId(), room);
    }
}

package Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Library {

    private String name;
    private String id;
    private int rackSize;

    public Library(int rackSize) {
        this.rackSize = rackSize;
        this.id = UUID.randomUUID().toString();
    }
}

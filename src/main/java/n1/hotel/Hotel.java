package n1.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter

public class Hotel {
    private int id;
    private String name;
    private String address;
    private Manager manager;
    private String email;
    private ArrayList<Room> rooms;

    ArrayList<Room> getAvailableRooms() {
        if (this.rooms == null) {
            return new ArrayList<>();
        }
        return this.rooms.stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}

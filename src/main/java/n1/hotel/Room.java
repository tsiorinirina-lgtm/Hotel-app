package n1.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class Room {
    private int roomNumber;
    private int maxPeopleInRoom;
    private float price;
}

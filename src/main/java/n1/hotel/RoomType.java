package n1.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class RoomType {
    private String name;
    private String description;
    private double price;
    private int numberBeds;
}

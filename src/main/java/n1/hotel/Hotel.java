package n1.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

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
}

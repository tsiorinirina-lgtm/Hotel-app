package n1.hotel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {
    public RoomType type;
    private int roomNumber;
    @Setter
    private BedSize bedSize;
    private double price;
    @Setter
    private boolean available;
    private int capacity;
    private int floor;
    private RoomStatus status;

    public Room(int roomNumber, RoomType roomType, BedSize bedSize, double price, int capacity, int floor) {
        this.roomNumber = roomNumber;
        this.type = roomType;
        this.bedSize = bedSize;
        this.price = price;
        this.available = true;
        this.capacity = capacity;
        this.floor = floor;
    }

    public void setRoomNumber(int roomNumber) {
        if (roomNumber <= 0) {
            throw new IllegalArgumentException("The room number need to positive. got : " + roomNumber);
        }
        this.roomNumber = roomNumber;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("The price must be positive. Got : " + price);
        }
        this.price = price;
    }

    public void setCapacity(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("The capacity must be at least 1 person. Got : " + capacity);
        }
        this.capacity = capacity;
    }

    public void setFloor(int floor) {
        if (floor < 0) {
            throw new IllegalArgumentException("The floor can't be negative. Got : " + floor);
        }
        this.floor = floor;
    }
}

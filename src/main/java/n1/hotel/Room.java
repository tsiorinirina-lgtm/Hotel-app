package main.java.n1.hotel;

public class Room {
    public static final String[] TYPES_AUTORISES = {"SIMPLE", "DOUBLE", "SUITE", "DELUXE"};
    private int roomNumber;
    private String type;
    private double price;
    private boolean available;
    private int capacity;
    private int floor;

    public Room(int roomNumber, String type, double price, int capacity, int floor) {
        setRoomNumber(roomNumber);
        setType(type);
        setPrice(price);
        setCapacity(capacity);
        setFloor(floor);
        this.available = true; 
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        if (roomNumber <= 0) {
            throw new IllegalArgumentException("The rooom number need to positive. got : " + roomNumber);
        }
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("The room type cannot be null.");
        }
        String upper = type.toUpperCase();
        for (String t : TYPES_AUTORISES) {
            if (t.equals(upper)) {
                this.type = upper;
                return;
            }
        }
        throw new IllegalArgumentException(
            "Invalid room type : '" + type + "'. Accepted values : SIMPLE, DOUBLE, SUITE, DELUXE."
        );
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("The price must be positive. Got : " + price);
        }
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("The capacity must be at least 1 person. Got : " + capacity);
        }
        this.capacity = capacity;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        if (floor < 0) {
            throw new IllegalArgumentException("The floor can't be negative. Got : " + floor);
        }
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "Room{" +
            "roomNumber=" + roomNumber +
            ", type='" + type + '\'' +
            ", price=" + price +
            ", available=" + available +
            ", capacity=" + capacity +
            ", floor=" + floor +
            '}';
    }
}

package main.java.n1.hotel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    private String name;
    private String employeeId;
    private Map<Integer, Room> rooms;
    private Map<Client, Room> assignments;

    public Manager(String name, String employeeId) {
        setName(name);
        setEmployeeId(employeeId);
        this.rooms       = new HashMap<>();
        this.assignments = new HashMap<>();
    }

    public void addRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("The room cannot be null.");
        }
        if (rooms.containsKey(room.getRoomNumber())) {
            throw new IllegalArgumentException(
                "A room with the number " + room.getRoomNumber() + " already exists."
            );
        }
        rooms.put(room.getRoomNumber(), room);
        System.out.println("[Manager] Room #" + room.getRoomNumber() + " added to inventory.");
    }

    public void removeRoom(int roomNumber) {
        Room room = getRoomOrThrow(roomNumber);
        if (!room.isAvailable()) {
            throw new IllegalStateException(
                "Impossible to delete the room #" + roomNumber + " : it is currently occupied."
            );
        }
        rooms.remove(roomNumber);
        System.out.println("[Manager] Room #" + roomNumber + " removed from inventory.");
    }

    public void assignRoom(Client client, int roomNumber) {
        if (client == null) {
            throw new IllegalArgumentException("The client cannot be null.");
        }
        if (assignments.containsKey(client)) {
            Room current = assignments.get(client);
            throw new IllegalStateException(
                "The client " + client.getName() + " already has the room #" + current.getRoomNumber() + "."
            );
        }

        Room room = getRoomOrThrow(roomNumber);

        if (!room.isAvailable()) {
            throw new IllegalStateException(
                "The room #" + roomNumber + " is already occupied."
            );
        }

        room.setAvailable(false);
        assignments.put(client, room);
        System.out.println("[Manager] Room #" + roomNumber +
            " assigned to " + client.getName() + ".");
    }

    public void releaseRoom(Client client) {
        if (client == null || !assignments.containsKey(client)) {
            throw new IllegalArgumentException(
                "This client does not have a room assigned."
            );
        }
        Room room = assignments.remove(client);
        room.setAvailable(true);
        System.out.println("[Manager] Room #" + room.getRoomNumber() +
            " released by " + client.getName() + ".");
    }

    public List<Room> getAvailableRooms() {
        List<Room> available = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.isAvailable()) {
                available.add(room);
            }
        }
        return Collections.unmodifiableList(available);
    }

    public void printAvailableRooms() {
        List<Room> available = getAvailableRooms();
        if (available.isEmpty()) {
            System.out.println("[Manager] No available rooms at the moment.");
            return;
        }
        System.out.println("[Manager] Available rooms (" + available.size() + ") :");
        for (Room room : available) {
            System.out.println("  - " + room);
        }
    }

    public double calculateTotalRevenue() {
        double total = 0;
        for (Room room : assignments.values()) {
            total += room.getPrice();
        }
        return total;
    }

    public void printTotalRevenue() {
        System.out.printf("[Manager] Total revenue (occupied rooms) : %.2f Ar%n",
            calculateTotalRevenue());
    }

    private Room getRoomOrThrow(int roomNumber) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException(
                "Room #" + roomNumber + " not found in inventory."
            );
        }
        return room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The manager's name cannot be empty.");
        }
        this.name = name.trim();
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.isBlank()) {
            throw new IllegalArgumentException("The employee ID cannot be empty.");
        }
        this.employeeId = employeeId.trim();
    }

    public Map<Integer, Room> getRooms() {
        return Collections.unmodifiableMap(rooms);
    }

    public Map<Client, Room> getAssignments() {
        return Collections.unmodifiableMap(assignments);
    }

    @Override
    public String toString() {
        return "Manager{" +
            "name='" + name + '\'' +
            ", employeeId='" + employeeId + '\'' +
            ", totalRooms=" + rooms.size() +
            ", occupiedRooms=" + assignments.size() +
            '}';
    }
}

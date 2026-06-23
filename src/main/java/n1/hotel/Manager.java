package n1.hotel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {
    @Getter
    private String name;
    @Getter
    private String employeeId;
    private Map<Integer, Room> rooms;
    private Map<Customer, Room> assignments;

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

    public void assignRoom(Customer client, int roomNumber) {
        if (client == null) {
            throw new IllegalArgumentException("The client cannot be null.");
        }
        if (assignments.containsKey(client)) {
            Room current = assignments.get(client);
            throw new IllegalStateException(
                "The client " + client.getFullName() + " already has the room #" + current.getRoomNumber() + "."
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
            " assigned to " + client.getFullName() + ".");
    }

    public void releaseRoom(Customer client) {
        if (client == null || !assignments.containsKey(client)) {
            throw new IllegalArgumentException(
                "This client does not have a room assigned."
            );
        }
        Room room = assignments.remove(client);
        room.setAvailable(true);
        System.out.println("[Manager] Room #" + room.getRoomNumber() +
            " released by " + client.getFullName() + ".");
    }

    public double calculateTotalRevenue() {
        double total = 0;
        for (Room room : assignments.values()) {
            total += room.getPrice();
        }
        return total;
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

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The manager's name cannot be empty.");
        }
        this.name = name.trim();
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

    public Map<Customer, Room> getAssignments() {
        return Collections.unmodifiableMap(assignments);
    }
}

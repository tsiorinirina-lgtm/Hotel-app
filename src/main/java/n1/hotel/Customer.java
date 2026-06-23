package n1.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@Getter
@Setter

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;

    String getFullName() {
        return firstName + " " + lastName;
    }

    void bookHotel(Hotel hotel) {
        if (hotel == null) {
            throw new IllegalArgumentException("L'hôtel ne peut pas être null.");
        }
        ArrayList<Room> availableRooms = hotel.getAvailableRooms();
        if (availableRooms.isEmpty()) {
            throw new IllegalStateException("Désolé, aucune chambre n'est disponible dans cet hôtel.");
        }
        Room roomToBook = availableRooms.getFirst();
        hotel.getManager().assignRoom(this, roomToBook.getRoomNumber());
    }

    void bookHotel(Hotel hotel, int roomNumber) {
        if (hotel == null) {
            throw new IllegalArgumentException("L'hôtel ne peut pas être null.");
        }
        hotel.getManager().assignRoom(this, roomNumber);
    }

    void bookHotel(Hotel hotel, RoomType roomType) {
        if (hotel == null) {
            throw new IllegalArgumentException("L'hôtel ne peut pas être null.");
        }
        Room roomToBook = hotel.getAvailableRooms().stream()
                .filter(room -> room.getType() == roomType)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Aucune chambre disponible pour le type : " + roomType));

        hotel.getManager().assignRoom(this, roomToBook.getRoomNumber());
    }

    void bookHotel(Hotel hotel, double maxPrice) {
        if (hotel == null) {
            throw new IllegalArgumentException("L'hôtel ne peut pas être null.");
        }
        Room roomToBook = hotel.getAvailableRooms().stream()
                .filter(room -> room.getPrice() <= maxPrice)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Aucune chambre disponible en dessous ou égale au prix de : " + maxPrice + "€"));

        hotel.getManager().assignRoom(this, roomToBook.getRoomNumber());
    }

    void bookHotel(Hotel hotel, BedSize requiredBedSize) {
        if (hotel == null) {
            throw new IllegalArgumentException("L'hôtel ne peut pas être null.");
        }
        if (requiredBedSize == null) {
            throw new IllegalArgumentException("La taille de lit demandée ne peut pas être null.");
        }

        Room roomToBook = hotel.getAvailableRooms().stream()
                .filter(room -> room.getBedSize() != null &&
                        room.getBedSize().getLength() >= requiredBedSize.getLength() &&
                        room.getBedSize().getWidth() >= requiredBedSize.getWidth())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Aucune chambre disponible avec un lit d'au moins "
                        + requiredBedSize.getLength() + "x" + requiredBedSize.getWidth()));

        hotel.getManager().assignRoom(this, roomToBook.getRoomNumber());
    }
}

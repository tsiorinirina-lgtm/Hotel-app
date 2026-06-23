package n1.hotel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CustomerBookHotelTest {

    private Hotel hotel;
    private Manager manager;
    private Customer customer;
    private Room room1;
    private Room room2;

    @BeforeEach
    void setUp() {
        manager = new Manager("John Doe", "MGR-001");
        customer = new Customer(1, "Alice", "Smith", "alice@email.com", "123456", "123 Street");

        BedSize kingSize = new BedSize("King", 2.0f, 2.0f);
        room1 = new Room(101, RoomType.SIMPLE, kingSize, 80.0, 1, 1);
        room2 = new Room(102, RoomType.SUITE, kingSize, 250.0, 2, 1);

        manager.addRoom(room1);
        manager.addRoom(room2);

        ArrayList<Room> roomList = new ArrayList<>();
        roomList.add(room1);
        roomList.add(room2);

        hotel = new Hotel(1, "Grand Hotel", "Paris", manager, "contact@grandhotel.com", roomList);
    }

    @Test
    @DisplayName("Forme 1 : bookHotel(Hotel) - Doit réserver la première chambre disponible")
    void testBookHotelFirstAvailable() {
        customer.bookHotel(hotel);

        assertFalse(room1.isAvailable(), "La première chambre (101) devrait être occupée.");
        assertTrue(room2.isAvailable(), "La deuxième chambre (102) devrait rester libre.");
        assertEquals(room1, manager.getAssignments().get(customer), "Le client devrait avoir la chambre 101.");
    }

    @Test
    @DisplayName("Forme 1 : bookHotel(Hotel) - Doit lever une exception si l'hôtel est complet")
    void testBookHotelFirstAvailable_Full() {
        room1.setAvailable(false);
        room2.setAvailable(false);

        assertThrows(IllegalStateException.class, () -> customer.bookHotel(hotel),
                "Une exception aurait dû être levée car aucune chambre n'est disponible.");
    }

    @Test
    @DisplayName("Forme 2 : bookHotel(Hotel, roomNumber) - Doit réserver la chambre spécifiée")
    void testBookHotelByRoomNumber() {
        customer.bookHotel(hotel, 102);

        assertFalse(room2.isAvailable(), "La chambre 102 devrait être occupée.");
        assertTrue(room1.isAvailable(), "La chambre 101 devrait rester libre.");
        assertEquals(room2, manager.getAssignments().get(customer));
    }

    @Test
    @DisplayName("Forme 2 : bookHotel(Hotel, roomNumber) - Doit échouer si la chambre est déjà occupée")
    void testBookHotelByRoomNumber_AlreadyOccupied() {
        room1.setAvailable(false);

        assertThrows(IllegalStateException.class, () -> customer.bookHotel(hotel, 101),
                "Le manager devrait refuser d'assigner une chambre déjà occupée.");
    }

    @Test
    @DisplayName("Forme 3 : bookHotel(Hotel, RoomType) - Doit réserver le bon type de chambre")
    void testBookHotelByRoomType() {
        customer.bookHotel(hotel, RoomType.SUITE);

        assertFalse(room2.isAvailable(), "La suite (102) devrait être occupée.");
        assertTrue(room1.isAvailable(), "La chambre simple (101) devrait rester libre.");
        assertEquals(RoomType.SUITE, manager.getAssignments().get(customer).getType());
    }

    @Test
    @DisplayName("Forme 4 : bookHotel(Hotel, price) - Doit réserver une chambre selon le budget max")
    void testBookHotelByMaxPrice() {
        customer.bookHotel(hotel, 100.0);

        assertFalse(room1.isAvailable(), "La chambre à 80€ devrait être réservée.");
        assertTrue(room2.isAvailable(), "La chambre à 250€ dépasse le budget de 100€.");
    }

    @Test
    @DisplayName("Forme 4 : bookHotel(Hotel, price) - Doit lever une exception si le budget est trop bas")
    void testBookHotelByMaxPrice_TooLow() {
        assertThrows(IllegalStateException.class, () -> customer.bookHotel(hotel, 50.0),
                "Aucune chambre ne correspond à ce budget maximum.");
    }

    @Test
    @DisplayName("Forme 5 : bookHotel(Hotel, BedSize) - Doit réserver une chambre avec un lit assez grand")
    void testBookHotelByBedSize_Success() {
        var queenSize = new BedSize("Queen", 2.0f, 1.6f);
        var kingSize = new BedSize("King", 2.0f, 2.0f);

        room1.setBedSize(queenSize);
        room2.setBedSize(kingSize);

        var criteria = new BedSize("Target", 2.0f, 1.8f);

        customer.bookHotel(hotel, criteria);

        assertFalse(room2.isAvailable(), "La chambre 102 avec le lit King aurait dû être réservée.");
        assertTrue(room1.isAvailable(), "La chambre 101 avec le lit Queen ne convient pas et doit rester libre.");
        assertEquals(room2, manager.getAssignments().get(customer));
    }

    @Test
    @DisplayName("Forme 5 : bookHotel(Hotel, BedSize) - Doit lever une exception si aucun lit n'est assez grand")
    void testBookHotelByBedSize_TooLarge() {
        BedSize smallSize = new BedSize("Small", 1.9f, 1.4f);
        room1.setBedSize(smallSize);
        room2.setBedSize(smallSize);

        var giantBed = new BedSize("Giant", 2.0f, 2.2f);

        assertThrows(IllegalStateException.class, () -> customer.bookHotel(hotel, giantBed),
                "Une exception aurait dû être levée car aucun lit ne fait 2.2m de large.");
    }
}
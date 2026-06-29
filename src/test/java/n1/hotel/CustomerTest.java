package n1.hotel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    Customer customer;
    Hotel hotel;
    Manager manager;
    BedSize bedSize;
    Room room;
    LocalDateTime startDate;
    LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        customer  = new Customer(1, "Alice", "Dupont", "alice@mail.com", "0600000000", "1 Rue de la Paix", new ArrayList<>());
        manager   = new Manager("Bob Martin", "EMP-001");
        bedSize   = new BedSize("Queen", 200f, 160f);
        room      = new Room(101, RoomType.DOUBLE, bedSize, 120.0, 2, 1);

        var rooms = new ArrayList<Room>();
        rooms.add(room);
        hotel     = new Hotel(1, "Grand Hôtel", "Paris", manager, "contact@grand.com", rooms);

        startDate = LocalDateTime.of(2025, 7, 1, 14, 0);
        endDate   = LocalDateTime.of(2025, 7, 5, 11, 0);
    }

    @Test
    @DisplayName("pay() - full payment returns PAID status")
    void testPayFullPayment() {
        var payment = customer.pay(200.0, 200.0, PaymentMethod.CARD);

        assertNotNull(payment);
        assertEquals(PaymentStatus.PAID, payment.getStatus());
        assertEquals(200.0, payment.getAmountPaid());
        assertEquals(200.0, payment.getAmountCharged());
        assertTrue(payment.isFullyPaid());
    }

    @Test
    @DisplayName("pay() - partial payment returns PENDING status")
    void testPayPartialPayment() {
        var payment = customer.pay(200.0, 100.0, PaymentMethod.CASH);

        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        assertFalse(payment.isFullyPaid());
        assertTrue(payment.isPartiallyPaid());
    }

    @Test
    @DisplayName("pay() - zero amount paid returns FAILED status")
    void testPayZeroAmountPaid() {
        var payment = customer.pay(200.0, 0.0, PaymentMethod.CARD);

        assertEquals(PaymentStatus.FAILED, payment.getStatus());
        assertFalse(payment.isFullyPaid());
        assertFalse(payment.isPartiallyPaid());
    }

    @Test
    @DisplayName("pay() - overpayment is still PAID status")
    void testPayOverpayment() {
        var payment = customer.pay(100.0, 150.0, PaymentMethod.CASH);

        assertEquals(PaymentStatus.PAID, payment.getStatus());
        assertTrue(payment.isFullyPaid());
    }

    @Test
    @DisplayName("pay() - negative charged amount throws IllegalArgumentException")
    void testPayNegativeChargedAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> customer.pay(-50.0, 50.0, PaymentMethod.CARD));
    }

    @Test
    @DisplayName("pay() - negative paid amount throws IllegalArgumentException")
    void testPayNegativePaidAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> customer.pay(100.0, -10.0, PaymentMethod.CARD));
    }

    @Test
    @DisplayName("pay() - payment method is stored correctly")
    void testPayPaymentMethodStored() {
        var payment = customer.pay(80.0, 80.0, PaymentMethod.CASH);

        assertEquals(PaymentMethod.CASH, payment.getPaymentMethod());
    }

    @Test
    @DisplayName("book(Hotel) - returns a PENDING booking for the first available room")
    void testBookHotel() {
        var booking = customer.book(hotel, startDate, endDate);

        assertNotNull(booking);
        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertEquals(hotel, booking.getHotel());
        assertEquals(customer, booking.getCustomer());
        assertEquals(room, booking.getRoom());
    }

    @Test
    @DisplayName("book(Hotel) - throws when hotel has no available rooms")
    void testBookHotelNoRoomsAvailable() {
        room.setAvailable(false);

        assertThrows(IllegalStateException.class,
                () -> customer.book(hotel, startDate, endDate));
    }

    @Test
    @DisplayName("book(Hotel) - throws when end date is before start date")
    void testBookHotelInvalidDates() {
        assertThrows(IllegalArgumentException.class,
                () -> customer.book(hotel, endDate, startDate));
    }

    @Test
    @DisplayName("book(Hotel) - throws when hotel is null")
    void testBookNullHotel() {
        assertThrows(IllegalArgumentException.class,
                () -> customer.book(null, startDate, endDate));
    }

    @Test
    @DisplayName("book(Hotel, maxPrice) - returns booking when room price is within limit")
    void testBookHotelWithPriceMatch() {
        var booking = customer.book(hotel, 150.0, startDate, endDate);

        assertNotNull(booking);
        assertTrue(booking.getRoom().getPrice() <= 150.0);
    }

    @Test
    @DisplayName("book(Hotel, maxPrice) - throws when no room is within price limit")
    void testBookHotelWithPriceTooLow() {
        assertThrows(IllegalStateException.class,
                () -> customer.book(hotel, 50.0, startDate, endDate));
    }

    @Test
    @DisplayName("book(Hotel, numberOfPeople) - returns booking when capacity is sufficient")
    void testBookHotelWithCapacityMatch() {
        var booking = customer.book(hotel, 2, startDate, endDate);

        assertNotNull(booking);
        assertEquals(2, booking.getNumberPeople());
        assertTrue(booking.getRoom().getCapacity() >= 2);
    }

    @Test
    @DisplayName("book(Hotel, numberOfPeople) - throws when room capacity is insufficient")
    void testBookHotelCapacityTooLow() {
        assertThrows(IllegalStateException.class,
                () -> customer.book(hotel, 10, startDate, endDate));
    }

    @Test
    @DisplayName("book(Hotel, numberOfPeople) - throws when numberOfPeople < 1")
    void testBookHotelZeroPeople() {
        assertThrows(IllegalArgumentException.class,
                () -> customer.book(hotel, 0, startDate, endDate));
    }

    @Test
    @DisplayName("book(Hotel, BedSize) - returns booking when matching bed size exists")
    void testBookHotelWithBedSizeMatch() {
        var booking = customer.book(hotel, bedSize, startDate, endDate);

        assertNotNull(booking);
        assertEquals(bedSize, booking.getRoom().getBedSize());
    }

    @Test
    @DisplayName("book(Hotel, BedSize) - throws when no room has the requested bed size")
    void testBookHotelBedSizeNotFound() {
        var kingBed = new BedSize("King", 210f, 200f);

        assertThrows(IllegalStateException.class,
                () -> customer.book(hotel, kingBed, startDate, endDate));
    }

    @Test
    @DisplayName("book() - number of nights and total price are calculated correctly")
    void testBookingPriceCalculation() {
        var booking = customer.book(hotel, startDate, endDate);

        assertEquals(4, booking.getNumberNights());
        assertEquals(480.0, booking.getTotalPrice(), 0.001);
    }

    @Test
    @DisplayName("book() - addPayment transitions booking to CONFIRMED")
    void testBookingConfirmedAfterPayment() {
        var booking = customer.book(hotel, startDate, endDate);
        var payment = customer.pay(booking.getTotalPrice(), booking.getTotalPrice(), PaymentMethod.CARD);
        booking.addPayment(payment);

        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertEquals(payment, booking.getPayment());
    }
}

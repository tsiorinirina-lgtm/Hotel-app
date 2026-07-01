package n1.hotel;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Manager {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Room room;
    private Map<Integer, Room> rooms = new HashMap<>();
    private Map<Integer, Booking> bookings = new HashMap<>();
    private Map<Integer, Payment> payments = new HashMap<>();

    public void addRoom(Room room) {
        if (this.room == null) {
            throw new IllegalArgumentException("The room cannot be null.");
        }
        if (this.rooms.containsKey(room.getRoomNumber())) {
            throw new IllegalArgumentException("A room with the number " + room.getRoomNumber() + " already exists.");
        }
        this.rooms.put(room.getRoomNumber(), room);
        System.out.println("Room " + room.getRoomNumber() + " added to the inventory.");
    }

    public void removeRoom(int roomNumber) {
        Room room = getRoomOrThrow(roomNumber);
        if (!room.isAvailable()) {
            throw new IllegalStateException(
                    "Impossible to delete the room " + roomNumber + ", it is currently occupied!");
        }
    }

    public void updateRoomStatus(int roomNumber, RoomStatus newStatus) {
        Room room = getRoomOrThrow(roomNumber);
        room.setStatus(newStatus);
        System.out.println("Room " + roomNumber + " status updated to " + newStatus);
    }

    public void approveBooking(int BookingId) {
        Booking booking = getBookingOrThrow(BookingId);
        booking.setStatus(BookingStatus.CONFIRMED);
        System.out.println("Booking " + BookingId + " has been confirmed.");
    }

    public void cancelBooking(int BookingId) {
        Booking booking = getBookingOrThrow(BookingId);
        booking.setStatus(BookingStatus.CANCELED);
        System.out.println("Booking " + BookingId + " has been cancelled.");
    }

    public void checkInCustomer(int BookingId) {
        Booking booking = getBookingOrThrow(BookingId);
        booking.setStatus(BookingStatus.CHECKED_IN);
        Room room = getRoomOrThrow(booking.getRoomNumber());
        room.setStatus(RoomStatus.OCCUPIED);
        System.out.println("The customer " + booking.getCustomer().getFirstName() + " "
                + booking.getCustomer().getLastName() + " has checked in to room " + booking.getRoomNumber() + ".");
    }

    public void checkOutCustomer(int BookingId) {
        Booking booking = getBookingOrThrow(BookingId);
        booking.setStatus(BookingStatus.CHECKED_OUT);
        Room room = getRoomOrThrow(booking.getRoomNumber());
        room.setStatus(RoomStatus.AVAILABLE);
        System.out.println("The customer " + booking.getCustomer().getFirstName() + " "
                + booking.getCustomer().getLastName() + " has checked out from room " + booking.getRoomNumber() + ".");
    }

    public List<Booking> viewAllBooking(LocalDate filterDate, BookingStatus filterStatus, String filterCustomerName) {
        return this.bookings.values().stream().filter(b -> filterDate == null || filterDate.equals(b.getBookingDate()))
                .filter(b -> filterStatus == null || b.getStatus() == filterStatus)
                .filter(b -> filterCustomerName == null ||
                        (b.getCustomerName() != null && b.getCustomerName().equalsIgnoreCase(filterCustomerName)))
                .collect(Collectors.toList());
    }

    public void processRefund(int paymentId) {
        Payment payment = getPaymentOrThrow(paymentId);
        payment.setStatus(PaymentStatus.REFUNDED);
        System.out.println("Payment " + paymentId + " has been refunded.");
    }

    public double getFinancialReport(Date start, Date end) {
        return this.payments.values().stream()
                .filter(p -> p.getPaymentDateTime() != null &&
                        !p.getPaymentDateTime().before(start) &&
                        !p.getPaymentDateTime().after(end))
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    public void verifyPayment(int PaymentId){
        Payment payment = getPaymentOrThrow(PaymentId);
            if(payment.getPaymentMethod () == PaymentMethod.CARD){
                System.out.println("Verifying bank transfer from the payment " + PaymentId + "...");
            }
            payment.setStatus(PaymentStatus.PAID);
            System.out.println("Payment " + PaymentId + " has been verified.");
        
    }

    public Payment getPaymentOrThrow(int payementId) {
        Payment payment = this.payments.get(payementId);
        if (payment == null) {
            throw new IllegalArgumentException("Payment #" + payementId + " does not exist.");
        }
        return payment;
    }

    public Booking getBookingOrThrow(int bookingId) {
        Booking bookings = this.bookings.get(bookingId);
        if (bookings == null) {
            throw new IllegalArgumentException("Booking #" + bookingId + " does not exist.");
        }
        return bookings;
    }

    private Room getRoomOrThrow(int roomNumber) {
        Room room = this.rooms.get(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room #" + roomNumber + " does not exist.");
        }
        return room;
    }

}

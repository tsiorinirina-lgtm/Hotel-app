package n1.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

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
    private Booking currBooking;

    String getFullName() {
        return firstName + " " + lastName;
    }

    public Payment pay(@NonNull Booking booking, @NonNull double amountPaid, PaymentMethod paymentMethod) {
        if (amountPaid < 0) {
            throw new IllegalArgumentException("Amount paid cannot be negative.");
        }

        PaymentStatus status;
        if (amountPaid >= booking.getTotalPrice()) {
            status = PaymentStatus.PAID;
        } else if (amountPaid > 0) {
            status = PaymentStatus.PENDING;
        } else {
            status = PaymentStatus.FAILED;
        }

        return new Payment(
                UUID.randomUUID().toString(),
                status,
                paymentMethod,
                LocalDateTime.now(),
                amountPaid,
                booking.getTotalPrice()
        );
    }
    Booking book(@NonNull Hotel hotel, @NonNull LocalDateTime dateStart, @NonNull LocalDateTime dateEnd, @NonNull RoomType roomType, @NonNull int numberPeople) {
        var room = hotel.getAvailableRooms().getFirst();
        int nextId = hotel.getBookings().getLast().getId() + 1;
        var booking = new Booking(nextId, hotel, room, this, BookingStatus.PENDING, numberPeople, dateStart, dateEnd);
        setCurrBooking(booking);
        return booking;
    }

    Booking book(@NonNull Hotel hotel, @NonNull LocalDateTime dateStart, @NonNull LocalDateTime dateEnd, @NonNull double budget, @NonNull int numberPeople) {
        var room = hotel.getAvailableRooms().stream().filter(r -> r.getPrice() * ChronoUnit.DAYS.between(dateStart.toLocalDate(), dateEnd.toLocalDate()) < budget).findFirst().orElse(null);
        int nextId = hotel.getBookings().getLast().getId() + 1;
                if(room == null) {return null;}
                var booking = new Booking(nextId, hotel,  room, this, BookingStatus.PENDING, numberPeople, dateStart, dateEnd);
                setCurrBooking(booking);
                return booking;
    }
}

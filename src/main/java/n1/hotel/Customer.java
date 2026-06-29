package n1.hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
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

    String getFullName() {
        return firstName + " " + lastName;
    }

    public Payment pay(double amountCharged, double amountPaid, PaymentMethod paymentMethod) {
        if (amountCharged <= 0) {
            throw new IllegalArgumentException("Amount charged must be positive.");
        }
        if (amountPaid < 0) {
            throw new IllegalArgumentException("Amount paid cannot be negative.");
        }

        PaymentStatus status;
        if (amountPaid >= amountCharged) {
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
                amountCharged
        );
    }

    private static int nextBookingId = 1;

    public Booking book(Hotel hotel, LocalDateTime startDate, LocalDateTime endDate) {
        return bookInternal(hotel, -1, 1, null, startDate, endDate);
    }

    public Booking book(Hotel hotel, double maxPrice, LocalDateTime startDate, LocalDateTime endDate) {
        return bookInternal(hotel, maxPrice, 1, null, startDate, endDate);
    }

    public Booking book(Hotel hotel, int numberOfPeople, LocalDateTime startDate, LocalDateTime endDate) {
        return bookInternal(hotel, -1, numberOfPeople, null, startDate, endDate);
    }

    public Booking book(Hotel hotel, BedSize bedSize, LocalDateTime startDate, LocalDateTime endDate) {
        return bookInternal(hotel, -1, 1, bedSize, startDate, endDate);
    }

    private Booking bookInternal(Hotel hotel, double maxPrice, int numberOfPeople,
                                  BedSize bedSize, LocalDateTime startDate, LocalDateTime endDate) {
        if (hotel == null) {
            throw new IllegalArgumentException("Hotel cannot be null.");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null.");
        }
        if (!endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("End date must be after start date.");
        }
        if (numberOfPeople < 1) {
            throw new IllegalArgumentException("Number of people must be at least 1.");
        }

        List<Room> candidates = hotel.getAvailableRooms();

        Room chosen = candidates.stream()
                .filter(r -> r.getCapacity() >= numberOfPeople)
                .filter(r -> maxPrice < 0 || r.getPrice() <= maxPrice)
                .filter(r -> bedSize == null || r.getBedSize().equals(bedSize))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No available room matches the requested criteria in hotel: " + hotel.getName()));

        return new Booking(
                nextBookingId++,
                hotel,
                chosen,
                this,
                BookingStatus.PENDING,
                numberOfPeople,
                startDate,
                endDate
        );
    }
}

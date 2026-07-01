package n1.hotel;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Getter
@Setter

public class Booking {
    @NonNull private int id;
    @NonNull private Hotel hotel;
    @NonNull private Room room;
    private int roomNumber;
    @NonNull private Customer customer;
    @NonNull private BookingStatus status;
    @NonNull private int numberPeople;
    @NonNull private LocalDateTime StartDate;
    @NonNull private LocalDateTime EndDate;
    private Payment payment;
    private LocalDate bookingDate;
    private String customerName;

    int getNumberNights() {
        return (int) ChronoUnit.DAYS.between(StartDate.toLocalDate(), EndDate.toLocalDate());
    }

    double getTotalPrice() {
        return room.getPrice() * getNumberNights();
    }

    void addPayment(Payment payment) {
        this.payment = payment;
        this.status = BookingStatus.CONFIRMED;
    }
}

package n1.hotel;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Getter

public class Booking {
    @NonNull private int id;
    @NonNull private Hotel hotel;
    @NonNull private Room room;
    @NonNull private BookingStatus status;
    @NonNull private int numberPeople;
    @NonNull private LocalDateTime StartDate;
    @NonNull private LocalDateTime EndDate;
    private Payment payment;

    int getNumberNights() {
        return (int) ChronoUnit.DAYS.between(StartDate, EndDate);
    }

    double getTotalPrice() {
        return room.getPrice() * getNumberNights();
    }

    void addPayment(Payment payment) {
        this.payment = payment;
        this.status = BookingStatus.CONFIRMED;
    }
}

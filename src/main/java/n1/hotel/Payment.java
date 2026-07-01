package n1.hotel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode

public class Payment {
@NonNull private String id;
@NonNull private PaymentStatus status;
@NonNull private PaymentMethod paymentMethod;
@NonNull private LocalDateTime paymentDate;
private Date paymentDateTime;
private double amount;
@NonNull private double amountPaid;
@NonNull private double amountCharged;
private double amountDue = amountCharged;

void setAmountPaid(double amountPaid){
    this.amountPaid = amountPaid;
    this.amountDue = amountCharged - amountPaid;
}

boolean isFullyPaid(){
    return amountPaid >= amountCharged;
}

boolean isPartiallyPaid(){
    return amountPaid < amountCharged && amountPaid > 0;
}
}

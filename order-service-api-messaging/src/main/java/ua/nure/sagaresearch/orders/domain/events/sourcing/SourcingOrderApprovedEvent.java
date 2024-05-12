package ua.nure.sagaresearch.orders.domain.events.sourcing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.orders.domain.events.OrderDetails;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SourcingOrderApprovedEvent implements SourcingOrderEvent {
    private OrderDetails orderDetails;
}

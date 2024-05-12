package ua.nure.sagaresearch.orders.domain.events.sourcing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.orders.domain.events.OrderDetails;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SourcingOrderCancelledEvent implements SourcingOrderEvent {
    private OrderDetails orderDetails;
}

package ua.nure.sagaresearch.orders.domain.events.sourcing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SourcingOrderCancellationRequestedEvent implements SourcingOrderEvent {
    private String productId;
    private long quantity;
}

package ua.nure.sagaresearch.orders.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Getter
public class OrderCancellationRequestedEvent implements OrderEvent {

    private Map<Long, ProductOrderEntry> productEntries;

    public OrderCancellationRequestedEvent(Map<Long, ProductOrderEntry> productEntries) {
        this.productEntries = productEntries;
    }
}

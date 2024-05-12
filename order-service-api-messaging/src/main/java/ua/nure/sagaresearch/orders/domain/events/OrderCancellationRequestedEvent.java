package ua.nure.sagaresearch.orders.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.product.ProductOrderEntry;

import java.util.Map;

@NoArgsConstructor
@Getter
public class OrderCancellationRequestedEvent implements OrderEvent {

    private Map<String, ProductOrderEntry> productEntries;

    public OrderCancellationRequestedEvent(Map<String, ProductOrderEntry> productEntries) {
        this.productEntries = productEntries;
    }
}

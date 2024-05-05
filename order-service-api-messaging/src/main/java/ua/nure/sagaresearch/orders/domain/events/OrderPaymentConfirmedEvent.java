package ua.nure.sagaresearch.orders.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Getter
public class OrderPaymentConfirmedEvent implements OrderEvent {

    private Map<String, ProductOrderEntry> productEntries;

    public OrderPaymentConfirmedEvent(Map<String, ProductOrderEntry> productEntries) {
        this.productEntries = productEntries;
    }
}

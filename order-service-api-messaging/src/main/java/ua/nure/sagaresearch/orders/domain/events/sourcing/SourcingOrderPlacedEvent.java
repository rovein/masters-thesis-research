package ua.nure.sagaresearch.orders.domain.events.sourcing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.common.domain.basket.ProductBasketEntry;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SourcingOrderPlacedEvent implements SourcingOrderEvent {
    private String basketId;
    private Money totalPrice;
    private Map<String, ProductBasketEntry> productEntries;
}

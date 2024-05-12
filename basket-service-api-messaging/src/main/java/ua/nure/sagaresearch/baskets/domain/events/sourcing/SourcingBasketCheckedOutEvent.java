package ua.nure.sagaresearch.baskets.domain.events.sourcing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.common.domain.basket.ProductBasketEntry;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SourcingBasketCheckedOutEvent implements SourcingBasketEvent {
    private String orderId;
    private Money totalPrice;
    private Map<String, ProductBasketEntry> productEntries;
}

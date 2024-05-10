package ua.nure.sagaresearch.baskets.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.common.domain.basket.ProductBasketEntry;

import java.util.Map;

@NoArgsConstructor
@Getter
public class BasketCheckedOutEvent extends AbstractBasketOrderEvent {
    private Money totalPrice;
    private Map<String, ProductBasketEntry> productEntries;

    public BasketCheckedOutEvent(Long orderId, Money totalPrice, Map<String, ProductBasketEntry> productEntries) {
        super(orderId);
        this.totalPrice = totalPrice;
        this.productEntries = productEntries;
    }
}

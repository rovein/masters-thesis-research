package ua.nure.sagaresearch.baskets.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

import java.util.Map;

@NoArgsConstructor
@Getter
public class BasketCheckedOutEvent extends AbstractBasketOrderEvent {
    private Money totalPrice;
    private Map<Long, ProductBasketEntry> productEntries;

    public BasketCheckedOutEvent(Long orderId, Money totalPrice, Map<Long, ProductBasketEntry> productEntries) {
        super(orderId);
        this.totalPrice = totalPrice;
        this.productEntries = productEntries;
    }
}
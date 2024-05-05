package ua.nure.sagaresearch.baskets.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@NoArgsConstructor
@Getter
public class ProductAddedToBasketEvent extends AbstractBasketProductEvent {
    private Money pricePerUnit;

    public ProductAddedToBasketEvent(String productId, Money pricePerUnit) {
        super(productId);
        this.pricePerUnit = pricePerUnit;
    }
}

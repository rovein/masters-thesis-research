package ua.nure.sagaresearch.baskets.domain.events.sourcing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@NoArgsConstructor
@Getter
public class SourcingProductAddedToBasketEvent implements SourcingBasketEvent {
    private String productId;
    private Long quantity;
    private Money pricePerUnit;

    public SourcingProductAddedToBasketEvent(String productId, Long quantity, Money pricePerUnit) {
        this.productId = productId;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }
}

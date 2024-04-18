package ua.nure.sagaresearch.baskets.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@NoArgsConstructor
@Getter
public class ProductAddedToBasketEvent extends AbstractBasketProductEvent {
    private Long totalProductQuantity;
    private Long quantity;
    private Money pricePerUnit;

    public ProductAddedToBasketEvent(Long productId, Long totalProductQuantity, Long quantity, Money pricePerUnit) {
        super(productId);
        this.totalProductQuantity = totalProductQuantity;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }
}

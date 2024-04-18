package nure.ua.sagaresearch.products.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@Getter
@NoArgsConstructor
public class ProductBasketAdditionValidationFailedEvent extends AbstractProductBasketEvent {

    private Long quantity;
    private Money pricePerUnit;

    public ProductBasketAdditionValidationFailedEvent(Long basketId, Long quantity, Money pricePerUnit) {
        super(basketId);
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

}

package nure.ua.sagaresearch.products.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductBasketAdditionValidationFailedEvent extends AbstractProductBasketEvent {

    public ProductBasketAdditionValidationFailedEvent(Long basketId) {
        super(basketId);
    }

}

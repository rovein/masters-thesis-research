package nure.ua.sagaresearch.products.domain.events;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductBasketAdditionValidatedEvent extends AbstractProductBasketEvent {

    public ProductBasketAdditionValidatedEvent(Long basketId) {
        super(basketId);
    }

}

package nure.ua.sagaresearch.products.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductQuantityUpdatedEvent extends AbstractProductOrderEvent {
    public ProductQuantityUpdatedEvent(Long orderId) {
        super(orderId);
    }
}

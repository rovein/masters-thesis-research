package nure.ua.sagaresearch.products.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductQuantityRestoredEvent extends AbstractProductOrderEvent {
    public ProductQuantityRestoredEvent(Long orderId) {
        super(orderId);
    }
}

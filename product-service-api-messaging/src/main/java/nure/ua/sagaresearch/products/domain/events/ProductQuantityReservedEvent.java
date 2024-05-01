package nure.ua.sagaresearch.products.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductQuantityReservedEvent extends AbstractProductOrderEvent {
    public ProductQuantityReservedEvent(Long orderId) {
        super(orderId);
    }
}

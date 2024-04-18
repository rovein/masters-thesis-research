package ua.nure.sagaresearch.orders.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderPlacedEvent extends AbstractOrderBasketEvent {

    public OrderPlacedEvent(Long basketId) {
        super(basketId);
    }
}

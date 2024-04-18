package ua.nure.sagaresearch.orders.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderPlacementRequestedEvent extends AbstractOrderBasketEvent {

    public OrderPlacementRequestedEvent(Long basketId) {
        super(basketId);
    }
}

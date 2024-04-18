package ua.nure.sagaresearch.baskets.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class AbstractBasketOrderEvent implements BasketEvent {
    protected Long orderId;
}

package ua.nure.sagaresearch.baskets.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class AbstractBasketProductEvent implements BasketEvent {
    protected Long productId;
}

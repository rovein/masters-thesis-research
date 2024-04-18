package ua.nure.sagaresearch.orders.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class AbstractOrderBasketEvent implements OrderEvent {
    protected Long basketId;
}

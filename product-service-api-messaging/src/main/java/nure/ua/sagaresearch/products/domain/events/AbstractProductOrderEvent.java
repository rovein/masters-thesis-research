package nure.ua.sagaresearch.products.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class AbstractProductOrderEvent implements ProductEvent {
    protected Long orderId;
}

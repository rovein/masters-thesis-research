package ua.nure.sagaresearch.baskets.domain.event;

import io.eventuate.Event;
import io.eventuate.EventUtil;
import io.eventuate.ReflectiveMutableCommandProcessingAggregate;
import lombok.Getter;
import lombok.Setter;
import ua.nure.sagaresearch.baskets.domain.events.ProductBasketEntry;
import ua.nure.sagaresearch.baskets.domain.events.sourcing.SourcingBasketCreatedEvent;
import ua.nure.sagaresearch.common.domain.Money;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Basket extends ReflectiveMutableCommandProcessingAggregate<Basket, BasketCommand> {

    private Long totalQuantity;

    private Money totalPrice;

    private Map<Long, ProductBasketEntry> productEntries;

    private Long userId;

    public List<Event> process(CreateBasketCommand cmd) {
        return EventUtil.events(new SourcingBasketCreatedEvent());
    }

    public void apply(SourcingBasketCreatedEvent event) {
        this.totalQuantity = 0L;
        this.totalPrice = Money.ZERO;
        this.productEntries = Collections.emptyMap();
    }
}

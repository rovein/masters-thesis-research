package ua.nure.sagaresearch.baskets.domain.event;

import static ua.nure.sagaresearch.baskets.util.BasketServiceUtil.addProductEntryToBasket;

import io.eventuate.Event;
import io.eventuate.EventUtil;
import io.eventuate.ReflectiveMutableCommandProcessingAggregate;
import lombok.Getter;
import lombok.Setter;
import ua.nure.sagaresearch.baskets.domain.BaseBasket;
import ua.nure.sagaresearch.baskets.domain.events.ProductBasketEntry;
import ua.nure.sagaresearch.baskets.domain.events.sourcing.SourcingBasketCreatedEvent;
import ua.nure.sagaresearch.baskets.domain.events.sourcing.SourcingProductAddedToBasketEvent;
import ua.nure.sagaresearch.common.domain.Money;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Basket extends ReflectiveMutableCommandProcessingAggregate<Basket, BasketCommand> implements BaseBasket {

    private Long totalQuantity;

    private Money totalPrice;

    private Map<String, ProductBasketEntry> productEntries;

    public List<Event> process(CreateBasketCommand cmd) {
        return EventUtil.events(new SourcingBasketCreatedEvent());
    }

    public void apply(SourcingBasketCreatedEvent event) {
        this.totalQuantity = 0L;
        this.totalPrice = Money.ZERO;
        this.productEntries = new HashMap<>();
    }

    public List<Event> process(AddProductToBasketCommand cmd) {
        return EventUtil.events(
                new SourcingProductAddedToBasketEvent(cmd.getProductId(), cmd.getQuantity(), cmd.getPricePerUnit()));
    }

    public void apply(SourcingProductAddedToBasketEvent event) {
        addProductEntryToBasket(this, event.getProductId(), event.getQuantity(), event.getPricePerUnit());
    }
}

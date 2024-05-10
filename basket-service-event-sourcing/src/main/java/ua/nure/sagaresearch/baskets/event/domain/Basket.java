package ua.nure.sagaresearch.baskets.event.domain;

import static ua.nure.sagaresearch.common.util.BasketServiceUtil.addProductEntryToBasket;
import static ua.nure.sagaresearch.common.util.BasketServiceUtil.updateProductEntryPrice;
import static ua.nure.sagaresearch.common.util.LoggingUtils.EVENT_SOURCING_ADD_PRODUCT_TO_BASKET_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.logAggregateProcessMethod;

import io.eventuate.Event;
import io.eventuate.EventUtil;
import io.eventuate.ReflectiveMutableCommandProcessingAggregate;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.sagaresearch.baskets.domain.events.sourcing.SourcingBasketCreatedEvent;
import ua.nure.sagaresearch.baskets.domain.events.sourcing.SourcingBasketProductPriceUpdatedEvent;
import ua.nure.sagaresearch.baskets.domain.events.sourcing.SourcingBasketProductValidationHandledEvent;
import ua.nure.sagaresearch.baskets.domain.events.sourcing.SourcingProductAddedToBasketEvent;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.common.domain.basket.BaseBasket;
import ua.nure.sagaresearch.common.domain.basket.ProductBasketEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Basket extends ReflectiveMutableCommandProcessingAggregate<Basket, BasketCommand> implements BaseBasket {

    private static final Logger LOGGER = LoggerFactory.getLogger(Basket.class);

    private Long totalQuantity;

    private Money totalPrice;

    private Map<String, ProductBasketEntry> productEntries;

    public List<Event> process(CreateBasketCommand cmd) {
        SourcingBasketCreatedEvent event = new SourcingBasketCreatedEvent();
        logAggregateProcessMethod(LOGGER, this.getClass(), cmd, event, EVENT_SOURCING_ADD_PRODUCT_TO_BASKET_PREFIX);
        return EventUtil.events(event);
    }

    public void apply(SourcingBasketCreatedEvent event) {
        this.totalQuantity = 0L;
        this.totalPrice = Money.ZERO;
        this.productEntries = new HashMap<>();
    }

    public List<Event> process(AddProductToBasketCommand cmd) {
        SourcingProductAddedToBasketEvent event = new SourcingProductAddedToBasketEvent(cmd.getProductId(), cmd.getQuantity(), cmd.getPricePerUnit());
        logAggregateProcessMethod(LOGGER, this.getClass(), cmd, event, EVENT_SOURCING_ADD_PRODUCT_TO_BASKET_PREFIX);
        return EventUtil.events(event);
    }

    public void apply(SourcingProductAddedToBasketEvent event) {
        addProductEntryToBasket(this, event.getProductId(), event.getQuantity(), event.getPricePerUnit());
    }

    public List<Event> process(HandleProductValidationCommand cmd) {
        SourcingBasketProductValidationHandledEvent event = new SourcingBasketProductValidationHandledEvent(cmd.getProductId());
        logAggregateProcessMethod(LOGGER, this.getClass(), cmd, event, EVENT_SOURCING_ADD_PRODUCT_TO_BASKET_PREFIX);
        return EventUtil.events(event);
    }

    public void apply(SourcingBasketProductValidationHandledEvent event) {

    }

    public List<Event> process(UpdateBasketProductPriceCommand cmd) {
        SourcingBasketProductPriceUpdatedEvent event = new SourcingBasketProductPriceUpdatedEvent(cmd.getProductId(), cmd.getActualPricePerUnit());
        logAggregateProcessMethod(LOGGER, this.getClass(), cmd, event, EVENT_SOURCING_ADD_PRODUCT_TO_BASKET_PREFIX);
        return EventUtil.events(event);
    }

    public void apply(SourcingBasketProductPriceUpdatedEvent event) {
        updateProductEntryPrice(this, event.getProductId(), event.getActualPricePerUnit());
    }
}

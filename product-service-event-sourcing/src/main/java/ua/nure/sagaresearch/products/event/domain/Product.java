package ua.nure.sagaresearch.products.event.domain;

import static ua.nure.sagaresearch.common.util.LoggingUtils.EVENT_SOURCING_ADD_PRODUCT_TO_BASKET_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.logAggregateProcessMethod;

import io.eventuate.Event;
import io.eventuate.EventUtil;
import io.eventuate.ReflectiveMutableCommandProcessingAggregate;
import lombok.Getter;
import lombok.Setter;
import nure.ua.sagaresearch.products.domain.events.sourcing.SourcingProductBasketAdditionValidatedEvent;
import nure.ua.sagaresearch.products.domain.events.sourcing.SourcingProductBasketPriceHasChangedEvent;
import nure.ua.sagaresearch.products.domain.events.sourcing.SourcingProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.common.domain.product.ProductProperty;

import javax.persistence.ElementCollection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class Product extends ReflectiveMutableCommandProcessingAggregate<Product, ProductCommand> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Product.class);

    private String productName;

    private String description;

    private String image;

    private Money productPrice;

    private Long productQuantity;

    @ElementCollection
    private Map<Long, ProductProperty> productProperties;

    public List<Event> process(CreateProductCommand cmd) {
        return EventUtil.events(new SourcingProductCreatedEvent(
                cmd.getProductName(),
                cmd.getDescription(),
                cmd.getImage(),
                cmd.getProductPrice(),
                cmd.getProductQuantity()
        ));
    }

    public void apply(SourcingProductCreatedEvent event) {
        this.productName = event.getProductName();
        this.description = event.getDescription();
        this.image = event.getImage();
        this.productPrice = event.getProductPrice();
        this.productQuantity = event.getProductQuantity();
        this.productProperties = Collections.emptyMap();
    }

    public List<Event> process(ValidateProductAddedToBasketCommand cmd) {
        String basketId = cmd.getBasketId();
        Money priceFromBasket = cmd.getPricePerUnit();
        Event event = Objects.equals(priceFromBasket, productPrice)
                ? new SourcingProductBasketAdditionValidatedEvent(basketId)
                : new SourcingProductBasketPriceHasChangedEvent(basketId, productPrice);
        logAggregateProcessMethod(LOGGER, this.getClass(), cmd, event, EVENT_SOURCING_ADD_PRODUCT_TO_BASKET_PREFIX);
        return EventUtil.events(event);
    }

    public void apply(SourcingProductBasketAdditionValidatedEvent event) {
    }

    public void apply(SourcingProductBasketPriceHasChangedEvent event) {
    }
}

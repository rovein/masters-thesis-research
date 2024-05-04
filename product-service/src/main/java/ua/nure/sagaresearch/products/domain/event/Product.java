package ua.nure.sagaresearch.products.domain.event;

import io.eventuate.Event;
import io.eventuate.EventUtil;
import io.eventuate.ReflectiveMutableCommandProcessingAggregate;
import lombok.Getter;
import lombok.Setter;
import nure.ua.sagaresearch.products.domain.events.sourcing.SourcingProductCreatedEvent;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.products.domain.ProductProperty;

import javax.persistence.ElementCollection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Product extends ReflectiveMutableCommandProcessingAggregate<Product, ProductCommand> {

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
}

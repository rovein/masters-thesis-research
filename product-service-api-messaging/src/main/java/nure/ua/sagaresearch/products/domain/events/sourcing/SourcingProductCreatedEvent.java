package nure.ua.sagaresearch.products.domain.events.sourcing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SourcingProductCreatedEvent implements SourcingProductEvent {
    private String productName;
    private String description;
    private String image;
    private Money productPrice;
    private Long productQuantity;
}

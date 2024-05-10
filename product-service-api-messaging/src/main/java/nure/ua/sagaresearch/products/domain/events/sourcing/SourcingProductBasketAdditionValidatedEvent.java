package nure.ua.sagaresearch.products.domain.events.sourcing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SourcingProductBasketAdditionValidatedEvent implements SourcingProductEvent {
    private String basketId;
}

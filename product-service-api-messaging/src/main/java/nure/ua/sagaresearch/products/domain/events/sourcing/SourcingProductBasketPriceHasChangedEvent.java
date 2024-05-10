package nure.ua.sagaresearch.products.domain.events.sourcing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SourcingProductBasketPriceHasChangedEvent implements SourcingProductEvent {
    private String basketId;
    private Money actualPricePerUnit;
}

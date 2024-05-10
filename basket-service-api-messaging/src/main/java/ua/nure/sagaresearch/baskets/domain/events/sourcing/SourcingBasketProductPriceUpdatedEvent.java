package ua.nure.sagaresearch.baskets.domain.events.sourcing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SourcingBasketProductPriceUpdatedEvent implements SourcingBasketEvent {
    private String productId;
    private Money actualPricePerUnit;
}

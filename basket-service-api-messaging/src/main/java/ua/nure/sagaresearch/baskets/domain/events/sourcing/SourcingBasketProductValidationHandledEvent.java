package ua.nure.sagaresearch.baskets.domain.events.sourcing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SourcingBasketProductValidationHandledEvent implements SourcingBasketEvent {
    private String productId;
}

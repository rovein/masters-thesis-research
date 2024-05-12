package ua.nure.sagaresearch.baskets.domain.events.sourcing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SourcingBasketClearedEvent implements SourcingBasketEvent {
    private String orderId;
}

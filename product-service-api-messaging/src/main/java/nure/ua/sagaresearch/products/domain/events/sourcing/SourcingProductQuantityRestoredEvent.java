package nure.ua.sagaresearch.products.domain.events.sourcing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SourcingProductQuantityRestoredEvent implements SourcingProductEvent {
    private String orderId;
    private long quantity;
}

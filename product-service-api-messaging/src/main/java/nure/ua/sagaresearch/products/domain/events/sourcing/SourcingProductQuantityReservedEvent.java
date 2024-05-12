package nure.ua.sagaresearch.products.domain.events.sourcing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SourcingProductQuantityReservedEvent implements SourcingProductEvent {
    private String orderId;
    private long quantity;
}

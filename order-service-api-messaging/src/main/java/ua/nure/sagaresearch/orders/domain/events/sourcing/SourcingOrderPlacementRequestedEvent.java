package ua.nure.sagaresearch.orders.domain.events.sourcing;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SourcingOrderPlacementRequestedEvent implements SourcingOrderEvent {
    private String basketId;
    private String shippingType;
    private String paymentType;
    private String shippingAddress;

    public SourcingOrderPlacementRequestedEvent(String basketId, String shippingType, String paymentType, String shippingAddress) {
        this.basketId = basketId;
        this.shippingType = shippingType;
        this.paymentType = paymentType;
        this.shippingAddress = shippingAddress;
    }
}

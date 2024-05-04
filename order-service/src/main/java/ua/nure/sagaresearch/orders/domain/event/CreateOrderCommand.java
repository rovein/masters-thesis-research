package ua.nure.sagaresearch.orders.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateOrderCommand implements OrderCommand {
    private String basketId;
    private String shippingType;
    private String paymentType;
    private String shippingAddress;
}

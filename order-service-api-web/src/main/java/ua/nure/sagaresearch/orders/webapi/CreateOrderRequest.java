package ua.nure.sagaresearch.orders.webapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateOrderRequest {
    private String basketId;
    private String shippingType;
    private String paymentType;
    private String shippingAddress;
}

package ua.nure.sagaresearch.orders.webapi;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.orders.domain.events.OrderState;
import ua.nure.sagaresearch.common.domain.product.ProductOrderEntry;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetOrderResponse {
    private String orderId;
    private OrderState orderState;
    private String basketId;
    private Money totalPrice;
    private Collection<ProductOrderEntry> productEntries;
}

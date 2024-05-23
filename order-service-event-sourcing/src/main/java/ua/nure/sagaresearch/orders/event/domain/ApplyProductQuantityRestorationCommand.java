package ua.nure.sagaresearch.orders.event.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApplyProductQuantityRestorationCommand implements OrderCommand {
    private String orderId;
    private String productId;
}

package ua.nure.sagaresearch.experiments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.orders.domain.events.OrderState;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderViewDto {
    private String orderId;
    private OrderState orderState;
}

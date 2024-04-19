package ua.nure.sagaresearch.orders.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderApprovedEvent implements OrderEvent {
    private OrderDetails orderDetails;
}

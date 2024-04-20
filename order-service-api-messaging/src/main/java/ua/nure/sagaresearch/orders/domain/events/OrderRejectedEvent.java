package ua.nure.sagaresearch.orders.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderRejectedEvent implements OrderEvent {
  private OrderDetails orderDetails;
}

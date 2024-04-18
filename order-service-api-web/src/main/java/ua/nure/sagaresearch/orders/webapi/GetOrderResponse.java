package ua.nure.sagaresearch.orders.webapi;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.orders.domain.events.OrderState;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetOrderResponse {
  private Long orderId;
  private OrderState orderState;
  private Long basketId;
  private Money totalPrice;
}

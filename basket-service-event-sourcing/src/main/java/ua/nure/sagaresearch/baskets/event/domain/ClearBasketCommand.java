package ua.nure.sagaresearch.baskets.event.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ClearBasketCommand implements BasketCommand {
    private String orderId;
}

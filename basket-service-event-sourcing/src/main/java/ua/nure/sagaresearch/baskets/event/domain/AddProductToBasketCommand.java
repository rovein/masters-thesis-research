package ua.nure.sagaresearch.baskets.event.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddProductToBasketCommand implements BasketCommand {
    private String productId;
    private Long quantity;
    private Money pricePerUnit;
}

package ua.nure.sagaresearch.baskets.event.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateBasketProductPriceCommand implements BasketCommand {
    private String productId;
    private Money actualPricePerUnit;
}

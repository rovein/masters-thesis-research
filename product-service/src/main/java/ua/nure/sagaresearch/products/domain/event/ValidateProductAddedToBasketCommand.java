package ua.nure.sagaresearch.products.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateProductAddedToBasketCommand implements ProductCommand {
    private String basketId;
    private Money pricePerUnit;
}

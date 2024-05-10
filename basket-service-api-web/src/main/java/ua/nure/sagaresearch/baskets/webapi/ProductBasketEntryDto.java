package ua.nure.sagaresearch.baskets.webapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductBasketEntryDto {
    private String productId;
    private Long quantity;
    private Money price;
}

package ua.nure.sagaresearch.baskets.webapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BasketDtoResponse {
    private Long basketId;
    private Long totalQuantity;
    private Money totalPrice;
    private List<ProductBasketEntryDto> productBasketEntries;
}

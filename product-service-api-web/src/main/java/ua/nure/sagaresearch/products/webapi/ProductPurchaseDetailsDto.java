package ua.nure.sagaresearch.products.webapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductPurchaseDetailsDto {
    private String productId;
    private Long quantity;
    private Money pricePerUnit;
}

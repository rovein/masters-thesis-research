package ua.nure.sagaresearch.products.webapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    private String productName;
    private String description;
    private String image;
    private Money productPrice;
    private Long productQuantity;
}

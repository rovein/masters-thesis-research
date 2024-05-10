package ua.nure.sagaresearch.products.event.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ua.nure.sagaresearch.common.domain.Money;

@Getter
@AllArgsConstructor
public class CreateProductCommand implements ProductCommand {
    private String productName;
    private String description;
    private String image;
    private Money productPrice;
    private Long productQuantity;
}

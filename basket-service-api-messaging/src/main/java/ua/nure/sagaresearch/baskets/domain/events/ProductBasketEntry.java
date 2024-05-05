package ua.nure.sagaresearch.baskets.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.nure.sagaresearch.common.domain.Money;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductBasketEntry {
    private String productId;

    private Long quantity;

    @Embedded
    private Money price;

    public void increaseQuantity(Long value) {
        this.quantity += value;
    }
}

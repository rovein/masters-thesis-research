package ua.nure.sagaresearch.orders.domain.events;

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
public class ProductOrderEntry {
    private String productId;

    private Long quantity;

    @Embedded
    private Money price;
}

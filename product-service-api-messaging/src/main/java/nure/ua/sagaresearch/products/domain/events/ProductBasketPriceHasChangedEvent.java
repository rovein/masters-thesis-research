package nure.ua.sagaresearch.products.domain.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.nure.sagaresearch.common.domain.Money;

@Getter
@NoArgsConstructor
public class ProductBasketPriceHasChangedEvent extends AbstractProductBasketEvent {

    private Money actualPricePerUnit;

    public ProductBasketPriceHasChangedEvent(Long basketId, Money actualPricePerUnit) {
        super(basketId);
        this.actualPricePerUnit = actualPricePerUnit;
    }
}

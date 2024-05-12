package ua.nure.sagaresearch.orders.event.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.common.domain.basket.ProductBasketEntry;

import java.util.Map;

@Getter
@AllArgsConstructor
public class FillOrderWithProductEntriesCommand implements OrderCommand {
    private String basketId;
    private Money totalPrice;
    private Map<String, ProductBasketEntry> productEntries;
}

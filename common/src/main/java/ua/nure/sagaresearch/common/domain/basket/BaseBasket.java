package ua.nure.sagaresearch.common.domain.basket;

import ua.nure.sagaresearch.common.domain.Money;

import java.util.Map;

public interface BaseBasket {
    Map<String, ProductBasketEntry> getProductEntries();

    void setTotalQuantity(Long totalQuantity);

    void setTotalPrice(Money totalPrice);
}

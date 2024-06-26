package ua.nure.sagaresearch.common.util;

import ua.nure.sagaresearch.common.domain.basket.BaseBasket;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.common.domain.basket.ProductBasketEntry;

import java.util.Map;

public final class BasketServiceUtil {

    public static void addProductEntryToBasket(BaseBasket basket, String productId, Long quantity, Money pricePerUnit) {
        Map<String, ProductBasketEntry> productEntries = basket.getProductEntries();
        productEntries.compute(productId, (k, oldEntry) -> createNewOrUpdateProductEntry(oldEntry, productId, quantity, pricePerUnit));
        resetTotalPriceAndQuantity(basket);
    }

    public static void resetTotalPriceAndQuantity(BaseBasket basket) {
        Map<String, ProductBasketEntry> productEntries = basket.getProductEntries();
        basket.setTotalPrice(calculateTotalPrice(productEntries));
        basket.setTotalQuantity(calculateTotalQuantity(productEntries));
    }

    public static void updateProductEntryPrice(BaseBasket basket, String productId, Money actualPricePerUnit) {
        basket.getProductEntries().computeIfPresent(productId, (key, value) -> {
            Long quantity = value.getQuantity();
            value.setPrice(actualPricePerUnit.multiply(quantity));
            return value;
        });
        resetTotalPriceAndQuantity(basket);
    }

    private static ProductBasketEntry createNewOrUpdateProductEntry(ProductBasketEntry oldEntry, String productId, Long quantity, Money pricePerUnit) {
        if (oldEntry == null) {
            ProductBasketEntry newEntry = new ProductBasketEntry(productId, quantity, pricePerUnit);
            newEntry.setPrice(pricePerUnit.multiply(quantity));
            return newEntry;
        }
        oldEntry.increaseQuantity(quantity);
        oldEntry.setPrice(pricePerUnit.multiply(oldEntry.getQuantity()));
        return new ProductBasketEntry(productId, oldEntry.getQuantity(), oldEntry.getPrice());
    }

    private static long calculateTotalQuantity(Map<String, ProductBasketEntry> productEntries) {
        return productEntries.values().stream().mapToLong(ProductBasketEntry::getQuantity).sum();
    }

    private static Money calculateTotalPrice(Map<String, ProductBasketEntry> productEntries) {
        return productEntries.values().stream().map(ProductBasketEntry::getPrice).reduce(Money.ZERO, Money::add);
    }

    private BasketServiceUtil() {
    }
}

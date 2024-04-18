package ua.nure.sagaresearch.baskets.service;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.nure.sagaresearch.baskets.domain.Basket;
import ua.nure.sagaresearch.baskets.domain.BasketRepository;
import ua.nure.sagaresearch.baskets.domain.ProductBasketEntry;
import ua.nure.sagaresearch.baskets.domain.events.ProductAddedToBasketEvent;
import ua.nure.sagaresearch.common.domain.Money;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Map;

@Service
public class BasketService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private BasketRepository basketRepository;

    private DomainEventPublisher domainEventPublisher;

    public BasketService(BasketRepository basketRepository, DomainEventPublisher domainEventPublisher) {
        this.basketRepository = basketRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    public Basket createBasket() {
        return basketRepository.save(new Basket());
    }

    public Basket getBasket(Long basketId) {
        return basketRepository.findById(basketId).orElseThrow();
    }

    @Transactional
    public Basket addProductToBasket(Long basketId, Long productId, Long quantity, Money pricePerUnit) {
        logger.info("Adding product {} for basket {}", productId, basketId);

        Basket basket = basketRepository.findById(basketId).orElseThrow();

        Map<Long, ProductBasketEntry> productEntries = basket.getProductEntries();
        productEntries.compute(productId, (k, oldEntry) -> {
            if (oldEntry == null) {
                ProductBasketEntry newEntry = new ProductBasketEntry(productId, quantity, pricePerUnit);
                newEntry.setPrice(pricePerUnit.multiply(quantity));
                return newEntry;
            }
            oldEntry.increaseQuantity(quantity);
            oldEntry.setPrice(oldEntry.getPrice().add(pricePerUnit.multiply(quantity)));
            return new ProductBasketEntry(productId, oldEntry.getQuantity(), oldEntry.getPrice());
        });

        resetTotalPriceAndQuantity(basket);

        var totalProductQuantity = productEntries.get(productId).getQuantity();
        publishProductAddedToBasketEvent(productId, totalProductQuantity, quantity, pricePerUnit, basket);
        return basket;
    }

    private void publishProductAddedToBasketEvent(Long productId, Long totalQuantity, Long quantity, Money pricePerUnit, Basket basket) {
        ProductAddedToBasketEvent event = new ProductAddedToBasketEvent(productId, totalQuantity, quantity, pricePerUnit);

        logger.info("Product {} added to basket {} successfully, publishing {}", productId, basket.getId(), event.getClass().getSimpleName());

        domainEventPublisher.publish(Basket.class, basket.getId(), Collections.singletonList(event));
    }

    @Transactional
    public void removeProductEntryWithinQuantity(Long basketId, Long productId, Long quantity, Money pricePerUnit) {
        basketRepository.findById(basketId)
                .ifPresent(basket -> {
                    Map<Long, ProductBasketEntry> productEntries = basket.getProductEntries();
                    productEntries.computeIfPresent(productId, (key, value) -> {
                        if (quantity < value.getQuantity()) {
                            var updatedQuantity = value.getQuantity() - quantity;
                            var updatedPrice = value.getPrice().subtract(pricePerUnit.multiply(quantity));
                            return new ProductBasketEntry(productId, updatedQuantity, updatedPrice);
                        }
                        return null;
                    });
                    resetTotalPriceAndQuantity(basket);
                });
    }

    private static void resetTotalPriceAndQuantity(Basket basket) {
        Map<Long, ProductBasketEntry> productEntries = basket.getProductEntries();
        basket.setTotalPrice(calculateTotalPrice(productEntries));
        basket.setTotalQuantity(calculateTotalQuantity(productEntries));
    }

    private static long calculateTotalQuantity(Map<Long, ProductBasketEntry> productEntries) {
        return productEntries.values().stream().mapToLong(ProductBasketEntry::getQuantity).sum();
    }

    private static Money calculateTotalPrice(Map<Long, ProductBasketEntry> productEntries) {
        return productEntries.values().stream().map(ProductBasketEntry::getPrice).reduce(Money.ZERO, Money::add);
    }
}

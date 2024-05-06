package ua.nure.sagaresearch.baskets.service;

import static ua.nure.sagaresearch.baskets.util.BasketServiceUtil.addProductEntryToBasket;
import static ua.nure.sagaresearch.baskets.util.BasketServiceUtil.resetTotalPriceAndQuantity;
import static ua.nure.sagaresearch.baskets.util.BasketServiceUtil.updateProductEntryPrice;
import static ua.nure.sagaresearch.common.util.LoggingUtils.ADD_PRODUCT_TO_BASKET_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.PLACE_ORDER_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.log;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.nure.sagaresearch.baskets.domain.Basket;
import ua.nure.sagaresearch.baskets.domain.BasketIsEmptyException;
import ua.nure.sagaresearch.baskets.domain.BasketRepository;
import ua.nure.sagaresearch.baskets.domain.events.BasketCheckedOutEvent;
import ua.nure.sagaresearch.baskets.domain.events.ProductAddedToBasketEvent;
import ua.nure.sagaresearch.baskets.domain.events.ProductBasketEntry;
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
    public Basket addProductToBasket(Long basketId, String productId, Long quantity, Money pricePerUnit) {
        log(logger, "{} Adding product {} for basket {}",
                ADD_PRODUCT_TO_BASKET_PREFIX, productId, basketId);

        Basket basket = basketRepository.findById(basketId).orElseThrow();

        addProductEntryToBasket(basket, productId, quantity, pricePerUnit);
        publishProductAddedToBasketEvent(basketId, productId, pricePerUnit);

        return basket;
    }

    @Transactional
    public void removeProductEntry(Long basketId, String productId) {
        basketRepository.findById(basketId)
                .ifPresent(basket -> {
                    Map<String, ProductBasketEntry> productEntries = basket.getProductEntries();
                    productEntries.remove(productId);
                    resetTotalPriceAndQuantity(basket);
                });
    }

    @Transactional
    public void actualizeProductEntryPrice(Long basketId, String productId, Money actualPricePerUnit) {
        basketRepository.findById(basketId)
                .ifPresent(basket -> updateProductEntryPrice(basket, productId, actualPricePerUnit));
    }

    @Transactional
    public void checkOutBasket(Long basketId, Long orderId) {
        Basket basket = basketRepository.findById(basketId).orElseThrow();
        if (basket.getProductEntries().isEmpty()) {
            throw new BasketIsEmptyException("Basket %s is empty, order can't be placed".formatted(basket));
        }

        BasketCheckedOutEvent event = new BasketCheckedOutEvent(orderId, basket.getTotalPrice(), basket.getProductEntries());
        log(logger, "{} Checking out basket {} for order {}, publishing {}",
                PLACE_ORDER_PREFIX, basketId, orderId, event.getClass().getSimpleName());
        domainEventPublisher.publish(Basket.class, basketId, Collections.singletonList(event));
    }

    @Transactional
    public void checkInBasket(Long basketId, Long orderId) {
        Basket basket = basketRepository.findById(basketId).orElseThrow();
        basket.clearProductEntries();

        log(logger, "{} Basket {} successfully checked in and cleared, order {} is now pending for payment",
                PLACE_ORDER_PREFIX, basketId, orderId);
    }

    private void publishProductAddedToBasketEvent(Long basketId, String productId, Money pricePerUnit) {
        ProductAddedToBasketEvent event = new ProductAddedToBasketEvent(productId, pricePerUnit);

        log(logger, "{} Product {} added to basket {} successfully, publishing {}",
                ADD_PRODUCT_TO_BASKET_PREFIX, productId, basketId, event.getClass().getSimpleName());

        domainEventPublisher.publish(Basket.class, basketId, Collections.singletonList(event));
    }
}

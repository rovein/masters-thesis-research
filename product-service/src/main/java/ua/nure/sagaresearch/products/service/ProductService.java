package ua.nure.sagaresearch.products.service;

import static ua.nure.sagaresearch.common.util.LoggingUtils.ADD_PRODUCT_TO_BASKET_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.CONFIRM_PAYMENT_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.log;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import nure.ua.sagaresearch.products.domain.events.ProductBasketAdditionValidatedEvent;
import nure.ua.sagaresearch.products.domain.events.ProductBasketAdditionValidationFailedEvent;
import nure.ua.sagaresearch.products.domain.events.ProductEvent;
import nure.ua.sagaresearch.products.domain.events.ProductBasketPriceHasChangedEvent;
import nure.ua.sagaresearch.products.domain.events.ProductQuantityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.orders.domain.events.ProductOrderEntry;
import ua.nure.sagaresearch.products.domain.Product;
import ua.nure.sagaresearch.products.domain.ProductRepository;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ProductRepository productRepository;

    private DomainEventPublisher domainEventPublisher;

    public ProductService(ProductRepository productRepository, DomainEventPublisher domainEventPublisher) {
        this.productRepository = productRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId).orElseThrow();
    }

    public void validateProductAddedToBasket(Long productId, Long basketId, Money pricePerUnit) {
        productRepository.findById(productId)
                .map(product -> {
                    Money actualProductPrice = product.getProductPrice();
                    if (Objects.equals(pricePerUnit, actualProductPrice)) {
                        return new ProductBasketAdditionValidatedEvent(basketId);
                    }
                    return new ProductBasketPriceHasChangedEvent(basketId, actualProductPrice);
                })
                .ifPresentOrElse(
                        event -> publishProductAddedToBasketValidationResult(event, productId),
                        () -> publishProductAddedToBasketValidationResult(new ProductBasketAdditionValidationFailedEvent(basketId), productId));
    }

    private void publishProductAddedToBasketValidationResult(ProductEvent event, Long productId) {
        log(logger, "{} Finished validation, publishing {} for productId {}",
                ADD_PRODUCT_TO_BASKET_PREFIX, event.getClass().getSimpleName(), productId);
        domainEventPublisher.publish(Product.class, productId, Collections.singletonList(event));
    }

    public void updateProductsQuantityForOrder(long orderId, Map<Long, ProductOrderEntry> productEntries) {
        Iterable<Product> productsFromOrder = productRepository.findAllById(productEntries.keySet());
        productsFromOrder.forEach(product -> product.decreaseQuantity(productEntries.get(product.getId()).getQuantity()));
        productRepository.saveAll(productsFromOrder);

        ProductQuantityUpdatedEvent event = new ProductQuantityUpdatedEvent(orderId);
        log(logger, "{} Updated products quantity to approve the order {}, publishing {}",
                CONFIRM_PAYMENT_PREFIX, orderId, event.getClass().getSimpleName());
        domainEventPublisher.publish(Product.class, joinProductIds(productEntries), Collections.singletonList(event));
    }

    private static String joinProductIds(Map<Long, ProductOrderEntry> productEntries) {
        return productEntries.keySet().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}

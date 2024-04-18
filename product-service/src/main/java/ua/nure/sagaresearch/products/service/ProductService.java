package ua.nure.sagaresearch.products.service;

import io.eventuate.tram.events.publisher.DomainEventPublisher;
import nure.ua.sagaresearch.products.domain.events.ProductBasketAdditionValidatedEvent;
import nure.ua.sagaresearch.products.domain.events.ProductBasketAdditionValidationFailedEvent;
import nure.ua.sagaresearch.products.domain.events.ProductEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.products.domain.Product;
import ua.nure.sagaresearch.products.domain.ProductRepository;

import java.util.Collections;
import java.util.Objects;

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

    public void validateProductAddedToBasket(Long productId, Long basketId, Long totalProductQuantity, Long quantity, Money pricePerUnit) {
        productRepository.findById(productId)
                .filter(product -> totalProductQuantity <= product.getProductQuantity())
                .filter(product -> Objects.equals(pricePerUnit, product.getProductPrice()))
                .map(product -> new ProductBasketAdditionValidatedEvent(basketId))
                .ifPresentOrElse(
                        event -> publishEvent(event, productId),
                        () -> publishEvent(new ProductBasketAdditionValidationFailedEvent(basketId, quantity, pricePerUnit), productId));
    }

    private void publishEvent(ProductEvent event, Long productId) {
        logger.info("Finished validation, publishing {} for productId {}", event.getClass().getSimpleName(), productId);
        domainEventPublisher.publish(Product.class, productId, Collections.singletonList(event));
    }
}

package ua.nure.sagaresearch.products.event.service;

import io.eventuate.EntityWithIdAndVersion;
import io.eventuate.EntityWithMetadata;
import io.eventuate.sync.AggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.products.event.domain.CreateProductCommand;
import ua.nure.sagaresearch.products.event.domain.Product;
import ua.nure.sagaresearch.products.event.domain.ProductCommand;

@Service
@RequiredArgsConstructor
public class SourcingProductService {
    private final AggregateRepository<Product, ProductCommand> productRepository;

    public EntityWithIdAndVersion<Product> createProduct(String productName, String description, String image, Money productPrice, Long productQuantity) {
        return productRepository.save(new CreateProductCommand(productName, description, image, productPrice, productQuantity));
    }

    public EntityWithMetadata<Product> findById(String productId) {
        return productRepository.find(productId);
    }
}

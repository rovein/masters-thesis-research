package ua.nure.sagaresearch.products.event.web;

import io.eventuate.EntityNotFoundException;
import io.eventuate.EntityWithIdAndVersion;
import io.eventuate.EntityWithMetadata;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.sagaresearch.products.webapi.ProductPurchaseDetailsDto;
import ua.nure.sagaresearch.products.event.domain.Product;
import ua.nure.sagaresearch.products.event.service.SourcingProductService;
import ua.nure.sagaresearch.products.webapi.CreateProductRequest;

@RestController
@AllArgsConstructor
@Tag(name = "Product", description = "Event Sourcing Product API")
public class SourcingProductsController {

    private final SourcingProductService sourcingProductService;

    @PostMapping(value = "/products")
    @Operation(summary = "Create product and receive product ID", tags = "Product")
    public ProductPurchaseDetailsDto createProduct(@RequestBody CreateProductRequest request) {
        EntityWithIdAndVersion<Product> entity = sourcingProductService.createProduct(
                request.getProductName(),
                request.getDescription(),
                request.getImage(),
                request.getProductPrice(),
                request.getProductQuantity()
        );
        return convertToProductPurchaseDetailsDto(entity.getEntityId(), entity.getAggregate());
    }

    @GetMapping(value = "/products/{productId}")
    @Operation(summary = "Get product by its ID", tags = "Product")
    public ResponseEntity<Product> getProduct(@PathVariable String productId) {
        EntityWithMetadata<Product> productWithMetadata;
        try {
            productWithMetadata = sourcingProductService.findById(productId);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productWithMetadata.getEntity(), HttpStatus.OK);
    }

    private ProductPurchaseDetailsDto convertToProductPurchaseDetailsDto(String id, Product product) {
        return new ProductPurchaseDetailsDto(id, product.getProductQuantity(), product.getProductPrice());
    }
}

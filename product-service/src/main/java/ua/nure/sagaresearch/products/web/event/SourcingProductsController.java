package ua.nure.sagaresearch.products.web.event;

import io.eventuate.EntityNotFoundException;
import io.eventuate.EntityWithIdAndVersion;
import io.eventuate.EntityWithMetadata;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.sagaresearch.products.domain.event.Product;
import ua.nure.sagaresearch.products.service.event.SourcingProductService;
import ua.nure.sagaresearch.products.webapi.CreateProductRequest;

@RestController
@RequestMapping("/event-sourcing")
@AllArgsConstructor
public class SourcingProductsController {

    private final SourcingProductService sourcingProductService;

    @PostMapping(value = "/products")
    public String createProduct(@RequestBody CreateProductRequest request) {
        EntityWithIdAndVersion<Product> entity = sourcingProductService.createProduct(
                request.getProductName(),
                request.getDescription(),
                request.getImage(),
                request.getProductPrice(),
                request.getProductQuantity()
        );
        return entity.getEntityId();
    }

    @GetMapping(value = "/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable String productId) {
        EntityWithMetadata<Product> productWithMetadata;
        try {
            productWithMetadata = sourcingProductService.findById(productId);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productWithMetadata.getEntity(), HttpStatus.OK);
    }
}

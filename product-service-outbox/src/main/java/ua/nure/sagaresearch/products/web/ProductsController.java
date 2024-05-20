package ua.nure.sagaresearch.products.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.sagaresearch.products.domain.Product;
import ua.nure.sagaresearch.products.service.ProductService;
import ua.nure.sagaresearch.products.webapi.CreateProductRequest;

@RestController
@AllArgsConstructor
@Tag(name = "Product", description = "OUTBOX Product API")
public class ProductsController {

    private final ProductService productService;

    @PostMapping(value = "/products")
    @Operation(summary = "Create product and receive product ID", tags = "Product")
    public String createProduct(@RequestBody CreateProductRequest request) {
        return productService.createProduct(request).getId();
    }

    @GetMapping(value = "/products/{productId}")
    @Operation(summary = "Get product by its ID", tags = "Product")
    public Product getProduct(@PathVariable String productId) {
        return productService.findById(productId);
    }
}

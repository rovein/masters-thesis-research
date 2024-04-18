package ua.nure.sagaresearch.products.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.sagaresearch.products.domain.Product;
import ua.nure.sagaresearch.products.service.ProductService;

@RestController
@AllArgsConstructor
public class ProductsController {

    private final ProductService productService;

    @GetMapping(value = "/products/{productId}")
    public Product getProduct(@PathVariable Long productId) {
        return productService.findById(productId);
    }
}

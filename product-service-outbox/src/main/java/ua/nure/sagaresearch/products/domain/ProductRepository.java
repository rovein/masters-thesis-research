package ua.nure.sagaresearch.products.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product, String> {

    @Query("select p from Product p left join fetch p.productProperties where p.id = :productId")
    Optional<Product> findById(@Param("productId") String productId);
}

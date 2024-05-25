package ua.nure.sagaresearch.baskets.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BasketRepository extends PagingAndSortingRepository<Basket, Long> {

    @Query("select b from Basket b left join fetch b.productEntries where b.id = :basketId")
    Optional<Basket> findById(@Param("basketId") Long basketId);
}

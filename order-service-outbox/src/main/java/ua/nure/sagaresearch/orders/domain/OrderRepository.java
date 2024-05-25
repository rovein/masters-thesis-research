package ua.nure.sagaresearch.orders.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    @Query("select o from Order o left join fetch o.productEntries where o.id = :orderId")
    Optional<Order> findById(@Param("orderId") Long orderId);
}

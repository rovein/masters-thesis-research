package ua.nure.sagaresearch.orders.service.event;

import io.eventuate.EntityWithIdAndVersion;
import io.eventuate.EntityWithMetadata;
import io.eventuate.sync.AggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.nure.sagaresearch.orders.domain.event.CreateOrderCommand;
import ua.nure.sagaresearch.orders.domain.event.Order;
import ua.nure.sagaresearch.orders.domain.event.OrderCommand;

@Service
@RequiredArgsConstructor
public class SourcingOrderService {
    private final AggregateRepository<Order, OrderCommand> orderRepository;

    public EntityWithIdAndVersion<Order> createOrder(String basketId, String shippingType, String paymentType, String shippingAddress) {
        return orderRepository.save(new CreateOrderCommand(basketId, shippingType, paymentType, shippingAddress));
    }

    public EntityWithMetadata<Order> findById(String productId) {
        return orderRepository.find(productId);
    }
}

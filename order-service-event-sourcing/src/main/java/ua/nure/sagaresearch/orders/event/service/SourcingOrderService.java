package ua.nure.sagaresearch.orders.event.service;

import io.eventuate.EntityWithIdAndVersion;
import io.eventuate.EntityWithMetadata;
import io.eventuate.sync.AggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.nure.sagaresearch.orders.event.domain.ConfirmPaymentCommand;
import ua.nure.sagaresearch.orders.event.domain.CreateOrderCommand;
import ua.nure.sagaresearch.orders.event.domain.Order;
import ua.nure.sagaresearch.orders.event.domain.OrderCommand;


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

    public EntityWithIdAndVersion<Order> confirmPayment(String orderId) {
        return orderRepository.update(orderId, new ConfirmPaymentCommand());
    }
}

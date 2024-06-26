package ua.nure.sagaresearch.orders.service;

import static java.util.Collections.singletonList;
import static ua.nure.sagaresearch.common.util.ConverterUtil.convertToProductOrderEntries;
import static ua.nure.sagaresearch.common.util.LoggingUtils.CANCEL_ORDER_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.CONFIRM_PAYMENT_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.PLACE_ORDER_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.log;
import static ua.nure.sagaresearch.common.util.LoggingUtils.logEndTime;

import com.google.common.base.Preconditions;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.common.domain.basket.ProductBasketEntry;
import ua.nure.sagaresearch.orders.domain.Order;
import ua.nure.sagaresearch.orders.domain.OrderRepository;
import ua.nure.sagaresearch.orders.domain.events.OrderApprovedEvent;
import ua.nure.sagaresearch.orders.domain.events.OrderCancellationRequestedEvent;
import ua.nure.sagaresearch.orders.domain.events.OrderCancelledEvent;
import ua.nure.sagaresearch.orders.domain.events.OrderDetails;
import ua.nure.sagaresearch.orders.domain.events.OrderPaymentConfirmedEvent;
import ua.nure.sagaresearch.orders.domain.events.OrderPlacedEvent;
import ua.nure.sagaresearch.orders.domain.events.OrderRejectedEvent;
import ua.nure.sagaresearch.orders.domain.events.OrderState;

import java.util.Collections;
import java.util.Map;

public class OrderService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final DomainEventPublisher domainEventPublisher;
    private final OrderRepository orderRepository;


    public OrderService(DomainEventPublisher domainEventPublisher, OrderRepository orderRepository) {
        this.domainEventPublisher = domainEventPublisher;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(OrderDetails orderDetails) {
        ResultWithEvents<Order> orderWithEvents = Order.createOrder(orderDetails);
        Order order = orderWithEvents.result;
        orderRepository.save(order);
        log(logger, "{} Creating the order, publishing the {}",
                PLACE_ORDER_PREFIX, orderWithEvents.events.get(0).getClass().getSimpleName());
        domainEventPublisher.publish(Order.class, order.getId(), orderWithEvents.events);
        return order;
    }

    @Transactional
    public void placeOrder(Long orderId, Long basketId, Money totalPrice, Map<String, ProductBasketEntry> productEntries) {
        Order order = getOrder(orderId);
        order.setTotalPrice(totalPrice);
        order.setProductEntries(convertToProductOrderEntries(productEntries));

        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(basketId);

        log(logger, "{} Order {} is placed, publishing {} for basket {}",
                PLACE_ORDER_PREFIX, orderId, orderPlacedEvent.getClass().getSimpleName(), basketId);
        domainEventPublisher.publish(Order.class, orderId, Collections.singletonList(orderPlacedEvent));
    }

    @Transactional
    public Order confirmPayment(Long orderId) {
        Order order = getOrder(orderId);
        Preconditions.checkState(order.getState() == OrderState.PENDING);
        order.setState(OrderState.PENDING_PAYMENT_CONFIRMED);

        OrderPaymentConfirmedEvent event = new OrderPaymentConfirmedEvent(order.getProductEntries());

        log(logger, "{} Payment is confirmed for order {}, state is {}, publishing {}",
                CONFIRM_PAYMENT_PREFIX, orderId, order.getState(), event.getClass().getSimpleName());

        domainEventPublisher.publish(Order.class, orderId, Collections.singletonList(event));

        return order;
    }

    @Transactional
    public void approveOrder(Long orderId) {
        Order order = getOrder(orderId);
        order.onPaymentSuccess();

        OrderApprovedEvent event = new OrderApprovedEvent(order.getOrderDetails());
        log(logger, "{} Order {} is marked as {}, publishing {}",
                CONFIRM_PAYMENT_PREFIX, orderId, order.getState(), event.getClass().getSimpleName());
        logEndTime(logger, CONFIRM_PAYMENT_PREFIX, orderId);
        domainEventPublisher.publish(Order.class,
                orderId, singletonList(event));
    }

    // TODO: remove it when Event Sourcing is implemented
    public void rejectOrder(Long orderId) {
        Order order = getOrder(orderId);
        order.onPaymentFailed();
        domainEventPublisher.publish(Order.class,
                orderId, singletonList(new OrderRejectedEvent(order.getOrderDetails())));
    }

    @Transactional
    public Order requestCancellation(Long orderId) {
        Order order = getOrder(orderId);
        order.setState(OrderState.CANCELLATION_REQUESTED);
        OrderCancellationRequestedEvent event = new OrderCancellationRequestedEvent(order.getProductEntries());

        log(logger, "{} Cancellation requested for the order {}, publishing the {}",
                CANCEL_ORDER_PREFIX, orderId, event.getClass().getSimpleName());

        domainEventPublisher.publish(Order.class, orderId, Collections.singletonList(event));

        return order;
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = getOrder(orderId);
        order.cancel();
        OrderCancelledEvent event = new OrderCancelledEvent(order.getOrderDetails());
        log(logger, "{} Order {} is marked as {}, publishing {}",
                CANCEL_ORDER_PREFIX, orderId, order.getState(), event.getClass().getSimpleName());
        domainEventPublisher.publish(Order.class,
                orderId, singletonList(event));
        logEndTime(logger, CANCEL_ORDER_PREFIX, orderId);
        return order;
    }

    private Order getOrder(Long orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));
    }
}

package ua.nure.sagaresearch.orders.web;

import static ua.nure.sagaresearch.common.util.LoggingUtils.CANCEL_ORDER_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.CONFIRM_PAYMENT_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.PLACE_ORDER_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.logStartTime;

import io.eventuate.common.json.mapper.JSonMapper;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.sagaresearch.orders.domain.Order;
import ua.nure.sagaresearch.orders.domain.OrderRepository;
import ua.nure.sagaresearch.orders.domain.events.OrderDetails;
import ua.nure.sagaresearch.orders.service.OrderService;
import ua.nure.sagaresearch.orders.webapi.CreateOrderRequest;
import ua.nure.sagaresearch.orders.webapi.CreateOrderResponse;
import ua.nure.sagaresearch.orders.webapi.GetOrderResponse;

@RestController
@Tag(name = "Order", description = "OUTBOX Order API")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private OrderService orderService;
    private OrderRepository orderRepository;
    private DomainSnapshotExportService<Order> domainSnapshotExportService;

    @Autowired
    public OrderController(OrderService orderService,
                           OrderRepository orderRepository,
                           DomainSnapshotExportService<Order> domainSnapshotExportService) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.domainSnapshotExportService = domainSnapshotExportService;
    }

    @PostMapping(value = "/orders")
    @Operation(summary = "[Place Order SAGA] starting point", tags = "Order")
    public CreateOrderResponse placeOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        long startTime = System.nanoTime();
        Order order = orderService.createOrder(new OrderDetails(
                createOrderRequest.getBasketId(),
                createOrderRequest.getShippingType(),
                createOrderRequest.getPaymentType(),
                createOrderRequest.getShippingAddress()));
        String orderId = String.valueOf(order.getId());
        logStartTime(LOGGER, PLACE_ORDER_PREFIX, startTime, orderId);
        return new CreateOrderResponse(orderId);
    }

    @GetMapping(value = "/orders/{orderId}")
    @Operation(summary = "Get order by its ID", tags = "Order")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable Long orderId) {
        return orderRepository
                .findById(orderId)
                .map(this::makeSuccessfulResponse)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/orders/{orderId}/confirm-payment")
    @Operation(summary = "[Confirm Payment SAGA] starting point", tags = "Order")
    public ResponseEntity<GetOrderResponse> confirmPayment(@PathVariable Long orderId) {
        logStartTime(LOGGER, CONFIRM_PAYMENT_PREFIX, orderId);
        Order order = orderService.confirmPayment(orderId);
        return makeSuccessfulResponse(order);
    }

    @PostMapping(value = "/orders/{orderId}/cancel")
    @Operation(summary = "[Cancel order SAGA] starting point", tags = "Order")
    public ResponseEntity<GetOrderResponse> cancelOrder(@PathVariable Long orderId) {
        logStartTime(LOGGER, CANCEL_ORDER_PREFIX, orderId);
        Order order = orderService.requestCancellation(orderId);
        return makeSuccessfulResponse(order);
    }

    @RequestMapping(value = "/orders/make-snapshot", method = RequestMethod.POST)
    public String makeSnapshot() {
        return JSonMapper.toJson(domainSnapshotExportService.exportSnapshots());
    }

    private ResponseEntity<GetOrderResponse> makeSuccessfulResponse(Order order) {
        return new ResponseEntity<>(new GetOrderResponse(
                String.valueOf(order.getId()),
                order.getState(),
                order.getOrderDetails().getBasketId(),
                order.getTotalPrice(),
                order.getProductEntries().values()
        ), HttpStatus.OK);
    }
}

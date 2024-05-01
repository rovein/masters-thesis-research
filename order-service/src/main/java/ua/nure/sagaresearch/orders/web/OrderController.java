package ua.nure.sagaresearch.orders.web;

import io.eventuate.common.json.mapper.JSonMapper;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
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
public class OrderController {

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
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        Order order = orderService.createOrder(new OrderDetails(
                createOrderRequest.getBasketId(),
                createOrderRequest.getShippingType(),
                createOrderRequest.getPaymentType(),
                createOrderRequest.getShippingAddress()));
        return new CreateOrderResponse(order.getId());
    }

    @GetMapping(value = "/orders/{orderId}")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable Long orderId) {
        return orderRepository
                .findById(orderId)
                .map(this::makeSuccessfulResponse)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/orders/{orderId}/confirmPayment")
    public ResponseEntity<GetOrderResponse> confirmPayment(@PathVariable Long orderId) {
        Order order = orderService.confirmPayment(orderId);
        return makeSuccessfulResponse(order);
    }

    // TODO [Cancel Order SAGA] Step 1:
    //  1.1 accept cancel action and request the cancellation (already done)
    @PostMapping(value = "/orders/{orderId}/cancel")
    public ResponseEntity<GetOrderResponse> cancelOrder(@PathVariable Long orderId) {
        Order order = orderService.requestCancellation(orderId);
        return makeSuccessfulResponse(order);
    }

    @RequestMapping(value = "/orders/make-snapshot", method = RequestMethod.POST)
    public String makeSnapshot() {
        return JSonMapper.toJson(domainSnapshotExportService.exportSnapshots());
    }

    private ResponseEntity<GetOrderResponse> makeSuccessfulResponse(Order order) {
        return new ResponseEntity<>(new GetOrderResponse(
                order.getId(),
                order.getState(),
                order.getOrderDetails().getBasketId(),
                order.getTotalPrice(),
                order.getProductEntries().values()
        ), HttpStatus.OK);
    }
}

package ua.nure.sagaresearch.orders.event.web;

import io.eventuate.EntityNotFoundException;
import io.eventuate.EntityWithIdAndVersion;
import io.eventuate.EntityWithMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.sagaresearch.orders.event.domain.Order;
import ua.nure.sagaresearch.orders.event.service.SourcingOrderService;
import ua.nure.sagaresearch.orders.webapi.CreateOrderRequest;
import ua.nure.sagaresearch.orders.webapi.CreateOrderResponse;
import ua.nure.sagaresearch.orders.webapi.GetOrderResponse;

@RestController
@RequestMapping("/event-sourcing")
@RequiredArgsConstructor
public class SourcingOrderController {

    private final SourcingOrderService sourcingOrderService;

    @PostMapping(value = "/orders")
    public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        EntityWithIdAndVersion<Order> entity = sourcingOrderService.createOrder(
                createOrderRequest.getBasketId(),
                createOrderRequest.getShippingType(),
                createOrderRequest.getPaymentType(),
                createOrderRequest.getShippingAddress());
        return new CreateOrderResponse(entity.getEntityId());
    }

    @GetMapping(value = "/orders/{orderId}")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable String orderId) {
        EntityWithMetadata<Order> orderWithMetadata;
        try {
            orderWithMetadata = sourcingOrderService.findById(orderId);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return makeSuccessfulResponse(orderId, orderWithMetadata.getEntity());
    }

    private ResponseEntity<GetOrderResponse> makeSuccessfulResponse(String orderId, Order order) {
        return new ResponseEntity<>(new GetOrderResponse(
                orderId,
                order.getState(),
                order.getOrderDetails().getBasketId(),
                order.getTotalPrice(),
                order.getProductEntries().values()
        ), HttpStatus.OK);
    }
}

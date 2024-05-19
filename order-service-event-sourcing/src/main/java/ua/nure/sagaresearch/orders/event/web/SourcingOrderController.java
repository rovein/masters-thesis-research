package ua.nure.sagaresearch.orders.event.web;

import static ua.nure.sagaresearch.common.util.ConverterUtil.supplyAndConvertToResponseEntity;
import static ua.nure.sagaresearch.common.util.ConverterUtil.toEntityWithIdAndVersion;
import static ua.nure.sagaresearch.common.util.LoggingUtils.EVENT_SOURCING_PLACE_ORDER_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.logStartTime;

import io.eventuate.EntityWithIdAndVersion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Tag(name = "Order", description = "Event Sourcing Order API")
public class SourcingOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SourcingOrderController.class);

    private final SourcingOrderService sourcingOrderService;

    @PostMapping(value = "/orders")
    @Operation(summary = "[Place Order SAGA] starting point", tags = "Order")
    public CreateOrderResponse placeOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        logStartTime(LOGGER, EVENT_SOURCING_PLACE_ORDER_PREFIX);
        EntityWithIdAndVersion<Order> entity = sourcingOrderService.createOrder(
                createOrderRequest.getBasketId(),
                createOrderRequest.getShippingType(),
                createOrderRequest.getPaymentType(),
                createOrderRequest.getShippingAddress());
        return new CreateOrderResponse(entity.getEntityId());
    }

    @PostMapping(value = "/orders/{orderId}/confirm-payment")
    @Operation(summary = "[Confirm Payment SAGA] starting point", tags = "Order")
    public ResponseEntity<GetOrderResponse> confirmPayment(@PathVariable String orderId) {
        return supplyAndConvertToResponseEntity(() -> sourcingOrderService.confirmPayment(orderId), this::convertToOrderResponseDto);
    }

    @PostMapping(value = "/orders/{orderId}/cancel")
    @Operation(summary = "[Cancel order SAGA] starting point", tags = "Order")
    public ResponseEntity<GetOrderResponse> cancelOrder(@PathVariable String orderId) {
        return supplyAndConvertToResponseEntity(() -> sourcingOrderService.requestCancellation(orderId), this::convertToOrderResponseDto);
    }

    @GetMapping(value = "/orders/{orderId}")
    @Operation(summary = "Get order by its ID", tags = "Order")
    public ResponseEntity<GetOrderResponse> getOrder(@PathVariable String orderId) {
        return supplyAndConvertToResponseEntity(() -> toEntityWithIdAndVersion(sourcingOrderService.findById(orderId)), this::convertToOrderResponseDto);
    }

    private GetOrderResponse convertToOrderResponseDto(String orderId, Order order) {
        return new GetOrderResponse(
                orderId,
                order.getState(),
                order.getOrderDetails().getBasketId(),
                order.getTotalPrice(),
                order.getProductEntries().values()
        );
    }
}

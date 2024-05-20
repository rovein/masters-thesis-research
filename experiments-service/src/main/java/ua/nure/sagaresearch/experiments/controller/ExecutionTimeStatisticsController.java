package ua.nure.sagaresearch.experiments.controller;

import static ua.nure.sagaresearch.common.util.LoggingUtils.ADD_PRODUCT_TO_BASKET_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.CANCEL_ORDER_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.CONFIRM_PAYMENT_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.EVENT_SOURCING_ADD_PRODUCT_TO_BASKET_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.EVENT_SOURCING_CANCEL_ORDER_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.EVENT_SOURCING_CONFIRM_PAYMENT_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.EVENT_SOURCING_PLACE_ORDER_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.PLACE_ORDER_PREFIX;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.sagaresearch.experiments.config.ConfigProperties;
import ua.nure.sagaresearch.experiments.service.ExecutionTimeExtractorService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExecutionTimeStatisticsController {

    private final ConfigProperties configProperties;
    private final ExecutionTimeExtractorService executionTimeExtractorService;

    @GetMapping("/outbox/time-statistic/add-product")
    @Operation(summary = "[Add product to Basket SAGA] Outbox. Get execution time of N last transactions", tags = "1. Outbox time statistic")
    public List<Long> outboxAddProductTimeStatistic(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getBasketLogsLocation();
        String endTimeLogsLocation = configProperties.getBasketLogsLocation();
        return executionTimeExtractorService.extractExecutionTimesStatistic(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                ADD_PRODUCT_TO_BASKET_PREFIX);
    }

    @GetMapping("/outbox/time-average/add-product")
    @Operation(summary = "[Add product to Basket SAGA] Outbox. Get average execution time of N last transactions", tags = "2. Outbox average time")
    public Double outboxAddProductTimeAverage(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getBasketLogsLocation();
        String endTimeLogsLocation = configProperties.getBasketLogsLocation();
        return executionTimeExtractorService.extractAverageExecutionTime(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                ADD_PRODUCT_TO_BASKET_PREFIX);
    }

    @GetMapping("/outbox/time-statistic/place-order")
    @Operation(summary = "[Place order SAGA] Outbox. Get execution time of N last transactions", tags = "1. Outbox time statistic")
    public List<Long> outboxPlaceOrderTimeStatistic(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getOrderLogsLocation();
        String endTimeLogsLocation = configProperties.getBasketLogsLocation();
        return executionTimeExtractorService.extractExecutionTimesStatistic(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                PLACE_ORDER_PREFIX);
    }

    @GetMapping("/outbox/time-average/place-order")
    @Operation(summary = "[Place order SAGA] Outbox. Get average execution time of N last transactions", tags = "2. Outbox average time")
    public Double outboxPlaceOrderTimeAverage(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getOrderLogsLocation();
        String endTimeLogsLocation = configProperties.getBasketLogsLocation();
        return executionTimeExtractorService.extractAverageExecutionTime(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                PLACE_ORDER_PREFIX);
    }

    @GetMapping("/outbox/time-statistic/confirm-payment")
    @Operation(summary = "[Confirm payment SAGA] Outbox. Get execution time of N last transactions", tags = "1. Outbox time statistic")
    public List<Long> outboxConfirmPaymentTimeStatistic(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getOrderLogsLocation();
        String endTimeLogsLocation = configProperties.getOrderLogsLocation();
        return executionTimeExtractorService.extractExecutionTimesStatistic(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                CONFIRM_PAYMENT_PREFIX);
    }

    @GetMapping("/outbox/time-average/confirm-payment")
    @Operation(summary = "[Confirm payment SAGA] Outbox. Get average execution time of N last transactions", tags = "2. Outbox average time")
    public Double outboxConfirmPaymentTimeAverage(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getOrderLogsLocation();
        String endTimeLogsLocation = configProperties.getOrderLogsLocation();
        return executionTimeExtractorService.extractAverageExecutionTime(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                CONFIRM_PAYMENT_PREFIX);
    }

    @GetMapping("/outbox/time-statistic/cancel-order")
    @Operation(summary = "[Cancel order SAGA] Outbox. Get execution time of N last transactions", tags = "1. Outbox time statistic")
    public List<Long> outboxCancelOrderTimeStatistic(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getOrderLogsLocation();
        String endTimeLogsLocation = configProperties.getOrderLogsLocation();
        return executionTimeExtractorService.extractExecutionTimesStatistic(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                CANCEL_ORDER_PREFIX);
    }

    @GetMapping("/outbox/time-average/cancel-order")
    @Operation(summary = "[Cancel order SAGA] Outbox. Get average execution time of N last transactions", tags = "2. Outbox average time")
    public Double outboxCancelOrderTimeAverage(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getOrderLogsLocation();
        String endTimeLogsLocation = configProperties.getOrderLogsLocation();
        return executionTimeExtractorService.extractAverageExecutionTime(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                CANCEL_ORDER_PREFIX);
    }

    @GetMapping("/event-sourcing/time-statistic/add-product")
    @Operation(summary = "[Add product to Basket SAGA] Event Sourcing. Get execution time of N last transactions", tags = "3. Event Sourcing time statistic")
    public List<Long> eventSourcingAddProductTimeStatistic(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getBasketLogsLocation();
        String endTimeLogsLocation = configProperties.getBasketLogsLocation();
        return executionTimeExtractorService.extractExecutionTimesStatistic(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                EVENT_SOURCING_ADD_PRODUCT_TO_BASKET_PREFIX);
    }

    @GetMapping("/event-sourcing/time-average/add-product")
    @Operation(
            summary = "[Add product to Basket SAGA] Event Sourcing. Get average execution time of N last transactions", tags = "4. Event Sourcing average time")
    public Double eventSourcingAddProductTimeAverage(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getBasketLogsLocation();
        String endTimeLogsLocation = configProperties.getBasketLogsLocation();
        return executionTimeExtractorService.extractAverageExecutionTime(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                EVENT_SOURCING_ADD_PRODUCT_TO_BASKET_PREFIX);
    }

    @GetMapping("/event-sourcing/time-statistic/place-order")
    @Operation(summary = "[Place order SAGA] Event Sourcing. Get execution time of N last transactions", tags = "3. Event Sourcing time statistic")
    public List<Long> eventSourcingPlaceOrderTimeStatistic(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getOrderLogsLocation();
        String endTimeLogsLocation = configProperties.getBasketLogsLocation();
        return executionTimeExtractorService.extractExecutionTimesStatistic(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                EVENT_SOURCING_PLACE_ORDER_PREFIX);
    }

    @GetMapping("/event-sourcing/time-average/place-order")
    @Operation(summary = "[Place order SAGA] Event Sourcing. Get average execution time of N last transactions", tags = "4. Event Sourcing average time")
    public Double eventSourcingPlaceOrderTimeAverage(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getOrderLogsLocation();
        String endTimeLogsLocation = configProperties.getBasketLogsLocation();
        return executionTimeExtractorService.extractAverageExecutionTime(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                EVENT_SOURCING_PLACE_ORDER_PREFIX);
    }

    @GetMapping("/event-sourcing/time-statistic/confirm-payment")
    @Operation(summary = "[Confirm payment SAGA] Event Sourcing. Get execution time of N last transactions", tags = "3. Event Sourcing time statistic")
    public List<Long> eventSourcingConfirmPaymentTimeStatistic(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getOrderLogsLocation();
        String endTimeLogsLocation = configProperties.getOrderLogsLocation();
        return executionTimeExtractorService.extractExecutionTimesStatistic(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                EVENT_SOURCING_CONFIRM_PAYMENT_PREFIX);
    }

    @GetMapping("/event-sourcing/time-average/confirm-payment")
    @Operation(summary = "[Confirm payment SAGA] Event Sourcing. Get average execution time of N last transactions", tags = "4. Event Sourcing average time")
    public Double eventSourcingConfirmPaymentTimeAverage(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getOrderLogsLocation();
        String endTimeLogsLocation = configProperties.getOrderLogsLocation();
        return executionTimeExtractorService.extractAverageExecutionTime(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                EVENT_SOURCING_CONFIRM_PAYMENT_PREFIX);
    }

    @GetMapping("/event-sourcing/time-statistic/cancel-order")
    @Operation(summary = "[Cancel order SAGA] Event Sourcing. Get execution time of N last transactions", tags = "3. Event Sourcing time statistic")
    public List<Long> eventSourcingCancelOrderTimeStatistic(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getOrderLogsLocation();
        String endTimeLogsLocation = configProperties.getOrderLogsLocation();
        return executionTimeExtractorService.extractExecutionTimesStatistic(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                EVENT_SOURCING_CANCEL_ORDER_PREFIX);
    }

    @GetMapping("/event-sourcing/time-average/cancel-order")
    @Operation(summary = "[Cancel order SAGA] Event Sourcing. Get average execution time of N last transactions", tags = "4. Event Sourcing average time")
    public Double eventSourcingCancelOrderTimeAverage(@RequestParam Long numberOfTransactions) throws IOException {
        String startTimeLogsLocation = configProperties.getOrderLogsLocation();
        String endTimeLogsLocation = configProperties.getOrderLogsLocation();
        return executionTimeExtractorService.extractAverageExecutionTime(
                numberOfTransactions,
                startTimeLogsLocation,
                endTimeLogsLocation,
                EVENT_SOURCING_CANCEL_ORDER_PREFIX);
    }
}

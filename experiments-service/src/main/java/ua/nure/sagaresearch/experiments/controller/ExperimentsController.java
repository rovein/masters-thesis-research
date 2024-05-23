package ua.nure.sagaresearch.experiments.controller;

import static ua.nure.sagaresearch.experiments.util.UrlResolverUtil.resolveAddProductToBasketUrl;
import static ua.nure.sagaresearch.experiments.util.UrlResolverUtil.resolveCancelOrderUrl;
import static ua.nure.sagaresearch.experiments.util.UrlResolverUtil.resolveConfirmPaymentUrl;
import static ua.nure.sagaresearch.experiments.util.UrlResolverUtil.resolveCreateBasketUrl;
import static ua.nure.sagaresearch.experiments.util.UrlResolverUtil.resolveCreateProductUrl;
import static ua.nure.sagaresearch.experiments.util.UrlResolverUtil.resolvePlaceOrderUrl;
import static ua.nure.sagaresearch.experiments.util.UrlResolverUtil.resolveRetrieveOrderUrl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.sagaresearch.baskets.webapi.AddProductToBasketRequest;
import ua.nure.sagaresearch.baskets.webapi.BasketDtoResponse;
import ua.nure.sagaresearch.experiments.config.ConfigProperties;
import ua.nure.sagaresearch.experiments.dto.OrderViewDto;
import ua.nure.sagaresearch.experiments.service.RestClientHelper;
import ua.nure.sagaresearch.experiments.util.ExperimentType;
import ua.nure.sagaresearch.orders.webapi.CreateOrderRequest;
import ua.nure.sagaresearch.orders.webapi.CreateOrderResponse;
import ua.nure.sagaresearch.orders.webapi.GetOrderResponse;
import ua.nure.sagaresearch.products.webapi.CreateProductRequest;
import ua.nure.sagaresearch.products.webapi.ProductPurchaseDetailsDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/experiments")
@RequiredArgsConstructor
public class ExperimentsController {

    private final ConfigProperties configProperties;
    private final RestClientHelper restClientHelper;

    private final List<String> basketIds = new CopyOnWriteArrayList<>();
    private final List<ProductPurchaseDetailsDto> products = new CopyOnWriteArrayList<>();
    private final List<String> orderIds = new ArrayList<>();
    private final List<OrderViewDto> orderViews = new ArrayList<>();

    @PostMapping(value = "/step-1/pre-setup-baskets")
    @Operation(summary = "Step 1, create N baskets", tags = "Experiments")
    public List<String> step1(@RequestParam Integer numberOfBaskets, @RequestParam ExperimentType experimentType) {
        basketIds.clear();
        String createBasketUrl = resolveCreateBasketUrl(experimentType, configProperties);
        restClientHelper.supplyAsyncAndWaitForAllEntityCreationTasks(numberOfBaskets,
                () -> restClientHelper.performPostRequest(createBasketUrl), basketIds::add);
        return basketIds;
    }

    @PostMapping(value = "/step-2/pre-setup-products")
    @Operation(summary = "Step 2, create N products", tags = "Experiments")
    public List<ProductPurchaseDetailsDto> step2(@RequestParam Integer numberOfProducts, @RequestParam ExperimentType experimentType,
                                                 @RequestBody CreateProductRequest createProductRequest) {
        products.clear();
        String createProductUrl = resolveCreateProductUrl(experimentType, configProperties);
        restClientHelper.supplyAsyncAndWaitForAllEntityCreationTasks(numberOfProducts,
                () -> restClientHelper.performPostRequest(createProductUrl, createProductRequest, ProductPurchaseDetailsDto.class), products::add);
        return products;
    }

    @PostMapping(value = "/step-3/add-products-to-baskets")
    @Operation(summary = "Step 3, add products to N baskets", tags = "Experiments")
    public List<BasketDtoResponse> step3(@RequestParam Integer numberOfExperiments, @RequestParam ExperimentType experimentType) throws InterruptedException {
        if (basketIds.isEmpty() || products.isEmpty()) {
            return Collections.emptyList();
        }
        String addProductToBasketUrl = resolveAddProductToBasketUrl(experimentType, configProperties);
        List<BasketDtoResponse> responses = new ArrayList<>();
        for (int i = 0; i < numberOfExperiments; i++) {
            String basketId = basketIds.get(i);
            AddProductToBasketRequest addProductToBasketRequest = convertToAddProductToBasketRequest(products.get(i));
            BasketDtoResponse response = restClientHelper.performPostRequest(
                    addProductToBasketUrl, addProductToBasketRequest, BasketDtoResponse.class, basketId);
            responses.add(response);
            Thread.sleep(100L);
        }
        return responses;
    }

    @PostMapping(value = "/step-4/place-orders")
    @Operation(summary = "Step 4, place N orders based on N recently filled baskets", tags = "Experiments")
    public List<String> step4(@RequestParam Integer numberOfExperiments, @RequestParam ExperimentType experimentType) throws InterruptedException {
        if (basketIds.isEmpty()) {
            return Collections.emptyList();
        }
        orderIds.clear();
        String placeOrderUrl = resolvePlaceOrderUrl(experimentType, configProperties);
        for (int i = 0; i < numberOfExperiments; i++) {
            String basketId = basketIds.get(i);
            CreateOrderRequest createOrderRequest = new CreateOrderRequest(basketId, "POST", "ONLINE", "Ukraine, Kyiv");
            String orderId = restClientHelper.performPostRequest(placeOrderUrl, createOrderRequest, CreateOrderResponse.class).getOrderId();
            orderIds.add(orderId);
            Thread.sleep(100L);
        }
        return orderIds;
    }

    @PostMapping(value = "/step-5/confirm-payments-for-orders")
    @Operation(summary = "Step 5, confirm payments for N orders", tags = "Experiments")
    public List<OrderViewDto> step5(@RequestParam Integer numberOfExperiments, @RequestParam ExperimentType experimentType) throws InterruptedException {
        if (orderIds.isEmpty()) {
            return Collections.emptyList();
        }

        String confirmPaymentUrl = resolveConfirmPaymentUrl(experimentType, configProperties);
        performChangeOrderStateExperiment(numberOfExperiments, confirmPaymentUrl);

        orderViews.clear();
        String retrieveOrderUrl = resolveRetrieveOrderUrl(experimentType, configProperties);
        for (String orderId : orderIds) {
            GetOrderResponse getOrderResponse = restClientHelper.performGetRequest(retrieveOrderUrl, GetOrderResponse.class, orderId);
            orderViews.add(convertToOrderViewDto(getOrderResponse));
        }
        return orderViews;
    }

    @PostMapping(value = "/step-6/cancel-orders")
    @Operation(summary = "Step 6, cancel N orders", tags = "Experiments")
    public List<OrderViewDto> step6(@RequestParam Integer numberOfExperiments, @RequestParam ExperimentType experimentType) throws InterruptedException {
        if (orderIds.isEmpty()) {
            return Collections.emptyList();
        }

        String cancelOrderUrl = resolveCancelOrderUrl(experimentType, configProperties);
        performChangeOrderStateExperiment(numberOfExperiments, cancelOrderUrl);

        String retrieveOrderUrl = resolveRetrieveOrderUrl(experimentType, configProperties);
        for (int i = 0; i < orderIds.size(); i++) {
            String orderId = orderIds.get(i);
            GetOrderResponse getOrderResponse = restClientHelper.performGetRequest(retrieveOrderUrl, GetOrderResponse.class, orderId);
            orderViews.set(i, convertToOrderViewDto(getOrderResponse));
        }
        return orderViews;
    }

    private void performChangeOrderStateExperiment(Integer numberOfExperiments, String cancelOrderUrl) throws InterruptedException {
        for (int i = 0; i < numberOfExperiments; i++) {
            String orderId = orderIds.get(i);
            restClientHelper.performPostRequest(cancelOrderUrl, orderId);
            Thread.sleep(100L);
        }
    }

    private static AddProductToBasketRequest convertToAddProductToBasketRequest(ProductPurchaseDetailsDto productPurchaseDetailsDto) {
        return new AddProductToBasketRequest(
                productPurchaseDetailsDto.getProductId(),
                productPurchaseDetailsDto.getQuantity(),
                productPurchaseDetailsDto.getPricePerUnit()
        );
    }

    private static OrderViewDto convertToOrderViewDto(GetOrderResponse getOrderResponse) {
        return new OrderViewDto(getOrderResponse.getOrderId(), getOrderResponse.getOrderState());
    }
}

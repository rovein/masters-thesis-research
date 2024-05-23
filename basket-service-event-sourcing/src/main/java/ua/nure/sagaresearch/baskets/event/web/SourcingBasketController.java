package ua.nure.sagaresearch.baskets.event.web;

import static ua.nure.sagaresearch.common.util.ConverterUtil.supplyAndConvertToResponseEntity;
import static ua.nure.sagaresearch.common.util.ConverterUtil.toEntityWithIdAndVersion;
import static ua.nure.sagaresearch.common.util.LoggingUtils.EVENT_SOURCING_ADD_PRODUCT_TO_BASKET_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.logStartTime;

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
import org.springframework.web.bind.annotation.RestController;
import ua.nure.sagaresearch.baskets.event.domain.Basket;
import ua.nure.sagaresearch.baskets.event.service.SourcingBasketService;
import ua.nure.sagaresearch.baskets.webapi.AddProductToBasketRequest;
import ua.nure.sagaresearch.baskets.webapi.BasketDtoResponse;
import ua.nure.sagaresearch.baskets.webapi.ProductBasketEntryDto;

@RestController
@RequiredArgsConstructor
@Tag(name = "Basket", description = "Event Sourcing Basket API")
public class SourcingBasketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SourcingBasketController.class);

    private final SourcingBasketService sourcingBasketService;

    @PostMapping(value = "/baskets")
    @Operation(summary = "Create basket and receive basket ID", tags = "Basket")
    public String createBasket() {
        return sourcingBasketService.createBasket().getEntityId();
    }

    @GetMapping(value = "/baskets/{basketId}")
    @Operation(summary = "Get basket by its ID", tags = "Basket")
    public ResponseEntity<BasketDtoResponse> getBasket(@PathVariable String basketId) {
        return supplyAndConvertToResponseEntity(() -> toEntityWithIdAndVersion(sourcingBasketService.findById(basketId)), this::convertToBasketDtoResponse);
    }

    @PostMapping(value = "/baskets/{basketId}/products")
    @Operation(summary = "[Add Product to Basket SAGA] starting point", tags = "Basket")
    public ResponseEntity<BasketDtoResponse> addProductToBasket(@PathVariable String basketId,
                                                                @RequestBody AddProductToBasketRequest addProductToBasketRequest) {
        logStartTime(LOGGER, EVENT_SOURCING_ADD_PRODUCT_TO_BASKET_PREFIX, basketId);
        return supplyAndConvertToResponseEntity(() -> sourcingBasketService.addProductToBasket(
                basketId,
                addProductToBasketRequest.getProductId(),
                addProductToBasketRequest.getQuantity(),
                addProductToBasketRequest.getPricePerUnit()
        ), this::convertToBasketDtoResponse);
    }

    private BasketDtoResponse convertToBasketDtoResponse(String basketId, Basket basket) {
        return new BasketDtoResponse(
                basketId,
                basket.getTotalQuantity(),
                basket.getTotalPrice(),
                basket.getProductEntries().values().stream()
                        .map(entry ->
                                new ProductBasketEntryDto(entry.getProductId(), entry.getQuantity(), entry.getPrice()))
                        .toList()
        );
    }
}

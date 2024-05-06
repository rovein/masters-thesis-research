package ua.nure.sagaresearch.baskets.event.web;

import static ua.nure.sagaresearch.common.util.ConverterUtil.supplyAndConvertToResponseEntity;
import static ua.nure.sagaresearch.common.util.ConverterUtil.toEntityWithIdAndVersion;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.sagaresearch.baskets.event.domain.Basket;
import ua.nure.sagaresearch.baskets.event.service.SourcingBasketService;
import ua.nure.sagaresearch.baskets.webapi.AddProductToBasketRequest;
import ua.nure.sagaresearch.baskets.webapi.BasketDtoResponse;
import ua.nure.sagaresearch.baskets.webapi.ProductBasketEntryDto;

@RestController
@RequestMapping("/event-sourcing")
@RequiredArgsConstructor
public class SourcingBasketController {

    private final SourcingBasketService sourcingBasketService;

    @PostMapping(value = "/baskets")
    public String createBasket() {
        return sourcingBasketService.createBasket().getEntityId();
    }

    @GetMapping(value = "/baskets/{basketId}")
    public ResponseEntity<BasketDtoResponse> getBasket(@PathVariable String basketId) {
        return supplyAndConvertToResponseEntity(() -> toEntityWithIdAndVersion(sourcingBasketService.findById(basketId)), this::convertToBasketDtoResponse);
    }

    @PostMapping(value = "/baskets/{basketId}/products")
    public ResponseEntity<BasketDtoResponse> addProductToBasket(@PathVariable String basketId,
                                                                @RequestBody AddProductToBasketRequest addProductToBasketRequest) {
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

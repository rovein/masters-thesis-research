package ua.nure.sagaresearch.baskets.web;

import static ua.nure.sagaresearch.common.util.LoggingUtils.ADD_PRODUCT_TO_BASKET_PREFIX;
import static ua.nure.sagaresearch.common.util.LoggingUtils.logStartTime;

import io.eventuate.common.json.mapper.JSonMapper;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.sagaresearch.baskets.domain.Basket;
import ua.nure.sagaresearch.baskets.service.BasketService;
import ua.nure.sagaresearch.baskets.webapi.AddProductToBasketRequest;
import ua.nure.sagaresearch.baskets.webapi.BasketDtoResponse;
import ua.nure.sagaresearch.baskets.webapi.ProductBasketEntryDto;

@RestController
@Tag(name = "Basket", description = "OUTBOX Basket API")
public class BasketController {

    private static final Logger logger = LoggerFactory.getLogger(BasketController.class);

    private final BasketService basketService;

    private final DomainSnapshotExportService<Basket> domainSnapshotExportService;

    @Autowired
    public BasketController(BasketService basketService,
                            DomainSnapshotExportService<Basket> domainSnapshotExportService) {
        this.basketService = basketService;
        this.domainSnapshotExportService = domainSnapshotExportService;
    }

    @PostMapping(value = "/baskets")
    @Operation(summary = "Create basket and receive basket ID", tags = "Basket")
    public String createEmptyBasket() {
        Basket basket = basketService.createBasket();
        return String.valueOf(basket.getId());
    }

    @GetMapping(value = "/baskets/{basketId}")
    @Operation(summary = "Get basket by its ID", tags = "Basket")
    public BasketDtoResponse getBasket(@PathVariable Long basketId) {
        Basket basket = basketService.getBasket(basketId);
        return convertToBasketDtoResponse(basket);
    }

    @PostMapping(value = "/baskets/{basketId}/products")
    @Operation(summary = "[Add Product to Basket SAGA] starting point", tags = "Basket")
    public BasketDtoResponse addProductToBasket(@PathVariable Long basketId, @RequestBody AddProductToBasketRequest addProductToBasketRequest) {
        logStartTime(logger, ADD_PRODUCT_TO_BASKET_PREFIX);
        Basket basket = basketService.addProductToBasket(
                basketId,
                addProductToBasketRequest.getProductId(),
                addProductToBasketRequest.getQuantity(),
                addProductToBasketRequest.getPricePerUnit()
        );
        return convertToBasketDtoResponse(basket);
    }

    @PostMapping(value = "/baskets/make-snapshot")
    public String makeSnapshot() {
        return JSonMapper.toJson(domainSnapshotExportService.exportSnapshots());
    }

    private BasketDtoResponse convertToBasketDtoResponse(Basket basket) {
        return new BasketDtoResponse(
                String.valueOf(basket.getId()),
                basket.getTotalQuantity(),
                basket.getTotalPrice(),
                basket.getProductEntries().values().stream()
                        .map(entry ->
                                new ProductBasketEntryDto(entry.getProductId(), entry.getQuantity(), entry.getPrice()))
                        .toList()
        );
    }
}

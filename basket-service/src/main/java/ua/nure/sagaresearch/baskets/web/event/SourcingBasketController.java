package ua.nure.sagaresearch.baskets.web.event;

import io.eventuate.EntityNotFoundException;
import io.eventuate.EntityWithIdAndVersion;
import io.eventuate.EntityWithMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nure.sagaresearch.baskets.domain.event.Basket;
import ua.nure.sagaresearch.baskets.service.event.SourcingBasketService;
import ua.nure.sagaresearch.baskets.webapi.BasketDtoResponse;
import ua.nure.sagaresearch.baskets.webapi.ProductBasketEntryDto;

@RestController
@RequestMapping("/event-sourcing")
@RequiredArgsConstructor
public class SourcingBasketController {

    private final SourcingBasketService sourcingBasketService;

    @PostMapping(value = "/baskets")
    public String createBasket() {
        EntityWithIdAndVersion<Basket> entity = sourcingBasketService.createBasket();
        return entity.getEntityId();
    }

    @GetMapping(value = "/baskets/{basketId}")
    public ResponseEntity<BasketDtoResponse> getBasket(@PathVariable String basketId) {
        EntityWithMetadata<Basket> basketWithMetadata;
        try {
            basketWithMetadata = sourcingBasketService.findById(basketId);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(convertToBasketDtoResponse(basketId, basketWithMetadata.getEntity()), HttpStatus.OK);
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

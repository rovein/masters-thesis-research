package ua.nure.sagaresearch.baskets.event.service;

import io.eventuate.EntityWithIdAndVersion;
import io.eventuate.EntityWithMetadata;
import io.eventuate.sync.AggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.nure.sagaresearch.baskets.event.domain.AddProductToBasketCommand;
import ua.nure.sagaresearch.baskets.event.domain.Basket;
import ua.nure.sagaresearch.baskets.event.domain.BasketCommand;
import ua.nure.sagaresearch.baskets.event.domain.CreateBasketCommand;
import ua.nure.sagaresearch.common.domain.Money;

@Service
@RequiredArgsConstructor
public class SourcingBasketService {
    private final AggregateRepository<Basket, BasketCommand> basketRepository;

    public EntityWithIdAndVersion<Basket> createBasket() {
        return basketRepository.save(new CreateBasketCommand());
    }

    public EntityWithMetadata<Basket> findById(String basketId) {
        return basketRepository.find(basketId);
    }

    public EntityWithIdAndVersion<Basket> addProductToBasket(String basketId, String productId, Long quantity, Money pricePerUnit) {
        AddProductToBasketCommand addProductToBasketCommand = new AddProductToBasketCommand(productId, quantity, pricePerUnit);
        return basketRepository.update(basketId, addProductToBasketCommand);
    }
}

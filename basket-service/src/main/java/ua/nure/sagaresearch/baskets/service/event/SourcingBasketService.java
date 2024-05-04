package ua.nure.sagaresearch.baskets.service.event;

import io.eventuate.EntityWithIdAndVersion;
import io.eventuate.EntityWithMetadata;
import io.eventuate.sync.AggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.nure.sagaresearch.baskets.domain.event.Basket;
import ua.nure.sagaresearch.baskets.domain.event.BasketCommand;
import ua.nure.sagaresearch.baskets.domain.event.CreateBasketCommand;

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
}


package ua.nure.sagaresearch.baskets.domain.events.sourcing;

import io.eventuate.Event;
import io.eventuate.EventEntity;

@EventEntity(entity = "ua.nure.sagaresearch.baskets.event.domain.Basket")
public interface SourcingBasketEvent extends Event {
}

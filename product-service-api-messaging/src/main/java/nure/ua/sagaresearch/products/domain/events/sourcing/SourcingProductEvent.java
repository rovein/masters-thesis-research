package nure.ua.sagaresearch.products.domain.events.sourcing;

import io.eventuate.Event;
import io.eventuate.EventEntity;

@EventEntity(entity = "ua.nure.sagaresearch.products.event.domain.Product")
public interface SourcingProductEvent extends Event {
}

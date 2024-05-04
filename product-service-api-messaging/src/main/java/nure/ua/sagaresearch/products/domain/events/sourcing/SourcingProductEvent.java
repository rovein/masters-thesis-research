package nure.ua.sagaresearch.products.domain.events.sourcing;

import io.eventuate.Event;
import io.eventuate.EventEntity;

@EventEntity(entity = "ua.nure.sagaresearch.products.domain.event.Product")
public interface SourcingProductEvent extends Event {
}

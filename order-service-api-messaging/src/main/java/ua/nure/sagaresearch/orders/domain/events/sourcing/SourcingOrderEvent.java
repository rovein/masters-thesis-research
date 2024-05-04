package ua.nure.sagaresearch.orders.domain.events.sourcing;

import io.eventuate.Event;
import io.eventuate.EventEntity;

@EventEntity(entity = "ua.nure.sagaresearch.products.domain.event.Product")
public interface SourcingOrderEvent extends Event {
}

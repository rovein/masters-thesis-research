package ua.nure.sagaresearch.orders.domain.events.sourcing;

import io.eventuate.Event;
import io.eventuate.EventEntity;

@EventEntity(entity = "ua.nure.sagaresearch.orders.event.domain.Order")
public interface SourcingOrderEvent extends Event {
}

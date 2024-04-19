package ua.nure.sagaresearch.orders.domain.events;

import io.eventuate.tram.events.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.nure.sagaresearch.common.domain.Money;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderSnapshotEvent implements DomainEvent {
    private Long id;
    private Long customerId;
    private Money orderTotal;
    private OrderState orderState;
}

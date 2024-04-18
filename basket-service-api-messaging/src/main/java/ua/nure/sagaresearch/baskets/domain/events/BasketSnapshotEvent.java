package ua.nure.sagaresearch.baskets.domain.events;

import io.eventuate.tram.events.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.nure.sagaresearch.common.domain.Money;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BasketSnapshotEvent implements DomainEvent {
    private Long id;
    private Long totalQuantity;
    private Money totalPrice;
}

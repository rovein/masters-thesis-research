package ua.nure.sagaresearch.products.event.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReserveProductQuantityCommand implements ProductCommand {
    private String orderId;
    private long quantity;
}

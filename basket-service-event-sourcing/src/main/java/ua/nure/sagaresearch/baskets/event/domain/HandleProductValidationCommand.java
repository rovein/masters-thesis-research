package ua.nure.sagaresearch.baskets.event.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HandleProductValidationCommand implements BasketCommand {
    private String productId;
}

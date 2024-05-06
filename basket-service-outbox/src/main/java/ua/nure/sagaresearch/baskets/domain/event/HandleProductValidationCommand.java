package ua.nure.sagaresearch.baskets.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class HandleProductValidationCommand implements BasketCommand {
    private String productId;
}

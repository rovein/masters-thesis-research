package ua.nure.sagaresearch.baskets.domain;

import lombok.Getter;
import lombok.Setter;
import ua.nure.sagaresearch.baskets.domain.events.ProductBasketEntry;
import ua.nure.sagaresearch.common.domain.Money;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Collections;
import java.util.Map;

@Entity
@Table(name = "Basket")
@Access(AccessType.FIELD)
@Getter
@Setter
public class Basket implements BaseBasket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalQuantity;

    @Embedded
    private Money totalPrice;

    @ElementCollection
    private Map<String, ProductBasketEntry> productEntries;

    private Long creationTime;

    @Version
    private Long version;

    public Basket() {
        clearProductEntries();
        this.creationTime = System.currentTimeMillis();
    }

    public void clearProductEntries() {
        this.totalQuantity = 0L;
        this.totalPrice = Money.ZERO;
        this.productEntries = Collections.emptyMap();
    }
}

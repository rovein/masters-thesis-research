package ua.nure.sagaresearch.products.domain;

import lombok.Getter;
import lombok.Setter;
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
import java.util.Collections;
import java.util.Map;

@Entity
@Table(name = "Product")
@Access(AccessType.FIELD)
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String description;

    @Embedded
    private Money productPrice;

    private String image;

    private Long productQuantity;

    @ElementCollection
    private Map<Long, ProductProperty> productProperties;

    public Product() {
        this.productQuantity = 0L;
        this.productPrice = Money.ZERO;
        this.productProperties = Collections.emptyMap();
    }
}

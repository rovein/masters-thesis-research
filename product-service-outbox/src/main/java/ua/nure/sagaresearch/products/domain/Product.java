package ua.nure.sagaresearch.products.domain;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import ua.nure.sagaresearch.common.domain.Money;
import ua.nure.sagaresearch.common.domain.product.ProductProperty;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

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

    public void decreaseQuantity(long decreaseValue) {
        long newQuantity = productQuantity - decreaseValue;
        Preconditions.checkState(newQuantity >= 0, "New quantity is going to be " + newQuantity);
        this.productQuantity = newQuantity;
    }
}

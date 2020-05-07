package com.vidaloca.skibidi.product.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.product.validation.Price;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @NotNull(message = "Product category is obligatory")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;

    @NotNull(message = "Name of product is obligatory")
    @Length(min=3, max=20, message = "Product name length has to be between 3 and 20")
    private String name;

    @NotNull(message = "Price is obligatory")
    @Price
    private BigDecimal price;

    @Builder.Default
    private Integer quantity = 1;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="event_user_id")
    private EventUser eventUser;

}

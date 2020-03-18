package com.vidaloca.skibidi.product.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.product.validation.Price;
import lombok.AllArgsConstructor;
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

    @ManyToMany(mappedBy = "products",  cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<EventUser> eventUsers;


    public static final class ProductBuilder {
        private Long id;
        private ProductCategory productCategory;
        private String name;
        private BigDecimal price;
        private List<EventUser> eventUsers;

        private ProductBuilder() {
        }

        public static ProductBuilder aProduct() {
            return new ProductBuilder();
        }

        public ProductBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder withProductCategory(ProductCategory productCategory) {
            this.productCategory = productCategory;
            return this;
        }

        public ProductBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductBuilder withEventUsers(List<EventUser> eventUsers) {
            this.eventUsers = eventUsers;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.setId(id);
            product.setProductCategory(productCategory);
            product.setName(name);
            product.setPrice(price);
            product.setEventUsers(eventUsers);
            return product;
        }
    }
}

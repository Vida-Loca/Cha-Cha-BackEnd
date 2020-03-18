package com.vidaloca.skibidi.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "product_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_category_id")
    @JsonIgnore
    private Long id;

    @NotNull(message = "Name of category cannot be null")
    @Length(min=3, max=20, message = "Product category length has to be between 3 and 20")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "productCategory", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    private List<Product> products;


    public static final class ProductCategoryBuilder {
        private Long id;
        private String name;
        private List<Product> products;

        private ProductCategoryBuilder() {
        }

        public static ProductCategoryBuilder aProductCategory() {
            return new ProductCategoryBuilder();
        }

        public ProductCategoryBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ProductCategoryBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ProductCategoryBuilder withProducts(List<Product> products) {
            this.products = products;
            return this;
        }

        public ProductCategory build() {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setId(id);
            productCategory.setName(name);
            productCategory.setProducts(products);
            return productCategory;
        }
    }
}

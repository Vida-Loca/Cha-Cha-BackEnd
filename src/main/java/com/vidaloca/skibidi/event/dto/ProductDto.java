package com.vidaloca.skibidi.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotNull
    @NotEmpty
    @Length(max=40, min=2)
    private String name;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "(\\d+\\.\\d{1,2})")
    private String price;
    @NotNull
    @NotEmpty
    private String productCategory;
}

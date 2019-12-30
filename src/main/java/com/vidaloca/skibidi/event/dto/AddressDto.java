package com.vidaloca.skibidi.event.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    @NotNull
    @NotEmpty
    private String country;
    @NotNull
    @NotEmpty
    private String city;
    @NotNull
    @NotEmpty
    private String postcode;
    private String street;
    @NotNull
    @NotEmpty
    private String number;

    public AddressDto(@NotEmpty String country, @NotEmpty String city, @NotEmpty String postcode, @NotEmpty String number) {
        this.country = country;
        this.city = city;
        this.postcode = postcode;
        this.number = number;
    }
}

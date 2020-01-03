package com.vidaloca.skibidi.event.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    @NotNull
    @NotEmpty
    @Length(max=255,min=3,message = "bad length")
    private String country;
    @NotNull
    @NotEmpty
    @Length(max=255,min=3,message = "bad length")
    private String city;
    @NotNull
    @NotEmpty
    @Length(max = 10, min=2, message = "postcode length cannot be above 10 and less than 2 ")
    private String postcode;
    @Length(max = 255, message = "bad length")
    private String street;
    @NotNull
    @NotEmpty
    @Length(max = 7, message = "bad length")
    private String number;

    public AddressDto(@NotEmpty String country, @NotEmpty String city, @NotEmpty String postcode, @NotEmpty String number) {
        this.country = country;
        this.city = city;
        this.postcode = postcode;
        this.number = number;
    }
}

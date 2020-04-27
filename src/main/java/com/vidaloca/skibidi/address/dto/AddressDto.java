package com.vidaloca.skibidi.address.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private String country;
    private String city;
    private String postcode;
    private String street;
    private String number;
    private Float longitude;
    private Float latitude;

    public AddressDto(String country, String city, String postcode, String street, String number) {
        this.country = country;
        this.city = city;
        this.postcode = postcode;
        this.street = street;
        this.number = number;
    }
}

package com.vidaloca.skibidi.address.repository;

import com.vidaloca.skibidi.address.model.Address;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AddressRepository extends CrudRepository<Address,Integer> {
    Optional<Address> findByCountryAndCityAndPostcodeAndStreetAndNumber(String country, String city, String postcode, String street, String number);
}

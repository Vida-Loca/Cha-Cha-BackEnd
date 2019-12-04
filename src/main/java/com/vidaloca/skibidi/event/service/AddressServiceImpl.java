package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.AddressDto;
import com.vidaloca.skibidi.event.repository.AddressRepository;
import com.vidaloca.skibidi.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address addAddress(AddressDto addressDto) {
        Address address = new Address();
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setPostcode(addressDto.getPostcode());
        address.setStreet(addressDto.getStreet());
        address.setNumber(addressDto.getNumber());
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Integer id, AddressDto addressDto) {
        Address address =addressRepository.findById(id).orElse(null);
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setPostcode(addressDto.getPostcode());
        address.setStreet(addressDto.getStreet());
        address.setNumber(addressDto.getNumber());
        return addressRepository.save(address);
    }

}

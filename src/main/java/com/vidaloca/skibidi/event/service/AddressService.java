package com.vidaloca.skibidi.event.service;

import com.vidaloca.skibidi.event.dto.AddressDto;
import com.vidaloca.skibidi.model.Address;

public interface AddressService {
    Address addAddress(AddressDto addressDto);
    Address updateAddress(Integer id, AddressDto addressDto);
}

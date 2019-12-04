package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.event.dto.AddressDto;
import com.vidaloca.skibidi.event.repository.AddressRepository;
import com.vidaloca.skibidi.event.service.AddressService;
import com.vidaloca.skibidi.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {

    private AddressRepository addressRepository;
    private AddressService as;

    @Autowired
    public AddressController(AddressRepository addressRepository, AddressService as){
        this.addressRepository = addressRepository;
        this.as = as;
    }

    @GetMapping("/address")
    public List<Address> getAll(){
        return (List<Address>) addressRepository.findAll();
    }
    @GetMapping("/address/{id}")
    public Address get(@PathVariable Integer id){
        return  addressRepository.findById(id).orElse(null);
    }
    @PostMapping("/address")
    public String add(@RequestBody AddressDto addressDto){
        as.addAddress(addressDto);
        return "Successfully added address";
    }
    @DeleteMapping("/address/{id}")
    public String delete(@PathVariable Integer id){
        addressRepository.deleteById(id);
        return "Deleted successfully co nie Daro XD ?";
    }
    @PutMapping("/address/{id}")
    public String update(@PathVariable Integer id, @RequestBody AddressDto addressDto){
        as.updateAddress(id,addressDto);
        return "Successfully update address";
    }
}

package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.event.dto.AddressDto;
import com.vidaloca.skibidi.event.repository.AddressRepository;
import com.vidaloca.skibidi.event.service.AddressService;
import com.vidaloca.skibidi.event.model.Address;
import com.vidaloca.skibidi.user.registration.utills.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public GenericResponse add(@Valid @RequestBody AddressDto addressDto){
        as.addAddress(addressDto);
        return new GenericResponse("Success");
    }
    @DeleteMapping("/address/{id}")
    public GenericResponse delete(@PathVariable Integer id){
        addressRepository.deleteById(id);
        return new GenericResponse("Success");
    }
    @PutMapping("/address/{id}")
    public GenericResponse update(Integer id, @Valid @RequestBody AddressDto addressDto){
        as.updateAddress(id,addressDto);
        return new GenericResponse("Success");
    }
}

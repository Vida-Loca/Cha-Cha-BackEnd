package com.vidaloca.skibidi.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.event.dto.AddressDto;
import com.vidaloca.skibidi.event.repository.AddressRepository;
import com.vidaloca.skibidi.event.service.AddressService;
import com.vidaloca.skibidi.model.Address;
import com.vidaloca.skibidi.registration.utills.GenericResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AddressControllerTest {

    @Mock
    AddressRepository addressRepository;
    @Mock
    AddressService addressService;
    @InjectMocks
    AddressController addressController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(addressController)
                .build();
    }

    @Test
    void getAll() {
        Address a1 = new Address();
        a1.setCountry("Add1");
        a1.setCity("Add1");
        a1.setPostcode("Add1");
        a1.setNumber("Add1");
        Address a2 = new Address();
        a2.setCountry("Add2");
        a2.setCity("Add2");
        a2.setPostcode("Add2");
        a2.setNumber("Add2");
        List<Address> addresses = new ArrayList<>();
        addresses.add(a1);
        addresses.add(a2);

        when(addressRepository.findAll()).thenReturn(addresses);

        List<Address> result = addressController.getAll();

        verify(addressRepository, times(1)).findAll();
        assertEquals(a1.getCountry(), result.get(0).getCountry());
        assertEquals(2, result.size());
    }

    @Test
    void getAllStatus() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/address")
        ).andExpect(status().isOk());
    }

    @Test
    void getAllVerifyTimes() {
        addressController.getAll();
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    void get() {
        Address a1 = new Address();
        a1.setAddress_id(1);
        a1.setCountry("Address1");
        a1.setCity("Add1");
        a1.setPostcode("Add1");
        a1.setNumber("Add1");
        List<Address> addresses = new ArrayList<>();
        addresses.add(a1);

        when(addressRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(a1));

        Address address = addressController.get(1);

        verify(addressRepository, times(1)).findById(1);
        assertEquals("Add1", address.getCity());
    }

    @Test
    void getStatus() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/address/{id}", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void getVerifyTimes() {
        addressController.get(1);
        verify(addressRepository, times(1)).findById(1);
    }

    @Test
    void add() {
        Address a1 = new Address();
        a1.setAddress_id(1);
        a1.setCountry("Address1");
        a1.setCity("Add1");
        a1.setPostcode("Add1");
        a1.setNumber("Add1");

        when(addressService.addAddress(any(AddressDto.class))).thenReturn(a1);

        AddressDto addressDto = new AddressDto();
        GenericResponse str = addressController.add(addressDto);

        verify(addressService, times(1)).addAddress(addressDto);
        assertEquals("Success", str.getMessage());
    }

    @Test
    void addStatus() throws Exception {
        AddressDto addressDto = new AddressDto("lala", "lala", "lala", "lala", "lala");
        mockMvc.perform(
                post("/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(addressDto))
        ).andExpect(status().isOk());
    }

    @Test
    void addVerifyTimes() {
        AddressDto addressDto = new AddressDto();
        addressController.add(addressDto);
        verify(addressService, times(1)).addAddress(addressDto);
    }

    @Test
    void testDelete() {
        addressController.delete(1);
        verify(addressRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteStatus() throws Exception {
        mockMvc.perform(
                delete("/address/{id}", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void update() {
        Address address = new Address();
        address.setAddress_id(1);
        AddressDto addressDto = new AddressDto("UPDT", "UPDT", "UPDT", "UPD", "UPD");

        addressController.update(1, addressDto);
        verify(addressService, times(1)).updateAddress(1, addressDto);
    }

    @Test
    void updateStatus() throws Exception {
        AddressDto addressDto = new AddressDto("UPDT", "UPDT", "UPDT", "UPD", "UPD");

        mockMvc.perform(
                put("/address/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addressDto))
        ).andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
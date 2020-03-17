package com.vidaloca.skibidi.address;

import com.vidaloca.skibidi.address.dto.AddressDto;
import com.vidaloca.skibidi.address.repository.AddressRepository;
import com.vidaloca.skibidi.address.model.Address;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@ComponentScan("com.vidaloca.skibidi.event.service")
@SpringBootTest
public class AddressTests {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AddressService addressService;

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterClass
    public static void close() {
        validatorFactory.close();
    }

    @Transactional
    @Test
    public void testAddCorrectAddress() {
        List<Address> addresses = (List<Address>) addressRepository.findAll();
        AddressDto addressDto = new AddressDto("Test", "Test", "Test", "Test", "Test");
        addressService.addAddress(addressDto);
        List<Address> addressesAfter = (List<Address>) addressRepository.findAll();
        Assert.assertNotSame(addresses, addressesAfter);
    }

    @Test
    public void testAddIncorrectAddress() {
        AddressDto addressDto = new AddressDto("a", "a", "a", "aaaaaaaa");

        Set<ConstraintViolation<AddressDto>> violations
                = validator.validate(addressDto);

        Assert.assertEquals(4, violations.size());
    }

    @Test
    public void testUpdateAddressCorrect() {
        AddressDto addressDto = new AddressDto("Test", "Test", "Test", "Test");
        addressService.addAddress(addressDto);

        AddressDto addressUpdate = new AddressDto("CHANGE", "Test", "Test", "Test");
        addressService.updateAddress(1, addressUpdate);

        Assert.assertEquals("CHANGE", addressRepository.findById(1).get().getCountry());
    }

    @Transactional
    @Test
    public void testUpdateAddressIncorrect() {
        AddressDto addressDto = new AddressDto("Test", "Test", "Test", "Test");
        addressService.addAddress(addressDto);

        AddressDto addressUpdate = new AddressDto("a", "Test", "Test", "Test");
        addressService.updateAddress(1, addressUpdate);

        Set<ConstraintViolation<AddressDto>> violations
                = validator.validate(addressUpdate);

//        for (ConstraintViolation v : violations) {
//            System.out.println(v.getMessage());
//        }
        Assert.assertEquals(1, violations.size());
    }

    @Test
    public void testUpdateNullAddress() {
        AddressDto addressUpdate = new AddressDto("a", "Test", "Test", "Test");
        Address address = addressService.updateAddress(-3, addressUpdate);
        Assert.assertNull(address);
    }


}

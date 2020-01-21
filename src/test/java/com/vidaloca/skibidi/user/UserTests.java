package com.vidaloca.skibidi.user;

import com.vidaloca.skibidi.exceptions.EmailExistsException;
import com.vidaloca.skibidi.exceptions.UsernameExistsException;
import com.vidaloca.skibidi.model.Role;
import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.model.VerificationToken;
import com.vidaloca.skibidi.registration.dto.UserDto;
import com.vidaloca.skibidi.registration.repository.TokenRepository;
import com.vidaloca.skibidi.registration.repository.UserRepository;
import com.vidaloca.skibidi.registration.service.UserService;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;

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
    public void testRegisterNewUserAccount() throws EmailExistsException, UsernameExistsException {
        List<User> usersBefore = (List<User>) userRepository.findAll();
        UserDto userDto = new UserDto("new", "New", "New", "password", "password", "mail@o2.pl");
        userService.registerNewUserAccount(userDto);
        System.out.println(userDto.toString());
        List<User> usersAfter = (List<User>) userRepository.findAll();
        Assert.assertNotSame(usersBefore, usersAfter);
    }

    @Test(expected = EmailExistsException.class)
    public void testRegisterNewUserAccountWithExistingEmail() throws EmailExistsException, UsernameExistsException {
        UserDto userDto = new UserDto();
        userDto.setUsername("newU");
        userDto.setName("New");
        userDto.setSurname("New");
        userDto.setPassword("pass");
        userDto.setMatchingPassword("pass");
        userDto.setEmail("test1@o2.pl");
        userService.registerNewUserAccount(userDto);
    }

    @Test(expected = UsernameExistsException.class)
    public void testRegisterNewUserAccountWithExistingUsername() throws EmailExistsException, UsernameExistsException {
        UserDto userDto = new UserDto();
        userDto.setUsername("testowy1");
        userDto.setName("New");
        userDto.setSurname("New");
        userDto.setPassword("pass");
        userDto.setMatchingPassword("pass");
        userDto.setEmail("jakismail@o2.pl");
        userDto.setPicUrl("url");
        userService.registerNewUserAccount(userDto);
    }

    @Test
    public void testMailValidator() {
        UserDto userDto = new UserDto("user", "user", "user", "user", "user", "notmail", "url");

        Set<ConstraintViolation<UserDto>> violations
                = validator.validate(userDto);

        for (ConstraintViolation v : violations) {
            System.out.println(v.getMessage());
        }

        Assert.assertEquals(1, violations.size());
    }

    @Test
    public void testGetUser() {
        User user = userService.getUser("TOKEN");
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getUsername(), "testowy1");
    }

    @Test(expected = NullPointerException.class)
    public void testGetNotExistingUser() {
        User user = userService.getUser("BADTOKEN");
        Assert.assertNull(user);
    }

    @Test
    public void testGetVerificationToken() {
        VerificationToken verificationToken = userService.getVerificationToken("TOKEN");
        Assert.assertNotNull(verificationToken);
        Assert.assertEquals("testowy1", verificationToken.getUser().getUsername());
    }

    @Test
    public void testGetNotExistingVToken() {
        VerificationToken verificationToken = userService.getVerificationToken("BTOKEN");
        Assert.assertNull(verificationToken);
    }

    @Transactional
    @Test
    public void testSaveRegisteredUser() {
        List<User> usersB = (List<User>) userRepository.findAll();
        Role role = new Role();
        role.setId(1);
        role.setName("USER");
        User user = new User();
        user.setRole(role);
        user.setEnabled(true);
        user.setId(9999L);
        user.setName("imie");
        user.setEmail("imie@mail.com");
        user.setPassword("password");
        user.setUsername("usrnme");
        userService.saveRegisteredUser(user);
        List<User> usersA = (List<User>) userRepository.findAll();
        Assert.assertNotSame(usersB, usersA);
    }

    @Transactional
    @Test
    public void testCreateVToken() {
        List<VerificationToken> tokensB = (List<VerificationToken>) tokenRepository.findAll();
        String token = "NEW TOKEN";
        User user = userRepository.findById(1L).orElse(null);

        userService.createVerificationToken(user, token);

        List<VerificationToken> tokensA = (List<VerificationToken>) tokenRepository.findAll();
        Assert.assertNotSame(tokensB, tokensA);
    }

    @Test
    public void testValidateNotExistingToken() {
        String str = userService.validateVerificationToken("NOTEXIST");
        Assert.assertEquals("invalidToken", str);
    }

    @Test
    public void testValidateExpiredToken() {
        VerificationToken expired = new VerificationToken();
        expired.setId(2L);
        expired.setUser(userRepository.findById(1L).orElse(null));
        expired.setExpiryDate(Date.valueOf("2002-01-01"));
        expired.setToken("EXPIRED");
        tokenRepository.save(expired);

        String str = userService.validateVerificationToken("EXPIRED");
        Assert.assertEquals("expired", str);
    }

    @Test
    public void testValidateToken() {
        String str = userService.validateVerificationToken("TOKEN");
        Assert.assertEquals("valid", str);
    }

    @Test
    public void testGenerateNewToken() {
        List<VerificationToken> tokensB = (List<VerificationToken>) tokenRepository.findAll();

        VerificationToken expired = new VerificationToken();
        expired.setId(2L);
        expired.setUser(userRepository.findById(1L).orElse(null));
        expired.setExpiryDate(Date.valueOf("2002-01-01"));
        expired.setToken("EXPIRED");
        tokenRepository.save(expired);

        userService.generateNewVerificationToken("EXPIRED");

        List<VerificationToken> tokensA = (List<VerificationToken>) tokenRepository.findAll();
        Assert.assertNotSame(tokensB, tokensA);
    }
}

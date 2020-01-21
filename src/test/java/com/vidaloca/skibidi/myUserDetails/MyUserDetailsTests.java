package com.vidaloca.skibidi.myUserDetails;

import com.vidaloca.skibidi.model.User;
import com.vidaloca.skibidi.registration.repository.UserRepository;
import com.vidaloca.skibidi.registration.service.MyUserDetailsService;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyUserDetailsTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Test
    public void testLoadExistingUser() {
        String username = "testowy1";
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        Assert.assertEquals("test1", userDetails.getPassword());
    }

    @Test(expected = RuntimeException.class)
    public void testLoadNotExistingUser() {
        String username = "NotExisting";
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        expectedException.expectMessage("No user found with username: NotExisting");
    }

    @Test
    public void testLoadUserByExistingId() {
        User user = myUserDetailsService.loadUserById(1L);
        Assert.assertEquals("Test1", user.getName());
    }

    @Test
    public void testLoadUserByNotExistingId() {
        User user = myUserDetailsService.loadUserById(-10L);
        Assert.assertSame(null, user);
    }


}

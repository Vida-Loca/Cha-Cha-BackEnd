//package com.vidaloca.skibidi;
//
//import com.vidaloca.skibidi.unit.user.registration.dto.UserRegistrationDto;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//
//@Sql("/data-test.sql")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class AaaaaIT {
//
//    @Autowired
//    TestRestTemplate restTemplate;
//
//    @LocalServerPort
//    int port;
//
//    @Test
//    @Transactional
//    void name() throws URISyntaxException {
//        String ur = "http://localhost:" + port + "/registration";
//        URI uri = new URI(ur);
//
//        UserRegistrationDto dto = new UserRegistrationDto();
//        dto.setName("TestName2222");
//        dto.setSurname("TestSurname");
//        dto.setUsername("testUsername");
//        dto.setPassword("password");
//        dto.setMatchingPassword("password");
//        dto.setEmail("testMail@mail.com");
//        dto.setPicUrl("picUrl");
//
//        restTemplate.postForEntity(uri, dto, String.class);
//    }
//}

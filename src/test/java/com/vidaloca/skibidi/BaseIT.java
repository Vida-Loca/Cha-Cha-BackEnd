package com.vidaloca.skibidi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;

@Sql("/data-test.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application.properties")
@AutoConfigureMockMvc
public abstract class BaseIT {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    protected String asJson(Object o) throws JsonProcessingException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);
        String jsonString = mapper.writeValueAsString(o);
        mapper.setDateFormat(df);
        return jsonString;
    }
}

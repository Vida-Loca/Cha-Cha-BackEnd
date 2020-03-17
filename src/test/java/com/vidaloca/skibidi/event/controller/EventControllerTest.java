package com.vidaloca.skibidi.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vidaloca.skibidi.event.dto.AddressDto;
import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.product.dto.ProductDto;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.EventUserRepository;
import com.vidaloca.skibidi.event.repository.ProductRepository;
import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.event.service.ProductService;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.model.EventUser;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.user.model.User;
import com.vidaloca.skibidi.user.repository.UserRepository;
import com.vidaloca.skibidi.user.registration.utills.GenericResponse;
import com.vidaloca.skibidi.common.configuration.security.JwtAuthenticationFilter;
import com.vidaloca.skibidi.common.configuration.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.easymock.EasyMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class EventControllerTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventService eventService;
    @Mock
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventUserRepository event_userRepository;


    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(eventController)
                .build();
    }

    @Test
    void getEvents() {
        Event event1 = new Event();
        event1.setName("Event1");
        Event event2 = new Event();
        event2.setName("Event2");
        List<Event> eventList = new ArrayList<>();
        eventList.add(event1);
        eventList.add(event2);

        when(eventRepository.findAll()).thenReturn(eventList);

        List<Event> result = eventController.getEvents();

        verify(eventRepository, times(1)).findAll();
        assertEquals(event1.getName(), result.get(0).getName());
    }

    @Test
    void getEventStatus() throws Exception {
        mockMvc.perform(
                get("/event")
        ).andExpect(status().isOk());
    }

    @Test
    void getEventVerifyTimes() {
        eventController.getEvents();

        verify(eventRepository, times(1)).findAll();
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void getEventById() {
        Event event1 = new Event();
        event1.setEvent_id(1);
        event1.setName("Event1");

        when(eventRepository.findById(1)).thenReturn(java.util.Optional.of(event1));

        Event event = eventController.getEventById(1);

        verify(eventRepository, times(1)).findById(1);
        assertEquals("Event1", event.getName());
    }

    @Test
    void getEventByIdStatus() throws Exception {
        Event event = new Event();
        event.setEvent_id(1);
        event.setName("EVENT");

        when(eventRepository.findById(event.getEvent_id())).thenReturn(java.util.Optional.of(event));

        mockMvc.perform(
                get("/event/{id}", event.getEvent_id())
        ).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void getEventByIdVerifyTimes() {
        eventController.getEventById(1);

        verify(eventRepository, times(1)).findById(1);
        verifyNoMoreInteractions(eventRepository);
    }

    @Test
    void addNewEvent() {
        User user = new User();
        user.setId(1L);
        AddressDto addressDto = new AddressDto("Raz", "raz", "raz", "raz", "raz");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");
        eventDto.setName("AddEvent");
        eventDto.setStartDate(Date.valueOf("2030-01-01"));
        eventDto.setAddress(addressDto);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(eventService.addNewEvent(eventDto, 1L)).thenReturn("Successfully added event");

        GenericResponse str = eventController.addNewEvent(eventDto, request);

        assertEquals("Successfully added event", str.getMessage());
        verify(eventService, times(1)).addNewEvent(eventDto, 1L);
    }

    @Test
    void addNewEventStatus() throws Exception {
        AddressDto addressDto = new AddressDto("Raz", "raz", "raz", "raz");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);

        mockMvc.perform(
                post("/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(eventDto))
                        .content(String.valueOf(request))
        ).andDo(print());
    }

    @Test
    void addNewEventVerifyTimes() {
        AddressDto addressDto = new AddressDto("Raz", "raz", "raz", "raz");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);

        eventController.addNewEvent(eventDto, request);

        verify(eventService, times(1)).addNewEvent(eventDto, 1L);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    void updateEventNullUser() {
        Event event = new Event();
        event.setEvent_id(1);
        AddressDto addressDto = new AddressDto("Raz", "raz", "raz", "raz");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(-1L);
        when(eventService.updateEvent(eventDto, 1, -1L)).thenReturn("Unexpected failure");

        GenericResponse str = eventController.updateEvent(eventDto, 1, request);

        assertEquals("Unexpected failure", str.getMessage());
    }

    @Test
    void updateEvent() {
        Event event = new Event();
        event.setEvent_id(1);
        AddressDto addressDto = new AddressDto("Raz", "raz", "raz", "raz");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(eventService.updateEvent(eventDto, 1, 1L)).thenReturn("Updated successfully");

        GenericResponse str = eventController.updateEvent(eventDto, 1, request);

        assertEquals("Updated successfully", str.getMessage());
        verify(eventService, times(1)).updateEvent(eventDto, 1, 1L);
    }

    @Test
    void updateEventStatus() throws Exception {
        Event event = new Event();
        event.setEvent_id(1);
        AddressDto addressDto = new AddressDto("Raz", "raz", "raz", "raz");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);

        when(eventRepository.findById(1)).thenReturn(java.util.Optional.of(event));

        mockMvc.perform(
                put("/event/{id}", event.getEvent_id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(eventDto))
                        .content(String.valueOf(request))
        ).andDo(print());
    }

    @Test
    void updateEventVerifyTimes() {
        AddressDto addressDto = new AddressDto("Raz", "raz", "raz", "raz");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);

        eventController.updateEvent(eventDto, 1, request);

        verify(eventService, times(1)).updateEvent(eventDto, 1, 1L);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    void deleteById() {
        Event event = new Event();
        event.setEvent_id(1);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(eventService.deleteEvent(1, 1L)).thenReturn("Event delete successfully");

        GenericResponse str = eventController.deleteById(1, request);

        assertEquals("Event delete successfully", str.getMessage());
        verify(eventService, times(1)).deleteEvent(1, 1L);
    }

    @Test
    void deleteByIdStatus() throws Exception {
        mockMvc.perform(
                delete("/event/{id}", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void deleteByIdVerifyTimes() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);

        eventController.deleteById(1, request);

        verify(eventService, times(1)).deleteEvent(1, 1L);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    void getEventProducts() {
        Product product1 = new Product();
        product1.setName("Cola");
        Product product2 = new Product();
        product2.setName("Fanta");
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        Event event = new Event();
        event.setName("EVENT");
        event.setEvent_id(1);

        when(eventService.findAllEventProducts(1)).thenReturn(products);

        List<Product> result = eventController.getEventProducts(1);

        assertEquals(result.get(0).getName(), "Cola");
        verify(eventService, times(1)).findAllEventProducts(1);
    }

    @Test
    void getEventProductsStatus() throws Exception {
        mockMvc.perform(
                get("/event/{id}/product", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void getEventProductsVerifyTimes() {
        eventController.getEventProducts(1);

        verify(eventService, times(1)).findAllEventProducts(1);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    void addProductToEventByDto() {
        ProductDto productDto = new ProductDto("Product", "20.0", "Category");
        Product product = new Product();

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(productService.addProduct(productDto)).thenReturn(product);
        when(eventService.addProductToEvent(product, 1, 1L)).thenReturn("Successfully added product");

        GenericResponse str = eventController.addProductToEvent(productDto, 1, request);

        assertEquals("Successfully added product", str.getMessage());
        verify(productService, times(1)).addProduct(productDto);
        verify(eventService, times(1)).addProductToEvent(product, 1, 1L);
    }

    @Test
    void addNullProductToEventByDto() {
        ProductDto productDto = null;

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);

        GenericResponse str = eventController.addProductToEvent(productDto, 1, request);

        assertEquals("Cant't add product", str.getMessage());
    }

    @Test
    void addProductToEventByDtoStatus() throws Exception {
        ProductDto productDto = new ProductDto("Product", "20.0", "Category");

        mockMvc.perform(
                post("/event/{id}/productNew", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(productDto))
        ).andExpect(status().isOk());
    }

    @Test
    void addProductToEventByDtoVerifyTimes() {
        ProductDto productDto = new ProductDto("Product", "20.0", "Category");
        Product product = new Product();
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(productService.addProduct(productDto)).thenReturn(product);

        eventController.addProductToEvent(productDto, 1, request);

        verify(productService, times(1)).addProduct(productDto);
        verify(eventService, times(1)).addProductToEvent(product, 1, 1L);
        verifyNoMoreInteractions(productService, eventService);
    }

    @Test
    void addProductToEventById() {
        Product product = new Product();
        product.setId(1);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(productRepository.findById(1)).thenReturn(java.util.Optional.of(product));
        when(eventService.addProductToEvent(product, 1, 1L)).thenReturn("Successfully added product");

        GenericResponse response = eventController.addProductToEvent(1, 1, request);

        assertEquals("Successfully added product", response.getMessage());
    }

    @Test
    void addNullProductToEventById() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);

        GenericResponse str = eventController.addProductToEvent(1, 1, request);

        assertEquals("Product not exist", str.getMessage());
    }

    @Test
    void addProductToEventByIdStatus() throws Exception {
        mockMvc.perform(
                post("/event/{id}/product", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("productId", String.valueOf(1))
        ).andExpect(status().isOk());
    }

    @Test
    void addProductToEventByIdVerifyTimes() {
        Product product = new Product();
        product.setId(1);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(productRepository.findById(1)).thenReturn(java.util.Optional.of(product));


        eventController.addProductToEvent(1, 1, request);

        verify(productRepository, times(1)).findById(1);
        verify(eventService, times(1)).addProductToEvent(product, 1, 1L);
        verifyNoMoreInteractions(productRepository, eventService);
    }

    @Test
    void addUserToEvent() {
        User user = new User();
        user.setUsername("username");
        Event event = new Event();
        event.setEvent_id(1);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(eventService.addUserToEvent(user.getUsername(), 1, 1L)).thenReturn("Successfully added user");

        GenericResponse str = eventController.addUserToEvent(user.getUsername(), 1, request);

        assertEquals("Successfully added user", str.getMessage());
        verify(eventService, times(1)).addUserToEvent(user.getUsername(), 1, 1L);
    }

    @Test
    void addUserToEventStatus() throws Exception {
        mockMvc.perform(
                post("/event/{id}/user", 1)
                        .param("username", "username")
        ).andExpect(status().isOk());
    }

    @Test
    void addUserToEventVerifyTimes() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);

        eventController.addUserToEvent("username", 1, request);

        verify(eventService, times(1)).addUserToEvent("username", 1, 1L);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    void getEventUsers() {
        Event event = new Event();
        event.setEvent_id(1);
        User user = new User();
        user.setName("USER");
        user.setId(1L);
        List<User> users = new ArrayList<>();
        users.add(user);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(eventService.findAllUsers(1)).thenReturn(users);

        List<User> result = eventController.getEventUsers(1, request);

        assertEquals("USER", result.get(0).getName());
        verify(eventService, times(1)).findAllUsers(1);
    }

    @Test
    void getEventUsersStatus() throws Exception {
        mockMvc.perform(
                get("/event/{id}/user", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void getEventUsersVerifyTimes() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        eventController.getEventUsers(1, request);

        verify(eventService, times(1)).findAllUsers(1);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    void getEventUserProducts() {
        Product product = new Product();
        product.setName("BANAN");
        List<Product> products = new ArrayList<>();
        products.add(product);

        when(eventService.findUserEventProducts(1, 1L)).thenReturn(products);

        List<Product> result = eventController.getEventUserProducts(1, 1L);

        assertEquals("BANAN", result.get(0).getName());
        verify(eventService, times(1)).findUserEventProducts(1, 1L);
    }

    @Test
    void getEventUserProductsStatus() throws Exception {
        mockMvc.perform(
                get("/event/{event_id}/user/{user_id}/products", 1, 1)
        ).andExpect(status().isOk());
    }

    @Test
    void getEventUserProductsVerifyTimes() {
        eventController.getEventUserProducts(1, 1L);
        verify(eventService, times(1)).findUserEventProducts(1, 1L);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    void getMyEventUserProducts() {
        Product product = new Product();
        product.setName("JAPKO");
        List<Product> products = new ArrayList<>();
        products.add(product);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(eventService.findUserEventProducts(1, 1L)).thenReturn(products);

        List<Product> result = eventController.getMyEventUserProducts(1, request);

        assertEquals("JAPKO", result.get(0).getName());
        verify(eventService, times(1)).findUserEventProducts(1, 1L);
    }

    @Test
    void getMyEventUserProductsStatus() throws Exception {
        mockMvc.perform(
                get("/event/{id}/myproducts", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void getMyEventUserProductsVerifyTimes() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);

        eventController.getMyEventUserProducts(1, request);

        verify(eventService, times(1)).findUserEventProducts(1, 1L);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    void deleteProductFromEvent() {
        Event event = new Event();
        event.setEvent_id(1);

        Product product = new Product();
        product.setName("JAPKO");
        product.setId(1);
        List<Product> products = new ArrayList<>();
        products.add(product);

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(eventService.deleteProduct(1, 1, 1L)).thenReturn("Successfully delete products");

        GenericResponse str = eventController.deleteProductFromEvent(1, 1, request);

        assertEquals("Successfully delete products", str.getMessage());
        verify(eventService, times(1)).deleteProduct(1, 1, 1L);
    }

    @Test
    void deleteProductFromEventStatus() throws Exception {
        mockMvc.perform(
                delete("/event/{id}/product", 1)
                .param("productToDeleteId", "1")
        ).andExpect(status().isOk());
    }

    @Test
    void deleteProductFromEventVerifyTimes() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);

        eventController.deleteProductFromEvent(1, 1, request);

        verify(eventService, times(1)).deleteProduct(1, 1, 1L);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    void deleteUserFromEvent() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(eventService.deleteUser(1, 2L, 1L)).thenReturn("Successfully removed user from event");

        GenericResponse str = eventController.deleteUserFromEvent(1, 2L, request);

        assertEquals("Successfully removed user from event", str.getMessage());
        verify(eventService, times(1)).deleteUser(1, 2L, 1L);
    }

    @Test
    void deleteUserFromEventStatus() throws Exception {
        mockMvc.perform(
                delete("/event/{id}/user", 1)
                .param("userToDeleteId", "1")
        ).andExpect(status().isOk());
    }

    @Test
    void deleteUserFromEventVerifyTimes() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);

        eventController.deleteUserFromEvent(1, 2L, request);

        verify(eventService, times(1)).deleteUser(1, 2L, 1L);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    void grantAdminForUser() {
        User user = new User();
        user.setUsername("JAREK");

        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(userRepository.findById(2L)).thenReturn(java.util.Optional.of(user));
        when(eventService.grantUserAdmin(1, 2L, 1L)).thenReturn("Successfully granted admin to JAREK");

        GenericResponse str = eventController.grantAdminForUser(1, 2L, request);

        assertEquals("Successfully granted admin to JAREK", str.getMessage());
    }

    @Test
    void grantAdminForUserStatus() throws Exception {
        mockMvc.perform(
                put("/event/{event_id}/user/{user_id}/grantAdmin", 1, 1)
        ).andExpect(status().isOk());
    }

    @Test
    void grantAdminForUserVerifyTimes() {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(2L);

        eventController.grantAdminForUser(2, 1L, request);

        verify(eventService, times(1)).grantUserAdmin(2, 1L, 2L);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    void isAdminNullEvent() {
        Event event = null;
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(eventRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(event));

        GenericResponse response = eventController.isAdmin(1, request);
        assertEquals("failed", response.getMessage());
    }

    @Test
    void isAdminNullUser() {
        Event event = new Event();
        User user = null;
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(eventRepository.findById(1)).thenReturn(java.util.Optional.of(event));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));

        GenericResponse response = eventController.isAdmin(1, request);
        assertEquals("failed", response.getMessage());
    }

    @Test
    void isAdminFalse() {
        Event event = new Event();
        User user = new User();
        EventUser eventUser = new EventUser();
        eventUser.setUser(user);
        eventUser.setEvent(event);
        eventUser.setAdmin(false);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(eventRepository.findById(1)).thenReturn(java.util.Optional.of(event));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(event_userRepository.findByUserAndEvent(user, event)).thenReturn(eventUser);

        GenericResponse response = eventController.isAdmin(1, request);
        assertEquals("false", response.getMessage());
    }

    @Test
    void isAdmin() {
        Event event = new Event();
        User user = new User();
        EventUser eventUser = new EventUser();
        eventUser.setUser(user);
        eventUser.setEvent(event);
        eventUser.setAdmin(true);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(eventRepository.findById(1)).thenReturn(java.util.Optional.of(event));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(event_userRepository.findByUserAndEvent(user, event)).thenReturn(eventUser);

        GenericResponse response = eventController.isAdmin(1, request);
        assertEquals("true", response.getMessage());
    }

    @Test
    void isAdminStatus() throws Exception {
        mockMvc.perform(
                get("/event/{event_id}/isAdmin", 1)
        ).andExpect(status().isOk());
    }

    @Test
    void isAdminVerifyTimes() {
        Event event = new Event();
        User user = new User();
        EventUser eventUser = new EventUser();
        eventUser.setUser(user);
        eventUser.setEvent(event);
        eventUser.setAdmin(true);
        HttpServletRequest request = createMock(HttpServletRequest.class);
        String token = "TOKEN";

        when(jwtAuthenticationFilter.getJWTFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.getUserIdFromJWT(token)).thenReturn(1L);
        when(eventRepository.findById(1)).thenReturn(java.util.Optional.of(event));
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(event_userRepository.findByUserAndEvent(user, event)).thenReturn(eventUser);

        eventController.isAdmin(1, request);
        verify(eventRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findById(1L);
        verify(event_userRepository, times(1)).findByUserAndEvent(user, event);
        verifyNoMoreInteractions(eventRepository, userRepository, event_userRepository);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
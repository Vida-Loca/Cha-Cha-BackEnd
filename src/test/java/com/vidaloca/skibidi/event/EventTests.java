/*
package com.vidaloca.skibidi.event;

import com.vidaloca.skibidi.address.dto.AddressDto;
import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.repository.*;
import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.event.model.*;
import com.vidaloca.skibidi.product.model.Product;
import com.vidaloca.skibidi.user.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@RunWith(SpringRunner.class)
@ComponentScan("com.vidaloca.skibidi.event.service")
@SpringBootTest
public class EventTests {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private ProductRepository productRepository;

    @Mock
    private EventService es;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    //---------------------------addNewEvent

    @Transactional
    @Test
    public void addNewEvent() {
        AddressDto addressDto = new AddressDto("AddTest", "AddTest", "AddTest", "Add");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");
        Event event = new Event();
        event.setName("AddTest");
        User user = new User();
        user.setId(2L);

        when(es.addNewEvent(eventDto, 2L)).thenReturn("Successfully added event");

        String str = es.addNewEvent(eventDto, 2L);

        Assert.assertEquals("Successfully added event", str);
    }

    @Transactional
    @Test
    public void addNewEventTest() {
        AddressDto addressDto = new AddressDto("AddTest", "AddTest", "AddTest", "Add");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");

        String str = eventService.addNewEvent(eventDto, 1L);
        Assert.assertEquals( "Successfully added event", str);
    }

    //---------------------------updateEvent

    @Test
    public void testUpdateEventNullUser() {
        AddressDto addressDto = new AddressDto("TestCountry", "TestCity", "TestCode", "TestNo");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");
        String str = eventService.updateEvent(eventDto, 1, -3L);
        Assert.assertEquals("Unexpected failure", str);
    }

    @Test
    public void testUpdateEventNullEvent() {
        AddressDto addressDto = new AddressDto("TestCountry", "TestCity", "TestCode", "TestNo");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");
        String str = eventService.updateEvent(eventDto, -3, 1L);
        Assert.assertEquals("Event doesn't exist", str);
    }

    @Test
    public void testUpdateEventUserNot() {
        AddressDto addressDto = new AddressDto("TestCountry", "TestCity", "TestCode", "TestNo");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");
        String str = eventService.updateEvent(eventDto, 1, 2L);
        Assert.assertEquals("User is not in that event", str);
    }

    @Test
    public void testUpdateEventNotAdmin() {
        AddressDto addressDto = new AddressDto("TestCountry", "TestCity", "TestCode", "TestNo");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");
        String str = eventService.updateEvent(eventDto, 2, 1L);
        Assert.assertEquals("User is not allowed to update event", str);
    }

    @Test
    public void testUpdateEvent() {
        Event eventBefore = eventRepository.findById(1).orElse(null);
        AddressDto addressDto = new AddressDto("TestCountry", "TestCity", "TestCode", "TestNo");
        EventDto eventDto = new EventDto("Event", Date.valueOf("2020-02-02"), addressDto, Time.valueOf(LocalTime.now()), "Info");
        String str = eventService.updateEvent(eventDto, 1, 1L);
        Event eventAfter = eventRepository.findById(1).orElse(null);
        Assert.assertNotEquals(eventBefore.getName(), eventAfter.getName());
        eventAfter.setName(eventBefore.getName());
        eventRepository.save(eventAfter);
        Assert.assertEquals("Updated successfully", str);
    }

    //---------------------------addProductToEvent

    @Test
    public void testAddProductToEvent() {
        Event event = new Event();
        event.setEvent_id(1);
        User user = new User();
        user.setId(1L);
        Product product = new Product();
        product.setId(1);
        when(es.addProductToEvent(product, event.getEvent_id(), user.getId())).thenReturn("Successfully added product");

        String str = es.addProductToEvent(product, event.getEvent_id(), user.getId());
        Assert.assertEquals("Successfully added product", str);
    }

    @Test
    public void addProductToEventNotExistingUser() {
        Product product = new Product();
        product.setId(1);
        String str = eventService.addProductToEvent(product, 1, -1L);
        Assert.assertEquals("Unexpected failure", str);
    }

    @Test
    public void addProductToNotExistingEvent() {
        Product product = new Product();
        product.setId(1);
        String str = eventService.addProductToEvent(product, -1, 1L);
        Assert.assertEquals("Event doesn't exist", str);
    }

    @Test
    public void addProductToEventUserNotInEvent() {
        Product product = new Product();
        product.setId(1);
        String str = eventService.addProductToEvent(product, 1, 2L);
        Assert.assertEquals("User is not in that event", str);
    }

    @Transactional
    @Test
    public void addProductToEvent() {
        Product product = productRepository.findById(1).orElse(null);
        String str = eventService.addProductToEvent(product, 1, 1L);
        Assert.assertEquals("Successfully added product", str);
    }

    //---------------------------addUserToEvent

    @Test
    public void testAddUserToEventHostNE() {
        String str = eventService.addUserToEvent("testowy2", 1, -3L);
        Assert.assertEquals("Unexpected failure", str);
    }

    @Test
    public void testAddNotExistingUserToEvent() {
        String str = eventService.addUserToEvent("notexisting", 1, 2L);
        Assert.assertEquals("User doesn't exists", str);
    }

    @Test
    public void testAddUserToNotExistingEvent() {
        String str = eventService.addUserToEvent("testowy2", -3, 1L);
        Assert.assertEquals("Event doesn't exist", str);
    }

    @Test
    public void testAddUserToEventWrongHost() {
        String str = eventService.addUserToEvent("testowy1", 1, 2L);
        Assert.assertEquals("User is not in that event", str);
    }

    @Test
    public void testAddUserToEventActuallyIn() {
        String str = eventService.addUserToEvent("testowy1", 2, 2L);
        Assert.assertEquals("User is acctually in that event", str);
    }

    @Test
    public void testAddUserToEventNotAdmin() {
        String str = eventService.addUserToEvent("testowy3", 2, 1L);
        Assert.assertEquals("User don't have permission to add new user to event", str);
    }

    @Transactional
    @Test
    public void testAddUserToEvent() {
        String str = eventService.addUserToEvent("testowy3", 2, 2L);
        Assert.assertEquals("Successfully added user", str);
    }

    //---------------------------deleteEvent

    @Transactional
    @Test
    public void testDeleteEventIsAdmin() {
        List<Event> eventsBefore = (List<Event>) eventRepository.findAll();
        String str = eventService.deleteEvent(2, 2L);
        List<Event> eventsAfter = (List<Event>) eventRepository.findAll();
        Assert.assertNotSame(eventsBefore, eventsAfter);
        Assert.assertEquals("Event delete successfully", str);
    }

    @Test
    public void testDeleteEventIsNotAdmin() {
        String str = eventService.deleteEvent(2, 1L);
        Assert.assertEquals("User don't have permission to delete event", str);
    }

    @Test
    public void testDeleteEventUserNotInEvent() {
        String str = eventService.deleteEvent(1, 2L);
        Assert.assertEquals("User is not in that event", str);
    }

    @Test
    public void testDeleteNotExistingEvent() {
        String str = eventService.deleteEvent(-3, 1L);
        Assert.assertEquals("Event doesn't exist", str);
    }

    //---------------------------findAllEventProducts

    @Transactional
    @Test
    public void testFindAllEventProducts() {
        Event event = eventRepository.findById(1).orElse(null);
        List<Product> products = eventService.findAllEventProducts(event.getEvent_id());
        Assert.assertEquals(1, products.size());
    }

    @Test
    public void testFindAllNullEventProducts() {
        List<Product> products = eventService.findAllEventProducts(-1);
        Assert.assertNull(products);
    }

    //---------------------------findAllUsers

    @Test
    public void testFindAllUsers() {
        List<User> list = eventService.findAllEventUsers(1);
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void testFindAllUsersNullEvent() {
        List<User> list = eventService.findAllEventUsers(-1);
        Assert.assertNull(list);
    }

    //---------------------------findUserEventProducts

    @Test
    public void testFindUserEventProductNullUser() {
        List<Product> products = eventService.findUserEventProducts(1, -1L);
        Assert.assertEquals(null, products);
    }

    @Test
    public void testFindUserEventProductNullEvent() {
        List<Product> products = eventService.findUserEventProducts(-1, 1L);
        Assert.assertNull(products);
    }

    @Test
    public void testFindUserEventProductNullEventUser() {
        List<Product> products = eventService.findUserEventProducts(3, 2L);
        Assert.assertNull(products);
    }

    @Test
    public void testFindUserEventProduct() {
        List<Product> products = eventService.findUserEventProducts(1, 1L);
        Assert.assertNotNull(products);
        Assert.assertEquals("TestCoke", products.get(0).getName());
    }

    //---------------------------deleteUser

    @Test
    public void testDeleteUserNotExistingEvent() {
        String str = eventService.deleteUser(-3, 1L, 2L);
        Assert.assertEquals("Event doesn't exist", str);
    }

    @Test
    public void testDeleteUserNotExistingHost() {
        String str = eventService.deleteUser(1, 2L, -3L);
        Assert.assertEquals("Unexpected failure", str);
    }

    @Test
    public void testDeleteUserNotExistingUser() {
        String str = eventService.deleteUser(1, -3L, 1L);
        Assert.assertEquals("User to delete doesn't exist", str);
    }

    @Test
    public void testDeleteUserNotBeingInEvent() {
        String str = eventService.deleteUser(1, 1L, 2L);
        Assert.assertEquals("User is not in that event", str);
    }

    @Test
    public void testDeleteUserNotInEvent() {
        String str = eventService.deleteUser(1, 2L, 1L);
        Assert.assertEquals("User is not acctually in that event", str);
    }

    @Test
    public void testDeleteUserIsNotAdmin() {
        String str = eventService.deleteUser(2, 2L, 1L);
        Assert.assertEquals("You don't have permission to delete user", str);
    }

    @Transactional
    @Test
    public void testDeleteUser() {
        String str = eventService.deleteUser(2, 1L, 2L);
        Assert.assertEquals("Successfully removed user from event", str);
    }

    //---------------------------deleteProduct

    @Test
    public void testDeleteProductNotExistingEvent() {
        String str = eventService.deleteProduct(-3, 1, 1L);
        Assert.assertEquals("Event doesn't exist", str);
    }

    @Test
    public void testDeleteProductNotExistingUser() {
        String str = eventService.deleteProduct(1, 1, -3L);
        Assert.assertEquals("Unexpected failure", str);
    }

    @Test
    public void testDeleteProductUserNotInEvent() {
        String str = eventService.deleteProduct(1, 1, 2L);
        Assert.assertEquals("User is not in that event", str);
    }

    @Transactional
    @Test
    public void testDeleteProduct() {
        String str = eventService.deleteProduct(1, 1, 1L);
        Assert.assertEquals("Successfully delete products", str);
    }

    //---------------------------grantUserAdmin

    @Test
    public void testGrantAdminEventNotExist() {
        String str = eventService.grantUserAdmin(-3, 1L, 2L);
        Assert.assertEquals("Event doesn't exist", str);
    }

    @Test
    public void testGrantAdminHostNotExist() {
        String str = eventService.grantUserAdmin(2, 1L, -3L);
        Assert.assertEquals("Unexpected failure", str);
    }

    @Test
    public void testGrantAdminUserNotExist() {
        String str = eventService.grantUserAdmin(2, -3L, 2L);
        Assert.assertEquals("User to grant doesn't exist", str);
    }

    @Test
    public void testGrantAdminHostNotInEvent() {
        String str = eventService.grantUserAdmin(1, 1L, 2L);
        Assert.assertEquals("You are not in that event", str);
    }

    @Test
    public void testGrantAdminUserNotInEvent() {
        String str = eventService.grantUserAdmin(1, 2L, 1L);
        Assert.assertEquals("User is not acctually in that event", str);
    }

    @Test
    public void testGrantAdminIsNotAdmin() {
        String str = eventService.grantUserAdmin(2, 2L, 1L);
        Assert.assertEquals("You don't have permission to grant admin to user", str);
    }

    @Transactional
    @Test
    public void testGrantAdmin() {
        String str = eventService.grantUserAdmin(2, 1L, 2L);
        Assert.assertEquals("Successfully granted admin to testowy2", str);
    }
}
*/

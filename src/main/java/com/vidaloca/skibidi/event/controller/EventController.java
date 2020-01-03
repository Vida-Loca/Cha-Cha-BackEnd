package com.vidaloca.skibidi.event.controller;

import com.vidaloca.skibidi.event.dto.EventDto;
import com.vidaloca.skibidi.event.dto.ProductDto;
import com.vidaloca.skibidi.event.repository.EventRepository;
import com.vidaloca.skibidi.event.repository.ProductRepository;
import com.vidaloca.skibidi.event.service.EventService;
import com.vidaloca.skibidi.event.service.ProductService;
import com.vidaloca.skibidi.model.Event;
import com.vidaloca.skibidi.model.Product;
import com.vidaloca.skibidi.registration.utills.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;

@RestController
public class EventController {
    private EventRepository eventRepository;
    private EventService eventService;
    private ProductService productService;
    @Autowired
    public EventController(EventRepository eventRepository, EventService eventService, ProductService productService) {
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.productService = productService;
    }

    @GetMapping("/event")
    public List<Event> getEvents(){
        return (List<Event>) eventRepository.findAll();
    }

    @GetMapping ("/event/{id}")
    public Event getEventById(@PathVariable Integer id){
        return eventRepository.findById(id).orElse(null);
    }
    @PostMapping("/event")
    public String addNewEvent(@Valid @RequestBody EventDto eventDto){
        Event event = eventService.addNewEvent(eventDto);
        return "successfully added event";
    }
    @PutMapping("/event/{id}")
    public String updateEvent(@Valid @RequestBody EventDto eventDto, Integer id){
        Event event = eventService.updateEvent(eventDto,id);
        return "successfully updated event";
    }
    @DeleteMapping("/event/{id}")
    public String deleteById(@PathVariable Integer id){
        eventRepository.deleteById(id);
        return "delete successfully";
    }
    @GetMapping("/event/products")
    public List<Product>  getEventProducts(@PathVariable Integer eventId){
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null)
            return event.getProducts();
        return  null;
    }
    @PostMapping ("/event/products")
    public String addNewProductToEvent(@Valid @RequestBody ProductDto productDto ){
        Product p = productService.addProduct(productDto);
        return "Successfully added product";
    }
}

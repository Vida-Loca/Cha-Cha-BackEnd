package com.vidaloca.skibidi.unit.admin.service;

import com.vidaloca.skibidi.admin.service.AdminEventServiceImpl;
import com.vidaloca.skibidi.event.exception.model.EventNotFoundException;
import com.vidaloca.skibidi.event.model.Event;
import com.vidaloca.skibidi.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AdminEventServiceImplTest {

    @Mock(lenient = true)
    EventRepository eventRepository;

    @InjectMocks
    AdminEventServiceImpl service;

    Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);

        given(eventRepository.findById(1L)).willReturn(Optional.of(event));
    }

    @Test
    void findById() {
        //given

        //when
        Event result = service.findById(1L);

        //then
        assertEquals(event, result);
        then(eventRepository).should().findById(anyLong());
        then(eventRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findByIdNotFound() {
        //given
        given(eventRepository.findById(1L)).willReturn(Optional.empty());

        //when
        Exception result = assertThrows(EventNotFoundException.class, () -> {
            service.findById(1L);
        });

        //then
        assertEquals("Event with id: 1 not found.", result.getMessage());
        then(eventRepository).should().findById(anyLong());
        then(eventRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void findAllEvents() {
        //given
        List<Event> events = new ArrayList<>();
        events.add(event);

        given(eventRepository.findAll()).willReturn(events);

        //when
        List<Event> result = service.findAllEvents();

        //then
        assertEquals(events, result);
        then(eventRepository).should().findAll();
        then(eventRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteById() {
        //given

        //when
        String result = service.deleteById(1L);

        //then
        assertEquals("Event deleted successfully", result);
        then(eventRepository).should().findById(anyLong());
        then(eventRepository).should().deleteById(anyLong());
        then(eventRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteByIdNotFound() {
        //given
        given(eventRepository.findById(1L)).willReturn(Optional.empty());

        //when
        Exception result = assertThrows(EventNotFoundException.class, () -> {
            service.deleteById(1L);
        });

        //then
        assertEquals("Event with id: 1 not found.", result.getMessage());
        then(eventRepository).should().findById(anyLong());
        then(eventRepository).shouldHaveNoMoreInteractions();
    }
}
package com.vidaloca.skibidi.registration.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;

class MapValidationErrorServiceTest {

    @Mock
    private MapValidationErrorService mapValidationErrorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void mapValidationServiceNoErrors() {
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity responseEntity = mapValidationErrorService.MapValidationService(bindingResult);

        Assert.assertNull(responseEntity);
    }
}
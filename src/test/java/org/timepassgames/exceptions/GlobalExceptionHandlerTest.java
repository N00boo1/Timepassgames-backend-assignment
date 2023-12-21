package org.timepassgames.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    @Mock
    private ResourceNotFoundException mockException;

    @Mock
    private FieldNotValidException fieldNotValidException;

    @Mock
    private ResourceAlreadyExistsException resourceAlreadyExistsException;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleResourceNotFoundException() {
        // Given
        when(mockException.getMessage()).thenReturn("Resource not found");

        // When
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleResourceNotFoundException(mockException);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Resource not found", responseEntity.getBody().getMessage());
    }
    @Test
    void handleFieldNotValidException() {
        // Given
        when(fieldNotValidException.getMessage()).thenReturn("Field not valid");

        // When
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleFieldNotValidException(fieldNotValidException);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Field not valid", responseEntity.getBody().getMessage());
    }

    @Test
    void handleResourceAlreadyExistsException() {
        // Given
        when(resourceAlreadyExistsException.getMessage()).thenReturn("Resource already exists");

        // When
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleResourceAlreadyExistsException(resourceAlreadyExistsException);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Resource already exists", responseEntity.getBody().getMessage());
    }

}

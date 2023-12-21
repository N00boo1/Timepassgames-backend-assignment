package org.timepassgames.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceNotFoundExceptionTest {

    @Test
    void testResourceNotFoundExceptionMessage() {
        // Given
        String resourceName = "Entity";
        String fieldName = "id";
        String fieldValue = "123";

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

        // Then
        String expectedMessage = "Entity not found with id 123";
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(resourceName, exception.getResourceName());
        assertEquals(fieldName, exception.getFieldName());
        assertEquals(fieldValue, exception.getFieldValue());
    }
}

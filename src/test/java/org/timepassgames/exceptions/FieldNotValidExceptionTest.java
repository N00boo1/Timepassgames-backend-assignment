package org.timepassgames.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldNotValidExceptionTest {

    @Test
    void testFieldNotValidException() {
        // Arrange
        String fieldName = "someField";
        String resourceName = "Game";

        // Act
        FieldNotValidException exception = new FieldNotValidException(fieldName, resourceName);

        // Assert
        assertEquals(
                "someField field in Game is invalid",
                exception.getMessage()
        );
        assertEquals(fieldName, exception.getFieldName());
        assertEquals(resourceName, exception.getResourceName());
    }
}

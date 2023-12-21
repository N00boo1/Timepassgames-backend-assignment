package org.timepassgames.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceAlreadyExistsExceptionTest {

    @Test
    void testResourceAlreadyExistsException() {
        // Arrange
        String resourceName = "Game";
        String name = "TestGame";
        String url = "testurl.com";

        // Act
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(resourceName, name, url);

        // Assert
        assertEquals(
                "Game with name TestGame or url testurl.com already exists in database",
                exception.getMessage()
        );
        assertEquals(resourceName, exception.getResourceName());
        assertEquals(name, exception.getName());
        assertEquals(url, exception.getUrl());
    }
}

package org.timepassgames.gameMapper;

import org.junit.jupiter.api.Test;
import org.timepassgames.dto.GameDto;
import org.timepassgames.model.Game;
import org.timepassgames.gameMapper.GameMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameMapperTest {

    @Test
    void testMapToGameDto() {
        // Given
        Game game = Game.builder()
                .name("TestGame")
                .url("testurl.com")
                .author("TestAuthor")
                .publishedDate("2023-01-01")
                .build();

        // When
        GameDto gameDto = GameMapper.mapToGameDto(game);

        // Then
        assertEquals("TestGame", gameDto.getName());
        assertEquals("testurl.com", gameDto.getUrl());
        assertEquals("TestAuthor", gameDto.getAuthor());
        assertEquals("2023-01-01", gameDto.getPublishedDate());
    }

    @Test
    void testMapToGame() {
        // Given
        GameDto gameDto = GameDto.builder()
                .name("TestGame")
                .url("testurl.com")
                .author("TestAuthor")
                .publishedDate("2023-01-01")
                .build();

        // When
        Game game = GameMapper.mapToGame(gameDto);

        // Then
        assertEquals("TestGame", game.getName());
        assertEquals("testurl.com", game.getUrl());
        assertEquals("TestAuthor", game.getAuthor());
        assertEquals("2023-01-01", game.getPublishedDate());
    }
}

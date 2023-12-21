package org.timepassgames.gameMapper;

import org.timepassgames.dto.GameDto;
import org.timepassgames.model.Game;

import java.util.UUID;

public class GameMapper {

    // Method to map a Game entity to a GameDto
    public static GameDto mapToGameDto(Game game) {
        // Build and return a new GameDto using information from the provided Game entity
        GameDto gameDto = GameDto.builder()
                .name(game.getName())
                .url(game.getUrl())
                .author(game.getAuthor())
                .publishedDate(game.getPublishedDate())
                .build();
        return gameDto;
    }

    // Method to map a GameDto to a Game entity
    public static Game mapToGame(GameDto gameDto) {
        // Build and return a new Game entity using information from the provided GameDto
        Game game = Game.builder()
                .id(UUID.randomUUID().toString().split("-")[0])  // Generating a random ID for the new game
                .name(gameDto.getName())
                .url(gameDto.getUrl())
                .author(gameDto.getAuthor())
                .publishedDate(gameDto.getPublishedDate())
                .build();

        return game;
    }

}

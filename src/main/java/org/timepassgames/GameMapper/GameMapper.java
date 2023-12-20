package org.timepassgames.GameMapper;

import org.timepassgames.dto.GameDto;
import org.timepassgames.model.Game;

import java.util.UUID;

public class GameMapper {

    public static GameDto mapToGameDto(Game game) {
        GameDto gameDto = GameDto.builder()
                .name(game.getName())
                .url(game.getUrl())
                .author(game.getAuthor())
                .publishedDate(game.getPublishedDate())
                .build();
        return gameDto;
    }

    public static Game mapToGame(GameDto gameDto) {
        Game game= Game.builder()
                .id(UUID.randomUUID().toString().split("-")[0])
                .name(gameDto.getName())
                .url(gameDto.getUrl())
                .author(gameDto.getAuthor())
                .publishedDate(gameDto.getPublishedDate())
                .build();

        return game;
    }

}

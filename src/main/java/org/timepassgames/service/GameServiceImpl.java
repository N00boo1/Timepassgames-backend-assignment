package org.timepassgames.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.timepassgames.dto.GameDto;
import org.timepassgames.model.Game;
import org.timepassgames.repository.GameRepository;

import java.util.List;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository repository;

    public Game addGame(GameDto game) {
        return repository.save(Game.builder()
                        .id(UUID.randomUUID().toString().split("-")[0])
                        .name(game.getName())
                        .url(game.getUrl())
                        .author(game.getAuthor())
                        .publishedDate(game.getPublishedDate())
                .build());
    }

    public List<Game> findAllGames() {
        return repository.findAll();
    }

    public Game getGameByGameName(String gameName) {
        return repository.findByName(gameName);
    }

    public Game updateGame(GameDto gameRequest) {
        // get the existing game from the db
        // populate new value from request to existing object

        Game existingGame=repository.findByName(gameRequest.getName());
        existingGame.setAuthor(gameRequest.getAuthor());
        existingGame.setUrl(gameRequest.getUrl());
        existingGame.setPublishedDate(gameRequest.getPublishedDate());
        repository.deleteByName(gameRequest.getName());
        return repository.save(existingGame);
    }

    public String deleteGame(String gameName) {
        repository.deleteByName(gameName);
        return gameName+" game deleted from the database";
    }

}

package org.timepassgames.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.timepassgames.gameMapper.GameMapper;
import org.timepassgames.dto.GameDto;
import org.timepassgames.exceptions.ResourceNotFoundException;
import org.timepassgames.model.Game;
import org.timepassgames.repository.GameRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository repository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.repository=gameRepository;
    }

    public GameDto addGame(GameDto gameDto) {
        Game game= GameMapper.mapToGame(gameDto);

        Game savedGame=repository.save(game);

        GameDto savedGameDto=GameMapper.mapToGameDto(savedGame);

        return savedGameDto;
    }

    public List<GameDto> findAllGames() {
        List<Game> games=repository.findAll();

        return games.stream().map(GameMapper::mapToGameDto)
                .collect(Collectors.toList());
    }

    public GameDto getGameByGameName(String gameName) {

        try {
            Game game = repository.findByName(gameName);

            return GameMapper.mapToGameDto(game);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Game","name",gameName);
        }

    }

    public GameDto updateGame(GameDto gameRequest) {
        // get the existing game from the db
        // populate new value from request to existing object

        try {
            Game existingGame = repository.findByName(gameRequest.getName());
            existingGame.setAuthor(gameRequest.getAuthor());
            existingGame.setUrl(gameRequest.getUrl());
            existingGame.setPublishedDate(gameRequest.getPublishedDate());
            repository.deleteByName(gameRequest.getName());

            Game updatedGame = repository.save(existingGame);
            return GameMapper.mapToGameDto(updatedGame);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Game","name",gameRequest.getName());
        }
    }

    public String deleteGame(String gameName) {
        Game game=repository.findByName(gameName);
        if(game==null)
            throw new ResourceNotFoundException("Game","name",gameName);
        repository.deleteByName(gameName);
        return gameName + " game deleted from the database";

    }

}

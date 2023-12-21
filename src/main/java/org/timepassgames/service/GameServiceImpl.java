package org.timepassgames.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.timepassgames.exceptions.FieldNotValidException;
import org.timepassgames.exceptions.ResourceAlreadyExistsException;
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

    // Constructor to inject GameRepository dependency
    public GameServiceImpl(GameRepository gameRepository) {
        this.repository = gameRepository;
    }

    // Private method to validate GameDto fields
    private void validateGameDto(GameDto gameDto) {
        validateFieldNotEmpty(gameDto.getName(), "name");
        validateFieldNotEmpty(gameDto.getUrl(), "url");
    }

    // Private method to validate if a field is not empty
    private void validateFieldNotEmpty(String value, String fieldName) {
        if (StringUtils.isEmpty(value)) {
            throw new FieldNotValidException(fieldName, "Game");
        }
    }

    // Method to add a new game to the database
    public GameDto addGame(GameDto gameDto) {
        validateGameDto(gameDto);

        try {
            Game game = GameMapper.mapToGame(gameDto);
            Game savedGame = repository.save(game);
            return GameMapper.mapToGameDto(savedGame);
        } catch (DuplicateKeyException e) {
            throw new ResourceAlreadyExistsException("Game", gameDto.getName(), gameDto.getUrl());
        } catch(Exception e) {
            throw new RuntimeException("An unexpected error occurred.", e);
        }
    }

    // Method to retrieve all games from the database
    public List<GameDto> findAllGames() {
        try {
            List<Game> games = repository.findAll();
            return games.stream().map(GameMapper::mapToGameDto)
                    .collect(Collectors.toList());
        } catch(Exception e) {
            throw new RuntimeException("An unexpected error occurred.", e);
        }
    }

    // Method to retrieve a game by its name from the database
    public GameDto getGameByGameName(String gameName) {
        try {
            Game game = repository.findByName(gameName);

            if (game == null) {
                throw new ResourceNotFoundException("Game", "name", gameName);
            }

            return GameMapper.mapToGameDto(game);
        } catch(ResourceNotFoundException e) {
            throw  e;
        } catch (Exception e) {
            // Handle any other exceptions or log them if needed
            throw new RuntimeException("An unexpected error occurred.", e);
        }
    }

    // Method to update an existing game in the database
    public GameDto updateGame(GameDto gameRequest) {
        validateGameDto(gameRequest);

        try {
            Game existingGame = GameMapper.mapToGame(getGameByGameName(gameRequest.getName()));
            if (repository.existsByUrlAndNameNot(gameRequest.getUrl(), gameRequest.getName())) {
                throw new ResourceAlreadyExistsException("Game", gameRequest.getName(), gameRequest.getUrl());
            }
            existingGame.setAuthor(gameRequest.getAuthor());
            existingGame.setUrl(gameRequest.getUrl());
            existingGame.setPublishedDate(gameRequest.getPublishedDate());

            repository.deleteByName(gameRequest.getName());

            Game updatedGame = repository.save(existingGame);
            return GameMapper.mapToGameDto(updatedGame);
        } catch (ResourceNotFoundException e) {
            throw e; // Re-throw the ResourceNotFoundException to maintain specific exception type
        } catch (DuplicateKeyException e) {
            throw new ResourceAlreadyExistsException("Game", gameRequest.getName(), gameRequest.getUrl());
        } catch(Exception e) {
            throw new RuntimeException("An unexpected error occurred.", e);
        }
    }

    // Method to delete a game from the database by its name
    public String deleteGame(String gameName) {
        try {
            Game existingGame = GameMapper.mapToGame(getGameByGameName(gameName));
            repository.deleteByName(gameName);
            return gameName + " game deleted from the database";
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred.", e);
        }
    }
}

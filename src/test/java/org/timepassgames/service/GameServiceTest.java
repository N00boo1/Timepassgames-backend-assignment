package org.timepassgames.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.timepassgames.dto.GameDto;
import org.timepassgames.exceptions.ResourceNotFoundException;
import org.timepassgames.gameMapper.GameMapper;
import org.timepassgames.model.Game;
import org.timepassgames.repository.GameRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    private GameService gameService;

    @BeforeEach
    void setUp() {
        this.gameService=new GameServiceImpl(gameRepository);
    }

    @Test
    void testFindAllGames() {
        // Create sample games for testing
        Game game1 = new Game();
        game1.setId("1");
        game1.setName("Game1");
        game1.setAuthor("Author1");
        game1.setUrl("url1.com");
        game1.setPublishedDate("2023-01-01");

        Game game2 = new Game();
        game2.setId("2");
        game2.setName("Game2");
        game2.setAuthor("Author2");
        game2.setUrl("url2.com");
        game2.setPublishedDate("2023-01-02");

        // Mock the behavior of the repository's findAll method
        when(gameRepository.findAll()).thenReturn(Arrays.asList(game1, game2));

        // Call the findAllGames method
        List<GameDto> result = gameService.findAllGames();

        // Verify that the repository's findAll method was called
        verify(gameRepository).findAll();

        // Verify that the returned list of GameDto objects is correct
        assertEquals(2, result.size());

        // Add additional assertions based on your requirements
        // For example, you might want to assert that the returned GameDto objects have the expected properties
        assertEquals("Game1", result.get(0).getName());
        assertEquals("Author1", result.get(0).getAuthor());
        assertEquals("url1.com", result.get(0).getUrl());
        assertEquals("2023-01-01", result.get(0).getPublishedDate());

        assertEquals("Game2", result.get(1).getName());
        assertEquals("Author2", result.get(1).getAuthor());
        assertEquals("url2.com", result.get(1).getUrl());
        assertEquals("2023-01-02", result.get(1).getPublishedDate());
    }


    @Test
    void testAddGame() {
        // Create a sample GameDto for testing
        GameDto gameDto = new GameDto();
        gameDto.setName("TestGame");
        gameDto.setAuthor("TestAuthor");
        gameDto.setUrl("testurl.com");
        gameDto.setPublishedDate("2023-01-01");

        // Mock the behavior of the repository's save method
        when(gameRepository.save(Mockito.any(Game.class))).thenAnswer(invocation -> {
            Game savedGame = invocation.getArgument(0);
            savedGame.setId("generatedId");  // Simulating the generation of an ID
            return savedGame;
        });

        // Call the addGame method
        GameDto savedGameDto = gameService.addGame(gameDto);

        // Verify that the repository's save method was called with the correct argument
        verify(gameRepository).save(Mockito.any(Game.class));

        // Add additional assertions based on your requirements
        // For example, you might want to assert that the returned GameDto has the expected properties
        assertEquals("TestGame", savedGameDto.getName());
        assertEquals("TestAuthor", savedGameDto.getAuthor());
        assertEquals("testurl.com", savedGameDto.getUrl());
        assertEquals("2023-01-01", savedGameDto.getPublishedDate());
    }

    @Test
    void testGetGameByGameNameSuccess() {
        // Create a sample game for testing
        Game game = new Game();
        game.setId("1");
        game.setName("TestGame");
        game.setAuthor("TestAuthor");
        game.setUrl("testurl.com");
        game.setPublishedDate("2023-01-01");

        // Mock the behavior of the repository's findByName method
        when(gameRepository.findByName("TestGame")).thenReturn(game);

        // Call the getGameByGameName method
        GameDto result = gameService.getGameByGameName("TestGame");

        // Verify that the repository's findByName method was called
        verify(gameRepository).findByName("TestGame");

        // Verify that the returned GameDto is correct

        assertEquals("TestGame", result.getName());
        assertEquals("TestAuthor", result.getAuthor());
        assertEquals("testurl.com", result.getUrl());
        assertEquals("2023-01-01", result.getPublishedDate());
    }

    @Test
    void testGetGameByGameNameNotFound() {
        // Mock the behavior of the repository's findByName method
        when(gameRepository.findByName("NonExistentGame")).thenReturn(null);

        // Verify that the service method throws the expected exception
        assertThrows(ResourceNotFoundException.class, () -> gameService.getGameByGameName("NonExistentGame"));

        // Verify that the repository's findByName method was called
        verify(gameRepository).findByName("NonExistentGame");
    }

    @Test
    void testUpdateGameSuccess() {
        // Create a sample existing game for testing
        Game existingGame = new Game();
        existingGame.setName("TestGame");
        existingGame.setAuthor("TestAuthor");
        existingGame.setUrl("testurl.com");
        existingGame.setPublishedDate("2023-01-01");

        // Create a sample game request for updating
        GameDto gameRequest = new GameDto();
        gameRequest.setName("TestGame");
        gameRequest.setAuthor("UpdatedAuthor");
        gameRequest.setUrl("updatedurl.com");
        gameRequest.setPublishedDate("2023-01-02");

        // Mock the behavior of the repository's findByName method
        when(gameRepository.findByName("TestGame")).thenReturn(existingGame);

        // Mock the behavior of the repository's deleteByName method
        doNothing().when(gameRepository).deleteByName("TestGame");

        when(gameRepository.save(any())).thenReturn(GameMapper.mapToGame(gameRequest));

        // Call the updateGame method
        GameDto result = gameService.updateGame(gameRequest);

        // Verify that the repository's findByName method was called
        verify(gameRepository).findByName("TestGame");
        // Verify that the repository's deleteByName method was called
        verify(gameRepository).deleteByName("TestGame");
        // Verify that the repository's save method was called with the updated game
        verify(gameRepository).save(existingGame);

        // Verify that the returned GameDto is correct
        assertEquals("TestGame", result.getName());
        assertEquals("UpdatedAuthor", result.getAuthor());
        assertEquals("updatedurl.com", result.getUrl());
        assertEquals("2023-01-02", result.getPublishedDate());
    }

    @Test
    void testUpdateGameNotFound() {
        // Create a sample game request for updating
        GameDto gameRequest = new GameDto();
        gameRequest.setName("NonExistentGame");
        gameRequest.setAuthor("UpdatedAuthor");
        gameRequest.setUrl("updatedurl.com");
        gameRequest.setPublishedDate("2023-01-02");

        // Mock the behavior of the repository's findByName method
        when(gameRepository.findByName("NonExistentGame")).thenReturn(null);

        // Verify that the service method throws the expected exception
        assertThrows(ResourceNotFoundException.class, () -> gameService.updateGame(gameRequest));

    }

    @Test
    void testDeleteGameSuccess() {
        // Create a sample game for testing
        Game existingGame = new Game();
        existingGame.setName("TestGame");
        existingGame.setAuthor("TestAuthor");
        existingGame.setUrl("testurl.com");
        existingGame.setPublishedDate("2023-01-01");

        // Mock the behavior of the repository's findByName method
        when(gameRepository.findByName("TestGame")).thenReturn(existingGame);

        // Call the deleteGame method
        String result = gameService.deleteGame("TestGame");

        // Verify that the repository's findByName method was called
        verify(gameRepository).findByName("TestGame");
        // Verify that the repository's deleteByName method was called
        verify(gameRepository).deleteByName("TestGame");

        // Verify that the returned message is correct
        assertEquals("TestGame game deleted from the database", result);
    }

    @Test
    void testDeleteGameNotFound() {
        // Mock the behavior of the repository's findByName method for a non-existent game
        when(gameRepository.findByName("NonExistentGame")).thenReturn(null);

        // Call the deleteGame method and expect a ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> gameService.deleteGame("NonExistentGame"));

        // Verify that the repository's findByName method was called
        verify(gameRepository).findByName("NonExistentGame");
        // Verify that the repository's deleteByName method was not called
        verify(gameRepository, never()).deleteByName(any());
    }

}

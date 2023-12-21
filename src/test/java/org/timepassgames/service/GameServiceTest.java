package org.timepassgames.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.timepassgames.dto.GameDto;
import org.timepassgames.exceptions.ResourceAlreadyExistsException;
import org.timepassgames.exceptions.ResourceNotFoundException;
import org.timepassgames.gameMapper.GameMapper;
import org.timepassgames.model.Game;
import org.timepassgames.repository.GameRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
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
    void testFindAllGamesSuccess() {
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
    void testFindAllGamesInternalError() {
        // Given: Setup mock data and simulate an internal error
        when(gameRepository.findAll()).thenThrow(new RuntimeException("Simulated internal error"));

        // When: Calling the findAllGames method with an internal error
        RuntimeException exception = assertThrows(RuntimeException.class, () -> gameService.findAllGames());

        // Then: Verify the exception is thrown and has the correct message
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("An unexpected error occurred.");

        // Verify that the repository's findAll method was called
        verify(gameRepository).findAll();
    }
    @Test
    void testAddGameSuccess() {
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
    void testAddGameDuplicateKeyException() {
        // Given: Setup a sample GameDto for testing
        GameDto gameDto = new GameDto();
        gameDto.setName("TestGame");
        gameDto.setAuthor("TestAuthor");
        gameDto.setUrl("testurl.com");
        gameDto.setPublishedDate("2023-01-01");

        // Mock the behavior of the repository's save method to throw a DuplicateKeyException
        when(gameRepository.save(any(Game.class))).thenThrow(DuplicateKeyException.class);

        // When: Calling the addGame method and expecting a ResourceAlreadyExistsException
        ResourceAlreadyExistsException e = assertThrows(ResourceAlreadyExistsException.class,
                () -> gameService.addGame(gameDto));

        // Then: Verify that the repository's save method was called
        verify(gameRepository).save(any(Game.class));

        // Add additional assertions based on your requirements
        // For example, you might want to assert that the exception has the expected details
        assertThat(e).isNotNull();
        assertThat(e.getResourceName()).isEqualTo("Game");
        assertThat(e.getName()).isEqualTo("TestGame");
        assertThat(e.getUrl()).isEqualTo("testurl.com");
    }

    @Test
    void testAddGameUnexpectedError() {
        // Given: Setup a sample GameDto for testing
        GameDto gameDto = new GameDto();
        gameDto.setName("TestGame");
        gameDto.setAuthor("TestAuthor");
        gameDto.setUrl("testurl.com");
        gameDto.setPublishedDate("2023-01-01");

        // Mock the behavior of the repository's save method to throw a general exception
        when(gameRepository.save(any(Game.class))).thenThrow(RuntimeException.class);

        // When: Calling the addGame method and expecting a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> gameService.addGame(gameDto));

        // Then: Verify that the repository's save method was called
        verify(gameRepository).save(any(Game.class));

        // Add additional assertions based on your requirements
        // For example, you might want to assert that the exception has the expected details
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("An unexpected error occurred.");
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
        // Mock the behavior of the repository's findByName method for a non-existent game
        when(gameRepository.findByName("NonExistentGame")).thenReturn(null);

        // When: Calling the getGameByGameName method and expecting a ResourceNotFoundException
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> gameService.getGameByGameName("NonExistentGame"));

        // Then: Verify that the exception is a ResourceNotFoundException
        assertThat(exception).isInstanceOf(ResourceNotFoundException.class);

        // Optional: Verify other details of the exception
        ResourceNotFoundException resourceNotFoundException = (ResourceNotFoundException) exception;
        assertThat(resourceNotFoundException.getResourceName()).isEqualTo("Game");
        assertThat(resourceNotFoundException.getFieldName()).isEqualTo("name");
        assertThat(resourceNotFoundException.getFieldValue()).isEqualTo("NonExistentGame");
    }

    @Test
    void testGetGameByGameNameUnexpectedError() {
        // Mock the behavior of the repository's findByName method to throw a general exception
        when(gameRepository.findByName("TestGame")).thenThrow(RuntimeException.class);

        // When: Calling the getGameByGameName method and expecting a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> gameService.getGameByGameName("TestGame"));

        // Then: Verify that the repository's findByName method was called
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("An unexpected error occurred.");
    }

    @Test
    void testUpdateGameSuccess() {
        // Create a sample existing game for testing
        Game existingGame = new Game();
        existingGame.setId("50e91720");
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

        // Mock the behavior of the repository's save method
        // Adjust the expected invocation to use any() for the id parameter
        when(gameRepository.save(any())).thenReturn(GameMapper.mapToGame(gameRequest));

        // When: Calling the updateGame method
        GameDto result = gameService.updateGame(gameRequest);

        // Then: Verify that the repository's findByName, deleteByName, and save methods were called
        verify(gameRepository).findByName("TestGame");
        verify(gameRepository).deleteByName("TestGame");
        // Adjust the expected invocation to use any() for the id parameter
        verify(gameRepository).save(any());

        // Verify that the returned GameDto is correct
        assertThat(result.getName()).isEqualTo("TestGame");
        assertThat(result.getAuthor()).isEqualTo("UpdatedAuthor");
        assertThat(result.getUrl()).isEqualTo("updatedurl.com");
        assertThat(result.getPublishedDate()).isEqualTo("2023-01-02");
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

        // When: Calling the updateGame method and expecting a ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> gameService.updateGame(gameRequest));

        // Then: Verify that the repository's findByName method was called
        assertThat(exception).isNotNull();
        assertThat(exception.getResourceName()).isEqualTo("Game");
        assertThat(exception.getFieldName()).isEqualTo("name");
        assertThat(exception.getFieldValue()).isEqualTo("NonExistentGame");

        // Verify that the repository's deleteByName and save methods were not called
        verify(gameRepository, never()).deleteByName(any());
        verify(gameRepository, never()).save(any());
    }

    @Test
    void testUpdateGameDuplicateKeyException() {
        // Create a sample game request
        Game existingGame = new Game();
        existingGame.setId("50e91720");
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

        // Mock the behavior of the repository's existsByUrlAndNameNot method to throw DuplicateKeyException
        when(gameRepository.existsByUrlAndNameNot(anyString(), anyString()))
                .thenThrow(new DuplicateKeyException("Duplicate key violation"));


        // When: Calling the updateGame method in the presence of DuplicateKeyException
        Throwable thrownException = catchThrowable(() -> gameService.updateGame(gameRequest));

        // Then: Verify that ResourceAlreadyExistsException is thrown
        assertThat(thrownException).isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage("Game with TestGame or updatedurl.com already exists in database");

    }


    @Test
    void testUpdateGameUnexpectedError() {
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

        // Mock an unexpected error when calling the save method
        doThrow(new RuntimeException("An unexpected error occurred.")).when(gameRepository).save(any());

        // When: Calling the updateGame method and expecting a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> gameService.updateGame(gameRequest));

        // Then: Verify that the repository's findByName, deleteByName, and save methods were called
        verify(gameRepository).findByName("TestGame");
        verify(gameRepository).deleteByName("TestGame");
        verify(gameRepository).save(any());  // Use any() to match any argument

        // Verify that the exception message contains the correct error message
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).contains("An unexpected error occurred.");
    }



    @Test
    void testDeleteGameSuccess() {
        // Mock the behavior of the repository's findByName method
        when(gameRepository.findByName("TestGame")).thenReturn(new Game());

        // When: Calling the deleteGame method
        String result = gameService.deleteGame("TestGame");

        // Then: Verify that the repository's findByName and deleteByName methods were called
        verify(gameRepository).findByName("TestGame");
        verify(gameRepository).deleteByName("TestGame");

        // Verify that the returned message is correct
        assertThat(result).isEqualTo("TestGame game deleted from the database");
    }

    @Test
    void testDeleteGameNotFound() {
        // Mock the behavior of the repository's findByName method for a non-existent game
        when(gameRepository.findByName("NonExistentGame")).thenReturn(null);

        // When: Calling the deleteGame method and expecting a ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> gameService.deleteGame("NonExistentGame"));

        // Then: Verify that the repository's findByName method was called
        assertThat(exception).isNotNull();
        assertThat(exception.getResourceName()).isEqualTo("Game");
        assertThat(exception.getFieldName()).isEqualTo("name");
        assertThat(exception.getFieldValue()).isEqualTo("NonExistentGame");

        // Verify that the repository's deleteByName method was not called
        verify(gameRepository, never()).deleteByName(any());
    }

    @Test
    void testDeleteGameUnexpectedError() {
        // Mock the behavior of the repository's findByName method
        when(gameRepository.findByName("TestGame")).thenReturn(new Game());

        // Mock an unexpected error when calling the deleteByName method
        doThrow(new RuntimeException("An unexpected error occurred.")).when(gameRepository).deleteByName("TestGame");

        // When: Calling the deleteGame method and expecting a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> gameService.deleteGame("TestGame"));

        // Then: Verify that the repository's findByName and deleteByName methods were called
        verify(gameRepository).findByName("TestGame");
        verify(gameRepository).deleteByName("TestGame");

        // Verify that the exception message contains the correct error message
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).contains("An unexpected error occurred.");
    }
}

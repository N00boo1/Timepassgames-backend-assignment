package org.timepassgames.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.timepassgames.dto.GameDto;
import org.timepassgames.service.GameService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @Test
    void testCreateGame() {
        GameDto gameDto = new GameDto();
        gameDto.setName("TestGame");

        when(gameService.addGame(any(GameDto.class))).thenReturn(gameDto);

        ResponseEntity<GameDto> responseEntity = gameController.createGame(gameDto);

        verify(gameService).addGame(gameDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(gameDto, responseEntity.getBody());
    }

    @Test
    void testGetGames() {
        List<GameDto> gameDtoList = Arrays.asList(new GameDto(), new GameDto());

        when(gameService.findAllGames()).thenReturn(gameDtoList);

        ResponseEntity<List<GameDto>> responseEntity = gameController.getGames();

        verify(gameService).findAllGames();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(gameDtoList, responseEntity.getBody());
    }

    @Test
    void testGetGame() {
        GameDto gameDto = new GameDto();
        gameDto.setName("TestGame");

        when(gameService.getGameByGameName("TestGame")).thenReturn(gameDto);

        ResponseEntity<GameDto> responseEntity = gameController.getGame("TestGame");

        verify(gameService).getGameByGameName("TestGame");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(gameDto, responseEntity.getBody());
    }

    @Test
    void testModifyGame() {
        GameDto gameDto = new GameDto();
        gameDto.setName("TestGame");

        when(gameService.updateGame(any(GameDto.class))).thenReturn(gameDto);

        ResponseEntity<GameDto> responseEntity = gameController.modifyGame(gameDto);

        verify(gameService).updateGame(gameDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(gameDto, responseEntity.getBody());
    }

    @Test
    void testDeleteGame() {
        when(gameService.deleteGame("TestGame")).thenReturn("TestGame deleted from the database");

        ResponseEntity<String> responseEntity = gameController.deleteGame("TestGame");

        verify(gameService).deleteGame("TestGame");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("TestGame deleted from the database", responseEntity.getBody());
    }
}

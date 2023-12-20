package org.timepassgames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.timepassgames.dto.GameDto;
import org.timepassgames.service.GameService;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GameDto> createGame(@RequestBody GameDto game) {
        return ResponseEntity.ok(gameService.addGame(game));
    }

    @GetMapping
    public ResponseEntity<List<GameDto>> getGames() {
        return ResponseEntity.ok(gameService.findAllGames());
    }

    @GetMapping("/{name}")
    public ResponseEntity<GameDto> getGame(@PathVariable("name") String name) {
        return ResponseEntity.ok(gameService.getGameByGameName(name));
    }

    @PutMapping
    public ResponseEntity<GameDto> modifyGame(@RequestBody GameDto game) {
        return ResponseEntity.ok(gameService.updateGame(game));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteGame(@PathVariable("name") String name) {
        return ResponseEntity.ok(gameService.deleteGame(name));
    }

}

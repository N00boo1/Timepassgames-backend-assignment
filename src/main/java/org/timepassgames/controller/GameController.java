package org.timepassgames.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.timepassgames.dto.GameDto;
import org.timepassgames.model.Game;
import org.timepassgames.service.GameService;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(@RequestBody GameDto game) {
        return gameService.addGame(game);
    }

    @GetMapping
    public List<Game> getGames() {
        return gameService.findAllGames();
    }

    @GetMapping("/{name}")
    public Game getGame(@PathVariable("name") String name) {
        return gameService.getGameByGameName(name);
    }

    @PutMapping
    public Game modifyGame(@RequestBody GameDto game) {
        return gameService.updateGame(game);
    }

    @DeleteMapping("/{name}")
    public String deleteGame(@PathVariable("name") String name) {
        return gameService.deleteGame(name);
    }

}

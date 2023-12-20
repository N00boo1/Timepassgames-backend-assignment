package org.timepassgames.service;

import org.timepassgames.dto.GameDto;
import org.timepassgames.model.Game;

import java.util.List;

public interface GameService {

    public Game addGame(GameDto game);

    public List<Game> findAllGames();

    public Game getGameByGameName(String gameName);

    public Game updateGame(GameDto gameRequest);

    public String deleteGame(String gameName);


}

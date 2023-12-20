package org.timepassgames.service;

import org.timepassgames.dto.GameDto;
import org.timepassgames.model.Game;

import java.util.List;

public interface GameService {

    public GameDto addGame(GameDto game);

    public List<GameDto> findAllGames();

    public GameDto getGameByGameName(String gameName);

    public GameDto updateGame(GameDto gameRequest);

    public String deleteGame(String gameName);


}

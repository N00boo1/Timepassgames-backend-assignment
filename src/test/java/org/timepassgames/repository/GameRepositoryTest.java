package org.timepassgames.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.timepassgames.model.Game;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
public class GameRepositoryTest {


    @Autowired
    private GameRepository gameRepository;

    private Game game;

    @BeforeEach
    public void setupData() {
        game = Game.builder()
                .id("testId")
                .name("test")
                .url("test.com")
                .author("dummyUser")
                .publishedDate("testDate")
                .build();
    }

    @Test
    @DisplayName("JUnit test for save game operation")
    public void givenGameObject_whenSave_thenReturnSaveGame(){


        // When : Action of behavious that we are going to test
        Game saveGame = gameRepository.save(game);

        // Then : Verify the output

        assertThat(saveGame).isNotNull();
        gameRepository.deleteByName(game.getName());
    }

    @Test
    @DisplayName("JUnit test for get Game List")
    public void givenGameList_whenFindAll_thenGameList(){

        // Given : Setup object or precondition
        Game gameOne = Game.builder()
                .id("testId1")
                .name("test1")
                .url("test1.com")
                .author("dummyUser")
                .publishedDate("testDate")
                .build();

        Game gameTwo = Game.builder()
                .id("testId2")
                .name("test2")
                .url("test2.com")
                .author("dummyUser")
                .publishedDate("testDate")
                .build();

        gameRepository.save(gameOne);
        gameRepository.save(gameTwo);

        // When : Action of behavious that we are going to test
        List<Game> games = gameRepository.findAll();

        // Then : Verify the output
        assertThat(games.size()).isGreaterThanOrEqualTo(2);
        gameRepository.deleteByName(gameOne.getName());
        gameRepository.deleteByName(gameTwo.getName());
    }

    @Test
    @DisplayName("JUnit test for find Game by name")
    public void givenGameName_whenFindByName_thenGame() {
        // Given : Setup object or precondition
        Game gameToFind = Game.builder()
                .id("testId3")
                .name("test3")
                .url("test3.com")
                .author("dummyUser")
                .publishedDate("testDate")
                .build();

        gameRepository.save(gameToFind);

        // When : Action of behavior that we are going to test
        Optional<Game> foundGame = Optional.ofNullable(gameRepository.findByName(gameToFind.getName()));

        // Then : Verify the output
        assertThat(foundGame).isPresent();
        assertThat(foundGame.get().getName()).isEqualTo(gameToFind.getName());

        gameRepository.deleteByName(gameToFind.getName());
    }

    @Test
    @DisplayName("JUnit test for delete Game by name")
    public void givenGameName_whenDeleteByName_thenNoGame() {
        // Given : Setup object or precondition
        Game gameToDelete = Game.builder()
                .id("testId4")
                .name("test4")
                .url("test4.com")
                .author("dummyUser")
                .publishedDate("testDate")
                .build();

        gameRepository.save(gameToDelete);

        // When : Action of behavior that we are going to test
        gameRepository.deleteByName(gameToDelete.getName());

        // Then : Verify the output
        Optional<Game> deletedGame = Optional.ofNullable(gameRepository.findByName(gameToDelete.getName()));
        assertThat(deletedGame).isEmpty();
    }

}

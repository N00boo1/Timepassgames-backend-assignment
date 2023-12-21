package org.timepassgames.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.timepassgames.model.Game;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {

    // Find game by name
    Game findByName(String name);

    // Delete game by name
    void deleteByName(String name);

    // Check whether the url or name exists in the database or not
    boolean existsByUrlAndNameNot(String url, String name);
}

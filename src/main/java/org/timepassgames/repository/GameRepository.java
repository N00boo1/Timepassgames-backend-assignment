package org.timepassgames.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.timepassgames.model.Game;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {

    Game findByName(String name);

    // Delete Game by name
    void deleteByName(String name);
}

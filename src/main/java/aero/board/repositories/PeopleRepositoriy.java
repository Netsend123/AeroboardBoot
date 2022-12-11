package aero.board.repositories;

import aero.board.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepositoriy extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);
}

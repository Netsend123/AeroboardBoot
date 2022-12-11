package aero.board.sevices;

import aero.board.model.Person;
import aero.board.repositories.PeopleRepositoriy;
import aero.board.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class  PersonDeteilsService implements UserDetailsService {
    private final PeopleRepositoriy peopleRepositoriy;

    @Autowired
    public PersonDeteilsService(PeopleRepositoriy peopleRepositoriy) {
        this.peopleRepositoriy = peopleRepositoriy;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepositoriy.findByUsername(s);
        System.out.println(person);
        if (person.isEmpty()){
            throw new UsernameNotFoundException("User not found!");
        }
        return new PersonDetails(person.get());
    }
}

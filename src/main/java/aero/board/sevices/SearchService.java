package aero.board.sevices;

import aero.board.model.DbObject;
import aero.board.repositories.SearchRepositoriy;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SearchService {
    private final SearchRepositoriy searchRepositoriy;
    @PersistenceContext
    private EntityManager em;

    @Autowired
    public SearchService(SearchRepositoriy searchRepositoriy) {
        this.searchRepositoriy = searchRepositoriy;
    }

    @Transactional
    public void saveSearch(DbObject dbObject) {
        searchRepositoriy.save(dbObject);
    }

    @Transactional
    public List<DbObject> getLastSeaecrchQueries() {
        return searchRepositoriy.lastListSearch();
    }
    @Transactional
    public void update(int id, String updateName){

        DbObject dbObjectToUpdate = searchRepositoriy.getReferenceById(id);
        dbObjectToUpdate.setAirportName(updateName);
    }
    @Transactional
    public void delete(int id){
        searchRepositoriy.delete(searchRepositoriy.getReferenceById(id));
    }
}

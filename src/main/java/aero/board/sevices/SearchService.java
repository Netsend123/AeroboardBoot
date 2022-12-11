package aero.board.sevices;

import aero.board.model.DbObject;
import aero.board.repositories.SearchRepositoriy;
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
}

package aero.board.dao;

import aero.board.model.DbObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class SearchDAO {

//    private final SessionFactory sessionFactory;
//
//    @Autowired
//    public SearchDAO(SessionFactory sessionFactory) {
//        this.sessionFactory = sessionFactory;
//    }
//
//    @Transactional
//    public void saveSearch(DbObject dbObject) {
//        Session session = sessionFactory.getCurrentSession();
//        session.save(dbObject);
//    }
//    @Transactional
//    public List<DbObject> listFromSearch() {
//        Session session = sessionFactory.getCurrentSession();
//        return  (List<DbObject>) session.createSelectionQuery("FROM DbObject ORDER BY id DESC LIMIT 30").getResultList();
//    }
}

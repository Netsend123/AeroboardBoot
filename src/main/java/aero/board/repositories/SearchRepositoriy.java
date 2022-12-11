package aero.board.repositories;

import aero.board.model.DbObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepositoriy extends JpaRepository<DbObject, Integer> {
        @Query("from DbObject ORDER BY id DESC")
        public List<DbObject> lastListSearch();
}

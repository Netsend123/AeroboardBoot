package aero.board.repositories;

import aero.board.model.DbObject;
import org.springframework.stereotype.Repository;

import java.util.List;
 @Repository
public interface GetLastSeaecrchQueries {
    List<DbObject> getLastSeaecrchQueries();
}

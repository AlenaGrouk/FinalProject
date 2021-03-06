package by.epam.project.dao.query.generic;

import by.epam.project.exception.DaoException;
import java.sql.Connection;
import java.util.List;

/**
 * Common interface of generator UPDATE query
 * @author Helena.Grouk
 */
public interface GenericUpdateQuery {
   <T> List<Integer> sendQuery(String query, Object[] params, Connection conn) throws DaoException;
}

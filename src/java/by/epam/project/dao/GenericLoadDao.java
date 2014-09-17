/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.dao;

import by.epam.project.dao.query.Params;
import java.util.List;

/**
 *
 * @author User
 */
public interface GenericLoadDao {
    public <T> List<T> query(String query, Object[] params, int pageSize, Params.RowMapper<T> mapper) throws DaoException;
    
}

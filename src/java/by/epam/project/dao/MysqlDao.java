/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.dao;

import by.epam.project.dao.query.GenericLoadQuery;
import by.epam.project.dao.query.GenericSaveQuery;
import by.epam.project.dao.query.GenericUpdateQuery;
import by.epam.project.dao.query.MysqlGenericLoadQuery;
import by.epam.project.dao.query.MysqlGenericSaveQuery;
import by.epam.project.dao.query.MysqlGenericUpdateQuery;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public interface MysqlDao {
    
    static final GenericLoadQuery loadDao = new MysqlGenericLoadQuery();
    static final GenericSaveQuery saveDao = new MysqlGenericSaveQuery();
    static final GenericUpdateQuery updateDao = new MysqlGenericUpdateQuery();
    
    
    public static Connection getConnection() throws DaoException{
        try {
            return ConnectionPool.getConnection();
        } catch (SQLException ex) {
            throw new DaoException("Cant take connection to database.");
        }
    }
    
    public static void returnConnection(Connection con) throws DaoException {
        
        try {
            con.commit();
        } catch (SQLException ex) {
            throw new DaoException("Error in commit connection in pool.");
        }
        
        try {
            
            ConnectionPool.returnConnection(con);
        } catch (SQLException ex) {
            throw new DaoException("Cant return connection in pool.");
        }
    }
    
    
    
    
}
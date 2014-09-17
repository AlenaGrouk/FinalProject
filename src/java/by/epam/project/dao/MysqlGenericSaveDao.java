/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.dao;

import static by.epam.project.controller.ProjectServlet.LOCALLOG;
import by.epam.project.dao.query.Params;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public class MysqlGenericSaveDao implements GenericSaveDao {
    
    private static final String PARAMS_IS_NULL_ERROR = "Query params should not be null";
    
    private static final String PARAMS_DEBUG_FMR = "index: {0}, value: {1}";

    private static final String DATASOURCE_IS_NULL =
        "The MDM DataSource is null, may be connection is not established " +
        "or broken";
    
    public MysqlGenericSaveDao(){}
    
    @Override
    public  <T> void query(String query, Params params) throws DaoException {
        if (params == null) {
            throw new DaoException(PARAMS_IS_NULL_ERROR);
        }
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnectionPool.getConnection();
            ps = conn.prepareStatement(query);
                 
            for (Object[] paramarray : params.params()) {
                
                for (int i = 0; i < paramarray.length; i++) {
                    
                    ps.setObject(i + 1, paramarray[i]);
                }
                ps.executeUpdate();
                // готовим новое исполнение запроса
                ps.clearParameters();                            
            }
        }
        catch (SQLException ex) {
            throw new DaoException(ex.getMessage(), ex);
        } finally {
            try {
               
                if (ps != null && !ps.isClosed()){
                    ps.close();
                }
                if (conn != null) {
                    ConnectionPool.returnConnection(conn);
                }
                
            } catch (SQLException ex) {
                LOCALLOG.info("Error in close connection.");
            }
        }
    }
    
   
}

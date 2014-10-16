/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.exception;

/**
 *
 * @author User
 */
public class DaoSqlException extends DaoException {
    
    private static final String str = "Error in MySQL query: ";
    
    public DaoSqlException(){
    }
    
    public DaoSqlException(String message, Throwable exception) {
        super(str + message, exception);
    }

    public DaoSqlException(Throwable exception) {
        super(exception);
    }
    
    public DaoSqlException(String message) {
        super(str + message);
    }
    
}

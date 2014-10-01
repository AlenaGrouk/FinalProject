/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.dao;

import by.epam.project.dao.entquery.*;
import by.epam.project.dao.entquery.CountryQuery;
import by.epam.project.dao.entquery.DirectionQuery;
import by.epam.project.dao.entquery.HotelQuery;
import by.epam.project.dao.entquery.RoleQuery;
import by.epam.project.dao.entquery.TourTypeQuery;
import by.epam.project.dao.entquery.TransModeQuery;
import by.epam.project.dao.entquery.UserQuery;
import by.epam.project.dao.query.Criteria;
import by.epam.project.dao.query.QueryExecutionException;
import by.epam.project.entity.City;
import by.epam.project.entity.Country;
import by.epam.project.entity.Direction;
import by.epam.project.entity.DirectionStayHotel;
import by.epam.project.entity.Hotel;
import by.epam.project.entity.LinkDirectionCity;
import by.epam.project.entity.LinkDirectionCountry;
import by.epam.project.entity.Role;
import by.epam.project.entity.TourType;
import by.epam.project.entity.TransportationMode;
import by.epam.project.entity.User;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class MysqlGuestDao implements MysqlDao, GuestDao {
    
    protected Connection mysqlConn;
    
    protected MysqlGuestDao() throws DaoException{}
    
    @Override
    public void open() throws DaoException {
        mysqlConn = MysqlDao.getConnection();
    }
    
    @Override
    public void close() throws DaoException {
        MysqlDao.returnConnection(mysqlConn);
    }
    
    @Override
    public void rollback() throws DaoException {
        try {
            mysqlConn.rollback();
        } catch (SQLException ex) {
            throw new DaoException("Rollback failed.");
        }
    }

    @Override
    public void toRegistrate(Criteria criteria) throws DaoException {
        
        Criteria test1 = new Criteria();
        Object login = criteria.getParam(PARAM_NAME_LOGIN);
        if (login == null) {
            throw new DaoException("Login is empty.");
        }
        test1.addParam(PARAM_NAME_LOGIN, login);
        try {
            List<User> person = new UserQuery().load(test1, loadDao, mysqlConn);
            if (!person.isEmpty()) {
                throw new DaoException("Login is not unique.");
            }
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
        
        Criteria test2 = new Criteria();
        Object email = criteria.getParam(PARAM_NAME_EMAIL);
        if (email == null) {
            throw new DaoException("Email is empty.");
        }
        test2.addParam(PARAM_NAME_EMAIL, email);
        try {
            List<User> person = new UserQuery().load(test2, loadDao, mysqlConn);
            if (!person.isEmpty()) {
                throw new DaoException("Email is not unique.");
            }
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
        
        Integer id_role;
        Criteria test3 = new Criteria();
        ClientType role = (ClientType) criteria.getParam(PARAM_NAME_ROLE);
        if (role == null) {
            throw new DaoException("Role is empty.");
        }
        test3.addParam(PARAM_NAME_ROLE, role.name());
        try {
            List<Role> listRole = new RoleQuery().load(test3, loadDao, mysqlConn);
            if (listRole.isEmpty() || listRole.size() > 1) {
                throw new DaoException("Unnoun role in database.");
            } else {
                id_role = listRole.get(0).getIdRole();
            }
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
        
        criteria.addParam(PARAM_NAME_ID_ROLE, id_role);
        
        List list = new ArrayList<>();
        list.add(new User(criteria));
        
        try {
            new UserQuery().save(list, saveDao, mysqlConn);
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
        
        //MysqlDao.returnConnection(mysqlConn);
    }
    
    @Override
    public User toLogin(Criteria criteria) throws DaoException {
        
        try {
            List<User> person = new UserQuery().load(criteria, loadDao, mysqlConn);
            if (person == null || person.size() > 1) {
                throw new DaoException("Error result of search.");
            } else {
                try {
                    return person.get(0);
                } catch (IndexOutOfBoundsException ex) {
                    return null;
                } 
            }
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
    }

    @Override
    public List<Country> toShowCountries(Criteria criteria) throws DaoException {
        try {
            List<Country> countries = new CountryQuery().load(criteria, loadDao, mysqlConn);
            return countries;
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
    }

    @Override
    public List<City> toShowCities(Criteria criteria) throws DaoException {
        try {
            List<City> cities = new CityQuery().load(criteria, loadDao, mysqlConn);
            return cities;
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
    }

    @Override
    public List<Hotel> toShowHotels(Criteria criteria) throws DaoException {
        try {
            List<Hotel> hotels = new HotelQuery().load(criteria, loadDao, mysqlConn);
            return hotels;
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
    }
    
    @Override
    public List<TourType> toShowTourTypes (Criteria criteria) throws DaoException {
        try {
            List<TourType> types = new TourTypeQuery().load(criteria, loadDao, mysqlConn);
            return types;
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
    }
    
    @Override
    public List<TransportationMode> toShowTransModes (Criteria criteria) throws DaoException {
        try {
            List<TransportationMode> modes = new TransModeQuery().load(criteria, loadDao, mysqlConn);
            return modes;
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
    }

    @Override
    public List<Direction> toShowDirections(Criteria criteria) throws DaoException {
        try {
            List<Direction> directions = new DirectionQuery().load(criteria, loadDao, mysqlConn);
            return directions;
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
    }

    @Override
    public List<LinkDirectionCountry> toShowLinkDirectionCountry(Criteria criteria) throws DaoException {
        try {
            List<LinkDirectionCountry> links = new DirectionCountryQuery().load(criteria, loadDao, mysqlConn);
            return links;
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
    }
    
    @Override
    public List<LinkDirectionCity> toShowLinkDirectionCity(Criteria criteria) throws DaoException {
        try {
            List<LinkDirectionCity> links = new DirectionCityQuery().load(criteria, loadDao, mysqlConn);
            return links;
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
    }

    @Override
    public List<DirectionStayHotel> toShowDirectionStayHotel(Criteria criteria) throws DaoException {
        try {
            List<DirectionStayHotel> stays = new DirectionStayHotelQuery().load(criteria, loadDao, mysqlConn);
            return stays;
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
    }

    

        
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.dao;

import static by.epam.project.dao.MysqlDao.saveDao;
import by.epam.project.dao.entquery.*;
import by.epam.project.dao.entquery.CountryQuery;
import by.epam.project.dao.entquery.DirectionQuery;
import by.epam.project.dao.entquery.HotelQuery;
import by.epam.project.dao.entquery.RoleQuery;
import by.epam.project.dao.entquery.TourTypeQuery;
import by.epam.project.dao.entquery.TransModeQuery;
import by.epam.project.dao.entquery.UserQuery;
import by.epam.project.dao.query.Criteria;
import by.epam.project.exception.QueryExecutionException;
import by.epam.project.entity.City;
import by.epam.project.entity.Country;
import by.epam.project.entity.Direction;
import by.epam.project.entity.DirectionStayHotel;
import by.epam.project.entity.Hotel;
import by.epam.project.entity.LinkDirectionCity;
import by.epam.project.entity.LinkDirectionCountry;
import by.epam.project.entity.Role;
import by.epam.project.entity.Tour;
import by.epam.project.entity.TourType;
import by.epam.project.entity.TransportationMode;
import by.epam.project.entity.User;
import by.epam.project.exception.DaoException;
import by.epam.project.exception.DaoLogicException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class MysqlGuestDao extends MysqlDao implements GuestDao {
    
    protected MysqlGuestDao(){}

    @Override
    public List<Role> showRoles(Criteria criteria) throws DaoException {
        try {
            List<Role> roles = new RoleQuery().load(criteria, loadDao, mysqlConn);
            return roles;
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
    }
    
    @Override
    public List<User> showUsers(Criteria criteria) throws DaoException {
        try {
            List<User> users = new UserQuery().load(criteria, loadDao, mysqlConn);
            return users;
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
    }
    
    @Override
    public Integer createNewUser(Criteria criteria) throws DaoException {
        try {
            User user = UserQuery.createBean(criteria);
            List list = new ArrayList<>();
            list.add(user);
            List<Integer> res = new UserQuery().save(list, saveDao, mysqlConn);
            if (res == null || res.isEmpty()) {
                throw new DaoLogicException("New user not created.");
            } else {
                return res.get(0);
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

    @Override
    public List<Tour> toShowTours(Criteria criteria) throws DaoException {
        try {
            List<Tour> tours = new TourQuery().load(criteria, loadDao, mysqlConn);
            return tours;
        } catch (QueryExecutionException ex) {
            throw new DaoException("Error in query.");
        }
    }

    

    

        
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.action.tour;

import by.epam.project.action.ActionCommand;
import static by.epam.project.action.JspParamNames.JSP_BOX_ALL_CITIES;
import static by.epam.project.action.JspParamNames.JSP_BOX_ALL_COUNTRIES;
import static by.epam.project.action.JspParamNames.JSP_BOX_ALL_DAYS_COUNT;
import static by.epam.project.action.JspParamNames.JSP_BOX_ALL_DEPART_DATE;
import static by.epam.project.action.JspParamNames.JSP_BOX_ALL_HOTELS;
import static by.epam.project.action.JspParamNames.JSP_BOX_ALL_PRICE;
import static by.epam.project.action.JspParamNames.JSP_CITY_LIST;
import static by.epam.project.action.JspParamNames.JSP_CITY_TAG_LIST;
import static by.epam.project.action.JspParamNames.JSP_COUNTRY_LIST;
import static by.epam.project.action.JspParamNames.JSP_COUNTRY_TAG_LIST;
import static by.epam.project.action.JspParamNames.JSP_DISCOUNT_STEP;
import static by.epam.project.action.JspParamNames.JSP_ID_DIRECTION;
import static by.epam.project.action.JspParamNames.JSP_IS_HIDDEN;
import static by.epam.project.action.JspParamNames.JSP_PAGE;
import static by.epam.project.action.JspParamNames.JSP_PRICE_STEP;
import static by.epam.project.action.JspParamNames.JSP_ROLE_TYPE;
import static by.epam.project.action.JspParamNames.JSP_TOUR_LIST;
import static by.epam.project.action.JspParamNames.JSP_USER_LOGIN;
import static by.epam.project.action.SessionGarbageCollector.cleanSession;
import static by.epam.project.action.city.GoShowCity.formCityList;
import static by.epam.project.action.country.GoShowCountry.formCountryList;
import static by.epam.project.action.direction.GoShowDirections.formTourTypeList;
import static by.epam.project.action.direction.GoShowDirections.formTransModeList;
import static by.epam.project.action.hotel.GoShowHotel.formHotelList;
import by.epam.project.controller.SessionRequestContent;
import static by.epam.project.dao.entquery.DirectionQuery.DAO_ID_DIRECTION;
import static by.epam.project.dao.entquery.RoleQuery.DAO_ROLE_NAME;
import static by.epam.project.dao.entquery.UserQuery.DAO_USER_LOGIN;
import by.epam.project.dao.query.Criteria;
import by.epam.project.entity.Tour;
import by.epam.project.exception.DaoException;
import by.epam.project.exception.DaoUserLogicException;
import by.epam.project.logic.SearchLogic;
import by.epam.project.logic.TourLogic;
import by.epam.project.manager.ConfigurationManager;
import by.epam.project.manager.MessageManager;
import java.util.List;

/**
 *
 * @author User
 */
public class GoShowTour implements ActionCommand {

    @Override
    public String execute(SessionRequestContent request) throws DaoUserLogicException {
        String page = ConfigurationManager.getProperty("path.page.tours");
        request.setSessionAttribute(JSP_PAGE, page);
        formTourList(request);
        formCountryList(request);
        formCityList(request);
        formHotelList(request);
        formTourTypeList(request);
        formTransModeList(request);
        
        request.setSessionAttribute(JSP_COUNTRY_TAG_LIST, request.getSessionAttribute(JSP_COUNTRY_LIST));
        request.setSessionAttribute(JSP_CITY_TAG_LIST, request.getSessionAttribute(JSP_CITY_LIST));
        request.setSessionAttribute(JSP_PRICE_STEP, ConfigurationManager.getProperty("price.step"));
        request.setSessionAttribute(JSP_DISCOUNT_STEP, ConfigurationManager.getProperty("discount.step"));
        request.setAttribute(JSP_BOX_ALL_DEPART_DATE, true);
        request.setAttribute(JSP_BOX_ALL_DAYS_COUNT, true);
        request.setAttribute(JSP_BOX_ALL_PRICE, true);
        request.setAttribute(JSP_BOX_ALL_COUNTRIES, true);
        request.setAttribute(JSP_BOX_ALL_CITIES, true);
        request.setAttribute(JSP_BOX_ALL_HOTELS, true);
        request.setAttribute(JSP_IS_HIDDEN, true);
        
        return page;
    }
    
    public static void formTourList(SessionRequestContent request) throws DaoUserLogicException {
        Criteria criteria = new Criteria();
        criteria.addParam(DAO_USER_LOGIN, request.getSessionAttribute(JSP_USER_LOGIN));
        criteria.addParam(DAO_ROLE_NAME, request.getSessionAttribute(JSP_ROLE_TYPE));
        criteria.addParam(DAO_ID_DIRECTION, request.getAttribute(JSP_ID_DIRECTION));
        
        try {
            List<Tour> tours = SearchLogic.getTours(criteria);
            request.setSessionAttribute(JSP_TOUR_LIST, tours);
        } catch (DaoException ex) {
            throw new DaoUserLogicException(MessageManager.getProperty("message.daoerror"));
        }
    }
            
}
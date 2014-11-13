/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.action.city;

import by.epam.project.action.ActionCommand;
import static by.epam.project.action.JspParamNames.*;
import by.epam.project.action.SessionRequestContent;
import static by.epam.project.dao.entquery.CityQuery.DAO_CITY_NAME;
import static by.epam.project.dao.entquery.CityQuery.DAO_CITY_PICTURE;
import static by.epam.project.dao.entquery.CityQuery.DAO_ID_CITY;
import static by.epam.project.dao.entquery.CountryQuery.DAO_ID_COUNTRY;
import static by.epam.project.dao.entquery.DescriptionQuery.DAO_DESCRIPTION_TEXT;
import static by.epam.project.dao.entquery.DescriptionQuery.DAO_ID_DESCRIPTION;
import static by.epam.project.dao.entquery.RoleQuery.DAO_ROLE_NAME;
import static by.epam.project.dao.entquery.UserQuery.DAO_USER_LOGIN;
import by.epam.project.dao.query.Criteria;
import by.epam.project.entity.City;
import by.epam.project.entity.ClientType;
import by.epam.project.entity.Description;
import by.epam.project.entity.User;
import by.epam.project.exception.LogicException;
import by.epam.project.exception.ServletLogicException;
import by.epam.project.exception.TechnicalException;
import by.epam.project.logic.CityLogic;
import by.epam.project.manager.ClientTypeManager;
import by.epam.project.manager.ConfigurationManager;
import by.epam.project.manager.MessageManager;
import static by.epam.project.manager.ParamManager.checkIntParam;
import by.epam.project.manager.Validator;


/**
 *
 * @author User
 */
public class SaveRedactCity extends CityCommand implements ActionCommand {

    @Override
    public String execute(SessionRequestContent request) throws ServletLogicException {
        String page = ConfigurationManager.getProperty("path.page.editcity");
        resaveParamsSaveCity(request);
        try {
            Criteria criteria = new Criteria();
            checkIntParam(request, criteria, JSP_CURR_ID_COUNTRY, DAO_ID_COUNTRY);

            City city = (City) request.getSessionAttribute(JSP_CURRENT_CITY);
            Validator.validateCity(city);
            if (city != null) {
                Integer idCity = city.getIdCity();
                if (idCity != null) {
                    criteria.addParam(DAO_ID_CITY, idCity);
                }
                Integer idDescription = city.getDescription().getIdDescription();
                if (idDescription != null) {
                    criteria.addParam(DAO_ID_DESCRIPTION, idDescription);
                }
                criteria.addParam(DAO_CITY_NAME, city.getName());
                criteria.addParam(DAO_CITY_PICTURE, city.getPicture());
                criteria.addParam(DAO_DESCRIPTION_TEXT, city.getDescription().getText());
            }

            User user = (User) request.getSessionAttribute(JSP_USER);
            if (user != null) {
                criteria.addParam(DAO_USER_LOGIN, user.getLogin());
                ClientType type = ClientTypeManager.clientTypeOf(user.getRole().getRoleName());
                criteria.addParam(DAO_ROLE_NAME, type);
            } else {
                criteria.addParam(DAO_ROLE_NAME, request.getSessionAttribute(JSP_ROLE_TYPE));
            }
        
            Integer resIdCity = new CityLogic().doRedactEntity(criteria);
            request.setParameter(JSP_SELECT_ID, resIdCity.toString());
            return new GoShowCity().execute(request); 
        } catch (TechnicalException | LogicException ex) {
            request.setAttribute("errorSaveReason", ex.getMessage());
            request.setAttribute("errorSave", "errorSaveData");
            request.setSessionAttribute(JSP_PAGE, page);
            return page;
        }         
    }

    private void resaveParamsSaveCity(SessionRequestContent request) {
        String currCountry = request.getParameter(JSP_CURR_ID_COUNTRY);
        if (currCountry != null && !currCountry.isEmpty()) {
            request.setAttribute(JSP_CURR_ID_COUNTRY, currCountry);
        }
        createCurrCity(request);
    }
    
    private void createCurrCity(SessionRequestContent request) {
        City currCity = (City) request.getSessionAttribute(JSP_CURRENT_CITY);
        if (currCity == null) {
            currCity = new City();
            currCity.setDescription(new Description());
        }
        currCity.setName(request.getParameter(JSP_CITY_NAME));
        currCity.setPicture(request.getParameter(JSP_CITY_PICTURE));
        currCity.getDescription().setText(request.getParameter(JSP_DESCRIPTION_TEXT));
        request.setSessionAttribute(JSP_CURRENT_CITY, currCity);
    }
    
}
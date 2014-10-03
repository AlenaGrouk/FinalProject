/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.action;

import static by.epam.project.action.ActionCommand.*;

import static by.epam.project.controller.JspParamNames.JSP_CITY_LIST;
import static by.epam.project.controller.JspParamNames.JSP_COUNTRY_LIST;
import static by.epam.project.controller.JspParamNames.JSP_CURRENT_COUNTRY;
import static by.epam.project.controller.JspParamNames.JSP_CURR_CITY_LIST;
import static by.epam.project.controller.JspParamNames.JSP_PAGE;
import by.epam.project.controller.SessionRequestContent;
import by.epam.project.entity.City;
import by.epam.project.entity.Country;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author User
 */
public class ShowCitiesOfCountry implements ActionCommand {

    public ShowCitiesOfCountry() {
    }

    @Override
    public String execute(SessionRequestContent request) throws DaoLogicException {
        String page = (String) request.getSessionAttribute(JSP_PAGE);
        
       
        
        List<Country> countryList = (List<Country>) request.getSessionAttribute(JSP_COUNTRY_LIST);
        if (countryList == null || countryList.isEmpty()){
            new GoShowCountry().execute(request);
            countryList = (List<Country>) request.getSessionAttribute(JSP_COUNTRY_LIST);
        }
        
        List<City> cityList = (List<City>) request.getSessionAttribute(JSP_CITY_LIST);
        if (cityList == null || cityList.isEmpty()){
            new GoShowCity().execute(request);
            cityList = (List<City>) request.getSessionAttribute(JSP_CITY_LIST);
        }

        Integer idCountry = Integer.decode(request.getParameter(PARAM_NAME_SELECT_ID));
       
        for (Country c: countryList) {
            if (Objects.equals(c.getIdCountry(), idCountry)) {
                request.setSessionAttribute(JSP_CURR_CITY_LIST, c.getCityCollection());
                request.setAttribute(JSP_CURRENT_COUNTRY, c);
                return page;
            }
        }
        request.setSessionAttribute(JSP_CURR_CITY_LIST, cityList);
        return page;
    }
    
}

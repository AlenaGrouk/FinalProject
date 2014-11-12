/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.action.country;

import by.epam.project.action.ActionCommand;
import static by.epam.project.action.JspParamNames.*;
import by.epam.project.action.SessionRequestContent;
import by.epam.project.entity.Country;
import by.epam.project.exception.ServletLogicException;
import by.epam.project.manager.ConfigurationManager;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author User
 */
public class ShowCountry implements ActionCommand {

    @Override
    public String execute(SessionRequestContent request) throws ServletLogicException {
        String page = ConfigurationManager.getProperty("path.page.countries");
        request.setSessionAttribute(JSP_PAGE, page);
        resaveParamsShowCountry(request);
        showSelectedCountry(request);
        return page;
    }
    
    public static void showSelectedCountry(SessionRequestContent request) {
        String selected = request.getParameter(JSP_SELECT_ID);
        Country currCountry = null;
        if (selected != null) {
            Integer idCountry = Integer.decode(selected); 
            if (idCountry != null) {
                List<Country> list = (List<Country>) request.getSessionAttribute(JSP_COUNTRY_LIST);
                Iterator<Country> it = list.iterator();
                while (it.hasNext() && currCountry == null) {
                    Country country = it.next();
                    if (Objects.equals(country.getIdCountry(), idCountry)) {
                        currCountry = country;
                        request.setAttribute(JSP_CURR_ID_COUNTRY, idCountry);
                    }
                }
            }
        }
        request.setSessionAttribute(JSP_CURRENT_COUNTRY, currCountry);
    }
    
    private void resaveParamsShowCountry(SessionRequestContent request) {
        String validCountryStatus = request.getParameter(JSP_COUNTRY_VALID_STATUS);
        if(validCountryStatus != null) {
            request.setAttribute(JSP_COUNTRY_VALID_STATUS, validCountryStatus);
        }
        
        String invalidCountryStatus = request.getParameter(JSP_COUNTRY_INVALID_STATUS);
        if(invalidCountryStatus != null) {
            request.setAttribute(JSP_COUNTRY_INVALID_STATUS, invalidCountryStatus);
        }
        
        String validCityStatus = request.getParameter(JSP_CITY_VALID_STATUS);
        if(validCityStatus != null) {
            request.setAttribute(JSP_CITY_VALID_STATUS, validCityStatus);
        }
        
        String invalidCityStatus = request.getParameter(JSP_CITY_INVALID_STATUS);
        if(invalidCityStatus != null) {
            request.setAttribute(JSP_CITY_INVALID_STATUS, invalidCityStatus);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.action.country;

import by.epam.project.action.ActionCommand;
import static by.epam.project.action.JspParamNames.JSP_CITY_LIST;
import static by.epam.project.action.JspParamNames.JSP_CURR_CITY_LIST;
import static by.epam.project.action.JspParamNames.JSP_CURR_ID_CITY;
import static by.epam.project.action.JspParamNames.JSP_CURR_ID_COUNTRY;
import static by.epam.project.action.JspParamNames.JSP_HOTEL_LIST;
import static by.epam.project.action.JspParamNames.JSP_ID_CITY;
import static by.epam.project.action.JspParamNames.JSP_ID_COUNTRY;
import static by.epam.project.action.JspParamNames.JSP_PAGE;
import static by.epam.project.action.ProcessSavedParameters.resaveParams;
import by.epam.project.action.SessionRequestContent;
import static by.epam.project.action.city.GoShowCity.formCityList;
import static by.epam.project.action.hotel.GoShowHotel.formHotelList;
import static by.epam.project.action.hotel.SaveRedactHotel.resaveParamsSaveHotel;
import by.epam.project.entity.City;
import by.epam.project.entity.Hotel;
import by.epam.project.exception.ServletLogicException;
import by.epam.project.manager.ConfigurationManager;
import by.epam.project.manager.ParamManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class IfCountrySelected implements ActionCommand {

    @Override
    public String execute(SessionRequestContent request) throws ServletLogicException {
        String page = (String) request.getSessionAttribute(JSP_PAGE);
        resaveParamsCountrySelected(request);
        request.setAttribute(JSP_CURR_ID_CITY, "0");

        String currCountry = request.getParameter(JSP_CURR_ID_COUNTRY);
        if (currCountry != null && !currCountry.isEmpty()){
            Integer idCountry = Integer.decode(currCountry);
            if (idCountry != 0) {
                request.setAttribute(JSP_ID_COUNTRY, idCountry);
            }
            formCityList(request);
            List<City> cityList = (List<City>) request.getSessionAttribute(JSP_CITY_LIST);
            request.setSessionAttribute(JSP_CURR_CITY_LIST, cityList);
            
            List<Hotel> commonHotelList = new ArrayList();
            for (City c : cityList) {
                request.setAttribute(JSP_ID_CITY, c.getIdCity());
                formHotelList(request);
                List<Hotel> hotelList = (List<Hotel>) request.getSessionAttribute(JSP_HOTEL_LIST);
                commonHotelList.addAll(hotelList);
            }
            request.setSessionAttribute(JSP_HOTEL_LIST, commonHotelList);
        }
        request.setSessionAttribute(JSP_PAGE, page);
        return page;
    }

    private static void resaveParamsCountrySelected(SessionRequestContent request) {
        String page = (String) request.getSessionAttribute(JSP_PAGE);
        String editHotelPage = ConfigurationManager.getProperty("path.page.edithotel");
        if (page == null ? editHotelPage == null : page.equals(editHotelPage)) {
            resaveParamsSaveHotel(request);
        } else {
            
        }
    }
    
}

package by.epam.project.action.city;


import by.epam.project.action.ActionCommand;
import static by.epam.project.action.JspParamNames.*;
import by.epam.project.action.SessionRequestContent;
import by.epam.project.entity.City;
import by.epam.project.entity.Country;
import by.epam.project.exception.ServletLogicException;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author User
 */
public class ShowCitiesOfCountry extends CityCommand implements ActionCommand {
    @Override
    public String execute(SessionRequestContent request) throws ServletLogicException {
        String page = (String) request.getSessionAttribute(JSP_PAGE);

        List<Country> countryList = (List<Country>) request.getSessionAttribute(JSP_COUNTRY_LIST);
        List<City> cityList = (List<City>) request.getSessionAttribute(JSP_CITY_LIST);
        Integer idCountry = Integer.decode(request.getParameter(JSP_SELECT_ID));

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

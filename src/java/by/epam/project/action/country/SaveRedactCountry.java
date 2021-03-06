package by.epam.project.action.country;

import by.epam.project.action.ActionCommand;
import static by.epam.project.action.JspParamNames.*;
import static by.epam.project.dao.DaoParamNames.*;
import by.epam.project.action.SessionRequestContent;
import by.epam.project.dao.query.Criteria;
import by.epam.project.dao.ClientType;
import by.epam.project.entity.Country;
import by.epam.project.entity.Description;
import by.epam.project.entity.User;
import by.epam.project.exception.LogicException;
import by.epam.project.exception.ServletLogicException;
import by.epam.project.exception.TechnicalException;
import by.epam.project.logic.AbstractLogic;
import by.epam.project.logic.LogicFactory;
import by.epam.project.logic.LogicType;
import by.epam.project.manager.ClientTypeManager;
import by.epam.project.manager.ConfigurationManager;
import by.epam.project.manager.Validator;

/**
 * Class of command to save redacted or created country object.
 * @author Helena.Grouk
 */
public class SaveRedactCountry extends CountryCommand implements ActionCommand {
    @Override
    public String execute(SessionRequestContent request) throws ServletLogicException {
        String page = ConfigurationManager.getProperty("path.page.editcountry");
        resaveParamsSaveCountry(request);
        try {
            Criteria criteria = new Criteria();
            Country country = (Country) request.getSessionAttribute(JSP_CURRENT_COUNTRY);
            Validator.validateCountry(country);
            if (country != null) {
                Integer idCountry = country.getIdCountry();
                if (idCountry != null) {
                    criteria.addParam(DAO_ID_COUNTRY, idCountry);
                }
                Integer idDescription = country.getDescription().getIdDescription();
                if (idDescription != null) {
                    criteria.addParam(DAO_ID_DESCRIPTION, idDescription);
                }
                criteria.addParam(DAO_COUNTRY_NAME, country.getName());
                criteria.addParam(DAO_COUNTRY_PICTURE, country.getPicture());
                criteria.addParam(DAO_DESCRIPTION_TEXT, country.getDescription().getText());
            }

            User user = (User) request.getSessionAttribute(JSP_USER);
            if (user != null) {
                criteria.addParam(DAO_USER_LOGIN, user.getLogin());
                ClientType type = ClientTypeManager.clientTypeOf(user.getRole().getRoleName());
                criteria.addParam(DAO_ROLE_NAME, type);
            } else {
                criteria.addParam(DAO_ROLE_NAME, request.getSessionAttribute(JSP_ROLE_TYPE));
            }
            AbstractLogic countryLogic = LogicFactory.getInctance(LogicType.COUNTRYLOGIC);
            Integer resIdCountry = countryLogic.doRedactEntity(criteria);
            request.setParameter(JSP_SELECT_ID, resIdCountry.toString());
            page = new GoShowCountry().execute(request);
        } catch (TechnicalException | LogicException ex) {
            request.setAttribute("errorSaveReason", ex.getMessage());
            request.setAttribute("errorSave", "message.errorSaveData");
            request.setSessionAttribute(JSP_PAGE, page);
        }
        return page;
    }

    /**
     * Resave common parameters of edit country page.
     * @param request parameters and attributes of the request and the session
     */
    private void resaveParamsSaveCountry(SessionRequestContent request) {
        Country currCountry = (Country) request.getSessionAttribute(JSP_CURRENT_COUNTRY);
        if (currCountry == null) {
            currCountry = new Country();
            currCountry.setDescription(new Description());
        }
        currCountry.setName(request.getParameter(JSP_COUNTRY_NAME));
        currCountry.setPicture(request.getParameter(JSP_COUNTRY_PICTURE));
        currCountry.getDescription().setText(request.getParameter(JSP_DESCRIPTION_TEXT));
        request.setSessionAttribute(JSP_CURRENT_COUNTRY, currCountry);
    }
}



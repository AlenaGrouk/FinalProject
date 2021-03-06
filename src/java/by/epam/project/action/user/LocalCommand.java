package by.epam.project.action.user;

import by.epam.project.action.ActionCommand;
import static by.epam.project.action.JspParamNames.*;
import static by.epam.project.dao.DaoParamNames.*;
import by.epam.project.action.SessionRequestContent;
import by.epam.project.dao.query.Criteria;
import by.epam.project.exception.LogicException;
import by.epam.project.exception.ServletLogicException;
import by.epam.project.exception.TechnicalException;
import by.epam.project.logic.AbstractLogic;
import by.epam.project.logic.LogicFactory;
import by.epam.project.logic.LogicType;
import by.epam.project.manager.ConfigurationManager;
import by.epam.project.manager.LocaleManager;
import java.util.Locale;

/**
 * Class of command of changing user locale and language
 * @author Helena.Grouk
 */
public class LocalCommand implements ActionCommand {
    @Override
    public String execute(SessionRequestContent request) throws ServletLogicException {
        String page = (String)request.getSessionAttribute(JSP_PAGE);
        if (page == null) {
            page = ConfigurationManager.getProperty("path.page.index");
            request.setSessionAttribute(JSP_PAGE, page);
        }
        String leng = request.getParameter(JSP_LENG);
        Locale locale = LocaleManager.getLocale(leng);
        if (locale != null) {
            request.setSessionAttribute(JSP_LOCALE, locale);
        }
        Integer idUser = (Integer) request.getSessionAttribute(JSP_ID_USER);
        if (idUser == null) {
            return page;
        }
        Criteria criteria = new Criteria();
        Locale currLocale = (Locale)(request.getSessionAttribute(JSP_LOCALE));
        criteria.addParam(DAO_USER_LANGUAGE, currLocale.getLanguage());
        criteria.addParam(DAO_ID_USER, idUser);
        criteria.addParam(DAO_ROLE_NAME, request.getSessionAttribute(JSP_ROLE_TYPE));
        try {
            AbstractLogic userLogic = LogicFactory.getInctance(LogicType.USERLOGIC);
            userLogic.doRedactEntity(criteria);
        } catch (TechnicalException | LogicException ex) {
            request.setAttribute("errorReason", ex.getMessage());
            request.setAttribute("errorSaveData", "message.errorSaveData");
            request.setSessionAttribute(JSP_PAGE, page);
        }
        return page;
    }
}

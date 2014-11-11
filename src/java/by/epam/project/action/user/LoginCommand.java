/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package by.epam.project.action.user;

import by.epam.project.action.ActionCommand;
import static by.epam.project.action.JspParamNames.*;
import by.epam.project.action.SessionRequestContent;
import static by.epam.project.dao.entquery.RoleQuery.DAO_ROLE_NAME;
import static by.epam.project.dao.entquery.UserQuery.DAO_USER_LOGIN;
import static by.epam.project.dao.entquery.UserQuery.DAO_USER_PASSWORD;
import static by.epam.project.dao.entquery.UserQuery.DAO_USER_STATUS;
import by.epam.project.dao.query.Criteria;
import by.epam.project.entity.ClientType;
import by.epam.project.entity.User;
import by.epam.project.exception.ServletLogicException;
import by.epam.project.exception.TechnicalException;
import by.epam.project.logic.UserLogic;
import by.epam.project.manager.ClientTypeManager;
import by.epam.project.manager.ConfigurationManager;
import static by.epam.project.manager.LocaleManager.getLocale;
import by.epam.project.manager.MessageManager;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author User
 */
public class LoginCommand implements ActionCommand{

    @Override
    public String execute(SessionRequestContent request) throws ServletLogicException{
        String page;
        
        Criteria criteria = new Criteria();
        criteria.addParam(DAO_USER_LOGIN, request.getParameter(JSP_USER_LOGIN));
        criteria.addParam(DAO_USER_PASSWORD, request.getParameter(JSP_USER_PASSWORD).hashCode());
        criteria.addParam(DAO_ROLE_NAME, request.getSessionAttribute(JSP_ROLE_TYPE));
        criteria.addParam(DAO_USER_STATUS, ACTIVE);
        try {
            List<User> users = new UserLogic().doGetEntity(criteria);
            if (users != null && ! users.isEmpty()) {
                User user = users.get(0);
                String role = user.getRole().getRoleName();
                request.setSessionAttribute(JSP_USER, user);
                ClientType type = ClientTypeManager.clientTypeOf(role);
                request.setSessionAttribute(JSP_ROLE_TYPE, type);
                Locale locale = getLocale(user.getLanguage());
                if (locale != null) {
                    request.setSessionAttribute(JSP_LOCALE, locale);
                }
                page = ConfigurationManager.getProperty("path.page.main");
            } else {
                request.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.loginerror"));
                page = ConfigurationManager.getProperty("path.page.login");
            }
            request.setSessionAttribute(JSP_PAGE, page);
            return page;
        } catch (TechnicalException ex) {
            throw new ServletLogicException(ex.getMessage(), ex);
        }
    }
    
}

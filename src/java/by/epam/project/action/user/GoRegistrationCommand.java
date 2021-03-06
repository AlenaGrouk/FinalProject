package by.epam.project.action.user;

import by.epam.project.action.ActionCommand;
import static by.epam.project.action.JspParamNames.JSP_PAGE;
import by.epam.project.action.SessionRequestContent;
import by.epam.project.manager.ConfigurationManager;

/**
 * Class of command of displaying the page of registration user.
 * @author Helena.Grouk
 */
public class GoRegistrationCommand implements ActionCommand {
    @Override
    public String execute(SessionRequestContent request) {
        String page = ConfigurationManager.getProperty("path.page.registration");
        request.setSessionAttribute(JSP_PAGE, page);
        return page;
    }
}

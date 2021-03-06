package by.epam.project.action.order;

import by.epam.project.action.ActionCommand;
import static by.epam.project.action.JspParamNames.*;
import static by.epam.project.dao.DaoParamNames.*;
import by.epam.project.action.SessionRequestContent;
import by.epam.project.dao.query.Criteria;
import by.epam.project.dao.ClientType;
import by.epam.project.entity.Order;
import by.epam.project.entity.Tourist;
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
import java.util.List;

/**
 * Class of command to save redacted order object.
 * @author Helena.Grouk
 */
public class SaveRedactOrder extends OrderCommand implements ActionCommand {
    private static final String MSG_ERR_NULL_ENTITY = "message.errorNullEntity";
    private static final String MSG_ERR_TOURIST_ENTITY = "message.errorTableTourist";

    @Override
    public String execute(SessionRequestContent request) throws ServletLogicException {
        String page = ConfigurationManager.getProperty("path.page.edittourist");
        try {
            resaveParamsSaveOrder(request);
            Criteria criteria = new Criteria();
            Order order = (Order) request.getSessionAttribute(JSP_CURRENT_ORDER);
            Validator.validateOrder(order);
            if (order != null) {
                Integer idOrder = order.getIdOrder();
                if (idOrder != null) {
                    criteria.addParam(DAO_ID_ORDER, idOrder);
                }
                criteria.addParam(DAO_ORDER_TOURIST_LIST, order.getTouristCollection());
            }

            User user = (User) request.getSessionAttribute(JSP_USER);
            if (user != null) {
                criteria.addParam(DAO_USER_LOGIN, user.getLogin());
                ClientType type = ClientTypeManager.clientTypeOf(user.getRole().getRoleName());
                criteria.addParam(DAO_ROLE_NAME, type);
            } else {
                criteria.addParam(DAO_ROLE_NAME, request.getSessionAttribute(JSP_ROLE_TYPE));
            }

            AbstractLogic orderLogic = LogicFactory.getInctance(LogicType.ORDERLOGIC);
            Integer resIdOrder = orderLogic.doRedactEntity(criteria);
            request.setParameter(JSP_SELECT_ID, resIdOrder.toString());
            page = new ShowOrder().execute(request);
        } catch (TechnicalException | LogicException ex) {
            request.setAttribute("errorSaveReason", ex.getMessage());
            request.setAttribute("errorSave", "message.errorSaveData");
            request.setSessionAttribute(JSP_PAGE, page);
        }
        return page;
    }

    /**
     * Resave common parameters of edit order page.
     * @param request parameters and attributes of the request and the session
     * @throws by.epam.project.exception.TechnicalException
     */
    private void resaveParamsSaveOrder(SessionRequestContent request) throws TechnicalException {
        Order order = (Order) request.getSessionAttribute(JSP_CURRENT_ORDER);
        if (order == null) {
            throw new TechnicalException(MSG_ERR_NULL_ENTITY);
        }
        Integer seats = order.getSeats();
        List<Tourist> touristList = (List<Tourist>) order.getTouristCollection();
        String[] firstName = request.getAllParameters(JSP_TOURIST_FIRST_NAME);
        String[] middleName = request.getAllParameters(JSP_TOURIST_MIDDLE_NAME);
        String[] lastName = request.getAllParameters(JSP_TOURIST_LAST_NAME);
        String[] passport = request.getAllParameters(JSP_TOURIST_PASSPORT);

        if (touristList.size() != seats || firstName == null || firstName.length < seats
                || middleName == null || middleName.length < seats
                || lastName == null || lastName.length < seats
                || passport == null || passport.length < seats) {
            throw new TechnicalException(MSG_ERR_TOURIST_ENTITY);
        }

        for (int i = 0; i < seats; i++) {
            Tourist tourist = touristList.get(i);
            tourist.setFirstName(firstName[i]);
            tourist.setMiddleName(middleName[i]);
            tourist.setLastName(lastName[i]);
            tourist.setPassport(passport[i]);
            Validator.validateTourist(tourist);
        }
        request.setSessionAttribute(JSP_CURRENT_ORDER, order);
    }
}

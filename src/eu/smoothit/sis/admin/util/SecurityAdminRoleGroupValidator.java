package eu.smoothit.sis.admin.util;

import java.util.Collection;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import eu.smoothit.sis.db.SisDAOFactory;
import eu.smoothit.sis.db.api.daos.IUserRoleDAO;
import eu.smoothit.sis.db.impl.entities.UserRole;

/**
 * @author ye cao Prevent storage when finding existig role/group in DB to the
 *         input TODO distinguish edit/add
 */
public class SecurityAdminRoleGroupValidator implements Validator {
	// define DAO
	private IUserRoleDAO roleGroupDAO = (IUserRoleDAO) SisDAOFactory
			.getFactory().createUserRoleDAO();

	public void validate(FacesContext context, UIComponent component, Object obj)
			throws ValidatorException {
		// Obtain the client ID of groupfield from f:attribute.
		String attributeGroup = (String) component.getAttributes().get(
				"groupID");
		// Find the actual JSF component for the client ID.
		UIInput inputGroup = (UIInput) context.getViewRoot().findComponent(
				attributeGroup);
		String roleName = (String) obj;
		UserRole roleGroup = new UserRole();
		roleGroup.setRole(roleName);
		String groupName = (String) inputGroup.getValue();
		roleGroup.setRoleGroup(groupName);
		Collection<UserRole> foundRoleGroupList;
		foundRoleGroupList = roleGroupDAO.get(roleGroup);
		if (foundRoleGroupList != null && !foundRoleGroupList.isEmpty()) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "role|group pair:" + roleName
							+ "|" + groupName + " Exists",
					"Input role+group exist in database, duplicated is not allowed");
			throw new ValidatorException(message);
		}
	}

}

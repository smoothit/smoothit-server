package eu.smoothit.sis.admin.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author ye cao
 * validator password by comparing the second input to the first one.
 */
public class SecurityAdminUserPswValidator implements Validator {

	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		 // Obtain the client ID of the first password field from f:attribute.
        String passwordId = (String) component.getAttributes().get("passwordID");
        // Find the actual JSF component for the client ID.
        UIInput passwordInput = (UIInput) context.getViewRoot().findComponent(passwordId);
        // Get its value, the entered password of the first field.
        String password = (String) passwordInput.getValue();
        // Cast the value of the entered password of the second field back to String.
        String confirm = (String) value;
        // Compare the first password with the second password.
        if (!password.equals(confirm)) {
            throw new ValidatorException(new FacesMessage("Passwords are not equal."));
        }
	}
}

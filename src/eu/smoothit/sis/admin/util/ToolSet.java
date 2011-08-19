package eu.smoothit.sis.admin.util;

import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;

public class ToolSet {
	// used by get instance of current FacesContext
	private static FacesContext fc;
	// Helpers
	// ------------------------------------------------------------------------------------

	/**
	 * Set global error message.
	 * 
	 * @param errorMessage
	 */
	public static void setErrorMessage(String errorMessage) {
		resetErrorMessage();
		// log(errorMessage);
		fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, new FacesMessage(errorMessage));
	}

	public static void resetErrorMessage() {
		// remove existing message
		fc = FacesContext.getCurrentInstance();
		Iterator<FacesMessage> iterator = fc.getMessages();
		while (iterator.hasNext()) {
			FacesMessage fm = (FacesMessage) iterator.next();
			fm.setDetail("");
			fm.setSummary("");
		}
	}
	

	/**
	 * Get attribute value from the component using action event and attribute
	 * name.
	 * 
	 * @param event
	 *            The action event.
	 * @param name
	 *            The attribute name.
	 */
	public static String getAttribute(ActionEvent event, String name) {
		String value = (String) event.getComponent().getAttributes().get(name);
		// log("Name=" + name + ", Value=" + value);
		return value;
	}
	
	/**
	 * update values of UI components
	 */
	public static void refreshPage() {
		FacesContext context = FacesContext.getCurrentInstance();
		String viewId = context.getViewRoot().getViewId();
		ViewHandler handler = context.getApplication().getViewHandler();
		UIViewRoot root = handler.createView(context, viewId);
		root.setViewId(viewId);
		context.setViewRoot(root);
	}

	/**
	 * advance submit progress
	 */
	public static void advanceSubmit(ValueChangeEvent event) {
		if (!event.getPhaseId().equals(PhaseId.INVOKE_APPLICATION)) {
			event.setPhaseId(PhaseId.INVOKE_APPLICATION);
			event.queue();
			return;
		}
	}

}

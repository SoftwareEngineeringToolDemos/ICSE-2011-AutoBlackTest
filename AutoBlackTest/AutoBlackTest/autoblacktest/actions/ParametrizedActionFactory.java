package autoblacktest.actions;

import org.apache.log4j.Logger;

import autoblacktest.actions.parameters.ComboBoxParameters;
import autoblacktest.actions.parameters.ListParameters;
import autoblacktest.actions.parameters.ParametrizedAction;
import autoblacktest.actions.parameters.TabbedPaneParameters;
import autoblacktest.actions.parameters.TableParameters;
import autoblacktest.actions.parameters.TextWidgetParameters;
import autoblacktest.actions.parameters.TreeParameters;


/**
 * Factory class used to get the correct category implementation of
 * {@link ParametrizedAction} to retrieve test object parameters.
 * 
 * @see ComboBoxParameters, ListParameters and TabbedPaneParameters.
 * 
 * @author Andrea Mattavelli
 */
public class ParametrizedActionFactory {

	private static final Logger logger = Logger.getLogger(ParametrizedActionFactory.class);
	
	/**
	 * Returns an instance of a class used to retrieve test object parameter
	 * values.
	 * 
	 * @param uIClassID
	 *            the UI category of test object.
	 * @return an instance of a class used to retrieve test object parameter
	 *         values.
	 * @throws Exception if the uIClassID passed as parameter is not supported.
	 */
	public static ParametrizedAction getParametrizedAction(String uIClassID) throws Exception {
		ParametrizedAction instance = null;
		
		if(uIClassID.equals("ComboBoxUI")) {
//			logger.debug("Instance of type ComboBoxParameters");
			
			instance = new ComboBoxParameters();
		} else if(uIClassID.equals("TableUI")) {
//			logger.debug("Instance of type TabbedPaneUI");
			
			instance = new TableParameters();
		} else if(uIClassID.equals("TreeUI")) {
//			logger.debug("Instance of type TabbedPaneUI");
			
			instance = new TreeParameters();
		} else if(uIClassID.equals("TabbedPaneUI")) {
//			logger.debug("Instance of type TabbedPaneUI");
			
			instance = new TabbedPaneParameters();
		} else if(uIClassID.equals("ListUI")) {
//			logger.debug("Instance of type ListUI");
			
			instance = new ListParameters();
		} else if(uIClassID.equals("TextFieldUI")
				||uIClassID.equals("TextAreaUI")
				||uIClassID.equals("TextPaneUI")
				||uIClassID.equals("PasswordFieldUI")
				||uIClassID.equals("FormattedTextFieldUI")
				||uIClassID.equals("EditorPaneUI")
				) {
//			logger.debug("Instance of type TextFieldUI");
			
			instance = new TextWidgetParameters();
		}
		
		if(instance == null) {
			logger.error("uIClassID not accepted. Throwing exception");
			
			throw new Exception("uIClassID not accepted");
		}
		
		return instance;
	}
}

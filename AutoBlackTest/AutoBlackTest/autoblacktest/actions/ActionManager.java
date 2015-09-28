package autoblacktest.actions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.env.Action;


import autoblacktest.actions.parameters.ParameterNumberFormat;
import autoblacktest.actions.parameters.ParametrizedAction;

import com.rational.test.ft.object.interfaces.TestObject;

/**
 * Class used for adding simple actions to teachingbox action set. It manages
 * actions with and without parameters, getting parameter values at runtime for
 * the specified test object under investigation.
 * 
 * @author Andrea Mattavelli
 */
public class ActionManager {

	private static final Logger logger = Logger.getLogger(ActionManager.class);

	/**
	 * Returns a list of all possible simple actions, with and without
	 * parameters, that can be performed on the selected test object, in
	 * teaching-box compatible format.
	 * 
	 * @param to
	 *            test object on which the action is performed
	 * @param toIndex
	 *            index of the test object in the actual global test objects
	 *            vector
	 * @param uIClassID
	 *            uIClassID of the selected test object
	 * @return a list of all possible action that can be performed on the
	 *         selected test object
	 * @throws ClassNotFoundException
	 *             if the action helper class for this test object is not found
	 */
	public static List<Action> getActions(TestObject to, int toIndex, String uIClassID) throws ClassNotFoundException {
//		logger.debug("Getting actions for testObject class: " + uIClassID + " at index: " + toIndex);
		
		Class<?> c = Class.forName("autoblacktest.actions.simple." + uIClassID);
		Method methods[] = c.getDeclaredMethods();

//		logger.debug("Number of methods: " + methods.length);
		
		List<Action> actions = new ArrayList<Action>();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if(method.getModifiers() == 9) {
				Class<?> parameterTypes[] = method.getParameterTypes();
	
				if (parameterTypes.length > 1) {
//					logger.debug("Parametrized actions");
					
					actions.addAll(getParametrizedActions(to, toIndex, i, uIClassID));
				} else {
					double values[] = { toIndex, i };
					
//					logger.debug("Standard action without parameters: " + Arrays.toString(values));
					
					actions.add(new Action(values));
				}
			}	
		}
		return actions;
	}

	/**
	 * Method used to get all possible parametrized actions for the selected
	 * test object.
	 * 
	 * @param to
	 *            the selected test object
	 * @param toIndex
	 *            index of the test object in the actual global test object
	 *            vector
	 * @param methodIndex
	 *            index of the method to call
	 * @param uIClassID
	 *            uIClassID of the test object
	 * @return a list of all possible parametrized actions into teaching-box
	 *         compatible format.
	 */
	private static List<Action> getParametrizedActions(TestObject to, int toIndex, int methodIndex, String uIClassID) {
		List<Action> toReturn = new ArrayList<Action>();
		
		try {
			ParametrizedAction parametrizedAction = ParametrizedActionFactory.getParametrizedAction(uIClassID);

			String parameterValues[] = parametrizedAction.getValues(to);
			
//			logger.debug("Parameters values: " + Arrays.toString(parameterValues));
			
			for (int i = 0; i < parameterValues.length; i++) {
				double values[] = {toIndex, Double.parseDouble(methodIndex + "." + parameterValues[i]) };
				
//				logger.debug("Action with parameter: " + Arrays.toString(values));
				
				toReturn.add(new Action(values));
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return toReturn;
	}

	/**
	 * Returns <tt>true</tt> if the number represent a parametrized action.
	 * 
	 * @param number teaching box action number
	 * @return <tt>true</tt> if the number represent a parametrized action.
	 */
	public static boolean isParametrized(double number) {
		if (ParameterNumberFormat.getParameter(number) < 0) {
			return false;
		} else {
			return true;
		}
	}

}

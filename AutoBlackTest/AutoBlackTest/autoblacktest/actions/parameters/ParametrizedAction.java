package autoblacktest.actions.parameters;

import com.rational.test.ft.object.interfaces.TestObject;

/**
 * Interface of a method to retrieve all test object parameter values at
 * runtime.
 * 
 * @author Andrea Mattavelli
 */
public interface ParametrizedAction {

	/**
	 * Returns an array of parameter values as string representation of integer
	 * ones.
	 * 
	 * @param to test object used to obtain parameter values
	 * @return an array of parameter values as string representation of integer
	 * ones.
	 */
	public String[] getValues(TestObject to);
	
}

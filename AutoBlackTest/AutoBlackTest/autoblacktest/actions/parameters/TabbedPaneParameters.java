package autoblacktest.actions.parameters;

import org.apache.log4j.Logger;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.vp.ITestDataElementList;
import com.rational.test.ft.vp.ITestDataList;

public class TabbedPaneParameters implements ParametrizedAction {

	private Logger logger = Logger.getLogger(TabbedPaneParameters.class);
	
	@Override
	public String[] getValues(TestObject to) {
		ITestDataElementList values = ((ITestDataList) to.getTestData("list")).getElements();
		
//		logger.debug("Tabbed panes contained: " + values.getLength());
		
		String toReturn[] = new String[values.getLength()];
		for (int i = 0; i < toReturn.length; i++) {
			// il valore viene trasformato in modo che un 1 termini la sequenza di cifre (1234 -> 12341)
			toReturn[i] = ParameterNumberFormat.getFormattedParameter(i);
		}
		
		return toReturn;
	}

}

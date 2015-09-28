package autoblacktest.actions.parameters;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.vp.ITestDataElementList;
import com.rational.test.ft.vp.ITestDataList;

public class ComboBoxParameters implements ParametrizedAction {

	private Logger logger = Logger.getLogger(ComboBoxParameters.class);
	
	@Override
	public String[] getValues(TestObject to) {
		ITestDataElementList values = ((ITestDataList) to.getTestData("list")).getElements();
		
		List<String> toReturn = new ArrayList<String>();
//modificato il 18/03/11 commentati i metodi non parametrizzati nella classe ListUI.
//		if(values.getLength() > 3) {
//			logger.debug("More than 3 values contained");
			
			for (int i = 0; i < values.getLength(); i++) {
				// il valore viene trasformato in modo che un 1 termini la sequenza di cifre (1234 -> 12341)
				//modificato il 18/03/11
//				if(i != 0 && i != (values.getLength() - 1) && i != (values.getLength() / 2)) {
					toReturn.add(ParameterNumberFormat.getFormattedParameter(i));
//				}
			}
//		}
		
		return toReturn.toArray(new String[0]);
	}

}

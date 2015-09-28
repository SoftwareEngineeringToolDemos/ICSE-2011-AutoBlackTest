package autoblacktest.actions.parameters;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.vp.ITestDataElementList;
import com.rational.test.ft.vp.ITestDataList;

public class ListParameters implements ParametrizedAction {

	private Logger logger = Logger.getLogger(ListParameters.class);

	@Override
	public String[] getValues(TestObject to) {
		ITestDataElementList values = ((ITestDataList) to.getTestData("list")).getElements();

		List<String> toReturn = new ArrayList<String>();

		for (int i = 0; i < values.getLength(); i++) {
			toReturn.add(ParameterNumberFormat.getFormattedParameter(i));

		}

		return toReturn.toArray(new String[0]);
	}

}

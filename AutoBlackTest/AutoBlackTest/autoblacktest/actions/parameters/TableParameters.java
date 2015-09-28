package autoblacktest.actions.parameters;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.vp.ITestDataElementList;
import com.rational.test.ft.vp.ITestDataList;
import com.rational.test.ft.vp.ITestDataTable;

public class TableParameters implements ParametrizedAction {

	private Logger logger = Logger.getLogger(TableParameters.class);
	
	@Override
	public String[] getValues(TestObject to) {

		int cellsNumber = Integer.parseInt(to.getProperty("rowCount").toString()) * Integer.parseInt(to.getProperty("columnCount").toString());

		List<String> toReturn = new ArrayList<String>();
		for (int i = 0; i < cellsNumber; i++) {
			toReturn.add(ParameterNumberFormat.getFormattedParameter(i));
		}

		return toReturn.toArray(new String[0]);
	}

}

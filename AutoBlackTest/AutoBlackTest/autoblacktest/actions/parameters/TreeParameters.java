package autoblacktest.actions.parameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.vp.ITestDataTree;
import com.rational.test.ft.vp.ITestDataTreeNode;
import com.rational.test.ft.vp.ITestDataTreeNodes;

public class TreeParameters implements ParametrizedAction {

	private Logger logger = Logger.getLogger(TreeParameters.class);
	
	@Override
	public String[] getValues(TestObject to) {

		ITestDataTree iTreeData = (ITestDataTree)to.getTestData("tree");
		ITestDataTreeNodes iNodes = iTreeData.getTreeNodes();
		int nodeCount = iNodes.getNodeCount();
		
		List<String> toReturn = new ArrayList<String>();
		for (int i = 0; i < nodeCount; i++) {
			toReturn.add(ParameterNumberFormat.getFormattedParameter(i));
		}

		return toReturn.toArray(new String[0]);
	}	
}

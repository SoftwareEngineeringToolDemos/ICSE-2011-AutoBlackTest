package autoblacktest.actions.simple;

import resources.TesterHelper;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.TextSelectGuiSubitemTestObject;
import com.rational.test.ft.vp.ITestDataElementList;
import com.rational.test.ft.vp.ITestDataList;

public class ComboBoxUI extends TesterHelper {
//commento del 18/03/2011 vedi teachingbox.actions.ListParameters
//	public static void clickFirst(TestObject testObject) {
//		ITestDataElementList values = ((ITestDataList) testObject.getTestData("list")).getElements();
//
//		if (values.getLength() == 0) {
//			return;
//		}
//		
//		click(testObject, 0);
//	}
//	
//	public static void clickAtHalf(TestObject testObject) {
//		ITestDataElementList values = ((ITestDataList) testObject.getTestData("list")).getElements();
//
//		if (values.getLength() == 0) {
//			return;
//		}
//		else if(values.getLength() == 1) {
//			click(testObject, 0);
//			return;
//		}
//		
//		click(testObject, values.getLength() / 2);
//	}
//	
//	public static void clickLast(TestObject testObject) {
//		ITestDataElementList values = ((ITestDataList) testObject.getTestData("list")).getElements();
//
//		if (values.getLength() == 0) {
//			return;
//		}
//		
//		click(testObject, (values.getLength() - 1));
//	}
// fine commento del 18/03/2011 vedi teachingbox.actions.ListParameters		
	public static void click(TestObject testObject, long itemCount) {
		if(itemCount < 0) {
			return;
		}
		
		TextSelectGuiSubitemTestObject combo = new TextSelectGuiSubitemTestObject(testObject);
		combo.click();
		
		sleep(1);
		
		combo.click(atIndex((int)itemCount));
	}

}

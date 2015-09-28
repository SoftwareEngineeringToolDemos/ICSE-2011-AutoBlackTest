package autoblacktest.actions.simple;

import resources.TesterHelper;

import com.rational.test.ft.object.interfaces.SelectGuiSubitemTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.vp.ITestDataElementList;
import com.rational.test.ft.vp.ITestDataList;

public class ListUI extends TesterHelper {
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
//		click(testObject, values.getLength() - 1);
//	}
// fine commento del 18/03/2011 vedi teachingbox.actions.ListParameters	
	public static void click (TestObject testObject, long itemIndex) {
		new SelectGuiSubitemTestObject(testObject).click(atIndex((int)itemIndex));
	}
	//Giovanni
	// aggiunta la possibilità di effettuare doppio click su un elemento della lista
	// sfruttando le azioni parametrizzate
	public static void doubleClick (TestObject testObject, long itemIndex) {
		new SelectGuiSubitemTestObject(testObject).doubleClick(atIndex((int)itemIndex));
	}
//commento del 18/03/2011 vedi teachingbox.actions.ListParameters	
//	//Oliviero
//	public static void doubleClickFirst(TestObject testObject) {
//		ITestDataElementList values = ((ITestDataList) testObject.getTestData("list")).getElements();
//
//		if (values.getLength() == 0) {
//			return;
//		}
//		
//		doubleClick(testObject, 0);
//	}
//	
//	public static void doubleClickAtHalf(TestObject testObject) {
//		ITestDataElementList values = ((ITestDataList) testObject.getTestData("list")).getElements();
//
//		if (values.getLength() == 0) {
//			return;
//		}
//		
//		doubleClick(testObject, values.getLength() / 2);
//	}
//	
//	public static void doubleClickLast(TestObject testObject) {
//		ITestDataElementList values = ((ITestDataList) testObject.getTestData("list")).getElements();
//
//		if (values.getLength() == 0) {
//			return;
//		}
//		
//		doubleClick(testObject, values.getLength() - 1);
//	}
//	
//	public static void doubleClick (TestObject testObject, Integer itemIndex) {
//		new SelectGuiSubitemTestObject(testObject).doubleClick(atIndex(itemIndex));
//	}
//fine commento del 18/03/2011 vedi teachingbox.actions.ListParameters	
}

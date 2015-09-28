package autoblacktest.actions.simple;

import java.io.IOException;

import resources.TesterHelper;

import com.rational.test.ft.object.interfaces.GuiSubitemTestObject;
import com.rational.test.ft.object.interfaces.TestObject;

public class TableUI extends TesterHelper  {

	public static void click(TestObject testObject, long cellsNumber) throws IOException {
		int cells = (int)cellsNumber;
		
		int columnNumber = ((Integer)testObject.getProperty("columnCount")).intValue();
		int selectedRow = cells / columnNumber;
		int selectedColumn = cells % columnNumber;

		new GuiSubitemTestObject(testObject).click(atCell(atRow(selectedRow), atColumn(selectedColumn)));

	}
	
	public static void multiSelect(TestObject testObject, long cellsNumber) throws IOException {
		int cells = (int)cellsNumber;

		int columnNumber = ((Integer)testObject.getProperty("columnCount")).intValue();
		int selectedRow = cells / columnNumber;
		int selectedColumn = cells % columnNumber;

		new GuiSubitemTestObject(testObject).click(CTRL_LEFT, atCell(atRow(selectedRow), atColumn(selectedColumn)));

	}

	public static void doubleClick(TestObject testObject, long cellsNumber) throws IOException {
		int cells = (int)cellsNumber;
		
		int columnNumber = ((Integer)testObject.getProperty("columnCount")).intValue();
		int selectedRow = cells / columnNumber;
		int selectedColumn = cells % columnNumber;

		new GuiSubitemTestObject(testObject).doubleClick(atCell(atRow(selectedRow), atColumn(selectedColumn)));
	}
}

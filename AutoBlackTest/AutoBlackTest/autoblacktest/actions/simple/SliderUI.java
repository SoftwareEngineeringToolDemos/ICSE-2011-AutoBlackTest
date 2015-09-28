package autoblacktest.actions.simple;

import java.util.Random;

import resources.TesterHelper;

import com.rational.test.ft.object.interfaces.SubitemTestObject;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.script.Action;

public class SliderUI extends TesterHelper {


	//public void com.rational.test.ft.object.interfaces.GuiTestObject.click()
	public static void click(TestObject testObject) {
		//range di valori possibili
		int minimum = (Integer) testObject.getProperty("minimum");
		int maximum = (Integer) testObject.getProperty("maximum");
		
		int index;
		if(minimum != maximum) {
			Random random = new Random();
			random.setSeed(System.nanoTime());

			double range = maximum - minimum;
			double fraction = range * random.nextDouble();

			index = (int) (fraction + minimum);
		}
		else {
			index = minimum;
		}
		
		SubitemTestObject guiObject = new SubitemTestObject(testObject);
		
		guiObject.click(atPoint(0, 0));
		guiObject.setState(Action.hScroll(index));
	}

	//public void com.rational.test.ft.object.interfaces.GuiTestObject.doubleClick()
	//    public static void doubleClick(TestObject testObject) {
	//        new SubitemTestObject(testObject).doubleClick();
	//    }

	//public void com.rational.test.ft.object.interfaces.GuiTestObject.drag()
	//    public static void drag(TestObject testObject) {
	//        new SubitemTestObject(testObject).drag();
	//    }

	//public void com.rational.test.ft.object.interfaces.GuiTestObject.hover()
	//    public static void hover(TestObject testObject) {
	//        new SubitemTestObject(testObject).hover();
	//    }



}

//PROPERTIES

/*
   {fontSet=true
   location=java.awt.Point[x=31,y=0]
   minorTickSpacing=17
   optimizedDrawingEnabled=true
   visible=true
   minimumSize=java.awt.Dimension[width=36,height=47]
   focusTraversalKeysEnabled=true
   displayable=true
   ignoreRepaint=false
   focusTraversalPolicyProvider=false
   requestFocusEnabled=true
   opaque=true
   enabled=true
   focusOwner=false
   y=0
   autoscrolls=false
   x=31
   paintTrack=true
   inverted=false
   foregroundSet=true
   size=java.awt.Dimension[width=200,height=47]
   focus=false
   minimum=0
   managingFocus=false
   value=192
   paintTicks=true
   maximum=255
   minimumSizeSet=false
   paintingForPrint=false
   snapToTicks=false
   maximumSizeSet=false
   majorTickSpacing=85
   alignmentY=0.5
   orientation=0
   alignmentX=0.5
   locationOnScreen=java.awt.Point[x=531,y=207]
   preferredSizeSet=false
   visibleRect=java.awt.Rectangle[x=0,y=0,width=200,height=47]
   bounds=java.awt.Rectangle[x=31,y=0,width=200,height=47]
   paintLabels=true
   class=javax.swing.JSlider
   font=com.rational.test.ft.value.FontInfo[name=Tahoma,style=0,size=11]
   focusTraversable=true
   inheritsPopupMenu=true
   uIClassID=SliderUI
   lightweight=true
   maximumSize=java.awt.Dimension[width=32767,height=47]
   paintingTile=false
   valueIsAdjusting=false
   verifyInputWhenFocusTarget=true
   foreground=java.awt.Color[r=236,g=233,b=216]
   preferredSize=java.awt.Dimension[width=200,height=47]
   debugGraphicsOptions=0
   doubleBuffered=false
   showing=true
   extent=0
   componentCount=0
   focusTraversalPolicySet=false
   cursorSet=false
   height=47
   valid=true
   focusCycleRoot=false
   width=200
   backgroundSet=true
   toolTipText=null Object
   focusable=true
   background=java.awt.Color[r=236,g=233,b=216]
   validateRoot=false
   mousePosition=null Object
   name=null Object}
 */

package autoblacktest.actions.simple;

import java.io.IOException;

import resources.TesterHelper;

import com.rational.test.ft.object.interfaces.GuiTestObject;
import com.rational.test.ft.object.interfaces.TestObject;

public class HyperlinkUI extends TesterHelper {

	//public void com.rational.test.ft.object.interfaces.GuiTestObject.click()
	public static void click(TestObject testObject) throws IOException {


		new GuiTestObject(testObject).click();    
	}

	//public void com.rational.test.ft.object.interfaces.GuiTestObject.doubleClick()
	public static void doubleClick(TestObject testObject) throws IOException {


		new GuiTestObject(testObject).doubleClick();
	}

//public void com.rational.test.ft.object.interfaces.GuiTestObject.drag()
//    public static void drag(TestObject testObject, BufferedWriter responderGeneratorWriter) throws IOException {
//		responderGeneratorWriter.write("testObject = to.find(atDescendant(\".class\", " + "\"" + testObject.getProperty(".class") + "\"" + ", " + "\"location\", " + "\"" + testObject.getProperty("location") + "\"));");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.write("new GuiTestObject(testObject[0]).drag();");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.write("sleep(1000);");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.flush();
//  	
//      new GuiTestObject(testObject).drag();
//    }

//public void com.rational.test.ft.object.interfaces.GuiTestObject.hover()
//    public static void hover(TestObject testObject, BufferedWriter responderGeneratorWriter) throws IOException {
//		responderGeneratorWriter.write("testObject = to.find(atDescendant(\".class\", " + "\"" + testObject.getProperty(".class") + "\"" + ", " + "\"location\", " + "\"" + testObject.getProperty("location") + "\"));");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.write("new GuiTestObject(testObject[0]).hover();");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.write("sleep(1000);");
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.newLine();
//		responderGeneratorWriter.flush();
//   	
//      new GuiTestObject(testObject).hover();
//    }

}

/*
	alignmentY=0.5 CHANGED WITH {alignmentY=0.5

	toolTipText= CHANGED WITH toolTipText=

	alignmentX=0.0 CHANGED WITH alignmentX=0.0

	iconTextGap=4 CHANGED WITH iconTextGap=4

	locationOnScreen=java.awt.Point[x=267,y=174] CHANGED WITH locationOnScreen=java.awt.Point[x=267,y=174]

	multiClickThreshhold=0 CHANGED WITH multiClickThreshhold=0

	preferredSizeSet=false CHANGED WITH preferredSizeSet=false

	preferredSize=java.awt.Dimension[width=1,height=0] CHANGED WITH preferredSize=java.awt.Dimension[width=1,height=0]

	clickedColor=java.awt.Color[r=153,g=0,b=153] CHANGED WITH clickedColor=java.awt.Color[r=153,g=0,b=153]

	autoscrolls=false CHANGED WITH autoscrolls=false

	focusable=true CHANGED WITH focusable=true

	contentAreaFilled=true CHANGED WITH contentAreaFilled=true

	focusOwner=false CHANGED WITH focusOwner=false

	debugGraphicsOptions=0 CHANGED WITH debugGraphicsOptions=0

	displayedMnemonicIndex=-1 CHANGED WITH displayedMnemonicIndex=-1

	background=java.awt.Color[r=240,g=240,b=240] CHANGED WITH background=java.awt.Color[r=240,g=240,b=240]

	borderPainted=false CHANGED WITH borderPainted=false

	minimumSize=java.awt.Dimension[width=1,height=0] CHANGED WITH minimumSize=java.awt.Dimension[width=1,height=0]

	lightweight=true CHANGED WITH lightweight=true

	fontSet=true CHANGED WITH fontSet=true

	overrulesActionOnClick=false CHANGED WITH overrulesActionOnClick=false

	optimizedDrawingEnabled=true CHANGED WITH optimizedDrawingEnabled=true

	y=0 CHANGED WITH y=0

	x=0 CHANGED WITH x=0

	unclickedColor=java.awt.Color[r=0,g=51,b=255] CHANGED WITH unclickedColor=java.awt.Color[r=0,g=51,b=255]

	focusPainted=true CHANGED WITH focusPainted=true

	name=null Object CHANGED WITH name=null Object

	maximumSize=java.awt.Dimension[width=1,height=0] CHANGED WITH maximumSize=java.awt.Dimension[width=1,height=0]

	requestFocusEnabled=true CHANGED WITH requestFocusEnabled=true

	focusCycleRoot=false CHANGED WITH focusCycleRoot=false

	verticalAlignment=0 CHANGED WITH verticalAlignment=0

	inheritsPopupMenu=false CHANGED WITH inheritsPopupMenu=false

	verifyInputWhenFocusTarget=true CHANGED WITH verifyInputWhenFocusTarget=true

	backgroundSet=true CHANGED WITH backgroundSet=true

	horizontalAlignment=10 CHANGED WITH horizontalAlignment=10

	defaultCapable=true CHANGED WITH defaultCapable=true

	displayable=true CHANGED WITH displayable=true

	paintingTile=false CHANGED WITH paintingTile=false

	cursorSet=false CHANGED WITH cursorSet=false

	ignoreRepaint=false CHANGED WITH ignoreRepaint=false

	showing=true CHANGED WITH showing=true

	label= CHANGED WITH label=

	location=java.awt.Point[x=0,y=0] CHANGED WITH location=java.awt.Point[x=0,y=0]

	horizontalTextPosition=11 CHANGED WITH horizontalTextPosition=11

	mousePosition=null Object CHANGED WITH mousePosition=null Object

	managingFocus=false CHANGED WITH managingFocus=false

	hideActionText=false CHANGED WITH hideActionText=false

	valid=true CHANGED WITH valid=true

	uIClassID=HyperlinkUI CHANGED WITH uIClassID=HyperlinkUI

	focusTraversable=true CHANGED WITH focusTraversable=true

	visible=true CHANGED WITH visible=true

	maximumSizeSet=false CHANGED WITH maximumSizeSet=false

	componentCount=0 CHANGED WITH componentCount=0

	foregroundSet=true CHANGED WITH foregroundSet=true

	mnemonic=0 CHANGED WITH mnemonic=0

	visibleRect=java.awt.Rectangle[x=0,y=0,width=1,height=0] CHANGED WITH visibleRect=java.awt.Rectangle[x=0,y=0,width=1,height=0]

	minimumSizeSet=false CHANGED WITH minimumSizeSet=false

	width=1 CHANGED WITH width=1

	doubleBuffered=false CHANGED WITH doubleBuffered=false

	font=com.rational.test.ft.value.FontInfo[name=Tahoma,style=0,size=11] CHANGED WITH font=com.rational.test.ft.value.FontInfo[name=Tahoma,style=0,size=11]

	class=org.jdesktop.swingx.JXHyperlink CHANGED WITH class=org.jdesktop.swingx.JXHyperlink

	focusTraversalPolicyProvider=false CHANGED WITH focusTraversalPolicyProvider=false

	opaque=false CHANGED WITH opaque=false

	enabled=true CHANGED WITH enabled=true

	actionCommand= CHANGED WITH actionCommand=

	clicked=false CHANGED WITH clicked=false

	verticalTextPosition=0 CHANGED WITH verticalTextPosition=0

	focusTraversalPolicySet=false CHANGED WITH focusTraversalPolicySet=false

	focus=false CHANGED WITH focus=false

	rolloverEnabled=true CHANGED WITH rolloverEnabled=true

	size=java.awt.Dimension[width=1,height=0] CHANGED WITH size=java.awt.Dimension[width=1,height=0]

	bounds=java.awt.Rectangle[x=0,y=0,width=1,height=0] CHANGED WITH bounds=java.awt.Rectangle[x=0,y=0,width=1,height=0]

	paintingForPrint=false CHANGED WITH paintingForPrint=false

	focusTraversalKeysEnabled=true CHANGED WITH focusTraversalKeysEnabled=true

	selected=false CHANGED WITH selected=false

	validateRoot=false CHANGED WITH validateRoot=false

	defaultButton=false CHANGED WITH defaultButton=false

	foreground=java.awt.Color[r=0,g=51,b=255] CHANGED WITH foreground=java.awt.Color[r=0,g=51,b=255]

	text= CHANGED WITH text=

	height=0 CHANGED WITH height=0
*/



package autoblacktest.actions.simple;

import java.io.IOException;

import resources.TesterHelper;

import autoblacktest.actions.parameters.ParameterNumberFormat;
import autoblacktest.actions.parameters.text.DataManager;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.TextGuiSubitemTestObject;


public class FormattedTextFieldUI extends TesterHelper {

	public static void setText(TestObject testObject, long index) throws IOException {

		//recupero gli indici della hashmap dei dati
		long[] parameters=ParameterNumberFormat.getDoubleParameter(index);
		//recupero il valore da insierire dalla hashmap dei dati 
		String text=DataManager.getInstance().getData((int)parameters[0],(int)parameters[1]);
		new TextGuiSubitemTestObject(testObject).setProperty("text", text);

	}
	
	
	/*
	 * anche i JSpinner utilizzano al loro interno un FormattedTextField che
	 * dovrebbero essere protetti da scritture erronee
	 */
	//old version
	//public static void setText(TestObject testObject) throws IOException {
	//	String text = testObject.getProperty("text").toString();
	//	if (text.matches(".\\d*.\\d*")) {
	//		text = text.replace(',', '.');
	//		try {
	//			Double.parseDouble(text);
	//			text = 100.0 + "";
	//		} catch (NumberFormatException e) {
	//			text = "generic string";
	//		}
	//	} else {
	//		text = "generic string";
	//	}
	//
	//	new TextGuiSubitemTestObject(testObject).setText(text);
	//}

}

/*
 * foregroundSet=true CHANGED WITH {foregroundSet=true
 * 
 * height=20 CHANGED WITH height=20
 * 
 * preferredSizeSet=false CHANGED WITH preferredSizeSet=false
 * 
 * managingFocus=false CHANGED WITH managingFocus=false
 * 
 * name=null Object CHANGED WITH name=null Object
 * 
 * autoscrolls=true CHANGED WITH autoscrolls=true
 * 
 * alignmentY=0.5 CHANGED WITH alignmentY=0.5
 * 
 * alignmentX=0.5 CHANGED WITH alignmentX=0.5
 * 
 * mousePosition=null Object CHANGED WITH mousePosition=null Object
 * 
 * focusTraversalPolicySet=false CHANGED WITH focusTraversalPolicySet=false
 * 
 * toolTipText=null Object CHANGED WITH toolTipText=null Object
 * 
 * backgroundSet=true CHANGED WITH backgroundSet=true
 * 
 * visible=true CHANGED WITH visible=true
 * 
 * lightweight=true CHANGED WITH lightweight=true
 * 
 * visibleRect=java.awt.Rectangle[x=0,y=0,width=38,height=20] CHANGED WITH
 * visibleRect=java.awt.Rectangle[x=0,y=0,width=38,height=20]
 * 
 * y=44 CHANGED WITH y=44
 * 
 * scrollOffset=0 CHANGED WITH scrollOffset=0
 * 
 * x=228 CHANGED WITH x=228
 * 
 * debugGraphicsOptions=0 CHANGED WITH debugGraphicsOptions=0
 * 
 * horizontalAlignment=10 CHANGED WITH horizontalAlignment=10
 * 
 * selectionColor=java.awt.Color[r=51,g=153,b=255] CHANGED WITH
 * selectionColor=java.awt.Color[r=51,g=153,b=255]
 * 
 * editValid=true CHANGED WITH editValid=true
 * 
 * bounds=java.awt.Rectangle[x=228,y=44,width=38,height=20] CHANGED WITH
 * bounds=java.awt.Rectangle[x=228,y=44,width=38,height=20]
 * 
 * selectedText=null Object CHANGED WITH selectedText=null Object
 * 
 * width=38 CHANGED WITH width=38
 * 
 * location=java.awt.Point[x=228,y=44] CHANGED WITH
 * location=java.awt.Point[x=228,y=44]
 * 
 * focusable=true CHANGED WITH focusable=true
 * 
 * doubleBuffered=false CHANGED WITH doubleBuffered=false
 * 
 * paintingForPrint=false CHANGED WITH paintingForPrint=false
 * 
 * columns=4 CHANGED WITH columns=4
 * 
 * optimizedDrawingEnabled=true CHANGED WITH optimizedDrawingEnabled=true
 * 
 * paintingTile=false CHANGED WITH paintingTile=false
 * 
 * foreground=java.awt.Color[r=0,g=0,b=0] CHANGED WITH
 * foreground=java.awt.Color[r=0,g=0,b=0]
 * 
 * focus=false CHANGED WITH focus=false
 * 
 * class=de.shandschuh.jaolt.gui.core.PriceJTextField CHANGED WITH
 * class=de.shandschuh.jaolt.gui.core.PriceJTextField
 * 
 * focusCycleRoot=false CHANGED WITH focusCycleRoot=false
 * 
 * scrollableTracksViewportHeight=false CHANGED WITH
 * scrollableTracksViewportHeight=false
 * 
 * enabled=true CHANGED WITH enabled=true
 * 
 * opaque=true CHANGED WITH opaque=true
 * 
 * componentCount=0 CHANGED WITH componentCount=0
 * 
 * displayable=true CHANGED WITH displayable=true
 * 
 * scrollableTracksViewportWidth=false CHANGED WITH
 * scrollableTracksViewportWidth=false
 * 
 * validateRoot=true CHANGED WITH validateRoot=true
 * 
 * selectionEnd=0 CHANGED WITH selectionEnd=0
 * 
 * inheritsPopupMenu=false CHANGED WITH inheritsPopupMenu=false
 * 
 * dragEnabled=false CHANGED WITH dragEnabled=false
 * 
 * selectedTextColor=java.awt.Color[r=255,g=255,b=255] CHANGED WITH
 * selectedTextColor=java.awt.Color[r=255,g=255,b=255]
 * 
 * font=com.rational.test.ft.value.FontInfo[name=Tahoma,style=0,size=11] CHANGED
 * WITH font=com.rational.test.ft.value.FontInfo[name=Tahoma,style=0,size=11]
 * 
 * minimumSize=java.awt.Dimension[width=6,height=20] CHANGED WITH
 * minimumSize=java.awt.Dimension[width=6,height=20]
 * 
 * preferredScrollableViewportSize=java.awt.Dimension[width=38,height=20]
 * CHANGED WITH
 * preferredScrollableViewportSize=java.awt.Dimension[width=38,height=20]
 * 
 * preferredSize=java.awt.Dimension[width=38,height=20] CHANGED WITH
 * preferredSize=java.awt.Dimension[width=38,height=20]
 * 
 * focusTraversalPolicyProvider=false CHANGED WITH
 * focusTraversalPolicyProvider=false
 * 
 * focusTraversable=true CHANGED WITH focusTraversable=true
 * 
 * valid=true CHANGED WITH valid=true
 * 
 * showing=true CHANGED WITH showing=true
 * 
 * disabledTextColor=java.awt.Color[r=128,g=128,b=128] CHANGED WITH
 * disabledTextColor=java.awt.Color[r=128,g=128,b=128]
 * 
 * uIClassID=FormattedTextFieldUI CHANGED WITH uIClassID=FormattedTextFieldUI
 * 
 * focusTraversalKeysEnabled=true CHANGED WITH focusTraversalKeysEnabled=true
 * 
 * verifyInputWhenFocusTarget=true CHANGED WITH verifyInputWhenFocusTarget=true
 * 
 * editable=true CHANGED WITH editable=true
 * 
 * background=java.awt.Color[r=255,g=255,b=255] CHANGED WITH
 * background=java.awt.Color[r=255,g=255,b=255]
 * 
 * fontSet=true CHANGED WITH fontSet=true
 * 
 * maximumSize=java.awt.Dimension[width=2147483647,height=2147483647] CHANGED
 * WITH maximumSize=java.awt.Dimension[width=2147483647,height=2147483647]
 * 
 * locationOnScreen=java.awt.Point[x=387,y=599] CHANGED WITH
 * locationOnScreen=java.awt.Point[x=387,y=599]
 * 
 * caretColor=java.awt.Color[r=0,g=0,b=0] CHANGED WITH
 * caretColor=java.awt.Color[r=0,g=0,b=0]
 * 
 * selectionStart=0 CHANGED WITH selectionStart=0
 * 
 * text=10.00 CHANGED WITH text=10.00
 * 
 * ignoreRepaint=false CHANGED WITH ignoreRepaint=false
 * 
 * focusAccelerator= CHANGED WITH focusAccelerator=
 * 
 * focusLostBehavior=1 CHANGED WITH focusLostBehavior=1
 * 
 * requestFocusEnabled=true CHANGED WITH requestFocusEnabled=true
 * 
 * minimumSizeSet=false CHANGED WITH minimumSizeSet=false
 * 
 * caretPosition=0 CHANGED WITH caretPosition=0
 * 
 * size=java.awt.Dimension[width=38,height=20] CHANGED WITH
 * size=java.awt.Dimension[width=38,height=20]
 * 
 * cursorSet=true CHANGED WITH cursorSet=true
 * 
 * focusOwner=false CHANGED WITH focusOwner=false
 * 
 * maximumSizeSet=false CHANGED WITH maximumSizeSet=false
 */
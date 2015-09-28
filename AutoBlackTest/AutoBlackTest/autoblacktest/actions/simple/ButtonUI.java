package autoblacktest.actions.simple;

import java.io.IOException;

import resources.TesterHelper;

import com.rational.test.ft.WindowActivateFailedException;
import com.rational.test.ft.object.interfaces.GuiTestObject;
import com.rational.test.ft.object.interfaces.TestObject;

public class ButtonUI extends TesterHelper {

	public static void click(TestObject testObject) throws IOException {

		try {
			new GuiTestObject(testObject).click();

		} catch (WindowActivateFailedException e) {
			throw new WindowActivateFailedException();
		} 

	}

	/*public static void drag(TestObject testObject) throws IOException {

		try {
			new GuiTestObject(testObject).click();
		} catch (WindowActivateFailedException e) {
			throw new WindowActivateFailedException();
		}

	}*/
}

/*
 * label=Salvare CHANGED WITH {label=Salvare
 * 
 * validateRoot=false CHANGED WITH validateRoot=false
 * 
 * verticalAlignment=0 CHANGED WITH verticalAlignment=0
 * 
 * font=com.rational.test.ft.value.FontInfo[name=Tahoma,style=0,size=11] CHANGED
 * WITH font=com.rational.test.ft.value.FontInfo[name=Tahoma,style=0,size=11]
 * 
 * actionCommand=Salvare CHANGED WITH actionCommand=Salvare
 * 
 * size=java.awt.Dimension[width=75,height=23] CHANGED WITH
 * size=java.awt.Dimension[width=75,height=23]
 * 
 * location=java.awt.Point[x=823,y=6] CHANGED WITH
 * location=java.awt.Point[x=823,y=6]
 * 
 * focusTraversalKeysEnabled=true CHANGED WITH focusTraversalKeysEnabled=true
 * 
 * alignmentY=0.5 CHANGED WITH alignmentY=0.5
 * 
 * alignmentX=0.0 CHANGED WITH alignmentX=0.0
 * 
 * y=6 CHANGED WITH y=6
 * 
 * x=823 CHANGED WITH x=823
 * 
 * iconTextGap=4 CHANGED WITH iconTextGap=4
 * 
 * preferredSize=java.awt.Dimension[width=69,height=23] CHANGED WITH
 * preferredSize=java.awt.Dimension[width=69,height=23]
 * 
 * backgroundSet=true CHANGED WITH backgroundSet=true
 * 
 * verifyInputWhenFocusTarget=true CHANGED WITH verifyInputWhenFocusTarget=true
 * 
 * displayedMnemonicIndex=-1 CHANGED WITH displayedMnemonicIndex=-1
 * 
 * displayable=true CHANGED WITH displayable=true
 * 
 * class=de.shandschuh.jaolt.gui.core.buttons.SaveJButton CHANGED WITH
 * class=de.shandschuh.jaolt.gui.core.buttons.SaveJButton
 * 
 * autoscrolls=false CHANGED WITH autoscrolls=false
 * 
 * foregroundSet=true CHANGED WITH foregroundSet=true
 * 
 * defaultCapable=true CHANGED WITH defaultCapable=true
 * 
 * horizontalAlignment=0 CHANGED WITH horizontalAlignment=0
 * 
 * showing=true CHANGED WITH showing=true
 * 
 * focusTraversalPolicyProvider=false CHANGED WITH
 * focusTraversalPolicyProvider=false
 * 
 * fontSet=true CHANGED WITH fontSet=true
 * 
 * preferredSizeSet=false CHANGED WITH preferredSizeSet=false
 * 
 * focusTraversable=true CHANGED WITH focusTraversable=true
 * 
 * visible=true CHANGED WITH visible=true
 * 
 * rolloverEnabled=true CHANGED WITH rolloverEnabled=true
 * 
 * background=java.awt.Color[r=240,g=240,b=240] CHANGED WITH
 * background=java.awt.Color[r=240,g=240,b=240]
 * 
 * defaultButton=false CHANGED WITH defaultButton=false
 * 
 * uIClassID=ButtonUI CHANGED WITH uIClassID=ButtonUI
 * 
 * inheritsPopupMenu=false CHANGED WITH inheritsPopupMenu=false
 * 
 * multiClickThreshhold=0 CHANGED WITH multiClickThreshhold=0
 * 
 * valid=true CHANGED WITH valid=true
 * 
 * mnemonic=0 CHANGED WITH mnemonic=0
 * 
 * locationOnScreen=java.awt.Point[x=971,y=697] CHANGED WITH
 * locationOnScreen=java.awt.Point[x=971,y=697]
 * 
 * name=null Object CHANGED WITH name=null Object
 * 
 * hideActionText=false CHANGED WITH hideActionText=false
 * 
 * foreground=java.awt.Color[r=0,g=0,b=0] CHANGED WITH
 * foreground=java.awt.Color[r=0,g=0,b=0]
 * 
 * mousePosition=null Object CHANGED WITH mousePosition=null Object
 * 
 * ignoreRepaint=false CHANGED WITH ignoreRepaint=false
 * 
 * borderPainted=true CHANGED WITH borderPainted=true
 * 
 * paintingTile=false CHANGED WITH paintingTile=false
 * 
 * focusTraversalPolicySet=false CHANGED WITH focusTraversalPolicySet=false
 * 
 * focusCycleRoot=false CHANGED WITH focusCycleRoot=false
 * 
 * focusPainted=true CHANGED WITH focusPainted=true
 * 
 * focusOwner=false CHANGED WITH focusOwner=false
 * 
 * enabled=true CHANGED WITH enabled=true
 * 
 * lightweight=true CHANGED WITH lightweight=true
 * 
 * opaque=true CHANGED WITH opaque=true
 * 
 * maximumSizeSet=false CHANGED WITH maximumSizeSet=false
 * 
 * componentCount=0 CHANGED WITH componentCount=0
 * 
 * toolTipText=null Object CHANGED WITH toolTipText=null Object
 * 
 * height=23 CHANGED WITH height=23
 * 
 * doubleBuffered=false CHANGED WITH doubleBuffered=false
 * 
 * minimumSizeSet=false CHANGED WITH minimumSizeSet=false
 * 
 * width=75 CHANGED WITH width=75
 * 
 * horizontalTextPosition=11 CHANGED WITH horizontalTextPosition=11
 * 
 * paintingForPrint=false CHANGED WITH paintingForPrint=false
 * 
 * maximumSize=java.awt.Dimension[width=69,height=23] CHANGED WITH
 * maximumSize=java.awt.Dimension[width=69,height=23]
 * 
 * managingFocus=false CHANGED WITH managingFocus=false
 * 
 * minimumSize=java.awt.Dimension[width=69,height=23] CHANGED WITH
 * minimumSize=java.awt.Dimension[width=69,height=23]
 * 
 * verticalTextPosition=0 CHANGED WITH verticalTextPosition=0
 * 
 * visibleRect=java.awt.Rectangle[x=0,y=0,width=75,height=23] CHANGED WITH
 * visibleRect=java.awt.Rectangle[x=0,y=0,width=75,height=23]
 * 
 * text=Salvare CHANGED WITH text=Salvare
 * 
 * selected=false CHANGED WITH selected=false
 * 
 * contentAreaFilled=true CHANGED WITH contentAreaFilled=true
 * 
 * optimizedDrawingEnabled=true CHANGED WITH optimizedDrawingEnabled=true
 * 
 * debugGraphicsOptions=0 CHANGED WITH debugGraphicsOptions=0
 * 
 * requestFocusEnabled=true CHANGED WITH requestFocusEnabled=true
 * 
 * focus=false CHANGED WITH focus=false
 * 
 * focusable=true CHANGED WITH focusable=true
 * 
 * bounds=java.awt.Rectangle[x=823,y=6,width=75,height=23] CHANGED WITH
 * bounds=java.awt.Rectangle[x=823,y=6,width=75,height=23]
 * 
 * cursorSet=false CHANGED WITH cursorSet=false
 */
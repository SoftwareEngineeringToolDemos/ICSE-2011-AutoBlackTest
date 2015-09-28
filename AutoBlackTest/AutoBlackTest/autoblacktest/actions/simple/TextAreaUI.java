package autoblacktest.actions.simple;

import java.io.IOException;

import resources.TesterHelper;

import autoblacktest.actions.parameters.ParameterNumberFormat;
import autoblacktest.actions.parameters.text.DataManager;

import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.object.interfaces.TextGuiSubitemTestObject;


public class TextAreaUI extends TesterHelper {
	public static void setText(TestObject testObject, long index) throws IOException {
		//recupero gli indici della hashmap dei dati
		long[] parameters=ParameterNumberFormat.getDoubleParameter(index);
		//recupero il valore da insierire dalla hashmap dei dati 
		String text=DataManager.getInstance().getData((int)parameters[0],(int)parameters[1]);
		new TextGuiSubitemTestObject(testObject).setProperty("text", text);
	}
}

/*
	foreground=java.awt.Color[r=0,g=0,b=0] CHANGED WITH foreground=java.awt.Color[r=0,g=0,b=0]
	
	mousePosition=null Object CHANGED WITH mousePosition=null Object
	
	background=java.awt.Color[r=255,g=255,b=255] CHANGED WITH background=java.awt.Color[r=255,g=255,b=255]
	
	text= CHANGED WITH text=
	
	maximumSize=java.awt.Dimension[width=2147483647,height=2147483647] CHANGED WITH maximumSize=java.awt.Dimension[width=2147483647,height=2147483647]
	
	optimizedDrawingEnabled=true CHANGED WITH optimizedDrawingEnabled=true
	
	minimumSizeSet=false CHANGED WITH minimumSizeSet=false
	
	valid=true CHANGED WITH valid=true
	
	autoscrolls=true CHANGED WITH autoscrolls=true
	
	componentCount=0 CHANGED WITH componentCount=0
	
	showing=true CHANGED WITH showing=true
	
	bounds=java.awt.Rectangle[x=0,y=0,width=406,height=112] CHANGED WITH bounds=java.awt.Rectangle[x=0,y=0,width=406,height=112]
	
	managingFocus=false CHANGED WITH managingFocus=false
	
	lightweight=true CHANGED WITH lightweight=true
	
	focusOwner=false CHANGED WITH focusOwner=false
	
	alignmentY=0.5 CHANGED WITH alignmentY=0.5
	
	alignmentX=0.5 CHANGED WITH alignmentX=0.5
	
	class=javax.swing.JTextArea CHANGED WITH class=javax.swing.JTextArea
	
	verifyInputWhenFocusTarget=true CHANGED WITH verifyInputWhenFocusTarget=true
	
	width=406 CHANGED WITH width=406
	
	preferredSize=java.awt.Dimension[width=12,height=112] CHANGED WITH preferredSize=java.awt.Dimension[width=12,height=112]
	
	minimumSize=java.awt.Dimension[width=4,height=22] CHANGED WITH minimumSize=java.awt.Dimension[width=4,height=22]
	
	scrollableTracksViewportWidth=true CHANGED WITH scrollableTracksViewportWidth=true
	
	y=0 CHANGED WITH y=0
	
	x=0 CHANGED WITH x=0
	
	selectedText=null Object CHANGED WITH selectedText=null Object
	
	lineWrap=false CHANGED WITH lineWrap=false
	
	scrollableTracksViewportHeight=false CHANGED WITH scrollableTracksViewportHeight=false
	
	opaque=true CHANGED WITH opaque=true
	
	lineStartOffset=[Ljava.lang.Object;@148c148c CHANGED WITH lineStartOffset=[Ljava.lang.Object;@5c025c02
	
	inheritsPopupMenu=false CHANGED WITH inheritsPopupMenu=false
	
	displayable=true CHANGED WITH displayable=true
	
	focusable=true CHANGED WITH focusable=true
	
	focus=false CHANGED WITH focus=false
	
	name=null Object CHANGED WITH name=null Object
	
	fontSet=true CHANGED WITH fontSet=true
	
	focusTraversable=true CHANGED WITH focusTraversable=true
	
	columns=1 CHANGED WITH columns=1
	
	lineCount=1 CHANGED WITH lineCount=1
	
	paintingForPrint=false CHANGED WITH paintingForPrint=false
	
	wrapStyleWord=false CHANGED WITH wrapStyleWord=false
	
	visible=true CHANGED WITH visible=true
	
	locationOnScreen=java.awt.Point[x=165,y=911] CHANGED WITH locationOnScreen=java.awt.Point[x=165,y=911]
	
	toolTipText=null Object CHANGED WITH toolTipText=null Object
	
	selectedTextColor=java.awt.Color[r=255,g=255,b=255] CHANGED WITH selectedTextColor=java.awt.Color[r=255,g=255,b=255]
	
	preferredSizeSet=false CHANGED WITH preferredSizeSet=false
	
	focusTraversalKeysEnabled=true CHANGED WITH focusTraversalKeysEnabled=true
	
	editable=true CHANGED WITH editable=true
	
	debugGraphicsOptions=0 CHANGED WITH debugGraphicsOptions=0
	
	location=java.awt.Point[x=0,y=0] CHANGED WITH location=java.awt.Point[x=0,y=0]
	
	selectionColor=java.awt.Color[r=51,g=153,b=255] CHANGED WITH selectionColor=java.awt.Color[r=51,g=153,b=255]
	
	preferredScrollableViewportSize=java.awt.Dimension[width=12,height=112] CHANGED WITH preferredScrollableViewportSize=java.awt.Dimension[width=12,height=112]
	
	disabledTextColor=java.awt.Color[r=128,g=128,b=128] CHANGED WITH disabledTextColor=java.awt.Color[r=128,g=128,b=128]
	
	ignoreRepaint=false CHANGED WITH ignoreRepaint=false
	
	selectionEnd=0 CHANGED WITH selectionEnd=0
	
	focusCycleRoot=false CHANGED WITH focusCycleRoot=false
	
	focusTraversalPolicyProvider=false CHANGED WITH focusTraversalPolicyProvider=false
	
	tabSize=8 CHANGED WITH tabSize=8
	
	doubleBuffered=false CHANGED WITH doubleBuffered=false
	
	cursorSet=true CHANGED WITH cursorSet=true
	
	font=com.rational.test.ft.value.FontInfo[name=Monospaced,style=0,size=13] CHANGED WITH font=com.rational.test.ft.value.FontInfo[name=Monospaced,style=0,size=13]
	
	requestFocusEnabled=true CHANGED WITH requestFocusEnabled=true
	
	caretPosition=0 CHANGED WITH caretPosition=0
	
	foregroundSet=true CHANGED WITH foregroundSet=true
	
	height=112 CHANGED WITH height=112
	
	dragEnabled=false CHANGED WITH dragEnabled=false
	
	enabled=true CHANGED WITH enabled=true
	
	rows=6 CHANGED WITH rows=6
	
	focusTraversalPolicySet=false CHANGED WITH focusTraversalPolicySet=false
	
	focusAccelerator=  CHANGED WITH focusAccelerator= 
	
	maximumSizeSet=false CHANGED WITH maximumSizeSet=false
	
	visibleRect=java.awt.Rectangle[x=0,y=0,width=0,height=0] CHANGED WITH visibleRect=java.awt.Rectangle[x=0,y=0,width=0,height=0]
	
	backgroundSet=true CHANGED WITH backgroundSet=true
	
	validateRoot=false CHANGED WITH validateRoot=false
	
	lineEndOffset=[Ljava.lang.Object;@15721572 CHANGED WITH lineEndOffset=[Ljava.lang.Object;@5e235e23
	
	paintingTile=false CHANGED WITH paintingTile=false
	
	size=java.awt.Dimension[width=406,height=112] CHANGED WITH size=java.awt.Dimension[width=406,height=112]
	
	selectionStart=0 CHANGED WITH selectionStart=0
	
	uIClassID=TextAreaUI CHANGED WITH uIClassID=TextAreaUI
	
	lineOfOffset=[Ljava.lang.Object;@15d015d0 CHANGED WITH lineOfOffset=[Ljava.lang.Object;@5f4b5f4b
	
	caretColor=java.awt.Color[r=0,g=0,b=0] CHANGED WITH caretColor=java.awt.Color[r=0,g=0,b=0]
*/
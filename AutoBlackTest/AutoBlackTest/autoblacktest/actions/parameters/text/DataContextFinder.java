package autoblacktest.actions.parameters.text;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hswgt.teachingbox.core.rl.env.State;

import resources.TesterHelper;

import autoblacktest.TestState;

import com.google.common.collect.Lists;
import com.rational.test.ft.script.*;
import com.rational.test.ft.object.interfaces.TestObject;
import com.rational.test.ft.script.Property;
import com.rational.test.ft.vp.ITestDataElement;
import com.rational.test.ft.vp.ITestDataElementList;
import com.rational.test.ft.vp.ITestDataList;
import com.rational.test.ft.vp.ITestDataText;

//import teachingbox.TestEnvironment;


/**
 * Classe utilizzata per la ricerca di informazioni contestuali 
 * che è possibile associare ai test object.
 * @author Giovanni Becce
 *
 */
public class DataContextFinder {
	
	private Logger logger = Logger.getLogger(DataContextFinder.class);
	
	/**
	 * Strategia di analisi globale, non tiene conto della struttura.
	 * Per ogni widget considerato effettua l'analisi tra tutti i descrittori selezionati.
	 */
	public static int GLOBAL_ANALYSIS=0;
	/**
	 * Strategia di analisi locale, nell'analisi tiene in considerazione la struttura
	 * dell'interfaccia esaminando localmente (stesso elemento padre) widget e descrittori. 
	 */
	public static int LOCAL_ANALYSIS=1;
	
	private static DataContextFinder instance = null;
	
	private HashMap<TestObject,String[]> dataMap = new HashMap<TestObject,String[]>();
	TestState testState;
	private Property[] properties = new Property[3];

	//widget analizzati e relativi descrittori
	private List<String> allWidgetDescriptor = Lists.newArrayList("LabelUI","CheckBoxUI","RadioButtonUI");
	//private List<String> allWidgetAnalyzed = Lists.newArrayList("TextFieldUI","FormattedTextFieldUI", "PasswordFieldUI", "TextAreaUI", "EditorPaneUI" , "TextPaneUI",
	//															"ButtonUI", "CheckBoxUI" , "RadioButtonUI", "ComboBoxUI","listUI",
	//															"Menu", "MunuItem", "CheckBoxMenuItem",
	//															"PanelUI", "ScrollPaneUI", "TabbedPaneUI" ,"SplitPaneUI");

	private List<String> textWidget = Lists.newArrayList("TextFieldUI","FormattedTextFieldUI", "PasswordFieldUI", "TextAreaUI", "EditorPaneUI" , "TextPaneUI");
	private List<String> baseTextWidgetDescriptor = Lists.newArrayList("LabelUI");
	private List<String> optionalTextWidgetDescriptor = Lists.newArrayList("CheckBoxUI","RadioButtonUI");
	
	private List<String> interactiveWidget = Lists.newArrayList("CheckBoxUI" , "RadioButtonUI", "ComboBoxUI", "ToggleButtonUI" ,"ListUI" );
	private List<String> optionalInteractiveWidgetDescriptor = Lists.newArrayList("LabelUI");

	private List<String> buttonWidget = Lists.newArrayList("ButtonUI");
	private List<String> optionalButtonDescriptor = Lists.newArrayList("LabelUI");
	
	private List<String> menuWidget = Lists.newArrayList( "MenuUI", "MenuItemUI", "CheckBoxMenuItemUI", "RadioButtonMenuItemUI" );
	
	// per analisi strutturata
	private List<String> panelWidget = Lists.newArrayList("PanelUI", "ScrollPaneUI", "TabbedPaneUI" ,"SplitPaneUI", "SpinnerUI", "DatePickerUI" );
	private List<String> optionalPanelWidgetDescriptor = Lists.newArrayList("LabelUI");
	
	//------------------- UTILIZZO DESCRITTORI OPZIONALI --------------------
	
	private boolean useOptionalTextWidgetDescriptor=true;
	private boolean useOptionalInteractiveWidgetDescriptor=true;
	private boolean useOptionalButtonDescriptor=false;
	// per analisi strutturata
	private boolean useOptionalPanelWidgetDescriptor=true;
	
	//------------------- VARIANTI OPZIONALI -------------------------------
	
	//Cerca informazioni anche in widget a livelli di distanza maggiori
	//(al massimo : maxNextWidgetDistanceLevel) se il widget più vicino non ha informazioni
	// opt_noise=Incremental
	private boolean useNextWidgetDescriptorIfNearestOneHasNoInformation=true;
	private int maxNextWidgetDistanceLevel=4;
	
	//alternativamente possiamo decidere di considerare solo widget descrittori
	//che hanno informazioni
	//opt_noise=Begin
	private boolean useOnlyWidgetDescriptorWithInformation=false;
	
	//Non mappare test object senza informazioni di contesto o proprietà
	private boolean discardTestObjectWithoutAnyInformation=false;
	
	//Effettua la ricerca di informazioni sui widget correlati
	//solamente sugli elementi realmente visualizzati 
	//opt_visible --> all=false , visbleOnly=true;
	private boolean doAnalysisOnlyForDisplayedWidget=false;
	
	//Utilizza differenti metodi di calcolo a seconda del to in esame
	private boolean useDifferentPolicyDistance=false;
	private List<String> defaultPolicy = Lists.newArrayList("TextFieldUI","FormattedTextFieldUI", "PasswordFieldUI",
															"CheckBoxUI","RadioButtonUI","ComboBoxUI","ListUI","ButtonUI",
															"MenuUI", "MunuItemUI", "CheckBoxMenuItemUI",
															"PanelUI", "ScrollPaneUI", "TabbedPaneUI" ,"SplitPaneUI", "SpinnerUI");
	private List<String> centerPolicy = Lists.newArrayList("TextAreaUI", "EditorPaneUI" , "TextPaneUI");
	
	//---------- varianti solo per analisi strutturata------------
	
	// opt_hierarchical= Yes or No
	private boolean useFatherInformationForChildWithoutContextInformation=true;
	

	public static synchronized DataContextFinder getInstance() {
		if (instance == null)
			instance = new DataContextFinder();
		return instance;
	}
	
	/**
	 * Metodo utilizzato per effettuare l'analisi del contesto alla ricerca 
	 * di informazioni associabili ai test object.
	 * @param policy
	 */
	public void doContextAnalysis(int policy){
		
		logger.info("Searching contextual data ");

		properties[0] = new Property("showing", "true");
		properties[1] = new Property("enabled", "true");
		properties[2] = new Property("visible", "true");
		//recupero lo stato corrente
		testState=TestState.getInstance();
		State s = testState.getCurrentState();
		
		//azzero la dataMap
		dataMap.clear();
		
		//recupero la mapDataContext
		//HashMap<Integer, HashMap<TestObject,String[]>> mapDataContext=testState.getMapDataContext();
		//se lo stato corrente non è già stato analizzato
		
		//commentato per il problema della coerenza temporale degli stati
		//if( !(mapDataContext.containsKey((int) s.get(0))) ){
			switch(policy){	
			// global analysis
			case 0: doGlobalAnalysis(testState.getActionableTestObjects(testState.getLastExecutedTestObject())); 
					break;
			// local analysis
			case 1: doLocalAnalysis(testState.getActiveWindowsTestObject());
					break;
			//default = global analysis
			default: doGlobalAnalysis(testState.getActionableTestObjects(testState.getLastExecutedTestObject())); 
					break;
			}
			
			logger.debug("Adding information for "+dataMap.size()+" widget");
			//inserisco i dati calcolati nella mappa
		//	System.out.println("numero stato: "+(int) s.get(0));
		//	mapDataContext.put((int) s.get(0),dataMap);
			//effettuo il set della mappa in TestState
		//	testState.setMapDataContext(mapDataContext);
			
			testState.addStateDataMapContext((int) s.get(0), dataMap);
			//stampa di prova
			//printDataMap();
			//logger.info("Searching contextual done ");
/*		}else{
			logger.info("Old State. Analysis already done");
		//	System.out.println("numero stato: "+(int) s.get(0));
			//stampa di prova
			printDataMap(mapDataContext.get((int) s.get(0)));
		}
*/
	}
	
	/**
	 * Metodo che esegue l'analisi contestuale con la strategia globale
	 * @param tos
	 */
	public void doGlobalAnalysis(TestObject[] tos){
		HashMap<String,List<Integer[]>> widgetDescriptor = new HashMap<String,List<Integer[]>>();
		for(String s : allWidgetDescriptor){
			widgetDescriptor.put(s, getSameWidgetDescriptorPosition(tos,s));
		}
			
			for(int i=0 ; i<tos.length ; i++){
				String uIClassID=tos[i].getProperty("uIClassID").toString();
				boolean knowWidget=false;
				
				boolean isDisplayed=true;
				if(doAnalysisOnlyForDisplayedWidget)
						isDisplayed=isDisplayed(tos[i]);
				
				//inizializzo vettore dei dati di contesto
				String dataContext[]=new String[3];
				dataContext[0]="";
				dataContext[1]="";
				dataContext[2]="";
				
				//inizializzo lista dei widget correlati
				List<Integer[]> relatedWidget= Lists.newArrayList();
				relatedWidget.clear();
				//recupero la posizione del to
				Integer[] toPosition=null;//=getTOPosition(tos[i]);
				
				// Se il to è un widget di testo
				if(textWidget.contains(uIClassID)){
					knowWidget=true;
					//recupero la posizione del to
					toPosition=getTOPosition(tos[i]);
					if( ((doAnalysisOnlyForDisplayedWidget)&&(isDisplayed))||(!doAnalysisOnlyForDisplayedWidget) ){
						for(String s : baseTextWidgetDescriptor){
							relatedWidget = findRelatedWidget(relatedWidget,widgetDescriptor.get(s),toPosition);
						}
						// Se attivati cerco i widget correlati
						// utilizzando i descrittori opzionali
						if(useOptionalTextWidgetDescriptor){
							for(String s : optionalTextWidgetDescriptor){
								relatedWidget = findRelatedWidget(relatedWidget,widgetDescriptor.get(s),toPosition);
							}
						}
					}
				}
				// Se il to è un interactive widget
				if(interactiveWidget.contains(uIClassID)){
					knowWidget=true;
					//recupero la posizione del to
					toPosition=getTOPosition(tos[i]);
					if( ((doAnalysisOnlyForDisplayedWidget)&&(isDisplayed))||(!doAnalysisOnlyForDisplayedWidget) ){
						// Se attivati cerco i widget correlati
						// utilizzando i descrittori opzionali
						if(useOptionalInteractiveWidgetDescriptor){
							for(String s : optionalInteractiveWidgetDescriptor){
									relatedWidget = findRelatedWidget(relatedWidget,widgetDescriptor.get(s),toPosition);
								}	
						}
					}
				}
				// Se il to è un button widget
				if(buttonWidget.contains(uIClassID)){
					knowWidget=true;
					//recupero la posizione del to
					toPosition=getTOPosition(tos[i]);
					if( ((doAnalysisOnlyForDisplayedWidget)&&(isDisplayed))||(!doAnalysisOnlyForDisplayedWidget) ){
						// Se attivati cerco i widget correlati
						// utilizzando i descrittori opzionali
						if(useOptionalButtonDescriptor){
							for(String s : optionalButtonDescriptor){
								relatedWidget = findRelatedWidget(relatedWidget,widgetDescriptor.get(s),toPosition);
							}
						}
					}
				}
				if(menuWidget.contains(uIClassID)){
					knowWidget=true;
				}
				
				if(relatedWidget.size()>0){
					//calcolo la distanza dal to per ogni widget correlato
					if(useDifferentPolicyDistance){
						if(centerPolicy.contains(uIClassID))
							relatedWidget=computeDistance(relatedWidget,toPosition,"centerPolicy");
						else
							relatedWidget=computeDistance(relatedWidget,toPosition,"defaultPolicy");
					}else
						relatedWidget=computeDistance(relatedWidget,toPosition,"defaultPolicy");
						
					//cerco il widget più vicino al to tra quelli correlati
					int toIndex=-1;
					int relatedIndex=findNearest(relatedWidget);
					if(relatedIndex!=-1)
						toIndex=relatedWidget.get(relatedIndex)[0];
					
					//recupero i dati di contesto
												
					if(toIndex!=-1){
						if( (tos[toIndex].getProperty("text")!=null)&&!(tos[toIndex].getProperty("text").toString().trim().equals("")) ){
							dataContext[0]=tos[toIndex].getProperty("text").toString().trim();
						}else{
							//Se il widget più vicino non ha informazioni 
							//ed è attivata la strategia di ricerca di informazioni
							//su widget a diversi livelli di distanza
							if(useNextWidgetDescriptorIfNearestOneHasNoInformation){
								int n=0;
								//mentre non sono al massimo livello di distanza consentito
								while(maxNextWidgetDistanceLevel>n){
									//elimino dai widget correlati il widget più vicino senza informazioni
									relatedWidget.remove(relatedIndex);
									//cerco il nuovo widget più vicino
									relatedIndex=findNearest(relatedWidget);
									//se esiste
									if(relatedIndex!=-1)
										//recupero l'indice del relativo to
										toIndex=relatedWidget.get(relatedIndex)[0];
									else
										break;
									//se il nuovo widget possiede informazioni
									if( (tos[toIndex].getProperty("text")!=null)&&!(tos[toIndex].getProperty("text").toString().trim().equals("")) ){
										dataContext[0]=tos[toIndex].getProperty("text").toString().trim();
										break;
									}
									n++;
								}
							}
						}
					}
				}
/////////////////////////////////////////////INIZIO codice per l'aggiunta dei dati di testo e tooltip /////////////////////////////						
//					//recupero le informazioni delle proprietà del to
//					if(uIClassID.equals("ComboBoxUI"))
//						//recupero l'elemento selezionato nella comboBox
//						dataContext[1]= getComboBoxSelectedElement(tos[i]).trim();
//					else
//						if(uIClassID.equals("ListUI"))
//								dataContext[1]= getListSelectedElement(tos[i]).trim();
//						else
//							if(tos[i].getProperty("text")!=null)
//								dataContext[1]=tos[i].getProperty("text").toString().trim();
//					
//					if(tos[i].getProperty("toolTipText")!=null)
//						dataContext[2]=tos[i].getProperty("toolTipText").toString().trim();
/////////////////////////////////////////////FINE codice per l'aggiunta dei dati di testo e tooltip /////////////////////////////				
					
					//aggiungo il to con i dati trovati alla mappa
					if(knowWidget){
						if(discardTestObjectWithoutAnyInformation){
							if(hasInformation(dataContext))
								dataMap.put(tos[i],dataContext);
						}else{
							dataMap.put(tos[i],dataContext);
						}
					}
										
				
						
			}
				
	}
	

	/**
	 * Metodo che esegue l'analisi contestuale con strategia locale a partire da root
	 * @param root
	 */
	public void doLocalAnalysis(TestObject root){
		
		TestObject tos[] = root.find(SubitemFactory.atChild(properties));

		if(tos.length >0){
			analyzeLocalContext(tos, root);
			
			for(int i=0;i<tos.length;i++){
				doLocalAnalysis(tos[i]);
			}	
		}
	}
	
	protected void analyzeLocalContext(TestObject[] tos,TestObject root){

		HashMap<String,List<Integer[]>> widgetDescriptor = new HashMap<String,List<Integer[]>>();
		for(String s : allWidgetDescriptor){
			widgetDescriptor.put(s, getSameWidgetDescriptorPosition(tos,s));
		}
		for(int i=0 ; i<tos.length ; i++){
			
			String dataContextFromOldState[] = testState.getDataContextFromOldState(tos[i]);
			if(dataContextFromOldState != null) {
				logger.info("data context for the test objcet already present into the map " + dataContextFromOldState[0] + " " + dataContextFromOldState[1] + " " + dataContextFromOldState[2]);
				dataMap.put(tos[i], dataContextFromOldState);
				
			} else {
				logger.info("data context to be search "); 
				String uIClassID=tos[i].getProperty("uIClassID").toString();
				boolean knowWidget=false;

				boolean isDisplayed=true;
				if(doAnalysisOnlyForDisplayedWidget)
					isDisplayed=isDisplayed(tos[i]);

				//inizializzo vettore dei dati di contesto
				String dataContext[]=new String[3];
				dataContext[0]="";
				dataContext[1]="";
				dataContext[2]="";

				//inizializzo lista dei widget correlati
				List<Integer[]> relatedWidget= Lists.newArrayList();
				relatedWidget.clear();
				//recupero la posizione del to
				Integer[] toPosition=null;//=getTOPosition(tos[i]);

				// Se il to è un widget di testo
				if(textWidget.contains(uIClassID)){
					knowWidget=true;
					//recupero la posizione del to
					toPosition=getTOPosition(tos[i]);
					if( ((doAnalysisOnlyForDisplayedWidget)&&(isDisplayed))||(!doAnalysisOnlyForDisplayedWidget) ){
						for(String s : baseTextWidgetDescriptor){
							relatedWidget = findRelatedWidget(relatedWidget,widgetDescriptor.get(s),toPosition);
						}
						// Se attivati cerco i widget correlati
						// utilizzando i descrittori opzionali
						if(useOptionalTextWidgetDescriptor){
							for(String s : optionalTextWidgetDescriptor){
								relatedWidget = findRelatedWidget(relatedWidget,widgetDescriptor.get(s),toPosition);
							}
						}
					}
				}
				// Se il to è un interactive widget
				if(interactiveWidget.contains(uIClassID)){
					knowWidget=true;
					//recupero la posizione del to
					toPosition=getTOPosition(tos[i]);
					if( ((doAnalysisOnlyForDisplayedWidget)&&(isDisplayed))||(!doAnalysisOnlyForDisplayedWidget) ){
						// Se attivati cerco i widget correlati
						// utilizzando i descrittori opzionali
						if(useOptionalInteractiveWidgetDescriptor){
							for(String s : optionalInteractiveWidgetDescriptor){
								relatedWidget = findRelatedWidget(relatedWidget,widgetDescriptor.get(s),toPosition);
							}	
						}
					}
				}
				// Se il to è un button widget
				if(buttonWidget.contains(uIClassID)){
					knowWidget=true;
					//recupero la posizione del to
					toPosition=getTOPosition(tos[i]);
					if( ((doAnalysisOnlyForDisplayedWidget)&&(isDisplayed))||(!doAnalysisOnlyForDisplayedWidget) ){
						// Se attivati cerco i widget correlati
						// utilizzando i descrittori opzionali
						if(useOptionalButtonDescriptor){
							for(String s : optionalButtonDescriptor){
								relatedWidget = findRelatedWidget(relatedWidget,widgetDescriptor.get(s),toPosition);
							}
						}
					}
				}
				if(panelWidget.contains(uIClassID)){
					knowWidget=true;
					//recupero la posizione del to
					toPosition=getTOPosition(tos[i]);
					if( ((doAnalysisOnlyForDisplayedWidget)&&(isDisplayed))||(!doAnalysisOnlyForDisplayedWidget) ){
						// Se attivati cerco i widget correlati
						// utilizzando i descrittori opzionali
						if(useOptionalPanelWidgetDescriptor){
							for(String s : optionalPanelWidgetDescriptor){
								relatedWidget = findRelatedWidget(relatedWidget,widgetDescriptor.get(s),toPosition);
							}
						}
					}
				}
				if(menuWidget.contains(uIClassID)){
					knowWidget=true;
				}

				if(relatedWidget.size()>0){
					//calcolo la distanza dal to per ogni widget correlato
					if(useDifferentPolicyDistance){
						if(centerPolicy.contains(uIClassID))
							relatedWidget=computeDistance(relatedWidget,toPosition,"centerPolicy");
						else
							relatedWidget=computeDistance(relatedWidget,toPosition,"defaultPolicy");
					}else
						relatedWidget=computeDistance(relatedWidget,toPosition,"defaultPolicy");

					//cerco il widget più vicino al to tra quelli correlati
					int toIndex=-1;
					int relatedIndex=findNearest(relatedWidget);
					if(relatedIndex!=-1)
						toIndex=relatedWidget.get(relatedIndex)[0];

					//recupero i dati di contesto

					if(toIndex!=-1){
						if( (tos[toIndex].getProperty("text")!=null)&&!(tos[toIndex].getProperty("text").toString().trim().equals("")) ){
							dataContext[0]=tos[toIndex].getProperty("text").toString().trim();
						}else{
							//Se il widget più vicino non ha informazioni 
							//ed è attivata la strategia di ricerca di informazioni
							//su widget a diversi livelli di distanza
							if(useNextWidgetDescriptorIfNearestOneHasNoInformation){
								int n=0;
								//mentre non sono al massimo livello di distanza consentito
								while(maxNextWidgetDistanceLevel>n){
									//elimino dai widget correlati il widget più vicino senza informazioni
									relatedWidget.remove(relatedIndex);
									//cerco il nuovo widget più vicino
									relatedIndex=findNearest(relatedWidget);
									//se esiste
									if(relatedIndex!=-1)
										//recupero l'indice del relativo to
										toIndex=relatedWidget.get(relatedIndex)[0];
									else
										break;
									//se il nuovo widget possiede informazioni
									if( (tos[toIndex].getProperty("text")!=null)&&!(tos[toIndex].getProperty("text").toString().trim().equals("")) ){
										dataContext[0]=tos[toIndex].getProperty("text").toString().trim();
										break;
									}
									n++;
								}
							}
						}
					}
				}
				if(useFatherInformationForChildWithoutContextInformation){
					if( dataContext[0].equals("") ){
						if(dataMap.containsKey(root)){
							String[] dataRoot=dataMap.get(root);
							//controllo se l'elemento root ha associato del testo interno
							if(!(dataRoot[1].equals("")))
								dataContext[0]=dataRoot[1];
							else
								//se non ha testo interno verifico se ha delle informazioni di contesto
								if(!(dataRoot[0].equals("")))
									dataContext[0]=dataRoot[0];
								else
									//Se non ha ne informazioni interne ne di contesto verifico se sono presenti informazioni di help
									if(!(dataRoot[2].equals("")))
										dataContext[0]=dataRoot[2];
						}
					}
				}
				/////////////////////////////////////////////INIZIO codice per l'aggiunta dei dati di testo e tooltip /////////////////////////////			
				//				//if(!(panelWidget.contains(uIClassID)))	{
				//				if((textWidget.contains(uIClassID)) || (interactiveWidget.contains(uIClassID)) 
				//						|| (menuWidget.contains(uIClassID)) || (buttonWidget.contains(uIClassID)) )	{
				//					//recupero le informazioni delle proprietà del to
				//					if(uIClassID.equals("ComboBoxUI"))
				//						//recupero l'elemento selezionato nella comboBox
				//						dataContext[1]= getComboBoxSelectedElement(tos[i]).trim();
				//					else
				//						if(uIClassID.equals("ListUI"))
				//								dataContext[1]=""; //getListSelectedElement(tos[i]).trim();
				//						else
				//							//if(!uIClassID.equals("OptionPaneUI"))
				//							if(tos[i].getProperty("text")!=null)
				//								dataContext[1]=tos[i].getProperty("text").toString().trim();
				//					
				//					if(tos[i].getProperty("toolTipText")!=null)
				//						dataContext[2]=tos[i].getProperty("toolTipText").toString().trim();
				//				}else{
				//					if(uIClassID.equals("TabbedPaneUI")){
				//						dataContext[1]= getTabbedPaneSelectedElement(tos[i]).trim();
				//					}
				//					if(tos[i].getProperty("toolTipText")!=null)
				//						dataContext[2]=tos[i].getProperty("toolTipText").toString().trim();
				//				}
				/////////////////////////////////////////////FINE codice per l'aggiunta dei dati di testo e tooltip /////////////////////////////					

				//aggiungo il to con i dati trovati alla mappa
				System.out.println("MAURO " + dataContext[0]);
				if(knowWidget){
					if(discardTestObjectWithoutAnyInformation){
						if(hasInformation(dataContext))
							dataMap.put(tos[i],dataContext);
					}else{
						dataMap.put(tos[i],dataContext);
					}
				}
			}
		}

	}
	
	//---------------------- METODI DI SUPPORTO -------------------------------
	
	/**
	 * Metodo utilizzato per verificare che un elemento sia realmente
	 * visualizzato nell'interfaccia
	 * @param to
	 */
	public boolean isDisplayed(TestObject to){
		boolean displayed=true;
		// recupero il rettangolo visibile
		String visibleRect=to.getProperty("visibleRect").toString();	
		int width=-1;
		int height=-1;
		//recupero la larghezza del rettangolo
		String[] strWidth=visibleRect.split("width=")[1].split(",");
		width=Integer.parseInt(strWidth[0]);
		//recupero l'altezza del rettangolo
		String[] strHeight=visibleRect.split("height=")[1].split("]");
		height=Integer.parseInt(strHeight[0]);
		//se sono uguali a 0 allora il TO non è visualizzato
		if((height==0)&&(width==0))
			displayed=false;
		return displayed;
	}
	/**
	 * Metodo per la verifica che il vettore informazioni legato a un to
	 * contenga informazioni
	 * @param s
	 * @return
	 */
	public boolean hasInformation(String[] s){
		boolean hasInformation=false;
		for(int i=0;i<s.length;i++)
			if( (s[i]!=null)&&(!(s[i].trim().equals("")))  ){
				hasInformation=true;
				break;
			}
		return hasInformation;
	}
	/**
	 * Metodo che ritorna il valore dell'elemento selezionato in una List
	 * @param to
	 * @return
	 */
	public String getListSelectedElement(TestObject to){
		String selected="";
		ITestDataList nameList;
		ITestDataElement nameListElement;
		nameList = (ITestDataList)to.getTestData("selected");
		ITestDataElementList nameListElements = nameList.getElements();
		if(nameListElements.getLength()>0){
			nameListElement=nameListElements.getElement(0);
			selected=nameListElement.getElement().toString();
		}
		return selected;
	}
	
	/**
	 * Metodo che recupera l'elemento selezionato all'interno della combo box
	 * @param to
	 * @return l'elemento selezionato all'interno combo box
	 */
	public String getComboBoxSelectedElement(TestObject to){
		String selected="";
		ITestDataText nameList;
		nameList = (ITestDataText)to.getTestData("selected");
		selected=nameList.getText();
		return selected;
	}
	/**
	 * Metodo che ritorna l'indice del widget correlato più vicino
	 * @param relatedWidget
	 * @return l'indice del widget correlato più vicino
	 */
	public int findNearest(List<Integer[]> relatedWidget){
		int minDistance=Integer.MAX_VALUE;
		int relatedIndex=-1;
		
		for(int i=0;i<relatedWidget.size();i++){
			Integer[] widgetPosition = relatedWidget.get(i);
			if(widgetPosition[5]<=minDistance){
				minDistance=widgetPosition[5];
				relatedIndex=i;
			}
		}		
		return relatedIndex;
	}
	
		
	/**
	 * Metodo che ricerca i widget correlati
	 * @param relatedWidget
	 * @param type
	 * @param toPosition
	 * @return List dei widget correlati
	 */
	public List<Integer[]> findRelatedWidget(List<Integer[]> relatedWidget, List<Integer[]> type, Integer[] toPosition){ 
		
		for(int i=0;i<type.size();i++){
			//recupero il primo widget da analizzare
			Integer[] widgetPosition = type.get(i);
			//calcolo le coordinate centrali del to in esame
			int xTOCenter=toPosition[0]+(toPosition[2]/2);
			int yTOCenter=toPosition[1]+(toPosition[3]/2);
			//controllo che il widget sia all'interno dell'area di interesse del to in esame
			if( (widgetPosition[1]<=xTOCenter)&&(widgetPosition[2]<=yTOCenter)){
				relatedWidget.add(widgetPosition);
			}
		}
		return relatedWidget;
	}

	/**
	 * Metodo che calcola la distanza tra i widget correlati e il widget in esame
	 * a seconda della policy scelta
	 * @param relatedWidget
	 * @param toPosition
	 * @param policy
	 * @return
	 */
	public List<Integer[]> computeDistance(List<Integer[]> relatedWidget, Integer[] toPosition,String policy){
		List<Integer[]> widgetDistance=Lists.newArrayList();
		
		for(int i=0;i<relatedWidget.size();i++){
			
			Integer[] widgetPosition=relatedWidget.get(i);
			int distance=0;
			
			if(policy.equals("defaultPolicy")){
				int xw= widgetPosition[1];
				int yw= widgetPosition[2];
				int xt= toPosition[0];
				int yt= toPosition[1];
				if(yw>=yt){
					distance = (int) (Math.pow(((xw+widgetPosition[3])-xt),2)+Math.pow((yw-yt),2));
				}else{
					distance = (int) (Math.pow((xw-xt),2)+Math.pow(((yw+widgetPosition[4])-yt),2));
				}
				widgetPosition[5]=distance;
				widgetDistance.add(widgetPosition);			
			}
			if(policy.equals("centerPolicy")){
				int xTOc=toPosition[0]+(toPosition[2]/2);
				int yTOc=toPosition[1]+(toPosition[3]/2);
				int xWc=widgetPosition[1]+(widgetPosition[3]/2);
				int yWc=widgetPosition[2]+(widgetPosition[4]/2);
				distance = (int) (Math.pow((xWc-xTOc),2)+Math.pow((yWc-yTOc),2));	
				widgetPosition[5]=distance;
				widgetDistance.add(widgetPosition);	
			}
		}
		
		return widgetDistance;	
	}
	/**
	 * Metodo che ritorna una List di tutti i descrittori di tipo uIClassID
	 * presenti in tos
	 * @param tos
	 * @param uIClassID
	 * @return List Integer[]
	 * @value [0]= indice all'interno di tos
	 * @value [1]= posizione assoluta x
	 * @value [2]= posizione assoluta y
	 * @value [3]= width
	 * @value [4]= height
	 * @value [5]= distanza fra i centri del widget e del to in esame (default -1)
	 */
	public List<Integer[]> getSameWidgetDescriptorPosition(TestObject[] tos, String uIClassID){
		//recupero delle posizioni
		List<Integer[]> tosPosition = Lists.newArrayList();
		int n=0;
		for(int i=0;i<tos.length;i++){
			if(tos[i].getProperty("uIClassID").equals(uIClassID)){
				if( ((doAnalysisOnlyForDisplayedWidget)&&(isDisplayed(tos[i])))||(!doAnalysisOnlyForDisplayedWidget) ){
					if( ((useOnlyWidgetDescriptorWithInformation)&&(tos[i].getProperty("text")!=null)&&!(tos[i].getProperty("text").toString().trim().equals("")) )||(!useOnlyWidgetDescriptorWithInformation)){
						Integer[] data=new Integer[6];
						data[0] = i;
						data[1] = getXPosition(tos[i]);
						data[2] = getYPosition(tos[i]);
						data[3] = Integer.parseInt(tos[i].getProperty("width").toString());
						data[4] = Integer.parseInt(tos[i].getProperty("height").toString());
						data[5] = -1;
						tosPosition.add(data);
						n++;
					}
				}
			}
		}
		//System.out.println("numero di oggetti aggiunti: "+n);
		return tosPosition;
	}
	
	/**
	 * Metodo che recupera la posizione assoluta e dimensioni
	 * del to
	 * @param to
	 * @return Integer[]
	 * @value [0]= posizione assoluta x
	 * @value [1]= posizione assoluta y
	 * @value [2]= width
	 * @value [3]= height
	 */
	public Integer[] getTOPosition(TestObject to){
		Integer[] position=new Integer[4];
				position[0] = getXPosition(to);
				position[1] = getYPosition(to);
				position[2] = Integer.parseInt(to.getProperty("width").toString());
				position[3] = Integer.parseInt(to.getProperty("height").toString());
		return position;
	}
	/**
	 * Metodo che ritorna la posizione assoluta x di to
	 * @param to
	 * @return la posizione assoluta x di to
	 */
	public int getXPosition(TestObject to){
		int x=-1;
		String absPos= to.getProperty("locationOnScreen").toString();
		String[] s=absPos.split("x=")[1].split(",");
		x=Integer.parseInt(s[0]);
		return x;
	}
	/**
	 * Metodo che ritorna la posizione assoluta y di to
	 * @param to
	 * @return la posizione assoluta y di to
	 */
	public int getYPosition(TestObject to){
		int y=-1;
		String absPos= to.getProperty("locationOnScreen").toString();
		String[] s=absPos.split("y=")[1].split("]");
		y=Integer.parseInt(s[0]);
		return y;
	}
	/**
	 * Metodo che ritorna il tab selezionato all'interno di un TabbedPane
	 * @param to
	 * @return il tab selezionato
	 */
	public String getTabbedPaneSelectedElement(TestObject to){
		String selected="";
		ITestDataList nameList;
		ITestDataElement nameListElement;
		nameList = (ITestDataList)to.getTestData("selected");
		ITestDataElementList nameListElements = nameList.getElements();
		if(nameListElements.getLength()>0){
			nameListElement=nameListElements.getElement(0);
			selected=nameListElement.getElement().toString();
		}
		return selected;
	}
	
	/**
	 * Metodo di test per la stampa della dataMap elaborata
	 */
	public void printDataMap(HashMap<TestObject,String[]> dataMap2){
		Set<TestObject> keys=dataMap2.keySet();
		for(TestObject to : keys){
			System.out.println("");
			System.out.println("TO: "); // "+to.getProperty("uIClassID").toString());
			String[] prop=dataMap2.get(to);
			System.out.println("dato contestuale: "+prop[0]);
			System.out.println("text to: "+prop[1]);
			System.out.println("toolTipText to: "+prop[2]);
		}
	}

}

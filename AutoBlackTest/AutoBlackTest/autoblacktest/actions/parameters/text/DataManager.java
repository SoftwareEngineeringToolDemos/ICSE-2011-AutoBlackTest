package autoblacktest.actions.parameters.text;

import java.io.BufferedReader;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;



import autoblacktest.actions.parameters.ParameterNumberFormat;

import com.google.common.collect.HashBiMap;

/**
 * Classe utilizzata per il caricamento e la gestione dei dati
 * usati durante la fase di test
 * 
 * @author Giovanni Becce
 *
 */
public class DataManager {
	private static final Logger logger = Logger.getLogger(DataManager.class);
	
	/**
	 * policy caricamento dati tramite un file *.xml
	 */
	public int LOAD_FROM_XML_FILE=0;
	/**
	 * policy caricamento dati tramite un file .dabt
	 */
	public int LOAD_FROM_FILE=1;
	
	//file xml
	private String tagNode="DATA";
	private String tagMetadata="metadata";
	private String tagValues="values";
	private String tagRight = "right";
	private String tagWrong = "wrong";
	private String dataSetXMLPath="data\\dataset.xml";
	
	//set di separatori per il caricamento dei dati da file
	private String dataSetPath="data\\dataset.dabt";
	private String metaDataSeparator="::";
	private String dataSeparator=",";
	private String erroneousDataSeparatorStart="%%";
	private String erroneousDataSeparatorClose="%%";
	
	//policy di caricamento dati correntemente utilizzata
	private int currentPolicy=-1;
	
	private static DataManager instance = null;
	/**
	 * Mappa tra il numero e il metadato corrispondente
	 */
	private HashBiMap<Integer, String> typeDataMap = HashBiMap.create();
	/**
	 * Mappa tra il numero del metadato e una mappa contenente il numero del dato e il valore
	 */
	private HashMap<Integer, HashMap<Integer,String>> dataMap = new HashMap<Integer, HashMap<Integer,String>>();
	/**
	 * mappa degli indici convertiti (tramite computeValueForDoubleParameter(index1,index2)
	 * dei dati errati e l'indicazione se già utilizzati o meno
	 */
	private HashMap<Long , Boolean> mapErroneousData = new HashMap<Long , Boolean>();
	
	public static synchronized DataManager getInstance() {
		if (instance == null)
			instance = new DataManager();
		return instance;
	}
	
	/**
	 * Gestisce il caricamento dei dati, secondo la policy scelta, mappandoli in memoria
	 */
	public void loadData(int policy){
		currentPolicy=policy;
		switch(policy){	
			//inizializzazione della dataMap tramite i dati contenuti nel dataSet
			case 0: loadDataFromXMLFile();
					break;
			case 1: loadDataFromFile();//(dataSetPath,metaDataSeparator,dataSeparator,erroneousDataSeparatorStart,erroneousDataSeparatorClose);
					break;
			default : currentPolicy=0;
					loadDataFromXMLFile();
					break;
		}
	}
	
	
	
	/**
	 * Metodo che ritorna la mappa dei dati (dataMap)
	 * @return dataMap
	 */
	public HashMap<Integer, HashMap<Integer,String>> getDataMap(){
		return dataMap;
	}
	
	public HashBiMap<Integer, String> getTypeDataMap(){
		return typeDataMap;
	}
	
	/**
	 * Metodo per l'aggiunta di un metadato - valore all'interno della mappa dei dati
	 * con gestione, dove necessario, della persistenza del dato.
	 * @param metadata
	 * @param value
	 */
	public void addEntry(String metadata,String value){
		 int index=0;
		 if(metadata!=null){
			 //controllo se il metadato è già presente nella mappa
			 if(!typeDataMap.containsValue(metadata)){
				 index=typeDataMap.size();
				 typeDataMap.put(index,metadata);
				 //inserisco in dataMap la nuova entry ed inizializzo l'hashmap annidata
				 HashMap<Integer,String> temp = new HashMap<Integer,String>();
				 dataMap.put(index,temp);
			 }else{
				 index = typeDataMap.inverse().get(metadata);
			 }
			 int index2level =dataMap.get(index).size();
			 // inserisco il dato nella hashmap annidata
			 dataMap.get(index).put(index2level, value);
		 }else{
			 if(!typeDataMap.containsValue("generic")){
		 			index=typeDataMap.size();
		 			typeDataMap.put(index,"generic");
		 			//inserisco in dataMap la nuova entry ed inizializzo l'hashmap annidata
		 			HashMap<Integer,String> temp = new HashMap<Integer,String>();
		 			dataMap.put(index,temp);
		 		}else{
		 			index = typeDataMap.inverse().get("generic");
		 		}
			 //recupero il numero di entry della hashmap annidata
			 int index2level =dataMap.get(index).size();
			 // inserisco il dato nella hashmap annidata
			 dataMap.get(index).put(index2level, value);
		 }
		 if(currentPolicy==0)
			 appendOnXMLFile(metadata,value);
		 if(currentPolicy==1)
			 appendOnFile(metadata,value);
	}
	
	private void appendOnXMLFile(String metadata,String value){
		File dataSet = new File(dataSetXMLPath); 
		boolean added=false;
	    if (dataSet.exists()) {
			SAXBuilder builder = new SAXBuilder();
			try {
				Document document = builder.build(new File(dataSetXMLPath));
				
				Element rootElement = document.getRootElement();
				
				List children = rootElement.getChildren(); 
				Iterator iterator = children.iterator(); 
				while ((iterator.hasNext())&&(!added)){ 
					 String readMetadata="";
				
					 Element nodo = (Element)iterator.next(); 
				
					 if(nodo.getName().equals(tagNode)){
						readMetadata=nodo.getChildText(tagMetadata).trim();
						if(readMetadata.equals(metadata)){
							Element valuesNode= (Element) nodo.getChild(tagValues);
							
							Element valueElement = new Element(tagRight); 
							valueElement.setText(value);
							valuesNode.addContent(valueElement);
							added=true;
						}
					 }
				}if(!added){
					//non esiste il metadato corrispondente
					Element dataNode = new Element(tagNode); 
					Element metadataNode = new Element(tagMetadata); 
					Element valuesNode = new Element(tagValues); 
					Element valueElement = new Element(tagRight);
					
					metadataNode.setText(metadata);
					valueElement.setText(value);
					
					valuesNode.addContent(valueElement);
					
					dataNode.addContent(metadataNode);
					dataNode.addContent(valuesNode);

					rootElement.addContent(dataNode);
				}
				//riscrivo il file xml
				XMLOutputter xmlOutputter = new XMLOutputter();
	            xmlOutputter.setFormat(Format.getPrettyFormat());
				FileOutputStream fileOutputStream = new FileOutputStream(new File(dataSetXMLPath)); 
	            xmlOutputter.output(document, fileOutputStream);
			
			} catch (JDOMException e) {
				// TODO Blocco catch generato automaticamente
				logger.error("Impossible to add data, file input error");
			} catch (IOException e) {
				// TODO Blocco catch generato automaticamente
				logger.error("Impossible to add data, file input error");
			}
			
	    }else{
	    	logger.error("Impossible to add data, file "+dataSetXMLPath+" does not exist");
	    }
	}
	
	private void appendOnFile(String metadata, String value){
		PrintWriter writer;
		try {
			writer = new PrintWriter(
					new BufferedWriter(new FileWriter(dataSetPath,true)));
			if( (metadata!=null)&&(!(metadata.equals(""))) ){
				writer.println("");
				writer.println(metadata+" :: "+value);
			}else{
				writer.println("");
				writer.println("generic :: "+value);
			}	
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Blocco catch generato automaticamente
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo utilizzato per il recupero del "dato effettivo" relativo agli indici passati 
	 * come parametro.
	 * @param index1 l'indice di riferimento al tipo di dato (metadato)
	 * @param index2 l'indice di rifermimento al valore nella hashmap relativa al metadato
	 * @return il "dato effettivo"  relativo alla key passata come parametro.
	 */
	public String getData(int index1,int index2){
		//recupero il valore assocciato alla posizione 
		//index1 in nella dataMap e alla posizione index2 nella hashmap annidata
		String value=dataMap.get(index1).get(index2);
		//ritorno la stringa contenente il dato
		return value;
	}
	
	/**
	 * Metodo utilizzato per il recupero del metadato
	 * corrispondente alla key passata come parametro
	 * @param key
	 * @return il metadato corrispondente alla key passata come parametro
	 */
	public String getMetaData(int key){
		//recupero i dati assocciati alla posizione key nella dataMap
		return typeDataMap.get(key);
	}
	
	/**
	 * Metodo utilizzato per il recupero della key
	 * associata al metadato passato come parametro
	 * @param metadata
	 * @return ritorna la key associata al metadato se presente, altrimenti ritorna -1
	 */
	public int getKey(String metadata){
		if(typeDataMap.containsValue(metadata))
			return typeDataMap.inverse().get(metadata);
		else
			return -1;
	}
	
	/**
	 * Metodo utilizzato per il recupero delle key associate
	 * al metadato passato come parametro
	 * @param metadata
	 * @return il Set di key relative al metadato passato come parametro
	 */
	public Set<Integer> getKeysWithMetadata(String metadata){
		int index= getKey(metadata);
		return dataMap.get(index).keySet();
	}
	
	/**
	 * Metodo utilizzato per il recupero delle key associate
	 * alla key passata come parametro
	 * @param key 
	 * @return il Set di key relative alla key passata come parametro
	 */
	public Set<Integer> getKeysWithKey(int key){
		return dataMap.get(key).keySet();
	}
	/**
	 * Metodo che ritorna il numero di entry nella hashmap
	 * @return ritorna il numero di entry presenti nella hashmap dataMap
	 */
	public int size(){
		return dataMap.size();
	}
	
	/**
	 * Metodo utilizzato per il recupero dei dati dal dataset in formato XML
	 * ed il loro inserimento nella hashmap dataMap. Nel caso in cui il file non 
	 * sia disponibile vengono inseriti nella dataMap dei vaolori di default.	
	 */
	private void loadDataFromXMLFile(){
	logger.info("Loading DataSet from XML file");
	File dataSet = new File(dataSetXMLPath); 
    if (dataSet.exists()) {
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = builder.build(new File(dataSetXMLPath));
			
			Element rootElement = document.getRootElement();
			
			List children = rootElement.getChildren(); 
			Iterator iterator = children.iterator(); 
			while (iterator.hasNext()){ 
				 String metadata="";
				 String value="";
			
				 Element nodo = (Element)iterator.next(); 
			
				 if(nodo.getName().equals(tagNode)){
					 //recupero il metadato
					 metadata=nodo.getChildText(tagMetadata).trim();
					 int index=typeDataMap.size();
					 if(!typeDataMap.containsValue(metadata)){
						 typeDataMap.put(index, metadata);
						 HashMap<Integer,String> temp = new HashMap<Integer,String>();
    					 dataMap.put(index,temp);
					 }else
						 index = typeDataMap.inverse().get(metadata);
					 
					 //recupero i valori
					if((nodo.getChild(tagValues)!=null)&&(nodo.getChild(tagValues).getChildren()!=null)){
						List values = nodo.getChild(tagValues).getChildren();
						Iterator iterator2 = values.iterator(); 
						while (iterator2.hasNext()){ 
							Element valueNode = (Element)iterator2.next(); 
							
							int index2level =dataMap.get(index).size();
							
							if(valueNode.getName().equals(tagRight)){
								value=valueNode.getText().trim();
								dataMap.get(index).put(index2level,value);	

							}else
								if(valueNode.getName().equals(tagWrong)){
									value=valueNode.getText().trim();
									dataMap.get(index).put(index2level,value);
									mapErroneousData.put(ParameterNumberFormat.computeValueForDoubleParameter(index,index2level),false);		 
								}
						}
					}
				 }
			}
					
		} catch (JDOMException e) {
			// TODO Blocco catch generato automaticamente
			logger.error("Impossible to load dataSet, file input error");
		} catch (IOException e) {
			// TODO Blocco catch generato automaticamente
			logger.error("Impossible to load dataSet, file input error");
		}
		
    }else{
    	logger.error("Impossible to load dataSet, file "+dataSetXMLPath+" does not exist");
    	// inserisco un valore generico nella multimap
    	 int index =0;
		 
	 		if(!typeDataMap.containsValue("generic")){
	 			index=typeDataMap.size();
	 			typeDataMap.put(index,"generic");
	 			//inserisco in dataMap la nuova entry ed inizializzo l'hashmap annidata
	 			HashMap<Integer,String> temp = new HashMap<Integer,String>();
	 			dataMap.put(index,temp);
	 		}else{
	 			index = typeDataMap.inverse().get("generic");
	 		}
	 		//recupero il numero di entry della hashmap annidata
			int index2level =dataMap.get(index).size();
			// inserisco il dato nella hashmap annidata
			dataMap.get(index).put(index2level, "generic string");
    }
	 //informazioni caricamento dati
    int numberOfData=0;
    for(int i=0;i <dataMap.size();i++){
		numberOfData=numberOfData+dataMap.get(i).size();
	}
    logger.debug("Loaded "+numberOfData+" data in "+dataMap.size()+" different data types");
    logger.debug("Loaded "+mapErroneousData.size()+" erroneous data");
}
	
	
	/**
	 * Metodo utilizzato per il recupero dei dati dal dataset in formato *.dabt
	 * ed il loro inserimento nella hashmap dataMap. Nel caso in cui il file non 
	 * sia disponibile vengono inseriti nella dataMap dei vaolori di default.
	 * @param path il percorso del dataSet
	 * @param metaDataSeparator stringa di separazione tra metadati e dati
	 * @param dataSeparator stringa di separazione dei singoli valori
	 */
	private void loadDataFromFile(){//(String path,String metaDataSeparator,String dataSeparator,String erroneousDataSeparatorStart,String erroneousDataSeparatorClose){
		String comment="//";
		logger.info("Loading DataSet from file");
		File dataSet = new File(dataSetPath); 
        if (dataSet.exists()) {
        	 try {
                 FileInputStream fStream = new FileInputStream(dataSet);
                 BufferedReader in = new BufferedReader(new InputStreamReader(fStream));
                 while (in.ready()) {
                     String line=in.readLine();
                     //elimino eventuali spazi ad inizio e fine riga
                     line=line.trim();
                     //controllo se è una riga di commento ed in caso la ignoro
                     if(!(line.startsWith(comment))){
                    	 
                    	 String[] dati=line.split(metaDataSeparator);
                		 // se non sono presenti solo dati, non presanta metadati senza valori (non termina con ::) 
                    	 // e sono presenti metadati (non inizia con ::)
                		if((dati.length>1)&&(!line.endsWith(metaDataSeparator))&&(!line.startsWith(metaDataSeparator))){
                				                				 
                				 String[] metadata=dati[0].split(dataSeparator);   				 
                				 String[] dataValues=dati[1].split(dataSeparator);
                				 int index=0;
                				 
                				 //controllo se il metadato è già presente nella mappa
                				 if(!typeDataMap.containsValue(metadata[0])){
                					 index=typeDataMap.size();
                					 typeDataMap.put(index,metadata[0]);
                					 //inserisco in dataMap la nuova entry ed inizializzo l'hashmap annidata
                					 HashMap<Integer,String> temp = new HashMap<Integer,String>();
                					 dataMap.put(index,temp);
                				 }else{
                					 index = typeDataMap.inverse().get(metadata[0]);
                				 }
                				 
                				 for(int i=0;i<(dataValues.length)&&(!dataValues[i].equals(""));i++){
                					 // inserisco nella hashmap di secondo livello il valore 
                					 //recupero il numero di entry della hashmap annidata
                					 int index2level =dataMap.get(index).size();
                					 // inserisco il dato nella hashmap annidata
                					 if(!(isErroneousData(dataValues[i].trim(),erroneousDataSeparatorStart,erroneousDataSeparatorClose))){
                						 dataMap.get(index).put(index2level, dataValues[i].trim());	
                					 }else{
                						 dataMap.get(index).put(index2level, dataValues[i].trim().split(erroneousDataSeparatorStart)[0].trim());
                						 mapErroneousData.put(ParameterNumberFormat.computeValueForDoubleParameter(index,index2level),false);
                						 //System.out.println("dato errato: "+dataValues[i].trim().split(erroneousDataSeparatorStart)[0].trim());
                					 }
                				 }
                		}else{
                			// se è una semplice linea di valori (senza ::)
                			if(!line.endsWith(metaDataSeparator)&&(!line.startsWith(metaDataSeparator)&&(!line.equals("")))){

                					 String[] dataValues=dati[0].split(dataSeparator);
                					 int index =0;
                					 
                					 if(!typeDataMap.containsValue("generic")){
                    					 index=typeDataMap.size();
                    					 typeDataMap.put(index,"generic");
                    					 //inserisco in dataMap la nuova entry ed inizializzo l'hashmap annidata
                    					 HashMap<Integer,String> temp = new HashMap<Integer,String>();
                    					 dataMap.put(index,temp);
                    				 }else{
                    					 index = typeDataMap.inverse().get("generic");
                    				 }
                					 
                					 for(int i=0;i<(dataValues.length)&&(!dataValues[i].equals(""));i++){
                    					 //inserisco nella hashmap il valore con metadata di tipo generic
                						 //recupero il numero di entry della hashmap annidata
                    					 int index2level =dataMap.get(index).size();
                    					 // inserisco il dato nella hashmap annidata
                    					 if(!(isErroneousData(dataValues[i].trim(),erroneousDataSeparatorStart,erroneousDataSeparatorClose))){
                    						 dataMap.get(index).put(index2level, dataValues[i].trim());	
                    					 }else{
                    						 dataMap.get(index).put(index2level, dataValues[i].trim().split(erroneousDataSeparatorStart)[0].trim());
                    						 mapErroneousData.put(ParameterNumberFormat.computeValueForDoubleParameter(index,index2level),false);
                    						 //System.out.println("dato errato: "+dataValues[i].trim().split(erroneousDataSeparatorStart)[0].trim());
                    					 }

                					 }
                						 
                			 }else{
                			 	if(line.startsWith(metaDataSeparator)){
                			 		String[] dataValues=dati[1].split(dataSeparator);
                			 		
                			 		int index =0;
               					 
                			 		if(!typeDataMap.containsValue("generic")){
                			 			index=typeDataMap.size();
                			 			typeDataMap.put(index,"generic");
                			 			//inserisco in dataMap la nuova entry ed inizializzo l'hashmap annidata
                			 			HashMap<Integer,String> temp = new HashMap<Integer,String>();
                			 			dataMap.put(index,temp);
                			 		}else{
                			 			index = typeDataMap.inverse().get("generic");
                			 		}
                					 
                			 		for(int i=0;i<(dataValues.length)&&(!dataValues[i].equals(""));i++){
                			 			//inserisco nella hashmap il valore con metadata di tipo generic
               						 //recupero il numero di entry della hashmap annidata
                   					 int index2level =dataMap.get(index).size();
                   					 // inserisco il dato nella hashmap annidata
                   					if(!(isErroneousData(dataValues[i].trim(),erroneousDataSeparatorStart,erroneousDataSeparatorClose))){
                   						dataMap.get(index).put(index2level, dataValues[i].trim());	
                   					}else{
                   						dataMap.get(index).put(index2level, dataValues[i].trim().split(erroneousDataSeparatorStart)[0].trim());
               						 mapErroneousData.put(ParameterNumberFormat.computeValueForDoubleParameter(index,index2level),false);
               						 //System.out.println("dato errato: "+dataValues[i].trim().split(erroneousDataSeparatorStart)[0].trim());
                   					}
                			 		}//chiusura for
                			 	}//chiusura if
                			 }//chiusura else
                		}//chiusura else
                			 
                     }//chiusura if commento iniziale
                 }//chiusura while  
                 in.close();
             } catch (IOException e) {
            	 logger.error("Impossible to load dataSet, file input error");
            	// inserisco un valore generico nella multimap
            	 int index =0;
					 
			 		if(!typeDataMap.containsValue("generic")){
			 			index=typeDataMap.size();
			 			typeDataMap.put(index,"generic");
			 			//inserisco in dataMap la nuova entry ed inizializzo l'hashmap annidata
			 			HashMap<Integer,String> temp = new HashMap<Integer,String>();
			 			dataMap.put(index,temp);
			 		}else{
			 			index = typeDataMap.inverse().get("generic");
			 		}
			 		//recupero il numero di entry della hashmap annidata
  					int index2level =dataMap.get(index).size();
  					// inserisco il dato nella hashmap annidata
  					dataMap.get(index).put(index2level, "generic string");
             }
        } else {
        	logger.error("Impossible to load dataSet, file "+dataSetPath+" does not exist");
        	// inserisco un valore generico nella multimap
        	 int index =0;
			 
		 		if(!typeDataMap.containsValue("generic")){
		 			index=typeDataMap.size();
		 			typeDataMap.put(index,"generic");
		 			//inserisco in dataMap la nuova entry ed inizializzo l'hashmap annidata
		 			HashMap<Integer,String> temp = new HashMap<Integer,String>();
		 			dataMap.put(index,temp);
		 		}else{
		 			index = typeDataMap.inverse().get("generic");
		 		}
		 		//recupero il numero di entry della hashmap annidata
				int index2level =dataMap.get(index).size();
				// inserisco il dato nella hashmap annidata
				dataMap.get(index).put(index2level, "generic string");
       	
        } 
        //informazioni caricamento dati
        int numberOfData=0;
        for(int i=0;i <dataMap.size();i++){
			numberOfData=numberOfData+dataMap.get(i).size();
		}
        logger.debug("Loaded "+numberOfData+" data in "+dataMap.size()+" different data types");
        logger.debug("Loaded "+mapErroneousData.size()+" erroneous data");
        /*
		//stampa
		for(int i=0;i <dataMap.size();i++){
			System.out.println("Elemento "+i+" della multimap con metadato: "+typeDataMap.get(i));
			HashMap<Integer,String> temporanea =dataMap.get(i);
			for(int j=0;j<temporanea.size();j++){
				System.out.println("\tvalore "+j+": "+temporanea.get(j));
			}
		}
		*/
	}

	protected boolean isErroneousData(String data,String erroneousDataSeparatorStart,String erroneousDataSeparatorClose){
		boolean erroneous=false;
		String errore="";
		if(data!=null){
			String[] temp=data.split(erroneousDataSeparatorStart);
			if((temp!=null )&&(temp.length>1)){
				errore=temp[1].split(erroneousDataSeparatorClose)[0];
			}
			if(errore.equals("erroneous"))
				erroneous=true;	
		}
		return erroneous;
	}
	
	/**
	 * Metodo che controlla se un dato è segnalato come errato
	 * @param index
	 * @return true se il dato è errato
	 */
	public boolean isErroneous(long index){
		boolean erroneous=false;
		if(mapErroneousData.containsValue(index))
			erroneous=true;
		return erroneous;
	}
	/**
	 * Metodo che ritorna true se il dato è errato ed è già stato utilizzato
	 * @param index
	 * @return true se il dato è errato ed è già stato utilizzato
	 */
	public boolean isErroneousDataUsed(long index){
		boolean used=false;
		if(isErroneous(index))
			if(mapErroneousData.get(index))
				used=true;
		return used;
	}
	/**
	 * Metodo per il set di un dato errato come utilizzato
	 * @param index
	 */
	public void setErroneousDataUsed(long index){
		if(isErroneous(index)){
			mapErroneousData.remove(index);
			mapErroneousData.put(index,true);
		}
	}
	/**
	 * Metodo per il reset di tutti i dati errati segnati come utilizzati
	 */
	public void resetAllErroneousDataUsed(){
		Set<Long> keys = mapErroneousData.keySet();
		HashMap<Long,Boolean> mapTemp= new HashMap<Long,Boolean>();
		for( long key : keys){
			mapTemp.put(key,false);
		}
		mapErroneousData=mapTemp;
	}
}








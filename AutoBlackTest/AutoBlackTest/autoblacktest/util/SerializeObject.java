package autoblacktest.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

public class SerializeObject {
	
	private static Logger logger = Logger.getLogger(SerializeObject.class);
	
	public static void serialize(String filename, Object obj) {
		
		logger.info("Serializing: " + filename);

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filename, false);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.debug("Serialization done");
		
	}

}

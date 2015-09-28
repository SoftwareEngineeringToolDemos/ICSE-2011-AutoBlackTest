package autoblacktest.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.rational.test.ft.object.interfaces.TestObject;

public class NewClassRecorder {

	private static Logger logger = Logger.getLogger(NewClassRecorder.class);
	
	public static void recordClass (TestObject to) {

		System.out.println("RECORDING NEW CLASS FOR THE CURRENT TEST OBJECT ");				

		try {
			FileWriter fstreamW = new FileWriter(System.getProperty("user.dir")+"\\actionsNEW\\" + to.getProperty("uIClassID").toString() + ".java", false); //se metto true faccio l'append
			BufferedWriter out = new BufferedWriter(fstreamW);
			out.write("package actions;");
			out.newLine();
			out.newLine();
			out.write("import resources.TesterHelper;");
			out.newLine();
			out.newLine();
			out.write("import com.rational.test.ft.object.interfaces.*;");
			out.newLine();
			out.newLine();
			out.write("public class " + to.getProperty("uIClassID").toString() + " extends TesterHelper {");
			out.newLine(); 
			out.newLine();

			Class<?> t = Class.forName(to.getClass().getName());

			Method[] methods = t.getMethods();
			out.write("/*");
			out.newLine();
			for(int u = 0; u < methods.length; u ++) {
				if((methods[u].toString().contains("void com.rational.test.ft.object.interfaces")) && 
						(!methods[u].toString().contains("com.rational.test.ft.object.interfaces.TestObject.")) &&
						((methods[u].getParameterTypes().length == 0) || (methods[u].getParameterTypes()[0].getName().contains("String"))) //&&
						//(!methods[u].toString().contains("hover()")) &&
						//(!methods[u].toString().contains("drag()")) &&
						/*(!methods[u].toString().contains("doubleClick()"))*/) {
					out.write("//" + methods[u].toString());
					out.newLine();
					out.write("    public static " + methods[u].getReturnType().getName() + " " + methods[u].toString().substring(0, methods[u].toString().indexOf('(')).substring(methods[u].toString().substring(0, methods[u].toString().indexOf('(')).lastIndexOf(".")+1) + "(TestObject testObject)" + " {");
					out.newLine();
					if(methods[u].getParameterTypes().length == 0) {
						out.write("        new " + to.getClass().getName().substring(to.getClass().getName().lastIndexOf(".")+1) + "(testObject)" + methods[u].toString().substring(methods[u].toString().lastIndexOf(".")) + ";");
					} else { 
						out.write("        new " + to.getClass().getName().substring(to.getClass().getName().lastIndexOf(".")+1) + "(testObject)" + methods[u].toString().substring(0, methods[u].toString().indexOf('(')).substring(methods[u].toString().substring(0, methods[u].toString().indexOf('(')).lastIndexOf(".")) + "(" + "\"STRINGA\"" + ");");
					}
					out.newLine();
					out.write("    }");	
					out.newLine();
					out.newLine();
				}
			}
			out.newLine();
			out.write("*/");
			out.newLine();
			out.write("}");
			out.newLine();
			out.newLine();
			out.write("//PROPERTIES");
			out.newLine();
			out.newLine();

			//stampo le proprietà
			out.write("/*");
			out.newLine();
			String[] properties = to.getProperties().toString().split(", ");
			for(int x=0; x < properties.length; x++) {
				out.write("   " + properties[x]);
				out.newLine();
			}
			out.write("*/");
			out.newLine();


			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e.getCause());
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e.getCause());
		}

	}

}

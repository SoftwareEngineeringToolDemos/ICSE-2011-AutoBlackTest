package rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import mockit.Mockit;

import org.junit.runner.RunWith;
import org.powermock.modules.junit4.legacy.PowerMockRunner;

import autoblacktest.util.MockSystem;


@RunWith(PowerMockRunner.class)
public class AUTMain extends UnicastRemoteObject implements RemoteCoberturaInterface  {

	protected AUTMain() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	private static final long serialVersionUID = 1L;


	/* (non-Javadoc)
	 * @see helloWorld.interfaces.HelloInterface#say()
	 */
	public void getCoverage() throws RemoteException {

		try {
			String className = "net.sourceforge.cobertura.coveragedata.ProjectData";
			String methodName = "saveGlobalProjectData";
			Class<?> saveClass = Class.forName(className);
			java.lang.reflect.Method saveMethod = saveClass.getDeclaredMethod(methodName, new Class[0]);
			saveMethod.invoke(null, new Object[0]);
			System.out.println("Cobertura RMI - Write done");
		} catch (Throwable t) {
			System.err.println("Cobertura RMI - Write failed");
		}
	}
	public void println(String s) {
		System.err.println(s);
	}

	public static void main(String[] args) {
		//added
		System.out.println("Creazione e registrazione dell'oggetto");
		try {
			LocateRegistry.createRegistry(2007);
			Registry registry = LocateRegistry.getRegistry(2007);
			registry.rebind("RemoteCobertura", new AUTMain());
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("Impossibile creare o registrare l'oggetto");
			System.exit(1);
		} 
		
		Mockit.setUpMocks(new MockSystem());

		try {
			Class<?> autClass = Class.forName(args[0]);
			String methodName = "main";
			Class<?>[] argsTypes = new Class[]{String[].class};
			Method mainMethod = autClass.getDeclaredMethod(methodName, argsTypes);
			String[] mainArgs = Arrays.copyOfRange(args, 1, args.length);
			mainMethod.invoke(null, (Object)mainArgs);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}

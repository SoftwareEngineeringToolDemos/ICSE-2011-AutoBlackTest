package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RemoteCoberturaInterface extends Remote{
	public void getCoverage() throws RemoteException;
	public void println(String s) throws RemoteException;
}
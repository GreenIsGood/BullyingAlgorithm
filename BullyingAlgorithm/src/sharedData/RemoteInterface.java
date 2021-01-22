package sharedData;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalTime;

public interface RemoteInterface extends Remote {
	int getId() throws RemoteException;

	void setId(int id) throws RemoteException;

	boolean isCoordinator() throws RemoteException;

	void setCoordinator(boolean isCoordinator) throws RemoteException;

	boolean isAlive() throws RemoteException;

	void setAlive(boolean isAlive) throws RemoteException;

	void setLstMsgTime(LocalTime lstMsgTime) throws RemoteException;

	LocalTime getLstMsgTime() throws RemoteException;

	void setMsg(String msg) throws RemoteException;

	String getMsg() throws RemoteException;

	void addMsg(String msg) throws RemoteException;

}

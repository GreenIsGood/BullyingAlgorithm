package sharedData;

import java.time.LocalTime;

public class ProcessData implements ProcessDataRemoteInterface {
	int id;
	boolean isCoordinator;
	boolean isAlive;
	LocalTime lstMsgTime;
	String msg;

	public synchronized int getId() {
		return id;
	}

	public synchronized void setId(int id) {
		this.id = id;
	}

	public synchronized boolean isCoordinator() {
		return isCoordinator;
	}

	public synchronized void setCoordinator(boolean isCoordinator) {
		this.isCoordinator = isCoordinator;
	}

	public synchronized boolean isAlive() {
		return isAlive;
	}

	public synchronized void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public synchronized LocalTime getLstMsgTime() {
		return lstMsgTime;
	}

	public synchronized void setLstMsgTime(LocalTime lstMsgTime) {
		this.lstMsgTime = lstMsgTime;
	}

	public synchronized String getMsg() {
		return msg;
	}

	public synchronized void setMsg(String msg) {
		this.msg = msg;
	}

	public synchronized void addMsg(String msg) {
		this.msg += msg;
	}
}

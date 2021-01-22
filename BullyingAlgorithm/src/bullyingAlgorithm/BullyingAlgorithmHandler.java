package bullyingAlgorithm;

import java.rmi.registry.Registry;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import sharedData.ProcessDataRemoteInterface;

import java.rmi.registry.LocateRegistry;

public class BullyingAlgorithmHandler extends MessageHandler {
	private int id;
	private int mxSize;

	BullyingAlgorithmHandler(int id, int mxSize) {
		this.id = id;
		this.mxSize = mxSize;
	}

	void start() {
		try {
			Registry registry = LocateRegistry.getRegistry(1000 + id);
			ProcessDataRemoteInterface stub = (ProcessDataRemoteInterface) registry.lookup("Reg");
			while (true) {
				if (stub.isCoordinator() == true) {
					coordinatorToProcessesMsg(id, mxSize);
				} else {
					processToProcessesMsg(id, mxSize);
					updateCoordinatorLstMsgTime(stub, recieveMsg(stub));
					if (checkCoordinatorDead(stub.getLstMsgTime()) == true) {
						if (electionStart(id, mxSize, stub) == true) {
							stub.setCoordinator(true);
							winningTheElectionMsg(id, mxSize);
						}else {
							waitF(7000);
						}
					}
				}
				waitF(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	boolean checkCoordinatorDead(LocalTime lstMsgTime) {
		if (lstMsgTime.until(LocalTime.now(), ChronoUnit.SECONDS) > 10) {
			return true;
		} else {
			return false;
		}
	}

	void updateCoordinatorLstMsgTime(ProcessDataRemoteInterface stub, String[] msg) {
		try {
			for (int i = 0; i < msg.length; i++) {
				if (msg[i].contains(msgWhenCoordinator)) {
					stub.setLstMsgTime(LocalTime.now());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	boolean checkHigherId(int id, String[] msg) {
		for (int i = 0; i < msg.length; i++) {
			if (Integer.parseInt((msg[i].split(" "))[1]) > id) {
				return true;
			}
		}
		return false;
	}

	boolean electionStart(int id, int mxSize, ProcessDataRemoteInterface stub) {
		int consecutiveNoHigherId = 0;
		while (true) {
			electionMsg(id, mxSize);
			String[] msg = recieveMsg(stub);
			if (checkHigherId(id, msg) == true) {
				return false;
			} else {
				consecutiveNoHigherId++;
			}
			if (consecutiveNoHigherId == 5) {
				return true;
			}
			waitF(1000);
		}
	}

	void waitF(long ms) {
		try {
			TimeUnit.MILLISECONDS.sleep(ms);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package bullyingAlgorithm;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import sharedData.ProcessDataRemoteInterface;

public class MessageHandler {
	String msgWhenCoordinator = "CoordinatorAlive";
	String msgWhenNotCoordinator = "ProcessAlive";
	String msgWhenCoordinatorIsDead = "CoordinatorDead";
	String msgWhenLose = "ILost";
	String msgWhenElection = "Election";
	String msgWhenWon = "WonElection";

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

	void processToProcessesMsg(int id, int mxSize) {
		sendMsg(createMsg(msgWhenNotCoordinator, id), id, mxSize);
	}

	void coordinatorToProcessesMsg(int id, int mxSize) {
		sendMsg(createMsg(msgWhenCoordinator, id), id, mxSize);
	}

	void electionMsg(int id, int mxSize) {
		sendMsg(createMsg(msgWhenElection, id), id, mxSize);
	}

	void winningTheElectionMsg(int id, int mxSize) {
		sendMsg(createMsg(msgWhenWon, id), id, mxSize);
	}

	void sendMsg(String msg, int id, int mxSize) {
		try {
			Registry registry;
			ProcessDataRemoteInterface stub;
			for (int i = 0; i < mxSize; i++) {
				if (i != id) {
					registry = LocateRegistry.getRegistry(1000 + i);
					stub = (ProcessDataRemoteInterface) registry.lookup("Reg");
					stub.addMsg(msg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String[] recieveMsg(ProcessDataRemoteInterface stub) {
		String[] msg = new String[0];
		try {
			if (stub.getMsg().length() != 0) {
				msg = stub.getMsg().split("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	String createMsg(String msg, int id) {
		return LocalTime.now().format(dtf).toString() + " " + ((Integer) id).toString() + " " + msg + '\n';
	}
}

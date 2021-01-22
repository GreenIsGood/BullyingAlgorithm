package processHandling;

import java.io.FileWriter;
import java.lang.ProcessBuilder.Redirect;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;

import sharedData.ProcessData;
import sharedData.ProcessDataRemoteInterface;

public class ProcessesHandler extends ProcessData {
	int mxSize = 1000;
	int noOfProcesses = 0;
	ProcessData[] processDataList = new ProcessData[mxSize];
	ProcessBuilder[] processBuilder = new ProcessBuilder[mxSize];
	Process[] processList = new Process[mxSize];
	ProcessDataRemoteInterface[] stub = new ProcessDataRemoteInterface[mxSize];

	public ProcessesHandler() {
		init();
	}

	void init() {
		try {
			for (int i = 0; i < mxSize; i++) {
				processBuilder[i] = new ProcessBuilder("java.exe", "-cp", "./*", "bullyingAlgorithm.ProcessStarter",
						((Integer) i).toString(), ((Integer) mxSize).toString());
				processBuilder[i].redirectOutput(Redirect.INHERIT);
				processBuilder[i].redirectError(Redirect.INHERIT);
				processDataList[i] = new ProcessData();
				stub[i] = (ProcessDataRemoteInterface) UnicastRemoteObject.exportObject(processDataList[i], 0);
				Registry registry = LocateRegistry.createRegistry(1000 + i);
				registry.bind("Reg", stub[i]);
				stub[i].setAlive(false);
				stub[i].setCoordinator(false);
				stub[i].setId(i);
				stub[i].setLstMsgTime(LocalTime.now());
				stub[i].setMsg("");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean startProcess(int id) {
		try {
			if (processList[id] == null || processList[id].isAlive() == false) {
				FileWriter w = new FileWriter("LogForProcessNo " + id + ".txt");
				w.write("");
				w.close();
				processList[id] = processBuilder[id].start();
				stub[id].setAlive(false);
				stub[id].setCoordinator(false);
				stub[id].setId(id);
				stub[id].setLstMsgTime(LocalTime.now().minusSeconds(11));
				stub[id].setMsg("");
				int currentCordinator = getCurrentCoordinator();
				if (currentCordinator != -1) {
					stub[getCurrentCoordinator()].setCoordinator(false);
				}
				noOfProcesses++;
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteProcess(int idx) {
		if (processList != null && processList[idx].isAlive() == true) {
			processList[idx].destroy();
			noOfProcesses--;
			return true;
		}
		return false;
	}

	public int getCurrentCoordinator() {
		try {
			for (int i = 0; i < mxSize; i++) {
				if (processList[i] != null && processList[i].isAlive()) {
					if (stub[i].isCoordinator() == true) {
						return i;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int[] getIds() {
		int[] aliveProcesses = new int[noOfProcesses];
		int idx = 0;
		for (int i = 0; i < mxSize; i++) {
			if (processList[i] != null && processList[i].isAlive()) {
				aliveProcesses[idx++] = i;
			}
		}
		return aliveProcesses;
	}

	public synchronized int getNoOfProcesses() {
		return noOfProcesses;
	}

	public synchronized void setNoOfProcesses(int noOfProcesses) {
		this.noOfProcesses = noOfProcesses;
	}

	public synchronized ProcessDataRemoteInterface[] getStub() {
		return stub;
	}

	public synchronized void setStub(ProcessDataRemoteInterface[] stub) {
		this.stub = stub;
	}

}

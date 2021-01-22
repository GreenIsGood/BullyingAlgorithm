package bullyingAlgorithm;

public class ProcessStarter {
	public static void main(String args[]) {
		int id = Integer.parseInt(args[0]);
		int mxSize = Integer.parseInt(args[1]);
		BullyingAlgorithmHandler bullyingAlgorithmHandler = new BullyingAlgorithmHandler(id, mxSize);
		bullyingAlgorithmHandler.start();
	}
}

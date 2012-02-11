import java.util.Arrays;

/**
 * DaiControl
 * @author erikreed@cmu.edu
 */
class DaiControl {

	//TODO: implement age layered for single core
	//private native void readInFactorgraph(String path);
	//private native void readInEvidence(String path);
	//private native void readInEMfile(String path);
	private native long createDai(String fg, String ev, String em);
	private native void prepEM(long data);
	private native double runEM(long data, int numIterations);
	private native void freeMem(long data);
	private native long copyDai(long data);
	private native void randomizeFG(long data);

	static {
		System.loadLibrary("daicontrol");
		//System.load("/data01/Projects/David_and_Erik/bullshit/ml/libdai/stochastic/DaiControl.so");
	}

	public static final int POPULATION = 10;
	public static final int NUM_ITERATIONS = 1;
	
	public static void main(String[] args) {
		DaiControl dai = new DaiControl();
		
		long dai_data = dai.createDai("dat/fg", "dat/tab", "dat/em");
		
		// initialize
		long[] dais = new long[POPULATION];
		for (int i=0; i<POPULATION; i++) {
			dais[i] = dai.copyDai(dai_data);
			dai.randomizeFG(dais[i]);
			dai.prepEM(dais[i]);
		}

		// run EM
		for (int i=0; i<POPULATION; i++) {
			double l = dai.runEM(dais[i], NUM_ITERATIONS);
			System.out.println("dai " + i + " lhood = " + l);
		}
		
		// free memory
		dai.freeMem(dai_data);
		for (int i=0; i<POPULATION; i++)
			dai.freeMem(dais[i]);
		//System.out.println(Arrays.toString(dais));
		System.out.println("Terminated successfully");
	}

}
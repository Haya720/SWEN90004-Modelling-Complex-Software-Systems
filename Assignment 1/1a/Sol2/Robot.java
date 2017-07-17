import java.util.Random;

public class Robot extends BicycleHandlingThread {

	// the bicycle needs inspection
	protected Bicycle bicycle;

	// the belt from which the robot takes the bicycles
	protected Belt mainBelt;
	// the belt from which the robot puts the bicycles
	protected Belt shortBelt;

	// the inspector used to check bicycles
	protected Inspector inspector;

	/**
	 * Create a new Robot, transmit bicycle between 2 belts and 1 inspector
	 */
	public Robot(Belt belt1, Belt belt2, Inspector inspector) {
		super();
		this.mainBelt = belt1;
		this.shortBelt = belt2;
		this.inspector = inspector;
	}

	/**
	 * The thread's main method. Continually tries to transmit tagged bicycle
	 * between belt and inspector
	 */
	public void run() {
		// System.out.println("Thread Robot start!");
		synchronized (this) {
			Random random = new Random();
			while (!isInterrupted()) {
				try {
					// if (mainBelt.sensorTag() && !mainBelt.checkFlag()) {
					if (mainBelt.sensorTag()) {
						// System.out.println("Sensor finds tag.");
						// inspector empty:
						// no -> wait; yes -> transmit
						if (inspector.getInspectorFlag()) {
							wait();
						} else {
							// check whether defective?
							bicycle = mainBelt.robotTakeBicycle();
							inspector.setBicycle(bicycle);
							System.out.println("* Robot moves " + bicycle + 
									" from Main belt to inspector *");

							System.out.println("* Inspector checks bicycle *");
							inspector.checkBicycle();

							// put inspected bicycle to short belt
							bicycle = inspector.getBicycle();
							System.out.println("* Robot moves " + bicycle + 
									" from inspector to Short belt *\n");
							shortBelt.put(bicycle, 0);
						}
					}
					// notify any waiting threads that the robot has changed
					notifyAll();

					// let some time pass ...

					int sleepTime = random.nextInt(Params.ROBOT_MOVE_TIME);
					sleep(sleepTime);
				} catch (InterruptedException e) {
					this.interrupt();
				}
			}
		}
	}
}

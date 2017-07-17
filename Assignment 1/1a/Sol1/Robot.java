import java.util.Random;

public class Robot extends BicycleHandlingThread {

	// the bicycle needs inspection
	protected Bicycle bicycle;

	// the belt from which the robot takes the bicycles
	protected Belt belt;

	// the inspector used to check bicycles
	protected Inspector inspector;

	/**
	 * Create a new Robot, transmit bicycle between belt and inspector
	 */
	public Robot(Belt belt, Inspector inspector) {
		super();
		this.belt = belt;
		this.inspector = inspector;
	}

	/**
	 * The thread's main method. Continually tries to transmit tagged bicycle
	 * between belt and inspector
	 */
	public void run() {
		// System.out.println("Thread Robot start!");
		synchronized (this) {
			while (!isInterrupted()) {
				try {
					if (belt.sensorTag() && !belt.checkFlag()) {
						// System.out.println("Sensor finds tag.");
						// inspector empty:
						// no -> wait; yes -> transmit
						if (inspector.getInspectorFlag()) {
							wait();
						} else {
							// check whether defective?
							bicycle = belt.robotTakeBicycle();
							inspector.setBicycle(bicycle);
							System.out.println("* Robot moves " + bicycle 
									+ " to inspector *");

							inspector.checkBicycle();
							System.out.println("* Inspector checks bicycle *");

							// return to belt if segment is empty
							bicycle = inspector.getBicycle();

							// segment 3 is occupied -> wait
							if (!belt.checkSegment3()) {
								wait();
							} else {
								belt.robotReturnBicycle(bicycle);
								System.out.println("* Robot moves " + bicycle 
										+ " to belt *\n");
							}
						}
					}
					// notify any waiting threads that the robot has changed
					notifyAll();

					// let some time pass ...
					Random random = new Random();
					int sleepTime = random.nextInt(Params.ROBOT_MOVE_TIME);
					sleep(sleepTime);
				} catch (InterruptedException e) {
					this.interrupt();
				}
			}
		}
	}
}

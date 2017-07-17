/**
 * The driver of the simulation
 */

public class Sim {
	/**
	 * Create all components and start all of the threads.
	 */
	public static void main(String[] args) {

		Belt mainBelt = new Belt("Main belt", 5);
		Belt shortBelt = new Belt("Short belt", 2);
		
		Producer producer = new Producer(mainBelt);
		Consumer consumer1 = new Consumer(mainBelt);
		Consumer consumer2 = new Consumer(shortBelt);
		BeltMover mover1 = new BeltMover(mainBelt);
		BeltMover mover2 = new BeltMover(shortBelt);		
		Inspector inspector = new Inspector();
		Robot robot = new Robot(mainBelt, shortBelt, inspector);

		consumer1.start();
		consumer2.start();
		producer.start();
		mover1.start();
		mover2.start();
		inspector.start();
		robot.start();

		while (consumer1.isAlive() && consumer2.isAlive() && 
				producer.isAlive() && mover1.isAlive() && mover2.isAlive() 
				&& inspector.isAlive() && robot.isAlive())
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				BicycleHandlingThread.terminate(e);
			}

		// interrupt other threads
		consumer1.interrupt();
		consumer2.interrupt();
		producer.interrupt();
		mover1.interrupt();
		mover2.interrupt();
		inspector.interrupt();
		robot.interrupt();

		System.out.println("Sim terminating");
		System.out.println(BicycleHandlingThread.getTerminateException());
		System.exit(0);
	}
}

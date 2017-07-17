import java.util.Random;

/**
 * A consumer continually tries to take bicycles from the end of a quality
 * control belt
 */

public class Consumer extends BicycleHandlingThread {

	// the belts from which the consumer takes the bicycles
	protected Belt belt;

	/**
	 * Create a new Consumer that consumes from belt
	 */
	public Consumer(Belt belt) {
		super();
		this.belt = belt;
	}

	/**
	 * Loop indefinitely trying to get bicycles from the quality control belt
	 */
	public void run() {
//		synchronized (this) {
			while (!isInterrupted()) {
				try {
					// get bicycle from both belts
					belt.getEndBelt();

					// let some time pass ...
					Random random = new Random();
					int sleepTime = Params.CONSUMER_MIN_SLEEP
							+ random.nextInt(Params.CONSUMER_MAX_SLEEP 
							- Params.CONSUMER_MIN_SLEEP);
					sleep(sleepTime);

				} catch (InterruptedException e) {
					// in Sim: consumer.interrupt() -> jump to here
					this.interrupt();
					// jump back to main thread

				}
			}
			System.out.println("Consumer terminated");
		}
	}
//}

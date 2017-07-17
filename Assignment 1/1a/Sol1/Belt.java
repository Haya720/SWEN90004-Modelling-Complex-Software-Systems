/**
 * The bicycle quality control belt
 */
public class Belt {

	// the items in the belt segments
	protected Bicycle[] segment;

	// the length of this belt
	protected int beltLength = 5;

	// to help format output trace
	final private static String indentation = "                        ";

	// the index position of sensor
	protected int sensorIndex = 2;

	// true if sensor finds tag
	protected boolean sensorTag = false;

	// true if belt 3 is empty
	protected boolean belt3empty = false;

	// instance of class Sensor, used to exam tags
	protected Sensor sensor;

	/**
	 * Create a new, empty belt, initialised as empty
	 */
	public Belt() {
		segment = new Bicycle[beltLength];
		for (int i = 0; i < segment.length; i++) {
			segment[i] = null;
		}

		this.sensor = new Sensor();
	}

	/**
	 * Put a bicycle on the belt.
	 * 
	 * @param bicycle
	 *            the bicycle to put onto the belt.
	 * @param index
	 *            the place to put the bicycle
	 * @throws InterruptedException
	 *             if the thread executing is interrupted.
	 */
	public synchronized void put(Bicycle bicycle, int index) 
			throws InterruptedException {

		// while there is another bicycle in the way, block this thread
		while (segment[index] != null) {
			wait();
		}

		// insert the element at the specified location
		segment[index] = bicycle;

		// make a note of the event in output trace
		System.out.println(bicycle + " arrived");

		// notify any waiting threads that the belt has changed
		notifyAll();
	}

	/**
	 * Take a bicycle off the end of the belt
	 * 
	 * @return the removed bicycle
	 * @throws InterruptedException
	 *             if the thread executing is interrupted
	 */
	public synchronized Bicycle getEndBelt() throws InterruptedException {

		Bicycle bicycle;

		// while there is no bicycle at the end of the belt, block this thread
		while (segment[segment.length - 1] == null) {
			wait();
		}

		// get the next item
		bicycle = segment[segment.length - 1];
		segment[segment.length - 1] = null;

		// make a note of the event in output trace
		System.out.print(indentation + indentation);
		System.out.println(bicycle + " departed");

		// notify any waiting threads that the belt has changed
		notifyAll();
		return bicycle;
	}

	/**
	 * Move the belt along one segment
	 * 
	 * @throws OverloadException
	 *             if there is a bicycle at position beltLength.
	 * @throws InterruptedException
	 *             if the thread executing is interrupted.
	 */
	public synchronized void move() throws InterruptedException, 
	OverloadException {
		// if there is something at the end of the belt,
		// or the belt is empty, do not move the belt
		while (isEmpty() || segment[segment.length - 1] != null) {
			wait();
		}

		// double check that a bicycle cannot fall of the end
		if (segment[segment.length - 1] != null) {
			String message = "Bicycle fell off end of " + " belt";
			throw new OverloadException(message);
		}

		// move the elements along, making position 0 null
		for (int i = segment.length - 1; i > 0; i--) {
			if (this.segment[i - 1] != null) {
				System.out.println(indentation + this.segment[i - 1] + 
						" [ s" + (i) + " -> s" + (i + 1) + " ]");
			}
			segment[i] = segment[i - 1];
		}
		segment[0] = null;
		// System.out.println(indentation + this);

		// notify any waiting threads that the belt has changed
		notifyAll();
	}

	/**
	 * @return the maximum size of this belt
	 */
	public int length() {
		return beltLength;
	}

	/**
	 * Peek at what is at a specified segment
	 * 
	 * @param index
	 *            the index at which to peek
	 * @return the bicycle in the segment (or null if the segment is empty)
	 */
	public Bicycle peek(int index) {
		Bicycle result = null;
		if (index >= 0 && index < beltLength) {
			result = segment[index];
		}
		return result;
	}

	/**
	 * Check whether the belt is currently empty
	 * 
	 * @return true if the belt is currently empty, otherwise false
	 */
	private boolean isEmpty() {
		for (int i = 0; i < segment.length; i++) {
			if (segment[i] != null) {
				return false;
			}
		}
		return true;
	}

	public String toString() {
		return java.util.Arrays.toString(segment);
	}

	/**
	 * @return the final position on the belt
	 */
	public int getEndPos() {
		return beltLength - 1;
	}

	/**
	 * Check bicycle on segment 3 has tag or not
	 * 
	 * @return true if the bicycle is tagged, otherwise false
	 * @throws InterruptedException
	 */
	public boolean sensorTag() throws InterruptedException {
		sensorTag = sensor.sensorTag(segment);
		return sensorTag;
	}

	/**
	 * Robot gets bicycle on segment 3
	 * 
	 * @return the bicycle on segment 3
	 * @throws InterruptedException
	 */
	public synchronized Bicycle robotTakeBicycle() throws InterruptedException {
		Bicycle bicycle = segment[sensorIndex];
		segment[sensorIndex] = null;
		checkSegment3();
		return bicycle;
	}

	/**
	 * Robot return bicycle to segment 3
	 * 
	 * @param bicycle
	 *            the bicycle needs to return
	 * @throws InterruptedException
	 */
	public synchronized void robotReturnBicycle(Bicycle bicycle) 
			throws InterruptedException {
		bicycle.setCheckFlag(true);
		segment[sensorIndex] = bicycle;
		checkSegment3();
	}

	/**
	 * Check whether segment 3 is empty or not
	 * 
	 * @return true is no bicycle on segment 3
	 */
	public boolean checkSegment3() {
		if (segment[sensorIndex] == null) {
			belt3empty = true;
		} else {
			belt3empty = false;
		}
		return belt3empty;
	}

	/**
	 * Check the bicycle on segment 3 has been test by inspector or not
	 * 
	 * @return true if bicycle is already checked by inspector
	 */
	public boolean checkFlag() {
		return segment[sensorIndex].getCheckFlag();
	}

}


public class Sensor {

	// the index position of sensor
	protected int sensorIndex = 2;

	/**
	 * Create a new, empty sensor, initialised as empty
	 */
	public Sensor() {

	}

	/**
	 * Check bicycle on segments has tag or not
	 * 
	 * @param segment
	 *            a bicycle list which contains all bicycles on belt
	 * @return true if bicycle on segment 3 has tag and never been inspected
	 */
	public boolean sensorTag(Bicycle[] segment) {
		// no bicycle on sensor
		if (segment[sensorIndex] == null) {
			return false;
		}
		// has bicycle, check tag? inspected?
		if (segment[sensorIndex].isTagged() && 
				!segment[sensorIndex].getCheckFlag()) {
			System.out.println("\n* " + segment[sensorIndex] 
					+ " is tagged, need inspection *");
			return true; // tagged
		} else {
			return false; // no tag
		}
	}

}

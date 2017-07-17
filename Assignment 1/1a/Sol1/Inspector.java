
public class Inspector extends BicycleHandlingThread {

	// the belt to be handled
	protected Belt belt;
	
	// true if inspector is checking bicycle
	protected boolean inspectorBusy;
	
	// the bicycle needs inspection
	protected Bicycle bicycle = null;

	/**
	 * Create a new Inspector
	 */
	public Inspector() {
		super();
		this.inspectorBusy = false;
	}

	/**
	 * The thread's main method. 
     * Continually tries to check received tagged bicycle is defective or not
	 */
	public void run() {
//		System.out.println("Thread Inspector start!");
		synchronized (this) {
			while (!isInterrupted()) {
				try {
					while (bicycle == null) {
						wait();
					}
					checkBicycle();
					notifyAll();
				} catch (InterruptedException e) {
					this.interrupt();
				}
			}
		}
	}

	/**
	 * Get tagged bicycle from robot
	 * @param bicycle 
	 * 				bicycle transformed from robot
	 */
	public void setBicycle(Bicycle bicycle) {
		inspectorBusy = true;
		this.bicycle = bicycle;
	}

	/**
	 * Check tagged bicycle if defective or non defective
	 * defective: keep tag
	 * non defective: remove tag
	 */
	public void checkBicycle() {
		if (!bicycle.isDefective()) {
			System.out.println("* " + bicycle 
					+ " is non defective, remove tag *");
			bicycle.setNotTagged();
		} else {
			System.out.println("* " + bicycle + " is defective, keep tag *");
		}
	}

	/**
	 * Get bicycle from inspector
	 * @return	inspected bicycle
	 */
	public Bicycle getBicycle() {
		Bicycle checkedBicycle = bicycle;
		bicycle = null;
		inspectorBusy = false;
		return checkedBicycle;
	}

	/**
	 * Check whether inspector is testing bicycle or empty
	 * @return true if inspector is checking bicycle
	 */
	public boolean getInspectorFlag() {
		return inspectorBusy;
	}

}

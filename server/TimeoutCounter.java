public class TimeoutCounter extends Thread {
	
	private boolean run = true;

	public TimeoutCounter() {
	}
	public void run() {
		// Run until connection is terminated.
		while (run) {
			boolean dead = true;

			// Sleep for 35 seconds unless pinged.
			try {
				this.sleep(35000);
			}
			// Recieved ping interrupted sleep (good).
			catch (InterruptedException e) {
				dead = false;
			}
			// No recieved ping... terminate connection.
			if (dead) {
				run = false;
			}
		}
	}
} 

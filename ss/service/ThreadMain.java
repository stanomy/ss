package service;


public class ThreadMain {
	private Thread t;

	private void runThread() {
		if (t == null) {
			t = new Thread() {
				public void run() {
					while (true) {
						PushServer p = new PushServer();
						try {
							p.getSFromConfigFile();
							this.sleep(10 * 1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			};
			t.start();

		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new ThreadMain().runThread();
	}

}
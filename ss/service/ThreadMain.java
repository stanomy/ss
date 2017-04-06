package service;

public class ThreadMain {
	private Thread t;
	

	private void procThread() {
		if (t == null) {
			t = new Thread() {
				public void run() {
					while (true) {
						PushServer p = new PushServer();
						try {
							p.push();
							this.sleep(2 * 1000);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			};
			t.start();

		}

	}

	private void consumThread() {
		if (t == null) {
			t = new Thread() {
				public void run() {
					while (true) {
						PushServer p = new PushServer();
						try {
							p.show();
							this.sleep(10 * 1000);
						} catch (Exception e) {
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

		new ThreadMain().procThread();
		new ThreadMain().consumThread();
	}

}
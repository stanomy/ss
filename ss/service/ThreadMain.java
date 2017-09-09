package service;

import java.util.concurrent.atomic.AtomicInteger;

import service.impl.SSCodeServerImpl;
import service.inf.SSCodeServiceInf;
import config.Config;

public class ThreadMain {
	private Thread t;
	private static AtomicInteger count = new AtomicInteger(0);
	
	private SSCodeServiceInf codeServiceInf=new SSCodeServerImpl();

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
							
							// 定时清理
							if (count.intValue() >= Config.LIMIT) {
								
								p.clearShow();
								p.clearResp();
							}
							//System.out.println("======"+count.intValue()+"==========");
							count.incrementAndGet();
							
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
		ThreadMain m=new ThreadMain();
		m.codeServiceInf.initCodeService();
		m.procThread();
		new ThreadMain().consumThread();
		
//		new ThreadMain().procThread();
//		new ThreadMain().consumThread();
	}

}
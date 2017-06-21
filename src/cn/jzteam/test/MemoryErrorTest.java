package simpletest;

public class MemoryErrorTest {
	
	public static void main(String[] args) {
		//new Thread(new TestDeidLock(1, 2)).start();
		//new Thread(new TestDeidLock(2, 1)).start();;
		testLiveLock(new Object());
	}
	
	public static void  testLiveLock(final Object obj){
		new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (obj) {
					try {
						obj.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
}
class TestDeidLock implements Runnable {
	int a,b;
	public TestDeidLock(int a,int b) {
		this.a = a;
		this.b = b;
	}
	@Override
	public void run() {
		synchronized (Integer.valueOf(a)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized(Integer.valueOf(b)){
				System.out.println(a+b);
			}
		}
	}
}


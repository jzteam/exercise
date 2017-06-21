package cn.jzteam.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestThread {
	
	public static void main(String[] args) {
		// 缓存线程池
		ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
		// 指定数量线程池
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
		
		// 延迟执行线程池
		ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(10);
		
		CountSheep task = new TestThread().new CountSheep();
		
		/*Future<String> submit = newCachedThreadPool.submit(task);
		String str = null;
		try {
			 str = submit.get(2, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			System.out.println("2毫秒内没有执行完");
			try {
				str = submit.get();
			} catch (InterruptedException | ExecutionException e1) {
				System.out.println("使用阻塞get失败");
			}
		}
		
		System.out.println(str);*/
		
		SayLove sayLove = new TestThread().new SayLove();
		ScheduledFuture<?> result = newScheduledThreadPool.scheduleAtFixedRate(sayLove,5,3,TimeUnit.SECONDS);
		try {
			System.out.println("result="+result.get());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class CountSheep implements Callable<String>{

		@Override
		public String call() throws Exception {
			int sum = 0;
			for(int i = 0; i< 100000000;i++){
				sum += i;
			}
			return "拷贝数据完成sum="+sum;
		}
		
	}
	
	class SayLove implements Runnable{

		@Override
		public void run() {
			System.out.println("我是线程"+Thread.currentThread().getName()+",I Love You");
		}
		
	}

}

package cn.jzteam.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class TestFutureTask {
    
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(5);
        CountSheep task = new CountSheep();
        FutureTask<Integer> ft = new FutureTask<>(task);
        for (int i = 0; i < 5; i++) {
            Future<Integer> submit = es.submit(task);
        }
        System.out.println("任务提交完毕");
        try {
            System.out.println("结果为="+ft.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("over");
    }

}
class MyFutureTask<V> extends FutureTask<V> {

    public MyFutureTask(Callable<V> callable) {
        super(callable);
    }
    
    public void run() {
        
    }
    
}
class CountSheep implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for(int i = 0 ;i < 5000;i++){
            sum += i;
        }
        System.out.println("sum="+sum);
        return sum;
    }
    
}

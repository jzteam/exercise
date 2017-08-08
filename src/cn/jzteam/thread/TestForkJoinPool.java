package cn.jzteam.thread;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import org.junit.Test;


public class TestForkJoinPool {
    
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        SumCalculate task = new SumCalculate(0L, 5000000000L);
        Instant start = Instant.now();
        
        ForkJoinTask<Long> submit = pool.submit(task);
        try {
            System.out.println(submit.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Instant end = Instant.now();
        System.out.println("耗时："+Duration.between(start, end).toMillis()+"s");
    }
    
    @Test
    public void test1(){
        Instant start = Instant.now();
        
        long sum = 0L;
        for(long i = 0L;i<=5000000000L;i++){
            sum += i;
        }
        System.out.println(sum);
        
        Instant end = Instant.now();
        System.out.println("耗时："+Duration.between(start, end).toMillis()+"s");
    }

}

// ForkJoin框架
class SumCalculate extends RecursiveTask<Long> {
    private static final long serialVersionUID = 1L;
    private long start;
    private long end;
    private long SHRESHOLD = 10000L;
    
    public SumCalculate(long start,long end){
        this.start = start;
        this.end = end;
    }
    
    @Override
    protected Long compute() {
        long length = end - start;
        long sum = 0;
        if(length < SHRESHOLD){
            // 阈值以内，进行计算
            for(long i=start;i<=end;i++){
                sum += i;
            }
        }else{
            // 阈值以外，进行拆分
            long mid = (start + end)/2;
            SumCalculate left = new SumCalculate(start, mid);
            left.fork(); // 把left对象扔进taskQueue
            
            SumCalculate right = new SumCalculate(mid+1, end);
            right.fork();
            
            sum = left.join() + right.join();
        }
        
        return sum;
    }
    
}
package cn.jzteam.module.spring;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class SpringTransactionUtil {
	
	private static DataSourceTransactionManager transactionManager = ApplicationContextHolder.getBean(DataSourceTransactionManager.class);
	private static final ThreadLocal<TransactionStatus> transactionStatusHolder = new ThreadLocal<>();
	private static final ThreadLocal<Integer> transactionSize = new ThreadLocal<>();
	// 默认的TransactionDefinition接口实现：传播required，隔离数据库默认repeated_read。
    private static TransactionDefinition def = new DefaultTransactionDefinition();
	
	// 开启事务
	public static void begin(){
		// 查看是否已经开启事务
		TransactionStatus transactionStatus = transactionStatusHolder.get();
		
		// 当前已有事务，且事务未结束，那就共用这个事务
		if(transactionStatus != null && !transactionStatus.isCompleted()){
			// 当前线程已经开启事务了，事务深度+1
			Integer integer = transactionSize.get();
			transactionSize.set(integer+1);
			return;
		}

		// 当前线程没有事务或者事务已经结束，那么begin方法会再开启一个事务
		// 开启事务
        transactionStatus = transactionManager.getTransaction(def);
        transactionSize.set(0);
        
        transactionStatusHolder.set(transactionStatus);
	}
	
	// 提交事务：内层的提交不算提交，内层的回滚则全部回滚
	public static void commit(){
		TransactionStatus transactionStatus = transactionStatusHolder.get();
		if(transactionStatus == null){
			throw new RuntimeException("当前线程没有事务，不能提交");
		}
		
		Integer integer = transactionSize.get();
		System.out.println("提交时：size="+integer);
		if(integer == 0){
			transactionManager.commit(transactionStatus);
		}else{
			transactionSize.set(integer-1);
		}
	}
	
	// 回滚事务：内层的提交不算提交，内层的回滚则全部回滚
	public static void rollback(){
		TransactionStatus transactionStatus = transactionStatusHolder.get();

		if(transactionStatus == null || transactionStatus.isCompleted()){
			// 所有回滚都会清除当前线程事务，所以下一次回滚就获取不到事务，视为回滚同一个事务，直接返回
			// 事务已经回滚，再执行rollback也没有意义
			return;
		}
		transactionManager.rollback(transactionStatus);
		
		transactionStatusHolder.remove();
		transactionSize.remove();
	}

}

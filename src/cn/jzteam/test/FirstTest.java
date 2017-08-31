package cn.jzteam.test;

import java.util.List;
import java.util.UUID;

public class FirstTest {

    public static void main(String[] args) {
         
    	

    }
    
    public void batchCreateTx(List<String> txList,String uuid){
    	// 大量查询判断 ...
    	
    	// 开启事务 DB.tran_begin();
    	
    	// 循环调用另一个service方法
    	for (String tx : txList) {
			createTx(tx,UUID.randomUUID().toString());
		}
    	
    	// 更新其他表状态 updateExcute_notry
    	
    	// 提交事务 DB.commit();
    }
    
    public void createTx(String tx,String uuid){
    	// 大量查询判断 ... 
    	
    	// 开启事务 DB.tran_begin();
    	
    	// 插入数据库 updateExcute_notry
    	
    	// 提交事务 DB.commit();
    	
    }
    
    
}

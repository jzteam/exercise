package cn.jzteam.jdbc.druid;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultipleDataSource extends AbstractRoutingDataSource {
	
	public static final String masterDataSource = "master";
	public static final String slaveDataSource = "slave1";
	
	private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<>();
	
	public void setDataSourceKey(String key){
		dataSourceKey.set(key);
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return dataSourceKey.get();
	}

}

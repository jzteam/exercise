package cn.jzteam.core.jdbc.druid;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidPool {
	
	private static MultipleDataSource multipleDataSource = new MultipleDataSource();
	
	static{
		try {
			DruidDataSource masterDataSource = new DruidDataSource();
			Properties prop = new Properties();
			prop.load(new FileInputStream("src/druid.properties"));
			masterDataSource.setUrl(prop.getProperty("url").trim());
			masterDataSource.setDriverClassName(prop.getProperty("driverClassName").trim());
			masterDataSource.setUsername(prop.getProperty("username").trim());
			masterDataSource.setPassword(prop.getProperty("password").trim());
			masterDataSource.setFilters(prop.getProperty("filters").trim());
			masterDataSource.setMaxActive(Integer.valueOf(prop.getProperty("maxActive").trim()));
			masterDataSource.setInitialSize(Integer.valueOf(prop.getProperty("initialSize").trim()));
			masterDataSource.setMaxWait(Integer.valueOf(prop.getProperty("maxWait").trim()));
			masterDataSource.setMinIdle(Integer.valueOf(prop.getProperty("minIdle").trim()));
			
			DruidDataSource slaveDataSource = new DruidDataSource();
			Properties prop1 = new Properties();
			prop1.load(new FileInputStream("src/druid.properties"));
			slaveDataSource.setUrl(prop1.getProperty("url").trim());
			slaveDataSource.setDriverClassName(prop1.getProperty("driverClassName").trim());
			slaveDataSource.setUsername(prop1.getProperty("username").trim());
			slaveDataSource.setPassword(prop1.getProperty("password").trim());
			slaveDataSource.setFilters(prop1.getProperty("filters").trim());
			slaveDataSource.setMaxActive(Integer.valueOf(prop1.getProperty("maxActive").trim()));
			slaveDataSource.setInitialSize(Integer.valueOf(prop1.getProperty("initialSize").trim()));
			slaveDataSource.setMaxWait(Integer.valueOf(prop1.getProperty("maxWait").trim()));
			slaveDataSource.setMinIdle(Integer.valueOf(prop1.getProperty("minIdle").trim()));
			
			// 默认数据源
			multipleDataSource.setDefaultTargetDataSource(masterDataSource);
			
			// 数据源map
			Map<Object,Object> dataSourceMap = new HashMap<>();
			dataSourceMap.put(MultipleDataSource.masterDataSource,masterDataSource);
			dataSourceMap.put(MultipleDataSource.slaveDataSource,slaveDataSource);
			multipleDataSource.setTargetDataSources(dataSourceMap);
			
			multipleDataSource.afterPropertiesSet();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException{
		return multipleDataSource.getConnection();
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
		
//		batchInsert();
//		queryIn();
		testLike();
		
	}
	
	public static void testLike() throws SQLException{
		
		String sql = "select * from  user where username like ?";// %?% 报错
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setString(1, "%am%");
		ResultSet rs = ps.executeQuery();
		int columnCount = rs.getMetaData().getColumnCount();
		int c =0;
		while(rs.next()){
			for(int i = 0;i< columnCount;i++){
			}
			System.out.println("条数："+ ++c);
		}
		System.out.println("查询完成");
	}
	
	public static void queryIn() throws SQLException{
		multipleDataSource.setDataSourceKey(MultipleDataSource.masterDataSource);
		
		String sql = "select * from  user where id in (?,?,?)";
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setString(1, "1");
		ps.setString(2, "2");
		ps.setString(3, "3");
		ResultSet rs = ps.executeQuery();
		int columnCount = rs.getMetaData().getColumnCount();
		int c =0;
		while(rs.next()){
			for(int i = 0;i< columnCount;i++){
			}
			System.out.println("条数："+ ++c);
		}
		System.out.println("查询完成");
	}
	
	public static void batchInsert() throws SQLException{
		multipleDataSource.setDataSourceKey(MultipleDataSource.masterDataSource);
		
		String sql = "insert into user(id,username,password,status,sex,age) values(?,?,?,?,?,?)";
		PreparedStatement ps = getConnection().prepareStatement(sql);
		for(int i = 0;i<100;i++){
			ps.setInt(1, i+1);
			ps.setString(2, "jzteam"+i);
			ps.setString(3, UUID.randomUUID().toString());
			ps.setInt(4, 0);
			ps.setInt(5, Double.valueOf(Math.random()*2).intValue());
			ps.setInt(6, Double.valueOf(Math.random()*100).intValue());
			ps.addBatch();
		}
		ps.executeBatch();
		System.out.println("批量插入完成");
	}
	
	public static void test1() throws SQLException{
		
		Connection conn = getConnection();
		PreparedStatement ps = conn.prepareStatement("select * from test_full where text like ?");
		ps.setString(1, "%yin%");
		ResultSet resultSet = ps.executeQuery();
		
		System.out.println("执行sql");
		
		List<Map<String,Object>> list = new ArrayList<>();
		
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columnCount = metaData.getColumnCount();
		
		System.out.println("columnCount="+columnCount);
		while(resultSet.next()){
			Map<String,Object> map = new HashMap<>();
			for(int i = 1;i<=columnCount;i++){
				Object obj = resultSet.getObject(i);
				map.put(metaData.getColumnLabel(i), obj);
			}
			list.add(map);
		}
		
		resultSet.close();
		ps.close();
		conn.close();
		
		System.out.println("关闭链接");
		
		
		list.forEach(System.out::println);
		
	}

}

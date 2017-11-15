package dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("/datasource.properties")
public class DatabaseConfig {
	
	@Value("${jdbcUrl}")
	private String jdbcUrl;
	
	@Value("${driverClassName}")
	private String driverClassName;
	
	@Value("${dataSource.user}")
	private String user;
	
	@Value("${dataSource.password}")
	private String password;
	
	@Bean
	public HikariDataSource getDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setDriverClassName(driverClassName);
		config.setUsername(user);
		config.setPassword(password);
		return new HikariDataSource(config);
	}
	
	@Bean
	public PlatformTransactionManager getPlatformTransactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(); 
	    transactionManager.setDataSource(getDataSource()); 
	    return transactionManager;
	}
	
}

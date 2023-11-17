package com.imss.sivimss.ods.prefun.on.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.imss.sivimss.ods.prefun.on.configuration.mapper.Consultas;
import com.imss.sivimss.ods.prefun.on.configuration.mapper.OrdenesServicioMapper;
import com.imss.sivimss.ods.prefun.on.configuration.mapper.PersonaMapper;

public class MyBatisConfig {
	private static String DRIVER = "org.mariadb.jdbc.Driver";
	private static String URL= "jdbc:mysql://10.102.44.18:3306/SIVIBDDS";
	private static String USERNAME= "SIVI_USER"; 
	private static String PASSWORD= "51V1_US3R";
	private static String ENVIROMENT= "Develop";
	
	public static SqlSessionFactory buildqlSessionFactory() {
	    DataSource dataSource = new PooledDataSource(DRIVER, URL, USERNAME, PASSWORD);

	    Environment environment = new Environment(ENVIROMENT, new JdbcTransactionFactory(), dataSource);
	        
	    Configuration configuration = new Configuration(environment);
	    configuration.addMapper(Consultas.class);
	    configuration.addMapper(OrdenesServicioMapper.class);
	    configuration.addMapper(PersonaMapper.class);

	    
	    SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
	    
	    
	    return builder.build(configuration);
	}
}

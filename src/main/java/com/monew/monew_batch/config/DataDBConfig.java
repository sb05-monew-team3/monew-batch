package com.monew.monew_batch.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
	basePackages = "com.monew.monew_batch.repository",
	entityManagerFactoryRef = "dataEntityManager",
	transactionManagerRef = "dataTransactionManager"
)
public class DataDBConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource-data")
	public DataSource dataDBSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean dataEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

		em.setDataSource(dataDBSource());
		em.setPackagesToScan("com.monew.monew_batch.entity");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", "validate");
		properties.put("hibernate.show_sql", "false");
		properties.put("hibernate.physical_naming_strategy",
			"org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

		em.setJpaPropertyMap(properties);
		return em;
	}

	@Bean
	public PlatformTransactionManager dataTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(dataEntityManager().getObject());
		return transactionManager;
	}
}

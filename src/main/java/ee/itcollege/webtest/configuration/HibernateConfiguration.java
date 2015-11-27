package ee.itcollege.webtest.configuration;

import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
public class HibernateConfiguration {

    @Bean
    public DataSource getDataSource() {
    	ComboPooledDataSource dataSource = new ComboPooledDataSource();
    	dataSource.setJdbcUrl("jdbc:h2:~/db/test");
        try {
			dataSource.setDriverClass("org.h2.Driver");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
        dataSource.setMinPoolSize(2);
        dataSource.setMaxPoolSize(5);
        return dataSource;
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(getDataSource());
        emf.setPackagesToScan(new String[]{"ee.itcollege.webtest.entity"});
        HibernateJpaVendorAdapter hibernateAdapter = new HibernateJpaVendorAdapter();
        hibernateAdapter.setGenerateDdl(true);
        emf.setJpaVendorAdapter(hibernateAdapter);
        return emf;
    }
    
    @Bean
    public DataSourceTransactionManager txManager() {
        return new DataSourceTransactionManager(getDataSource());
    }
    
    @Bean
    public JpaTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }
    
    
}

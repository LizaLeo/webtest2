package ee.itcollege.webtest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import ee.itcollege.webtest.dao.PersonDao;
import ee.itcollege.webtest.dao.PersonDaoImpl;
import ee.itcollege.webtest.service.PersonService;
import ee.itcollege.webtest.service.PersonServiceImpl;

@EnableAspectJAutoProxy
@Configuration
public class GeneralBeans {
    
    @Bean
    public PersonDao personDao() {
        return new PersonDaoImpl();
    }
    
    @Bean
    public PersonService personService() {
        return new PersonServiceImpl();
    }
    
}

package ee.itcollege.webtest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ee.itcollege.webtest.annotation.SlowQueries;
import ee.itcollege.webtest.dao.PersonDao;
import ee.itcollege.webtest.entity.Person;

public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonDao personDao;
    
    @SlowQueries
    @Override
    public List<Person> getPersons() {
        return personDao.getAll();
    }
    
    @Override
    @Transactional
    public Person save(Person p) {
        return personDao.persist(p);
    }

	@Override
	@Transactional
	public void delete(Person p) {
        personDao.delete(p);
	}
    
}

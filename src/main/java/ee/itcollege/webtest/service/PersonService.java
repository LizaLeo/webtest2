package ee.itcollege.webtest.service;

import java.util.List;

import ee.itcollege.webtest.entity.Person;

public interface PersonService {
    
    public List<Person> getPersons();
    
    public Person save(Person p);

	public void delete(Person p);
    
}

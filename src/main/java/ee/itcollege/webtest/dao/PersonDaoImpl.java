package ee.itcollege.webtest.dao;

import ee.itcollege.webtest.entity.Person;

public class PersonDaoImpl extends AbstractDaoImpl<Person> implements PersonDao {

    public PersonDaoImpl() {
        super(Person.class);
    }
    
}

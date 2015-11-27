package ee.itcollege.webtest.dao;

import java.util.List;

public interface AbstractDao<T> {

    List<T> getAll();
    
    T persist(T item);
    
    T update(T item);
    
    T getById(Long id);
    
    void delete(T item);
    
}

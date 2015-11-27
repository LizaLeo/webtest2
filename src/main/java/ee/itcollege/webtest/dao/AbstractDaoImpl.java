package ee.itcollege.webtest.dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaQuery;


public abstract class AbstractDaoImpl<T> implements AbstractDao<T> {
    @PersistenceContext
    protected EntityManager em;

    protected Class<T> entityClass;

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /*
    @SuppressWarnings("unchecked")
    public AbstractDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
    }
    */
    

    public AbstractDaoImpl(Class<T> clazz) {
        this.entityClass = clazz;
    }

    public EntityManager getEm() {
        return em;
    }

    public AbstractDao<T> setEm(EntityManager em) {
        this.em = em;
        return this;
    }
    
    public T persist(T item) {
        if (item == null)
            throw new PersistenceException("Item may not be null");
        em.persist(item);
        return item;
    }

    public T update(T item) {
        if (item == null)
            throw new PersistenceException("Item may not be null");

        em.merge(item);
        return item;
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        CriteriaQuery<T> cq = (CriteriaQuery<T>) em.getCriteriaBuilder()
            .createQuery();
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

    public T getById(Long id) {
        if (id == null || id < 1)
            throw new PersistenceException("Id may not be null or negative");

        return (T) em.find(entityClass, id);
    }

    public void delete(T item) {
        if (item == null)
            throw new PersistenceException("Item may not be null");
        em.remove(em.merge(item));
        em.flush();
    }

}
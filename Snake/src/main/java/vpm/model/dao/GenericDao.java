package vpm.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

public interface GenericDao<T , ID> {

	public void setEntityManager(EntityManager entityManager);
	public void create(T entity);
	public T merge(T entity);
	public void makeTransient(T entity);
	public void checkVersion(T entity, boolean forceUpdate);
	
	public T findById(ID id);
	public T findById(ID id, LockModeType lock);
	public T findReferenceById(ID id);
	public List<T> findAll();
	public Long getCount();
}

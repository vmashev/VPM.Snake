package vpm.model.dao.postgres;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.criteria.CriteriaQuery;

import vpm.model.dao.GenericDao;

public abstract class GenericDAOPostgreSQL<T, ID extends Serializable> implements GenericDao<T, ID>{

	protected EntityManager entityManager;
	protected final Class<T> entityClass;
	
	protected GenericDAOPostgreSQL(Class<T> entityClass , EntityManager entityManager){
		this.entityClass = entityClass;
		this.entityManager = entityManager;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public void create(T entity) {
		getEntityManager().persist(entity);
	}
	
	public T merge(T entity) {
		T result = getEntityManager().merge(entity); 
		
		return result;
	}

	public void makeTransient(T entity) {
		getEntityManager().remove(entity);
	}

	public void checkVersion(T entity, boolean forceUpdate) {
		getEntityManager().lock(entity, forceUpdate ? LockModeType.OPTIMISTIC_FORCE_INCREMENT : LockModeType.OPTIMISTIC);	
	}
	
	public T findById(ID id) {
		return findById(id, LockModeType.NONE);
	}

	public T findById(ID id, LockModeType lock) {
		return getEntityManager().find(entityClass, id, lock);
	}

	public T findReferenceById(ID id) {
		return getEntityManager().getReference(entityClass, id);
	}

	public List<T> findAll() {
		CriteriaQuery<T> c = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
		c.select(c.from(entityClass));
		
		return getEntityManager().createQuery(c).getResultList();
	}

	public Long getCount() {
		CriteriaQuery<Long> c = getEntityManager().getCriteriaBuilder().createQuery(Long.class);
		c.select(getEntityManager().getCriteriaBuilder().count(c.from(entityClass)));
	
		return getEntityManager().createQuery(c).getSingleResult();
	}
}

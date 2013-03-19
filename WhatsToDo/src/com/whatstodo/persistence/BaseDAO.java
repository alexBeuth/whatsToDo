package com.whatstodo.persistence;

import java.util.List;

public interface BaseDAO<T> {
	
	public void open();
	
	public void close();
	
	public T getById(long id);
	
	public List<T> findAll();
	
	public T create(T entity);
	
	public T read(long id);
	
	public T update(T entity);
	
	public void delete(T entity);
	
	public void deleteAll();

}

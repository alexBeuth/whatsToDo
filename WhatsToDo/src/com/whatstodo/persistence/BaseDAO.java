package com.whatstodo.persistence;

import java.util.List;

public interface BaseDAO<T> {
	
	void open();
	
	void close();
	
	T getById(long id);
	
	List<T> findAll();
	
	T create(T entity);
	
	T read(long id);
	
	T update(T entity);
	
	void delete(T entity);

}

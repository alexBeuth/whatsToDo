package com.whatstodo.persistence;

import java.util.List;

import com.whatstodo.models.Task;

public interface TaskDAO extends BaseDAO<Task>{
	
	public List<Task> findByListId(long id);
	
	public void deleteByListId(long id);

}

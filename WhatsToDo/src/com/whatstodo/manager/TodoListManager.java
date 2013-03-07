package com.whatstodo.manager;

import android.content.Context;
import android.database.SQLException;

import com.whatstodo.WhatsToDo;
import com.whatstodo.models.List;
import com.whatstodo.models.Task;
import com.whatstodo.persistence.TodoListDAO;
import com.whatstodo.persistence.TodoListDAOSqlite;

public class TodoListManager {

	private static TodoListManager instance;

	private TodoListDAO todoListDao;

	private TodoListManager() {

		// TODO Maybe get context via argument and instantiate in WhatsToDo.java
		Context context = WhatsToDo.getContext();
		todoListDao = new TodoListDAOSqlite(context);
	}

	public static TodoListManager getInstance() {

		if (instance == null) {
			instance = new TodoListManager();
		}
		return instance;
	}

	public List save(List listToSave){
		return save(listToSave, false);
	}
	
	public List save(List listToSave, boolean eager) {

		// We could offer two methods for saving and creating instead... Don't
		// know yet

		try {
			todoListDao.open();
			List listToReturn = todoListDao.getById(listToSave.getId());
			if (listToReturn == null) {
				listToReturn = todoListDao.create(listToSave);
			} else {
				listToReturn = todoListDao.update(listToSave);
			}

			if (eager) {
				for (Task task : listToSave) {
					TaskManager.getInstance().save(task);
				}
				listToReturn.addAll(TaskManager.getInstance().findByListId(listToReturn.getId()));
			}
			return listToReturn;
		} catch (SQLException e) {
			// TODO Define exception
			throw new RuntimeException(e);
		} finally {
			todoListDao.close();
		}
	}
	
	public List load(long id){
		return load(id, false);
	}

	public List load(long id, boolean eager) {
		try {
			todoListDao.open();
			List list = todoListDao.getById(id);
			
			if (eager) {
				list.addAll(TaskManager.getInstance().findByListId(id));
			}
			return list;
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		} finally {
			todoListDao.close();
		}
	}

	//TODO Should we delete all task automatically or let the client handle this?
	public void delete(List list) {
		try {
			todoListDao.open();
			todoListDao.delete(list);
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		} finally {
			todoListDao.close();
		}
	}
	
	public java.util.List<List> findAll() {
		try {
			todoListDao.open();
			return todoListDao.findAll();
		} finally {
			todoListDao.close();
		}
	}

}

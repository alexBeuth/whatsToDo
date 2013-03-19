package com.whatstodo.manager;

import java.util.Date;
import java.util.Observable;

import android.content.Context;
import android.database.SQLException;

import com.whatstodo.WhatsToDo;
import com.whatstodo.models.HistoryEvent;
import com.whatstodo.models.HistoryEvent.Action;
import com.whatstodo.models.HistoryEvent.Type;
import com.whatstodo.models.List;
import com.whatstodo.persistence.TodoListDAO;
import com.whatstodo.persistence.TodoListDAOSqlite;

public class TodoListManager extends Observable {

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
			instance.addObserver(HistoryEventManager.getInstance());
		}
		return instance;
	}

	public List save(List listToSave) {
		return save(listToSave, false);
	}

	public List save(List listToSave, boolean eager) {

		try {
			todoListDao.open();
			List listToReturn = todoListDao.getById(listToSave.getId());
			if (listToReturn == null) {
				listToReturn = todoListDao.create(listToSave);
				addToHistory(Action.Created, listToReturn.getId());
			} else {
				listToReturn = todoListDao.update(listToSave);
				addToHistory(Action.Updated, listToReturn.getId());
			}

			if (eager) {
				TaskManager.getInstance().saveAll(listToSave);
				// reset size because adding tasks will increase the size
				listToReturn.setSize(0);
				listToReturn.addAll(TaskManager.getInstance().findByListId(
						listToReturn.getId()));
			}
			return listToReturn;
		} catch (SQLException e) {
			// TODO Define exception
			throw new RuntimeException(e);
		} finally {
			todoListDao.close();
		}
	}

	public List load(long id) {
		return load(id, false);
	}

	public List load(long id, boolean eager) {
		try {
			todoListDao.open();
			List list = todoListDao.getById(id);
			if (eager) {
				// reset size because adding tasks will increase the size
				list.setSize(0);
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

	public void delete(List list) {
		try {
			todoListDao.open();

			TaskManager.getInstance().deleteByListId(list.getId());
			todoListDao.delete(list);

			addToHistory(Action.Deleted, list.getId());
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

	/**
	 * This method will delete all Todos and Tasks entities from the database
	 * and replace them with the new content. No history will be written.
	 * 
	 * @param todos
	 * @return the newly inserted todos, lazy loaded (no tasks)
	 */
	public java.util.List<List> replaceAll(java.util.List<List> todos) {

		try {
			todoListDao.open();

			TaskManager.getInstance().deleteAll();
			todoListDao.deleteAll();

			for (List todo : todos) {
				todoListDao.create(todo);
				TaskManager.getInstance().saveAll(todo);
			}

			return findAll();
		} finally {
			todoListDao.close();
		}
	}

	/**
	 * Will replace the todo with the one with the same id. No History will be
	 * created. But otherwise it has the same effect as an eager save.
	 * 
	 * @param todo
	 * @return An eager loaded todo
	 */
	public List replace(List todo) {

		try {
			todoListDao.open();

			List oldTodo = todoListDao.getById(todo.getId());
			todoListDao.delete(oldTodo);
			TaskManager.getInstance().deleteByListId(oldTodo.getId());

			todoListDao.create(todo);
			TaskManager.getInstance().saveAll(todo);

			return load(todo.getId(), true);
		} finally {
			todoListDao.close();
		}
	}

	private void addToHistory(Action action, long uid) {
		HistoryEvent change = new HistoryEvent();
		change.setTimeOfChange(new Date());
		change.setType(Type.Todo);
		change.setAction(action);
		change.setEntityUid(uid);
		setChanged();
		notifyObservers(change);
	}
}

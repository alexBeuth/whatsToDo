package com.whatstodo.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import android.content.Context;
import android.database.SQLException;

import com.whatstodo.WhatsToDo;
import com.whatstodo.models.HistoryEvent;
import com.whatstodo.models.HistoryEvent.Action;
import com.whatstodo.models.HistoryEvent.Type;
import com.whatstodo.models.Task;
import com.whatstodo.persistence.TaskDAO;
import com.whatstodo.persistence.TaskDAOSqlite;

public class TaskManager extends Observable {

	private static TaskManager instance;

	private TaskDAO taskDao;

	private TaskManager() {

		// TODO Maybe get context via argument and instantiate in WhatsToDo.java
		Context context = WhatsToDo.getContext();
		taskDao = new TaskDAOSqlite(context);
	}

	public static TaskManager getInstance() {

		if (instance == null) {
			instance = new TaskManager();
			instance.addObserver(HistoryEventManager.getInstance());
		}
		return instance;
	}

	public Task save(Task taskToSave) {
		try {
			taskDao.open();
			Task taskToReturn = taskDao.getById(taskToSave.getId());
			if (taskToReturn == null) {
				taskToReturn = taskDao.create(taskToSave);
				addToHistory(Action.Created, taskToReturn.getId(), taskToReturn.getListId());
			} else {
				taskToReturn = taskDao.update(taskToSave);
				addToHistory(Action.Updated, taskToReturn.getId(), taskToReturn.getListId());
			}
			return taskToReturn;
		} catch (SQLException e) {
			// TODO Define exception
			throw new RuntimeException(e);
		} finally {
			taskDao.close();
		}

	}

	public Task load(long id) {
		try {
			taskDao.open();
			Task task = taskDao.getById(id);
			return task;
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		} finally {
			taskDao.close();
		}
	}

	public void delete(Task task) {
		try {
			taskDao.open();
			taskDao.delete(task);
			addToHistory(Action.Read, task.getId(), task.getListId());
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		} finally {
			taskDao.close();
		}
	}

	public List<Task> findAll() {
		try {
			taskDao.open();
			return taskDao.findAll();
		} finally {
			taskDao.close();
		}
	}

	public List<Task> findByListId(long listId) {
		try {
			taskDao.open();
			List<Task> byListId = taskDao.findByListId(listId);
			return byListId;
		} finally {
			taskDao.close();
		}
	}

	/**
	 * This method will create a bunch of tasks without saving a history.
	 * 
	 * @param tasks
	 * @return the newly inserted tasks
	 */
	protected List<Task> saveAll(List<Task> tasks) {

		try {
			taskDao.open();
			List<Task> result = new ArrayList<Task>();
			for (Task task : tasks) {

				Task found = taskDao.getById(task.getId());
				if (found == null) {
					found = taskDao.create(task);
				} else {
					found = taskDao.update(task);
				}
				result.add(found);
			}

			return result;
		} finally {
			taskDao.close();
		}
	}

	protected void deleteAll() {
		try {
			taskDao.open();
			taskDao.deleteAll();
		} finally {
			taskDao.close();
		}
	}

	protected void deleteByListId(long listId) {
		try {
			taskDao.open();
			taskDao.deleteByListId(listId);
		} finally {
			taskDao.close();
		}
	}

	private void addToHistory(Action action, long uid, long parentUid) {

		HistoryEvent history = new HistoryEvent();
		history.setTimeOfChange(new Date().getTime());
		history.setType(Type.Task);
		history.setAction(action);
		history.setEntityUid(uid);
		history.setParentEntityUid(parentUid);
		setChanged();
		notifyObservers(history);
	}
}

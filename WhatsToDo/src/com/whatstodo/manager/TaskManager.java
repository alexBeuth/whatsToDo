package com.whatstodo.manager;

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

public class TaskManager extends Observable{

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

		// We could offer two methods for saving and creating instead... Don't
		// know yet

		try {
			taskDao.open();
			Task taskToReturn = taskDao.getById(taskToSave.getId());
			if (taskToReturn == null) {
				taskToReturn = taskDao.create(taskToSave);
				addToHistory(Action.Created, taskToReturn.getId());
			} else {
				taskToReturn = taskDao.update(taskToSave);
				addToHistory(Action.Updated, taskToReturn.getId());
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
			addToHistory(Action.Read, task.getId());
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
	
	private void addToHistory(Action action, long uid) {
		HistoryEvent history = new HistoryEvent();
		history.setTimeOfChange(new Date());
		history.setType(Type.Task);
		history.setAction(action);
		history.setEntityUid(uid);
		setChanged();
		notifyObservers(history);
	}
}

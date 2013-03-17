package com.whatstodo.manager;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.database.SQLException;

import com.whatstodo.WhatsToDo;
import com.whatstodo.models.HistoryEvent;
import com.whatstodo.models.HistoryEvent.Type;
import com.whatstodo.persistence.HistoryEventDAO;
import com.whatstodo.persistence.HistoryEventDAOSqlite;

public class HistoryEventManager implements Observer {

	private static HistoryEventManager instance;

	private HistoryEventDAO historyEventDAO;

	private HistoryEventManager() {

		Context context = WhatsToDo.getContext();
		historyEventDAO = new HistoryEventDAOSqlite(context);
	}

	public static HistoryEventManager getInstance() {

		if (instance == null) {
			instance = new HistoryEventManager();
		}
		return instance;
	}

	public HistoryEvent save(HistoryEvent event) {

		try {
			historyEventDAO.open();
			HistoryEvent result = historyEventDAO.getById(event.getId());
			if (result == null) {
				result = historyEventDAO.create(event);
			} else {
				result = historyEventDAO.update(event);
			}
			return result;
		} catch (SQLException e) {
			// TODO Define exception
			throw new RuntimeException(e);
		} finally {
			historyEventDAO.close();
		}
	}
	
	public HistoryEvent load(long id) {
		try {
			historyEventDAO.open();
			HistoryEvent event = historyEventDAO.getById(id);
			return event;
		} catch (SQLException e) {
			// TODO: handle exception
			throw new RuntimeException(e);
		} finally {
			historyEventDAO.close();
		}
	}
	
	public List<HistoryEvent> loadAll() {
		
		try {
			historyEventDAO.open();
			return historyEventDAO.findAll();
			
		} finally {
			historyEventDAO.close();
		}
	}
	
	public List<HistoryEvent> find(Type type, Date after, Long entityUid,
			Boolean isSynchronized) {
		
		try {
			historyEventDAO.open();
			return historyEventDAO.find(type, after, entityUid, isSynchronized);
		} finally {
			historyEventDAO.close();
		}
	}

	@Override
	public void update(Observable observable, Object data) {

		if (data instanceof HistoryEvent) {
			HistoryEvent event = (HistoryEvent) data;
			save(event);
		}

	}

}

package com.whatstodo.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.whatstodo.models.HistoryEvent;
import com.whatstodo.models.HistoryEvent.Action;
import com.whatstodo.models.HistoryEvent.Type;

public class HistoryEventDAOSqlite implements HistoryEventDAO {

	private final String idClause = DatabaseHelper.HISTORY_COLUMN_ID + " = ?";

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	public HistoryEventDAOSqlite(Context context) {
		dbHelper = new DatabaseHelper(context);

	}

	@Override
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	@Override
	public void close() {
		dbHelper.close();
	}

	@Override
	public HistoryEvent getById(long id) {

		Cursor cursor = db.query(DatabaseHelper.HISTORY_TABLE, null, idClause,
				new String[] { Long.toString(id) }, null, null, null);

		HistoryEvent event = null;
		if (cursor.moveToFirst()) {
			event = cursorToHistoryEvent(cursor);
		}
		cursor.close();
		return event;
	}

	@Override
	public List<HistoryEvent> findAll() {
		Cursor cursor = db.query(DatabaseHelper.HISTORY_TABLE, null, null,
				null, null, null, null);
		java.util.List<HistoryEvent> list = cursorToList(cursor);
		cursor.close();
		return list;
	}

	@Override
	public HistoryEvent create(HistoryEvent entity) {

		ContentValues values = historyEventToContentValues(entity);

		long eventId = db.insert(DatabaseHelper.HISTORY_TABLE, null, values);
		return getById(eventId);
	}

	@Override
	public HistoryEvent read(long id) {
		return getById(id);
	}

	@Override
	public HistoryEvent update(HistoryEvent entity) {
		ContentValues values = historyEventToContentValues(entity);

		int updatedRows = db.update(DatabaseHelper.HISTORY_TABLE, values,
				idClause, new String[] { Long.toString(entity.getId()) });
		if (updatedRows > 1) {
			throw new SQLException("More than one row with the same _ID");
		}

		return getById(entity.getId());
	}

	@Override
	public void delete(HistoryEvent entity) {
		throw new UnsupportedOperationException(
				"Cannot delete an important history event!");

	}

	@Override
	public void deleteAll() {
		throw new UnsupportedOperationException(
				"Cannot delete an important history event!");
	}

	@Override
	public List<HistoryEvent> find(Type type, Action action, Date after,
			Long entityUid, Long parentEntityUid, Boolean isSynchronized) {

		List<String> selectionArgsList = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		if (type != null) {
			builder.append(DatabaseHelper.HISTORY_COLUMN_TYPE + " = ?");
			selectionArgsList.add(type.toString());
		}
		if (action != null) {
			if (builder.length() > 0) {
				builder.append(" AND ");
			}
			builder.append(DatabaseHelper.HISTORY_COLUMN_ACTION + " = ?");
			selectionArgsList.add(action.toString());
		}
		if (after != null) {
			if (builder.length() > 0) {
				builder.append(" AND ");
			}
			builder.append(DatabaseHelper.HISTORY_COLUMN_TIME_OF_CHANGE
					+ " >= ?");
			selectionArgsList.add(Long.toString(after.getTime()));
		}
		if (entityUid != null) {
			if (builder.length() > 0) {
				builder.append(" AND ");
			}
			builder.append(DatabaseHelper.HISTORY_COLUMN_ENTITY_UID + " = ?");
			selectionArgsList.add(entityUid.toString());
		}
		if (parentEntityUid != null) {
			if (builder.length() > 0) {
				builder.append(" AND ");
			}
			builder.append(DatabaseHelper.HISTORY_COLUMN_PARENT_ENTITY_UID
					+ " = ?");
			selectionArgsList.add(parentEntityUid.toString());
		}
		if (isSynchronized != null) {
			if (builder.length() > 0) {
				builder.append(" AND ");
			}
			builder.append(DatabaseHelper.HISTORY_COLUMN_IS_SYNCHRONIZED
					+ " = ?");
			selectionArgsList.add(isSynchronized ? "1" : "0");
		}

		String selection = builder.toString();
		String[] selectionArgs = selectionArgsList
				.toArray(new String[selectionArgsList.size()]);
		Cursor cursor = db.query(DatabaseHelper.HISTORY_TABLE, null, selection,
				selectionArgs, null, null, null);
		java.util.List<HistoryEvent> list = cursorToList(cursor);
		cursor.close();
		return list;
	}

	private ContentValues historyEventToContentValues(HistoryEvent entity) {
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.HISTORY_COLUMN_ACTION, entity.getAction()
				.toString());
		values.put(DatabaseHelper.HISTORY_COLUMN_TYPE, entity.getType()
				.toString());
		values.put(DatabaseHelper.HISTORY_COLUMN_ENTITY_UID,
				entity.getEntityUid());
		values.put(DatabaseHelper.HISTORY_COLUMN_PARENT_ENTITY_UID,
				entity.getParentEntityUid());
		values.put(DatabaseHelper.HISTORY_COLUMN_IS_SYNCHRONIZED,
				entity.isSynchronized());
		values.put(DatabaseHelper.HISTORY_COLUMN_TIME_OF_CHANGE, entity
				.getTimeOfChange());
		return values;
	}

	private List<HistoryEvent> cursorToList(Cursor cursor) {
		List<HistoryEvent> resultList = new ArrayList<HistoryEvent>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			resultList.add(cursorToHistoryEvent(cursor));
		}
		return resultList;
	}

	private HistoryEvent cursorToHistoryEvent(Cursor cursor) {
		HistoryEvent event = new HistoryEvent();
		event.setId(cursor.getLong(cursor
				.getColumnIndex(DatabaseHelper.HISTORY_COLUMN_ID)));
		event.setAction(Action.valueOf(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.HISTORY_COLUMN_ACTION))));
		event.setType(Type.valueOf(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.HISTORY_COLUMN_TYPE))));
		event.setEntityUid(cursor.getLong(cursor
				.getColumnIndex(DatabaseHelper.HISTORY_COLUMN_ENTITY_UID)));
		event.setParentEntityUid(cursor.getLong(cursor
				.getColumnIndex(DatabaseHelper.HISTORY_COLUMN_PARENT_ENTITY_UID)));
		event.setSynchronized(cursor.getLong(cursor
				.getColumnIndex(DatabaseHelper.HISTORY_COLUMN_IS_SYNCHRONIZED)) > 0);
		event.setTimeOfChange(cursor.getLong(cursor
				.getColumnIndex(DatabaseHelper.HISTORY_COLUMN_TIME_OF_CHANGE)));
		return event;
	}

}

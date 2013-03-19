package com.whatstodo.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.whatstodo.models.Priority;
import com.whatstodo.models.Task;

//TODO Exception handling and logging
public class TaskDAOSqlite implements TaskDAO {

	private final String idClause = DatabaseHelper.TASK_COLUMN_ID + " = ?";
	private final String listIdClause = DatabaseHelper.TASK_COLUMN_LIST_ID
			+ "= ?";

	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;

	public TaskDAOSqlite(Context context) {
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
	public Task getById(long id) {

		Cursor cursor = db.query(DatabaseHelper.TASK_TABLE, null,
				DatabaseHelper.TASK_COLUMN_ID + " = " + id, null, null, null,
				null);
		Task task = null;
		if (cursor.moveToFirst()) {
			task = cursorToTask(cursor);
		}
		cursor.close();
		return task;
	}

	@Override
	public List<Task> findAll() {

		Cursor cursor = db.query(DatabaseHelper.TASK_TABLE, null, null, null,
				null, null, null);

		List<Task> resultList = cursorToList(cursor);
		cursor.close();

		return resultList;
	}

	@Override
	public Task create(Task entity) {

		ContentValues values = taskToContentValues(entity);

		long taskId = db.insert(DatabaseHelper.TASK_TABLE, null, values);

		if (taskId < 0) {
			throw new RuntimeException("Cannot insert task into db");
		}
		return getById(taskId);
	}

	@Override
	public Task read(long id) {
		return getById(id);
	}

	@Override
	public Task update(Task entity) {

		ContentValues values = taskToContentValues(entity);

		int updatedRows = db.update(DatabaseHelper.TASK_TABLE, values,
				idClause, new String[] { Long.toString(entity.getId()) });
		if (updatedRows > 1) {
			throw new SQLException("More than one row with the same _ID");
		}

		return getById(entity.getId());
	}

	@Override
	public void delete(Task entity) {
		int deleted = db.delete(DatabaseHelper.TASK_TABLE, idClause,
				new String[] { Long.toString(entity.getId()) });
		if (deleted > 1) {
			throw new RuntimeException("Deleted more than one row with id: "
					+ entity.getId());
		}
	}

	@Override
	public void deleteAll(){
		db.delete(DatabaseHelper.TASK_TABLE, null, null);
	}
	
	@Override
	public void deleteByListId(long id) {
		db.delete(DatabaseHelper.TASK_TABLE, listIdClause,
				new String[] { Long.toString(id) });
	}
	
	@Override
	public List<Task> findByListId(long listId) {

		Cursor cursor = db.query(DatabaseHelper.TASK_TABLE, null, listIdClause,
				new String[] { Long.toString(listId) }, null, null, null);

		List<Task> resultList = cursorToList(cursor);
		cursor.close();

		return resultList;
	}

	private ContentValues taskToContentValues(Task entity) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.TASK_COLUMN_ADDRESS, entity.getAddress());
		values.put(DatabaseHelper.TASK_COLUMN_CALENDAR_CREATED,
				entity.isCalendarCreated());
		if (entity.getDate() != null) {
			values.put(DatabaseHelper.TASK_COLUMN_DATE, entity.getDate()
					.getTime());
		}
		values.put(DatabaseHelper.TASK_COLUMN_DONE, entity.isDone());
		values.put(DatabaseHelper.TASK_COLUMN_LIST_ID, entity.getListId());
		values.put(DatabaseHelper.TASK_COLUMN_NAME, entity.getName());
		values.put(DatabaseHelper.TASK_COLUMN_NOTICE, entity.getNotice());
		if (entity.getPriority() != null) {
			values.put(DatabaseHelper.TASK_COLUMN_PRIORITY, entity
					.getPriority().getId());
		}
		if (entity.getReminder() != null) {
			values.put(DatabaseHelper.TASK_COLUMN_REMINDER, entity
					.getReminder().getTime());
		}
		return values;
	}

	private List<Task> cursorToList(Cursor cursor) {
		List<Task> resultList = new ArrayList<Task>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			resultList.add(cursorToTask(cursor));
		}
		return resultList;
	}

	private Task cursorToTask(Cursor cursor) {
		Task task = new Task();

		task.setId(cursor.getLong(cursor
				.getColumnIndex(DatabaseHelper.TASK_COLUMN_ID)));

		task.setName(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.TASK_COLUMN_NAME)));

		task.setAddress(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.TASK_COLUMN_ADDRESS)));

		//TODO dont save date as int.. we cant show dates before 1970 :/
		long date = cursor.getLong(cursor
				.getColumnIndex(DatabaseHelper.TASK_COLUMN_DATE));
		if (date > 0) {
			task.setDate(new Date(date));
		}

		long reminderDate = cursor.getLong(cursor
				.getColumnIndex(DatabaseHelper.TASK_COLUMN_REMINDER));
		if(reminderDate > 0) {
			task.setReminder(new Date(reminderDate));
		}

		task.setDone(cursor.getInt(cursor
				.getColumnIndex(DatabaseHelper.TASK_COLUMN_DONE)) > 0);

		task.setListId(cursor.getLong(cursor
				.getColumnIndex(DatabaseHelper.TASK_COLUMN_LIST_ID)));

		task.setNotice(cursor.getString(cursor
				.getColumnIndex(DatabaseHelper.TASK_COLUMN_NOTICE)));

		task.setPriority(Priority.fromId(cursor.getInt(cursor
				.getColumnIndex(DatabaseHelper.TASK_COLUMN_PRIORITY))));

		task.setCalendarCreated(cursor.getInt(cursor
				.getColumnIndex(DatabaseHelper.TASK_COLUMN_CALENDAR_CREATED)) > 0);

		return task;
	}

}

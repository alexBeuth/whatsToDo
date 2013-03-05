package com.whatstodo.persistence;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.whatstodo.models.List;
import com.whatstodo.models.Task;

public class TodoListDAOSqlite implements TodoListDAO {
	
	private final String idClause = DatabaseHelper.TASK_COLUMN_ID + " = ?";
	
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private Context context;

	public TodoListDAOSqlite(Context context) {
		dbHelper = new DatabaseHelper(context);
		this.context = context;

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
	public List getById(long id) {
		
		Cursor cursor = db.query(DatabaseHelper.TODOLIST_TABLE, null, idClause, new String[]{Long.toString(id)}, null, null, null);
		cursor.moveToFirst();
		
		List list = cursorToTodoList(cursor);
		
		cursor.close();
		return list;
	}

	@Override
	public java.util.List<List> findAll() {
		Cursor cursor = db.query(DatabaseHelper.TODOLIST_TABLE, null, null, null, null, null, null);
		return cursorToList(cursor);
	}

	@Override
	public List create(List entity) {
		
		ContentValues values = todoListToContentValues(entity);
		
		long listId = db.insert(DatabaseHelper.TODOLIST_TABLE, null, values);
		return getById(listId);
	}

	private ContentValues todoListToContentValues(List entity) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.TODOLIST_COLUMN_NAME, entity.getName());
		values.put(DatabaseHelper.TODOLIST_COLUMN_SIZE, entity.size());
		return values;
	}

	@Override
	public List read(long id) {
		return getById(id);
	}

	@Override
	public List update(List entity) {
		
		ContentValues values = todoListToContentValues(entity);
		int id = db.update(DatabaseHelper.TODOLIST_TABLE, values, idClause, new String[] {Long.toString(entity.getId())});
		
		return getById(id);
	}

	@Override
	public void delete(List entity) {
		db.delete(DatabaseHelper.TODOLIST_TABLE, idClause,
				new String[] { Long.toString(entity.getId()) });

	}
	
	private java.util.List<List> cursorToList(Cursor cursor) {
		java.util.List<List> resultList = new ArrayList<List>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			resultList.add(cursorToTodoList(cursor));
		}
		return resultList;
	}
	
	private List cursorToTodoList(Cursor cursor) {

		List list = new List();
		list.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.TODOLIST_COLUMN_ID)));
		list.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TODOLIST_COLUMN_NAME)));
		//TODO size
		//TODO LAZY LOADING
		//TODO get this via manager
		TaskDAOSqlite taskDAO = new TaskDAOSqlite(context);
		taskDAO.open();
		java.util.List<Task> findByListId = taskDAO.findByListId(list.getId());
		taskDAO.close();
		list.addAll(findByListId);
		return list;
	}
}

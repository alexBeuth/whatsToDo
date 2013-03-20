package com.whatstodo.persistence;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.whatstodo.models.List;

//TODO Exception handling and logging
public class TodoListDAOSqlite implements TodoListDAO {
	
	private final String idClause = DatabaseHelper.TODOLIST_COLUMN_ID + " = ?";
	
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	public TodoListDAOSqlite(Context context) {
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
	public List getById(long id) {
		
		Cursor cursor = db.query(DatabaseHelper.TODOLIST_TABLE, null, idClause, new String[]{Long.toString(id)}, null, null, null);
		
		List list = null;
		if (cursor.moveToFirst()) {
			list = cursorToTodoList(cursor);
		}
		
		cursor.close();
		return list;
	}

	@Override
	public java.util.List<List> findAll() {
		Cursor cursor = db.query(DatabaseHelper.TODOLIST_TABLE, null, null, null, null, null, null);
		java.util.List<List> list = cursorToList(cursor);
		cursor.close();
		return list;
	}

	@Override
	public List create(List entity) {
		
		ContentValues values = todoListToContentValues(entity);
		
		long listId = db.insert(DatabaseHelper.TODOLIST_TABLE, null, values);
		return getById(listId);
	}

	@Override
	public List read(long id) {
		return getById(id);
	}

	@Override
	public List update(List entity) {
		
		ContentValues values = todoListToContentValues(entity);
		int updatedRows = db.update(DatabaseHelper.TODOLIST_TABLE, values, idClause, new String[] {Long.toString(entity.getId())});
		if (updatedRows > 1) {
			throw new SQLException("More than one row with the same _ID");
		}
		
		return getById(entity.getId());
	}

	@Override
	public void delete(List entity) {
		db.delete(DatabaseHelper.TODOLIST_TABLE, idClause,
				new String[] { Long.toString(entity.getId()) });

	}
	
	@Override
	public void deleteAll(){
		db.delete(DatabaseHelper.TODOLIST_TABLE, null, null);
	}
	
	private java.util.List<List> cursorToList(Cursor cursor) {
		java.util.List<List> resultList = new ArrayList<List>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			resultList.add(cursorToTodoList(cursor));
		}
		return resultList;
	}
	
	private ContentValues todoListToContentValues(List entity) {
		ContentValues values = new ContentValues();
		
		if(entity.getId() != 0) {
			values.put(DatabaseHelper.TODOLIST_COLUMN_ID, entity.getId());
		}
		values.put(DatabaseHelper.TODOLIST_COLUMN_NAME, entity.getName());
		values.put(DatabaseHelper.TODOLIST_COLUMN_SIZE, entity.size());
		return values;
	}
	
	private List cursorToTodoList(Cursor cursor) {

		List list = new List();
		list.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.TODOLIST_COLUMN_ID)));
		list.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TODOLIST_COLUMN_NAME)));
		list.setSize(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TODOLIST_COLUMN_SIZE)));
		return list;
	}
}

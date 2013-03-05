package com.whatstodo.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	//Database
	private static final String DATABASE_NAME = "whatsToDo.db";
	private static final int DATABASE_VERSION = 1;

	//TodoList Table
	public static final String TODOLIST_TABLE = "todolist";
	public static final String TODOLIST_COLUMN_ID = "_id";
	public static final String TODOLIST_COLUMN_NAME = "name";
	public static final String TODOLIST_COLUMN_SIZE = "size";
	
	//Task Table
	public static final String TASK_TABLE = "tasklist";
	public static final String TASK_COLUMN_ID = "_id";
	public static final String TASK_COLUMN_NAME = "name";
	public static final String TASK_COLUMN_DONE = "done";
	public static final String TASK_COLUMN_DATE = "date";
	public static final String TASK_COLUMN_REMINDER = "reminder";
	public static final String TASK_COLUMN_LIST_ID = "listid";
	public static final String TASK_COLUMN_NOTICE = "notice";
	public static final String TASK_COLUMN_PRIORITY = "priority";
	public static final String TASK_COLUMN_ADDRESS = "address";
	public static final String TASK_COLUMN_CALENDAR_CREATED = "calendarCreated";
	public static final String[] TASK_ALL_COLUMNS = {};
	
	
	//SQL Statements
	private static final String TODOLIST_CREATE = "CREATE TABLE "
			+ TODOLIST_TABLE
			+ "("
			+ TODOLIST_COLUMN_ID + " integer primary key autoincrement, "
			+ TODOLIST_COLUMN_NAME + " text,"
			+ TODOLIST_COLUMN_SIZE + " integer"
			+ ");";
	
	private static final String TASK_CREATE = "CREATE TABLE "
			+ TASK_TABLE
			+ "("
			+ TASK_COLUMN_ID + " integer primary key autoincrement, "
			+ TASK_COLUMN_LIST_ID + " integer,"
			+ TASK_COLUMN_NAME + " text,"
			+ TASK_COLUMN_DONE + " integer,"
			+ TASK_COLUMN_DATE + " integer,"
			+ TASK_COLUMN_REMINDER + " integer,"
			+ TASK_COLUMN_NOTICE + " text,"
			+ TASK_COLUMN_PRIORITY + " integer,"
			+ TASK_COLUMN_ADDRESS + " text,"
			+ TASK_COLUMN_CALENDAR_CREATED + " integer,"
			+ "FOREIGN KEY(" + TASK_COLUMN_LIST_ID + ") REFERENCES "+ TODOLIST_TABLE + "(" + TODOLIST_COLUMN_ID + ")"
			+ ");";

	public DatabaseHelper(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(TODOLIST_CREATE);
		db.execSQL(TASK_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		//TODO save data on upgrade
		db.execSQL("DROP TABLE IF EXISTS " + TODOLIST_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + TASK_CREATE);
		
		onCreate(db);
	}

}

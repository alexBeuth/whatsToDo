package com.whatstodo;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OptionalDataException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import android.content.Context;

import com.whatstodo.list.List;
import com.whatstodo.list.ListPersistence;

public class ListContainerPersistence {

	private final String FILENAME_LISTS = "listIds";
	private final String FILENAME_IDS = "freeIds";

	Context context = WhatsToDo.getContext();

	public void saveLists(Iterable<List> lists) {

		saveContainerIds();
		ListPersistence listPersistence = new ListPersistence();

		StringBuilder ids = new StringBuilder();
		for (List list : lists) {
			ids.append(list.getId()).append(",");
			listPersistence.saveList(list);
		}

		saveStringToFile(ids.toString(), FILENAME_LISTS);

	}

	private void saveStringToFile(String toSave, String filename) {
		BufferedOutputStream listsStream = null;
		try {
			listsStream = new BufferedOutputStream(context.openFileOutput(
					filename, Context.MODE_PRIVATE));
			listsStream.write(toSave.getBytes());

		} catch (IOException e) {
			//TODO
			throw new NoSuchElementException();
		} finally {
			if (listsStream != null) {
				closeQuietly(listsStream);
			}
		}
	}

	public Iterable<List> loadLists() {

		String ids = getFileAsString(FILENAME_LISTS);
		ListPersistence listPersistence = new ListPersistence();

		LinkedList<List> lists = new LinkedList<List>();
		if (ids.length() != 0) {
			for (String listId : Arrays.asList(ids.split(","))) {
				lists.add(listPersistence.loadList(Long.valueOf(listId)));
			}
		}
		return lists;

	}

	private String getFileAsString(String filename) {

		FileInputStream listsStream = null;

		try {
			File file = context.getFileStreamPath(filename);
			// file.delete();
			if (!file.exists()) {
				file.createNewFile();
			}

			listsStream = context.openFileInput(filename);

			byte[] buffer = new byte[1024];

			StringBuilder names = new StringBuilder();
			int byteCount = 0;
			while (byteCount != -1) {
				byteCount = listsStream.read(buffer, 0, 1024);
				if (byteCount > 0) {
					names.append(new String(buffer, 0, byteCount));
				}
			}

			return names.toString();

		} catch (IOException e) {
			throw new NoSuchElementException("Cannot find file: " + filename);
		} finally {
			if (listsStream != null) {
				closeQuietly(listsStream);
			}
		}
	}

	public long loadListId() {

		String idString = getFileAsString(FILENAME_IDS);
		if (idString == "") {
			return 0;
		}
		String listId = idString.split(",")[0];
		return Long.valueOf(listId);
	}

	public long loadTaskId() {

		String idString = getFileAsString(FILENAME_IDS);
		if (idString == "") {
			return 0;
		}
		String taskId = idString.split(",")[1];
		return Long.valueOf(taskId);
	}

	public void saveContainerIds() {
		long listId = ListContainer.getNextListId(true);
		long taskId = ListContainer.getNextTaskId(true);

		String toSave = listId + "," + taskId;
		saveStringToFile(toSave, FILENAME_IDS);
	}

	private void closeQuietly(Closeable out) {
		try {
			out.close();
		} catch (Exception e) {
			// Empty
		}
	}
}

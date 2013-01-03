package com.whatstodo.list;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;

import android.content.Context;

import com.whatstodo.WhatsToDo;

public class ListPersistence {

	Context context = WhatsToDo.getContext();

	public void saveList(List list) {
		String fileName = "list_" + list.getId();

		ObjectOutputStream listStream = null;
		try {
			listStream = new ObjectOutputStream(context.openFileOutput(fileName,
					Context.MODE_PRIVATE));
			listStream.writeObject(list);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (listStream != null) {
				closeQuietly(listStream);
			}
		}
	}

	public List loadList(long listId) {

		ObjectInputStream listStream = null;
		
		String filename = "list_" + listId;

		try {
			listStream = new ObjectInputStream(context.openFileInput(filename));
			List list = (List) listStream.readObject();
			return list;
		} catch (IOException e) {
			throw new NoSuchElementException("Cannot load list with ID: " + listId);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Corrupted list with ID: " + listId);
		} finally {
			if (listStream != null) {
				closeQuietly(listStream);
			}
		}
	}

	private void closeQuietly(Closeable out) {
		try {
			out.close();
		} catch (Exception e) {
			// Empty
		}
	}

}

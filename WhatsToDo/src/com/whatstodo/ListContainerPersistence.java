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

import android.content.Context;

import com.whatstodo.list.List;

public class ListContainerPersistence {

	private final String FILENAME = "all_lists";

	Context context = WhatsToDo.getContext();

	public void saveLists(Iterable<List> lists) {

		StringBuilder names = new StringBuilder();
		for (List list : lists) {
			names.append(list.getName()).append(",");
		}

		BufferedOutputStream listsStream = null;
		try {
			listsStream = new BufferedOutputStream(context.openFileOutput(
					FILENAME, Context.MODE_PRIVATE));
			listsStream.write(names.toString().getBytes());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (listsStream != null) {
				closeQuietly(listsStream);
			}
		}
	}

	public Iterable<List> loadLists() {

		FileInputStream listsStream = null;

		try {
			File file = context.getFileStreamPath(FILENAME);
			//file.delete();
			if (!file.exists()) {
				file.createNewFile();
			}

			listsStream = context.openFileInput(FILENAME);

			byte[] buffer = new byte[1024];

			StringBuilder names = new StringBuilder();
			int byteCount = 0;
			while (byteCount != -1) {
				byteCount = listsStream.read(buffer, 0, 1024);
				if (byteCount > 0) {
					names.append(new String(buffer, 0, byteCount));
				}
			}

			LinkedList<List> lists = new LinkedList<List>();
			if (names.length() != 0) {
				for (String listName : Arrays.asList(names.toString()
						.split(","))) {
					// TODO Load lists with content...
					lists.add(new List(listName));
				}
			}
			return lists;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (listsStream != null) {
				closeQuietly(listsStream);
			}
		}

		return null;
	}

	private void closeQuietly(Closeable out) {
		try {
			out.close();
		} catch (Exception e) {
			// Empty
		}
	}

}

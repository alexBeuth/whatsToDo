package com.whatstodo;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OptionalDataException;
import java.util.Arrays;

import android.app.Application;
import android.content.Context;

import com.whatstodo.list.List;

public class ListContainerPersistence {

	private final String FILENAME = "all_lists";
	
	Context context;
	
	public ListContainerPersistence(Context context) {
		this.context = context;
	}

	public void saveLists(Iterable<List> lists) {

		StringBuilder names = new StringBuilder();
		for (List list : lists) {
			names.append(list.getName()).append(",");
		}

		BufferedOutputStream listsStream = null;
		try {
			listsStream = new BufferedOutputStream(context.openFileOutput(FILENAME,
					Context.MODE_PRIVATE));
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

	public Iterable<String> loadLists(String listName) {

		FileInputStream listsStream = null;

		try {
			listsStream = context.openFileInput(listName);
			byte[] buffer = new byte[100];

			StringBuilder names = new StringBuilder();
			int byteOffset = 0;
			int byteCount = 0;
			while (byteCount != -1) {
				byteOffset += byteCount;
				byteCount = listsStream.read(buffer, byteOffset, 100);
				names.append(buffer.toString());
			}
			
			return Arrays.asList(names.toString().split(","));
			
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

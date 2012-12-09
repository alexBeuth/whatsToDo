package com.whatstodo.list;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

import android.content.Context;

public class ListPersistence {

	Context context;

	public ListPersistence(Context context) {
		this.context = context;
	}

	public void saveList(List list) {
		String fileName = list.getName();

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

	public List loadList(String listName) {

		ObjectInputStream listStream = null;

		try {
			listStream = new ObjectInputStream(context.openFileInput(listName));
			List list = (List) listStream.readObject();
			return list;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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

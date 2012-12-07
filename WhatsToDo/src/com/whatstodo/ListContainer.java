package com.whatstodo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

import android.app.Application;
import android.content.Context;

public class ListContainer extends Application {
	

    private static ListContainer instance = null;
 
    /**
     * Default-Konstruktor, der nicht außerhalb dieser Klasse
     * aufgerufen werden kann
     */
    private ListContainer() {}
 
    /**
     * Statische Methode, liefert die einzige Instanz dieser
     * Klasse zurück
     */
    public static ListContainer getInstance() {
        if (instance == null) {
            instance = new ListContainer();
        }
        return instance;
    }
    
    public void createList(String titel){
     //TODO
    }
    
	private void saveList(List list) {
		String FILENAME = list.getName();
		
		FileOutputStream fos;
		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream object = new ObjectOutputStream(fos);
			object.writeObject(list);
			object.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private List loadList(String fileName){
		
		FileInputStream fis;
		
		try {
			fis = openFileInput(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			List list = (List) ois.readObject();
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
		}
		
		return null;
	}
    
}


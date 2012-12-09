package com.whatstodo;

import java.util.ArrayList;

import android.app.Application;

import com.whatstodo.list.List;

public class ListContainer {
	
	private ArrayList<List> lists = new ArrayList<List>();
	
    private static ListContainer container = null;
 
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
        if (container == null) {
            container = new ListContainer();
        }
        return container;
    }
    
    public void addList(String name){

    	lists.add(new List(name));
    }

	public Iterable<List> getLists() {
		return lists;
	}

	public void setLists(ArrayList<List> lists) {
		this.lists = lists;
	}
    

    
}


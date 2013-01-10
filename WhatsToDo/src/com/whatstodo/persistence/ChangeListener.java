package com.whatstodo.persistence;

import com.whatstodo.models.List;

public class ChangeListener {
	
	public static void onListChange(List list) {
		
		new ListPersistence().saveList(list);
		new ListContainerPersistence().saveContainerIds();
		//TODO
		//list.setLastModified()
	}
	
	public static void onListContainerChange(Iterable<List> lists) {
		
		new ListContainerPersistence().saveLists(lists);
		//TODO
		//container.setLastModified()
	}

}
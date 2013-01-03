package com.whatstodo.persistence;

import com.whatstodo.list.List;

public class ChangeListener {
	
	public static void onListChange(List list) {
		
		new ListPersistence().saveList(list);
		//TODO
		//list.setLastModified()
	}
	
	public static void onListContainerChange(Iterable<List> lists) {
		
		new ListContainerPersistence().saveLists(lists);
		//TODO
		//container.setLastModified()
	}

}

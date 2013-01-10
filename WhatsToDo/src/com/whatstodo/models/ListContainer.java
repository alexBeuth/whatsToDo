package com.whatstodo.models;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.whatstodo.persistence.ChangeListener;
import com.whatstodo.persistence.ListContainerPersistence;

public class ListContainer {

	private static ArrayList<List> lists = new ArrayList<List>();

	private static ListContainer container = null;

	private static long nextAvailableListId;

	private static long nextAvailableTaskId;

	/**
	 * Default-Konstruktor, der nicht außerhalb dieser Klasse aufgerufen werden
	 * kann
	 */
	private ListContainer() {
	}

	/**
	 * Statische Methode, liefert die einzige Instanz dieser Klasse zurück
	 */
	public static ListContainer getInstance() {

		if (container == null) {
			container = new ListContainer();

			ListContainerPersistence persistence = new ListContainerPersistence();

			nextAvailableListId = persistence.loadListId();
			nextAvailableTaskId = persistence.loadTaskId();
			for (List list : persistence.loadLists()) {
				lists.add(list);
			}
			ChangeListener.onListContainerChange(lists);
		}
		return container;
	}

	public void addList(String name) {

		lists.add(new List(name));
		ChangeListener.onListContainerChange(lists);
	}

	public java.util.List<List> getLists() {
		return lists;
	}

	public void setLists(ArrayList<List> lists) {
		ListContainer.lists = lists;
	}

	public static long getNextListId() {

		return getNextListId(false);
	}

	public static long getNextListId(boolean noIncrement) {

		if (noIncrement) {
			return nextAvailableListId;
		} else {
			return nextAvailableListId++;
		}

	}

	public static long getNextTaskId() {

		return getNextTaskId(false);
	}

	public static long getNextTaskId(boolean noIncrement) {

		if (noIncrement) {
			return nextAvailableTaskId;
		} else {
			return nextAvailableTaskId++;
		}

	}

	public List getList(Long listId) {

		for (List list : lists) {
			if (list.getId() == listId) {
				return list;
			}
		}
		throw new NoSuchElementException("Cannot find list with ID: " + listId);
	}

	public List getList(String listName) {

		for (List list : lists) {
			if (list.getName().equals(listName)) {
				return list;
			}
		}
		throw new NoSuchElementException("Cannot find list with Name: "
				+ listName);
	}

	public void deleteList(long listId) {

		for (List list : lists) {
			if (list.getId() == listId) {
				lists.remove(list);
				ChangeListener.onListContainerChange(lists);
				return;
			}
		}
		throw new NoSuchElementException("Cannot find the right list");
	}
}
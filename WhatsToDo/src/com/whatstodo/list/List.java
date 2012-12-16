package com.whatstodo.list;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.whatstodo.ListContainer;
import com.whatstodo.task.Task;

/**
 * The list class represents a LIFO-Stack of tasks. It has the usual stack
 * methods push and pop plus some methods for convenience.
 * 
 * @author alex
 * 
 */
public class List implements Serializable, Iterable<Task> {


	private static final long serialVersionUID = -2889639373188534039L;
	
	transient private int size;
	transient private Node first;
	
	private class Node {

		Node next;
		Task task;
		
		public Node(Task task) {
			this.task = task;
		}
	}

	private long id;
	private String name;

	public List(String name) {
		
		id = ListContainer.getNextListId();
		this.name = name + " (" + id + ")";
		first = null;
		size = 0;
	}

	/**
	 * Is the list empty?
	 */
	public boolean isEmpty() {
		return first == null;
	}

	/**
	 * Return the number of items in the list.
	 */
	public int size() {
		return size;
	}

	/**
	 * Add the item to the list.
	 */
	public void push(Task item) {
		Node oldfirst = first;
		first = new Node(item);
		first.next = oldfirst;
		size++;
	}

	/**
	 * Delete and return the item most recently added to the list.
	 * 
	 * @throws java.util.NoSuchElementException
	 *             if stack is empty.
	 */
	public Task pop() {
		if (isEmpty())
			throw new NoSuchElementException("Stack underflow");
		Task item = first.task; // save item to return
		first = first.next; // delete first node
		size--;
		return item; // return the saved item
	}

	@Override
	public Iterator<Task> iterator() {
		return new ListIterator();
	}

	/**
	 * An iterator to the stack that iterates through the items in LIFO order.
	 * remove() is not implemented.
	 * 
	 * @author alex
	 * 
	 */
	private class ListIterator implements Iterator<Task> {
		private Node current = first;

		public boolean hasNext() {
			return current != null;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Task next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Task item = current.task;
			current = current.next;
			return item;
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}



	/**
	 * Serializes this object to the given stream.
	 * 
	 * @param s
	 *            the stream to write to
	 * @throws IOException
	 *             if the underlying stream fails
	 * @serialData the size of the list (int), followed by all the elements
	 *             (Object) in proper order
	 */
	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		s.writeInt(size);
		Node e = first;
		while (e != null) {
			s.writeObject(e.task);
			e = e.next;
		}
	}

	/**
	 * Deserializes this object from the given stream.
	 * 
	 * @param s
	 *            the stream to read from
	 * @throws ClassNotFoundException
	 *             if the underlying stream fails
	 * @throws IOException
	 *             if the underlying stream fails
	 * @serialData the size of the list (int), followed by all the elements
	 *             (Object) in proper order
	 */
	private void readObject(ObjectInputStream s) throws IOException,
			ClassNotFoundException {
		s.defaultReadObject();
		int i = s.readInt();
		while (--i >= 0)
			push(((Task) s.readObject()));
	}
	
	public void addTask(String name) {
		push(new Task(name));
	}
	
}

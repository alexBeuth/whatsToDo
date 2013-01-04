package com.whatstodo.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * The list class represents a LIFO-Stack of tasks. It has the usual stack
 * methods push and pop plus some methods for convenience.
 * 
 * @author alex
 * 
 */
public class List implements Serializable, java.util.List<Task> {


	private static final long serialVersionUID = -2889639373188534039L;
	
	transient private int size;
	transient private Task[] orderedTasks; 
	transient private Comparator<Task> comparator;
	private long id;
	private String name;

	public List(String name) {
		
		id = ListContainer.getNextListId();
		this.name = name + " (" + id + ")";
		orderedTasks = new Task[1];
	}
	
	public List(String name, Comparator<Task> comparator) {
		
		this(name);
		this.comparator = comparator;
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

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<Task> iterator() {
		return new ListIterator();
	}

	@Override
	public void add(int location, Task task) {
		// TODO Maybe call add(Task)
		throw new UnsupportedOperationException("List.add(int, Task)");
	}

	@Override
	public boolean add(Task task) {
		// double size of array if necessary
		if (size == orderedTasks.length - 1)
			resize(2 * orderedTasks.length);

		// Add item
		int i = size - 1;
		while (i >= 0 && greater(task, orderedTasks[i])) {
			orderedTasks[i + 1] = orderedTasks[i];
			i--;
		}
		orderedTasks[i + 1] = task;
		size++;
		return true;
	}

	@Override
	public boolean addAll(int location, Collection<? extends Task> collection) {
		// TODO Maybe call addAll(Collection)
		throw new UnsupportedOperationException("List.addAll(int, Collection)");
	}

	@Override
	public boolean addAll(Collection<? extends Task> collection) {
		for (Task newTask : collection) {
			add(newTask);
		}
		return true;
	}

	@Override
	public void clear() {
		for (int i = 0; i < size; i++) {
			orderedTasks[i] = null;
		}
		size = 0;
	}

	@Override
	public boolean contains(Object object) {
		return indexOf(object) > 0;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Task get(int location) {
		checkBound(location);
		return orderedTasks[location];
	}

	@Override
	public int indexOf(Object object) {

		for (int i = 0; i < size; i++) {
			if (orderedTasks[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object object) {

		for (int i = size; i >= 0; i++) {
			if (orderedTasks[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public java.util.ListIterator<Task> listIterator() {
		throw new UnsupportedOperationException("List.listIterator");
	}

	@Override
	public java.util.ListIterator<Task> listIterator(int location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task remove(int location) {

		if (isEmpty())
			throw new NoSuchElementException("Underflow");
		Task toReturn = orderedTasks[location];

		size--;
		for (int i = location; i < size; i++) {
			orderedTasks[i] = orderedTasks[i + 1];
		}
		orderedTasks[size + 1] = null; // avoid loitering and help with garbage
		// collection

		// resize array
		if ((size > 0) && (size == (orderedTasks.length - 1) / 4))
			resize(orderedTasks.length / 2);

		return toReturn;

	}

	@Override
	public boolean remove(Object object) {
		int indexOf = indexOf(object);
		if (indexOf >= 0) {
			remove(indexOf);
			return true;
		} else
			return false;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Task set(int location, Task object) {
		throw new UnsupportedOperationException("List.set(int, Task)");
	}

	@Override
	public java.util.List<Task> subList(int start, int end) {
		throw new UnsupportedOperationException("List.subList(int, int)");
	}

	@Override
	public Object[] toArray() {
		return orderedTasks;
	}

	@Override
	public <T> T[] toArray(T[] array) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean greater(Task i, Task j) {
		if (comparator == null) {
			return i.compareTo(j) > 0;
		} else {
			return comparator.compare(i, j) > 0;
		}
	}

	private void checkBound(int index) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException("In " + index + ", S" + size);
	}

	// helper function to double the size of the heap array
	private void resize(int capacity) {
		assert capacity > size;
		Task[] temp = new Task[capacity];
		for (int i = 0; i <= size; i++)
			temp[i] = orderedTasks[i];
		orderedTasks = temp;
	}

	/**
	 * An iterator to the stack that iterates through the items in LIFO order.
	 * remove() is not implemented.
	 * 
	 * @author alex
	 * 
	 */
	private class ListIterator implements Iterator<Task> {

		private int i;

		public ListIterator() {
			i = 0;
		}

		@Override
		public boolean hasNext() {
			return i < size;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Task next() {
			if (!hasNext())
				throw new NoSuchElementException();

			return orderedTasks[i++];
		}
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
		for (int i = 0; i < orderedTasks.length; i++) {
			s.writeObject(orderedTasks[i]);
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
		orderedTasks = new Task[1];
		while (--i >= 0)
			add(((Task) s.readObject()));
	}

	public void addTask(String name) {
		add(new Task(name));
	}

	public Task getTask(long taskId) {

		for (Task task : this) {
			if ( task.getId() == taskId) {
				return task;
			}
		}
		throw new NoSuchElementException("Cannot find task with ID: " + taskId);
	}
}

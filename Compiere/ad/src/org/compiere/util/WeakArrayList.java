package org.compiere.util;

import java.lang.ref.*;
import java.util.*;
/**
 * 
 * @author dzhao
 * implements a basic "weak" array so that if only reference to the object is this array, then that object is gc'able
 * This is primarily used in cache situation.
 * TODO: may need to synchronize method calls
 * @param <T>
 */
public class WeakArrayList<T> {
	private ArrayList<WeakReference<T>> array;
	private  ReferenceQueue<T> queue;
	/**	Logger			*/
	protected static final CLogger	log = CLogger.getCLogger(WeakArrayList.class);

	public WeakArrayList() {
		array = new ArrayList<WeakReference<T>>();
		queue = new ReferenceQueue<T>(); 
	}
	public boolean add(T value) {
		expunge();
		return array.add(new WeakReference<T>(value, queue));
	}
	public int size() {
		expunge();
		return array.size();
	}
	
	public boolean contains(T value) {
		expunge();
		for(int i=0; i<array.size(); i++) {
			if(array.get(i).get().equals(value))
				return true;
		}
		return false;
	}
	//NOTE: get and remove is all called when size is called. By then expunge is already done, so no
	//need to expunge in get, remove. There may be slight problem of a WeakReference gets garbage collected and
	//therefore get() would return null. This would be handled outisde of this
	public T get(int i) {
		//expunge();
		return array.get(i).get();
	}
	
	public void remove(int i) {
		//no need to expunge since remove is always called when expunge is called in size -- expunge();
		array.remove(i);
	}
	
	private void expunge() {
		Reference<? extends T> r;
		while((r = queue.poll()) != null) {
			log.info("Remove cache:"+ r.get());
			array.remove(r);
		}
	}
}

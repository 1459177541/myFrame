package util;

import java.util.*;
import java.util.stream.Stream;

/**
 * 分页列表
 * @author 杨星辰
 *
 * @param <E>
 */
public class PageList<E> implements List<List<E>>{

	private List<E> list;
	private int thisPage;
	private int pageSize;
	private int pageSum;

	private void init(int pageSize) {
		if (0>=pageSize) {
			throw new RuntimeException("每页长度为非正数");
		}
		this.pageSize = pageSize;
		pageSum = 0;
		thisPage = 0;
	}

	public PageList(int pageSize) {
		init(pageSize);
		list = new ArrayList<>(128);
	}

	public PageList(int pageSize, List<E> list) {
		init(pageSize);
		this.list = list;
		computePage(true);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		computePage(true);
	}

	public int getThisPage() {
		return thisPage;
	}
	
	public List<E> getData(){
		return list;
	}

	/**
	 * 得到总页数
	 * @return 总页数
	 */
	public int getPageSum() {
		return pageSum;
	}

	private void computePage(boolean execute) {
		if (!execute) {
			return;
		}
		pageSum = (list.size()-1)/pageSize+1;
		if (thisPage>pageSum) {
			thisPage = pageSum;
		}
	}
	
	/**
	 * 请使用sortElement
	 */
	@Override
	@Deprecated
	public void sort(Comparator<? super List<E>> c) {
	}
	
	public void sortElement(Comparator<? super E> c) {
		list.sort(c);
	}

	
	
	
	
	
	@Override
	public boolean remove(Object o) {
		boolean flag = list.remove(o);
		computePage(flag);
		return flag;
	}

	public boolean removeElement(Object e) {
		boolean flag = list.remove(e);
		computePage(flag);
		return flag;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean flag = list.removeAll(c);
		computePage(flag);
		return flag;
	}

	/**
	 * 请使用removeElement
	 */
	@Override
	@Deprecated
	public List<E> remove(int index) {
		list.remove(index);
		return null;
	}

	public E removeElement(int index) {
		E e = list.remove(index);
		computePage(null != e);
		return e;
	}
	
	
	
	
	
	
	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}
	
	public int pageOf(Object o) {
		return indexOf(o)/pageSize+1;
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}
	
	public int lastPageOf(Object o) {
		return lastIndexOf(o)/pageSize+1;
	}
	
	
	
	
	
	
	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public int size() {
		return list.size();
	}
	
	
	
	
	
	
	/**
	 * 请使用streamElement
	 */
	@Override
	@Deprecated
	public Stream<List<E>> stream() {
		return null;
	}
	
	public Stream<E> elementStream(){
		return list.stream();
	}

	
	
	
	
	@Override
	public ListIterator<List<E>> listIterator() {
		return new ListItr(pageSize);
	}

	@Override
	public ListIterator<List<E>> listIterator(int index) {
		return new ListItr(pageSize, index);
	}
	
	@Override
	public Iterator<List<E>> iterator() {
		return listIterator();
	}

	private class ListItr implements ListIterator<List<E>> {

		private int thisPage;
		private int pageSum;
		private int pageSize;
		private ListIterator<E> iterator;
		
		public ListItr(int pageSize) {
			this(pageSize, 1);
		}
		
		public ListItr(int pageSize, int index) {
			this.pageSize = pageSize;
			thisPage = index;
			pageSum = getPageSum();
			iterator = list.listIterator((index-1)*this.pageSize);
		}
		
		private List<E> get(){
			List<E> list = new ArrayList<>(pageSize);
			for(int i = 0; i<pageSize; i++) {
				if(iterator.hasNext()) {
					list.add(iterator.next());
				}
			}
			return list;
		}
		
		@Override
		public boolean hasNext() {
			return thisPage<pageSum;
		}

		@Override
		public List<E> next() {
			thisPage++;
			return get();
		}

		@Override
		public boolean hasPrevious() {
			return thisPage>1;
		}

		@Override
		public List<E> previous() {
			thisPage--;
			for(int i = 0; i<pageSize;i++) {
				iterator.previous();
			}
			return get();
		}

		@Override
		public int nextIndex() {
			return hasNext()
						? thisPage+1
						: pageSum;
		}

		@Override
		public int previousIndex() {
			return hasPrevious()
						? thisPage-1
						: 1;
		}

		@Override
		@Deprecated
		public void remove() {
			
		}

		@Override
		@Deprecated
		public void set(List<E> e) {
			
		}

		@Override
		public void add(List<E> e) {
			computePage(list.addAll(e));
			pageSum = getPageSum();
		}
		
	}

	
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (null==o || !(o instanceof PageList)) {
			return false;
		}
		return list.equals(((PageList<E>)o).getData());
	}

	@Override
	public int hashCode() {
		return list.hashCode();
	}

	
	
	
	
	@Override
	public List<E> get(int index) {
		checkPage(index);
		List<E> list = new ArrayList<>(pageSize);
		Iterator<E> iterator = list.listIterator((index-1)*pageSize);
		for(int i = 0;i<pageSize;i++) {
			if (iterator.hasNext()) {
				list.add(iterator.next());
			}
		}
		return list;
	}
	
	public List<E> firstPage(){
		thisPage = 1;
		return get(thisPage);
	}
	
	public boolean hasLastPage() {
		return thisPage>1;
	}
	
	public List<E> lastPage(){
		thisPage--;
		return get(thisPage);
	}
	
	public boolean hasNextPage() {
		return thisPage<getPageSum();
	}
	
	public List<E> nextPage(){
		thisPage++;
		return get(thisPage);
	}
	
	public List<E> finalPage(){
		thisPage = getPageSum();
		return get(thisPage);
	}
	
	public List<E> updatePage(){
		computePage(true);
		return get(thisPage);
	}
	
	public List<E> toPage(int index){
		checkPage(index);
		thisPage = index;
		return get(thisPage);
	}
	
	private void checkPage(int index) {
		if (index>getPageSum() || index<1) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	
	
	
	

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	
	
	
	
	
	@Override
	public boolean add(List<E> e) {
		boolean flag = list.addAll(e);
		computePage(flag);
		return flag;
	}

	@Override
	public void add(int index, List<E> element) {
		computePage(list.addAll(index, element));
	}

	public boolean addElement(E e) {
		boolean flag = list.add(e);
		computePage(flag);
		return flag;
	}
	
	@Override
	public boolean addAll(Collection<? extends List<E>> c) {
		boolean flag = true;
		for (List<E> e : c) {
			if(!list.addAll(e)) {
				flag = false;
			}
		}
		computePage(true);
		return flag;
	}

	@Override
	public boolean addAll(int index, Collection<? extends List<E>> c) {
		boolean flag = true;
		for (List<E> e : c) {
			if(!list.addAll(index,e)) {
				flag = false;
			}
			index = e.size()+index;
		}
		return flag;
	}

	
	
	
	
	
	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Deprecated
	public List<E> set(int index, List<E> element) {
		return null;
	}

	@Override
	@Deprecated
	public List<List<E>> subList(int fromIndex, int toIndex) {
		return null;
	}
}

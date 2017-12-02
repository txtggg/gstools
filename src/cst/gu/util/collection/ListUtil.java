package cst.gu.util.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guweichao 2017 1016
 */
public class ListUtil { 
	
	public static <E> boolean isEmpty(List<E> list){
		return list == null || list.isEmpty();
	}
	
	public static <E> boolean isNotEmpty(List<E> list){
		return !isEmpty(list);
	}
	
	public static <E> ArrayList<E> newArrayListWithSize(int s){
		return new ArrayList<E>(s);
	}
	
	public static <E> ArrayList<E> newArrayList(){
		ArrayList<E> list =  new ArrayList<E>();
		return list;
	}
	
	public static <E> ArrayList<E> newArrayList(E...es){
		ArrayList<E> list =  new ArrayList<E>();
		for(E e : es){
			list.add(e);
		}
		return list;
	}
	
	
}

package cst.gu.util.map;

import java.util.HashMap;

import cst.gu.util.container.Containers;

/**
 * @author guweichao 2017 1103 双向map,互为key value
 * 非线程安全
 */
public class BothwayMap<K1, K2> {
	private int size ;
	Object[] k1Array;
	Object[] k2Array;
	
	public BothwayMap() { // 
		k1Array = new Object[8];
		k2Array = new Object[8];
		size = 0;
	}
	
	/**
	 * 生成hashmap
	 * k1为key,k2weivalue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<K1,K2> toHashMap1(){
		HashMap<K1,K2> m =  Containers.newHashMap(size);
		for(int x =0 ;x < size;x++){
			m.put((K1)k1Array[x], (K2)k2Array[x]);
		}
		return m;
	}
	
	/**
	 * 生成hashmap
	 * k2为key,k1为value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<K2,K1> toHashMap2(){
		HashMap<K2,K1> m = Containers.newHashMap(size);
		for(int x =0 ;x < size;x++){
			m.put((K2)k2Array[x], (K1)k1Array[x]);
		}
		return m;
	}

	public int size() {
		return size;
	}

	/**
	 * 如果k1 k2都存在,且在同一位置,则覆盖;
	 * 如果只有k1 存在,覆盖在k1的位置;
	 * 如果只有k2存在,覆盖在k2的位置;
	 * 如果都存在,但不在同一位置,则移除这两个对象,并在最后添加
	 * @param k1
	 * @param k2
	 * @return 存储的key对
	 */
	public void put(K1 k1, K2 k2) {
		int i1 = getK1Index(k1);
		int i2 = getK2Index(k2);
		if( i1 == i2){ // 同一位置,替换或新增
			if(i1 == -1){ //都是-1,不存在key对,是添加的操作,需要增加size
				setKeys(k1,k2,size); //添加key对
				size ++;
			}else{
				setKeys(k1,k2,i1); //替换key对
			}
		}else{
			if(i1 == -1 || i2 == -1){ // 如果有一个为-1 , 则覆盖在此位置
				setKeys(k1,k2,i1 + i2 +1);
			}else{ // 两个都不是-1,则需要移除两个,并在最后添加
				remove(i1);
				remove(i2);
				setKeys(k1, k2, size);
				size ++ ; // remove中已变更size,add中没有所以需要增加
			}
		}
	}

	public boolean containsK1(K1 k1) {
		return getK1Index(k1) > -1;
	}

	public boolean containsK2(K2 k2) {
		return getK2Index(k2) > -1;
	}

	@SuppressWarnings("unchecked")
	public K1 getK1(K2 k2) {
		int index = getK2Index(k2);
		if (index > -1) {
			return (K1) k1Array[index];
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public K1 getK1ByIndex(int index) {
		if (index < size) {
			return (K1) k1Array[index];
		}
		throw new IndexOutOfBoundsException("数组下标越界:" + index);
	}

	@SuppressWarnings("unchecked")
	public K2 getK2(K1 k1) {
		int index = getK1Index(k1);
		if (index > -1) {
			return (K2) k2Array[index];
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public K2 getK2ByIndex(int index) {
		if (index < size) {
			return (K2) k2Array[index];
		}
		throw new IndexOutOfBoundsException("数组下标越界:" + index);
	}
	
	/**
	 * 
	 * @return
	 * 长度为2的object[]
	 * 分别存储k1,k2
	 * 	
	 */
	public Object[] remove(int index){
		if(index < size){
			size -- ;
			Object[] oar = new Object[2];
			oar[0] = k1Array[index];
			oar[1] = k2Array[index];
			k1Array[index] = k1Array[size]; // 把最后的元素移动到要移除的位置
			k2Array[index] = k2Array[size]; // 把最后的元素移动到要移除的位置
			return oar;
		}else{
			throw new IndexOutOfBoundsException("数组下标越界:" + index);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		for (int x = 0; x < size; x++) {
			if (x > 0) {
				sb.append(',');
			}
			sb.append('[').append(k1Array[x]).append(':').append(k2Array[x]).append(']');
		}
		sb.append('}');
		return sb.toString();
	}


	/**************************************************************************/
	private int getK1Index(K1 k1) {
		if (k1 == null) {
			for (int x = 0; x < size; x++) {
				if (k1Array[x] == null) {
					return x;
				}
			}
		} else {
			for (int x = 0; x < size; x++) {
				Object o = k1Array[x];
				if (k1 == o || k1.equals(o)) {
					return x;
				}
			}
		}
		return -1;
	}

	private int getK2Index(K2 k2) {
		if (k2 == null) {
			for (int x = 0; x < size; x++) {
				if (k2Array[x] == null) {
					return x;
				}
			}
		} else {
			for (int x = 0; x < size; x++) {
				Object o = k2Array[x];
				if (k2 == o || k2.equals(o)) {
					return x;
				}
			}
		}
		return -1;
	}
	
	private void setKeys(K1 k1,K2 k2,int index){
		if(index >= k1Array.length ){
			int nsize = 2 * size;
			Object[] objs1 = new Object[nsize];
			Object[] objs2 = new Object[nsize];
			for(int x = 0 ;x < k1Array.length; x ++){
				objs1[x] = k1Array[x];
				objs2[x] = k2Array[x];
			}
		}
		k1Array[index] = k1;
		k2Array[index] = k2;
	}
}

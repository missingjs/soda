package soda.unittest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class DataUtils {
	
	public static <T> Object toPrimitiveArray(List<T> L, Class<T> wrapClass) {
		Class<?> primClass = Primitives.getPrimitiveClass(wrapClass);
		Object array = Array.newInstance(primClass, L.size());
		for (int i = 0; i < L.size(); ++i) {
			Array.set(array, i, L.get(i));
		}
		return array;
	}
	
	public static <W> List<W> fromPrimitiveArray(Object primArray, Class<W> wrapClass) {
		List<W> L = new ArrayList<>();
		for (int i = 0; i < Array.getLength(primArray); ++i) {
			L.add((W) Array.get(primArray, i));
		}
		return L;
	}
	
	public static <T> T[] toArray(List<T> L, Class<T> klass) {
		T[] array = (T[]) Array.newInstance(klass, L.size());
		L.toArray(array);
		return array;
	}
	
	public static <T> List<T> toList(T[] array) {
		return Arrays.asList(array);
	}
	
	public static Object toList(Object array) {
		Class<?> ec = array.getClass().getComponentType();
		if (ec == null) {
			throw new IllegalArgumentException("object must be an array");
		}
		List<Object> res = new ArrayList<>();
		for (int i = 0; i < Array.getLength(array); ++i) {
			Object element = Array.get(array, i);
			res.add(ec.isArray() ? toList(element) : element);
		}
		return res;
	}
	
	public static Object toArray(Object collection, Class<?> elementType, int D) {
		if (!Collection.class.isAssignableFrom(collection.getClass())) {
			throw new IllegalArgumentException("object must be an collection");
		}
		int[] sizes = new int[D];
//		getSizeOnDimensions(collection, sizes, 0);
		sizes[0] = ((Collection<Object>) collection).size();
		Object array = Array.newInstance(elementType, sizes);
		fillMultipleDimension((Collection<Object>) collection, array, D, elementType);
		return array;
	}
	
	private static void fillMultipleDimension(Collection<Object> coll, Object array, int k, Class<?> elementType) {
		Iterator<Object> iter = coll.iterator();
		for (int i = 0; i < Array.getLength(array); ++i) {
			Object element = iter.next();
			if (k <= 1) {
				Array.set(array, i, element);
			} else {
				Collection<Object> C = (Collection<Object>) element;
				int[] sizes = new int[k-1];
				sizes[0] = C.size();
				Object a = Array.newInstance(elementType, sizes);
				Array.set(array, i, a);
				fillMultipleDimension((Collection<Object>) element, a, k-1, elementType);
			}
		}
	}
	
	private static void getSizeOnDimensions(Object collection, int[] sizes, int index) {
		if (index == sizes.length) {
			return;
		}
		if (!Collection.class.isAssignableFrom(collection.getClass())) {
			throw new IllegalArgumentException("object must be an collection");
		}
		Collection<Object> coll = (Collection<Object>) collection;
		if (coll.size() == 0) {
			return;
		}
		sizes[index] = coll.size();
		Iterator<Object> iter = coll.iterator();
		getSizeOnDimensions(iter.next(), sizes, index+1);
	}
	
	public static <T> Object toPrimitiveArray2d(List<List<T>> L, Class<T> wrapClass) {
		int d1 = L.size(), d2 = 0;
		if (d1 > 0) {
			d2 = L.get(0).size();
		}
		Class<?> primClass = Primitives.getPrimitiveClass(wrapClass);
		Object array = Array.newInstance(primClass, d1, d2);
		for (int i = 0; i < d1; ++i) {
			Object a = Array.get(array, i);
			for (int j = 0; j < d2; ++j) {
				Array.set(a, j, L.get(i).get(j));
			}
		}
		return array;
	}
	
	public static <T> List<List<T>> fromPrimitiveArray2d(Object primArray2d, Class<T> wrapClass) {
		List<List<T>> L = new ArrayList<>();
		for (int i = 0; i < Array.getLength(primArray2d); ++i) {
			Object arr = Array.get(primArray2d, i);
			List<T> L2 = new ArrayList<>();
			for (int j = 0; j < Array.getLength(arr); ++j) {
				L2.add((T) Array.get(arr, j));
			}
			L.add(L2);
		}
		return L;
	}
	
	public static <T> T[][] toArray2d(List<List<T>> L, Class<T> klass) {
		int d1 = L.size(), d2 = 0;
		if (d1 > 0) {
			d2 = L.get(0).size();
		}
		T[][] array2d = (T[][]) Array.newInstance(klass, d1, d2);
		for (int i = 0; i < d1; ++i) {
			L.get(i).toArray(array2d[i]);
		}
		return array2d;
	}
	
	public static <T> List<List<T>> toList2d(T[][] array2d) {
		List<List<T>> L = new ArrayList<>();
		for (T[] arr : array2d) {
			L.add(toList(arr));
		}
		return L;
	}
	
	public static List<String> toStringList(char[] array) {
		return fromPrimitiveArray(array, Character.class).stream().map(c -> String.valueOf(c)).collect(Collectors.toList());
	}
	
	public static List<Integer> toList(int[] array) {
		return Arrays.stream(array).boxed().collect(Collectors.toList());
	}
	
	public static List<Long> toList(long[] array) {
		return Arrays.stream(array).boxed().collect(Collectors.toList());
	}
	
	public static List<Double> toList(double[] array) {
		return Arrays.stream(array).boxed().collect(Collectors.toList());
	}
	
	public static char[] toCharArray(List<?> L) {
		List<Character> chList = L.stream().map(o -> o instanceof String ? ((String)o).charAt(0) : (Character) o).collect(Collectors.toList());
		return (char[]) toPrimitiveArray(chList, Character.class);
	}
	
	public static int[] toIntArray(List<?> L) {
		return L.stream().mapToInt(i -> (Integer)i).toArray();
	}
	
	public static long[] toLongArray(List<?> L) {
		return L.stream().mapToLong(i -> (Long)i).toArray();
	}
	
	public static double[] toDoubleArray(List<?> L) {
		return L.stream().mapToDouble(i -> (Double)i).toArray();
	}
	
	public static List<List<Integer>> toList2d(int[][] array) {
		return Arrays.stream(array).map(arr -> toList(arr)).collect(Collectors.toList());
	}
	
	public static List<List<Long>> toList2d(long[][] array) {
		return Arrays.stream(array).map(arr -> toList(arr)).collect(Collectors.toList());
	}
	
	public static List<List<Double>> toList2d(double[][] array) {
		return Arrays.stream(array).map(arr -> toList(arr)).collect(Collectors.toList());
	}
	
	public static int[][] toIntArray2d(List<?> L) {
		return L.stream().map(a -> toIntArray((List<?>) a)).toArray(int[][]::new);
	}
	
	public static long[][] toLongArray2d(List<?> L) {
		return L.stream().map(a -> toLongArray((List<?>) a)).toArray(long[][]::new);
	}
	
	public static double[][] toDoubleArray2d(List<?> L) {
		return L.stream().map(a -> toDoubleArray((List<?>) a)).toArray(double[][]::new);
	}
	
	public static void main(String[] args) {
		int[][] arr2d = new int[][] {{1,2,3}, {4,5,6}, {7,8,9}};
		List<List<Integer>> L = toList2d(arr2d);
		System.out.println(L);
		int[][] arr2d1 = toIntArray2d(L);
		for (int i = 0; i < arr2d1.length; ++i) {
			for (int j = 0; j < arr2d1[i].length; ++j) {
				System.out.print(arr2d1[i][j] + " ");
			}
			System.out.println();
		}
		
		int[] arr = new int[] {1,2,3,4,5};
		List<Integer> L2 = toList(arr);
		System.out.println(L2);
		
		arr = toIntArray(L2);
		System.out.println(Arrays.toString(arr));
		
//		List<Integer> L1 = new ArrayList<>();
//		L1.add(1); L1.add(2); L1.add(3);
//		int[] arr1 = (int[]) toPrimitiveArray(L1, Integer.class);
//		for (int i = 0; i < L1.size(); ++i) {
//			System.out.println(arr1[i]);
//		}
//		System.out.println(fromPrimitiveArray(arr1, Integer.class));
		
	}
	
}

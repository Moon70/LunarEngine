package abyss.lunarengine.gfx;

import java.util.Comparator;

public class Point3DzComparator<T> implements Comparator<T>{

	public int compare(T o1, T o2) {
		if(((Point3D)o1).dz==((Point3D)o2).dz) {
			return 0;
		}
		return ((Point3D)o1).dz<((Point3D)o2).dz?-1:1;
	}

}

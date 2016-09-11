import java.util.ArrayList;
public class ObjectList extends Objects {
	private ArrayList<Object> obj;

	public ObjectList() {
		obj = new ArrayList<Object>();
	}

	public void add(Object o) {
		obj.add(o);
	}

	public Hit intersects(Ray r) {
		Hit h = new Hit(null,Hit.NO_HIT);
		for (int i = 0; i < obj.size(); ++i)
			h = Hit.less(new Hit(obj.get(i),obj.get(i).intersect(r)),h);
		return h;
	}
}

class Hit {
	public Object o;
	public double d;

	public static final int NO_HIT = -1;
	public static final double EPS = 1e-2;

	public Hit(Object oo, double dd) {
		o = oo;
		d = dd;
	}

	public static Hit less(Hit a, Hit b) {
		if (b.d == NO_HIT)
			return a;
		if (a.d == NO_HIT)
			return b;
		
		return a.d < b.d ? a : b;
	}

	public String toString() {
		return String.format("%.3f",d);
	}
}
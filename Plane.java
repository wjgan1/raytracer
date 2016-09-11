import java.lang.Math;
public class Plane extends Object {
	public Ray norm;

	public Plane(Ray r, Material m) {
		super(m);
		norm = r;
	}

	public double intersect(Ray r) {
		double p = norm.u.dot(norm.o.sub(r.o));
		double q = norm.u.dot(r.u);
		if (Math.abs(q) <= Vec.ERR)
			return Hit.NO_HIT;
		double d = p/q;
		if (d >= Hit.EPS)
			return d;
		return Hit.NO_HIT;
	}

	public Vec normal(Vec p) {
		return norm.u;
	}
}
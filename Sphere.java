public class Sphere extends Object {
	public Vec c;
	public double rad;

	public Sphere(Vec cc, double rr, Material mt) {
		super(mt);
		c = cc;
		rad = rr;
	}

	public double intersect(Ray r) {
		Vec oc = r.o.sub(c);
		double loc = r.u.dot(oc);
		double det = loc*loc-oc.dd()+rad*rad;
		if (det < 0)
			return Hit.NO_HIT;
		det = Math.sqrt(det);
		double d1 = -loc-det;
		double d2 = -loc+det;
		if (d1 < 0 && d2 >= Hit.EPS)
			return d2;
		if (d1 >= Hit.EPS)
			return d1;
		return Hit.NO_HIT;
	}

	public Vec normal(Vec p) {
		Vec v = p.sub(c);
		if (Math.abs(v.dd()-rad*rad) > Vec.ERR)
			return Vec.BLANK;
		v.normalize();
		return v;
	}
}
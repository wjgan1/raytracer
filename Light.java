import java.lang.Math;

public class Light {
	Vec p, light;

	public Light(Vec pp, Vec l) {
		p = pp;
		light = l;
	}
	/*
	public Color net() {
		Vec col = s.add(d);
		int rr = Raytracer.rgbRep;
		return new Color((int)Math.min(col.x*rr,rr-1),(int)Math.min(col.y*rr,rr-1),(int)Math.min(col.z*rr,rr-1));
	}
	*/
	public double dist(Vec q) {
		return p.sub(q).d();
	}

	public Vec ray(Vec q) {
		Vec v = q.sub(p);
		v.normalize();
		return v;
	}
}
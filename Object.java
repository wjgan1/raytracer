public abstract class Object {
	public Material mt;

	public Object(Material m) {
		mt = m;
	}

	public abstract double intersect(Ray r);
	public abstract Vec normal(Vec p);
	//public abstract Ray reflect()
}
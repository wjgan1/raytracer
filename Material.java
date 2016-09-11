public class Material {
	public Vec ka, ks, kd;
	public double a, n;

	public Material(Vec a, Vec s, Vec d, double aa) {
		ka = a;
		ks = s;
		kd = d;
		this.a = aa;
		n = 0;
	}
}
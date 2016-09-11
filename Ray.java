import java.lang.Math;

public class Ray {
	public Vec o, u;

	public Ray(Vec o, Vec u) {
		this.o = o;
		this.u = u;
		if (!this.u.ddEqual(1))
			this.u.normalize();
	}

	public Ray reflect(Vec n) {
		return new Ray(o,u.sub(n.mult(2*u.dot(n))));
	}

	public String toString() {
		return o.toString()+" "+u.toString();
	}
}
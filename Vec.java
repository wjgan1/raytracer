import java.lang.Math;

public class Vec {
	public double x, y, z;
	private double d, dd;
	public static final double ERR = 1e-6;
	public static final double NOT_SET = -1;
	public static final Vec BLANK = new Vec(0,0,0);

	public Vec(Vec v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.dd = v.dd;
		this.d = v.d;
	}

	public Vec(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		dd = NOT_SET;
		d = NOT_SET;
		//dd();
		//d();
	}

	public double dd() {
		if (Math.abs(dd-NOT_SET) <= Vec.ERR)
			dd = x*x+y*y+z*z;
		return dd;
	}

	public double d() {
		if (Math.abs(dd-NOT_SET) <= Vec.ERR)
			dd();
		if (Math.abs(d-NOT_SET) <= Vec.ERR)
			d = Math.sqrt(dd);
		return d;
	}

	public Vec add(Vec v) {
		return new Vec(x+v.x,y+v.y,z+v.z);
	}

	public Vec mult(double s) {
		return new Vec(x*s,y*s,z*s);
	}

	public Vec mult(Vec v) {
		return new Vec(x*v.x,y*v.y,z*v.z);
	}

	public Vec sub(Vec v) {
		return new Vec(x-v.x,y-v.y,z-v.z);
	}

	public Vec div(double s) {
		return new Vec(x/s,y/s,z/s);
	}

	public double dot(Vec v) {
		return x*v.x+y*v.y+z*v.z;
	}

	public Vec cross(Vec v) {
		return new Vec(y*v.z-v.y*z,v.x*z-x*v.z,x*v.y-v.x*y);
	}

	public boolean ddEqual(double mag) {
		return Math.abs(dd()-mag) <= ERR;
	}

	public boolean equals(Vec v) {
		return Math.abs(x-v.x) <= ERR && Math.abs(y-v.y) <= ERR && Math.abs(z-v.z) <= ERR;
	}

	public void normalize() {
		x /= d();
		y /= d();
		z /= d();
		dd = d = 1;
	}

	public String toString() {
		return String.format("(%.3f,%.3f,%.3f)",x,y,z);
	}
}
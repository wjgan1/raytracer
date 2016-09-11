import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class Raytracer {
	public Image img;
	public Objects obj;
	public Vec amb;
	public ArrayList<Light> lig;

	public static final Vec bgColor = new Vec(0,0,0);
	public static final int rgbRep = 256;
	public static final int MAX_DEPTH = 5;
	public static final double ALP_THRES = 1e-3;
	public static double mind = 1000;

	public Raytracer(Image i, Objects o, Vec a) {
		img = i;
		obj = o;
		lig = new ArrayList<Light>();
		amb = a;
	}

	public static void main (String [] args) throws IOException {
		long start = System.currentTimeMillis();
		Ray eye = new Ray(new Vec(0,0,20),new Vec(0,1,0));
		Vec a = new Vec(0.2,0.2,0.2);
		double k = 0.3;
		Raytracer rt = new Raytracer(new Image(640,480,2,eye,320,new Vec(0,0,1)), new ObjectList(), a);
		rt.obj.add(new Sphere(new Vec(0,80,30),30,new Material(new Vec(0,.7,0.7),new Vec(k,k,k), new Vec(0,0.7,0.7),4)));
		//rt.obj.add(new Sphere(new Vec(30,50,10),10,new Material(new Vec(0.8,0,0),new Vec(k,k,k), new Vec(0.8,0,0),4)));
		Sphere s = new Sphere(new Vec(0,40,10),10,new Material(new Vec(0,0,0),new Vec(1,1,1), new Vec(0,0,0),4));
		s.mt.n = 1.5;
		rt.obj.add(s);
		rt.obj.add(new Plane(new Ray(new Vec(0,0,0),new Vec(0,0,1)),new Material(new Vec(0,0.8,0),new Vec(0.2,0.2,0.2), new Vec(0,0.8,0),2)));
		rt.obj.add(new Plane(new Ray(new Vec(0,150,0),new Vec(0,-1,0)),new Material(new Vec(0.7,0,0),new Vec(k,k,k), new Vec(0.7,0,0),2)));
		rt.obj.add(new Plane(new Ray(new Vec(100,0,0),new Vec(-1,0,0)),new Material(new Vec(0.5,0.5,0),new Vec(k,k,k), new Vec(0.5,0.5,0),2)));
		rt.obj.add(new Plane(new Ray(new Vec(-100,0,0),new Vec(1,0,0)),new Material(new Vec(0.5,0,0.5),new Vec(k,k,k), new Vec(0.5,0,0.5),2)));
		rt.obj.add(new Plane(new Ray(new Vec(0,0,100),new Vec(0,0,-1)),new Material(new Vec(0.8,0.8,0.8),new Vec(k,k,k), new Vec(0.5,0.5,0.5),2)));
		rt.obj.add(new Plane(new Ray(new Vec(0,-50,0),new Vec(0,1,0)),new Material(new Vec(0.6,0.3,0),new Vec(k,k,k), new Vec(0.5,0.5,0.5),2)));
		rt.lig.add(new Light(new Vec(-40,40,50), new Vec(0.7,0.7,0.7)));
		//rt.lig.add(new Light(new Vec(50,60,70), new Vec(0.5,0.5,0.5)));
		rt.img.renderPixels(rt);
		rt.img.export(new File("saved.png"));
		System.out.println(System.currentTimeMillis()-start);
		//System.out.println(mind);
		//System.out.println((mind>=Hit.EPS));
	}

	public Vec trace(Ray r, int depth, double alp) {
		if (depth == MAX_DEPTH || alp < ALP_THRES)
			return bgColor;
		Hit h = obj.intersects(r);
		if (h.d == Hit.NO_HIT)
			return bgColor;
		Object o = h.o; 
		Vec col = amb.mult(o.mt.ka);
		Vec p = r.o.add(r.u.mult(h.d));
		Vec n = o.normal(p);
		for (int i = 0; i < lig.size(); ++i) {
			double dist = lig.get(i).dist(p);
			Vec lu = lig.get(i).ray(p);
			h = obj.intersects(new Ray(lig.get(i).p,lu));
			if (Math.abs(dist-h.d) <= Vec.ERR) {
				double dif = Math.max(n.dot(lu)*-1,0);
				col = col.add(lig.get(i).light.mult(o.mt.kd).mult(dif));
				Vec ref = reflect(lu,n);
				double spec = Math.max(ref.dot(r.u)*-1,0);
				spec = Math.pow(spec,o.mt.a);
				col = col.add(lig.get(i).light.mult(o.mt.ks).mult(spec));
			}
		}
		alp *= Math.max(Math.max(o.mt.ks.x,o.mt.ks.y),o.mt.ks.z);
		Vec rr = reflect(r.u,n);
		Vec lr = trace(new Ray(p,rr),depth+1,alp);
		if (o.mt.n == 0) {
			col = col.add(lr.mult(o.mt.ks));
			return col;
		}
		double rn = r.u.dot(n);
		Vec rf = rn < 0 ? refract(r.u,n,1,o.mt.n) : refract(r.u,n,o.mt.n,1);
		double rfc = rn < 0 ? reflectance(r.u,n,1,o.mt.n) : reflectance(r.u,n,o.mt.n,1);
		Vec lf = rf.equals(Vec.BLANK) ? Vec.BLANK : trace(new Ray(p,rf),depth+1,alp);
		col = col.add(lr.mult(o.mt.ks).mult(rfc));
		col = col.add(lf.mult(o.mt.ks).mult(1-rfc));
		return col;
	}

	//i and n must be normalized
	public Vec reflect(Vec i, Vec n) {
		return i.sub(n.mult(2*i.dot(n)));
	}

	//i and n must be normalized
	public Vec refract(Vec i, Vec n, double n1, double n2) {
		double nn = n1/n2;
		double cosI = i.dot(n);
		if (cosI > 0) {
			cosI *= -1;
			n = n.mult(-1);
		}
		double sinT2 = nn*nn*(1-cosI*cosI);
		if (sinT2 > 1)
			return Vec.BLANK;
		double cosT = Math.sqrt(1-sinT2);
		return i.mult(nn).add(n.mult(nn*cosI-cosT));
	}

	//i and n must be normalized
	public double reflectance(Vec i, Vec n, double n1, double n2) {
		double nn = n1/n2;
		double in = i.dot(n);
		in = in < 0 ? in*-1 : in;
		double sint2 = nn*nn*(1-in*in);
		if (sint2 > 1)
			return 1;
		double cost = Math.sqrt(1-sint2);
		double rs = (n1*in-n2*cost)/(n1*in+n2*cost);
		double rp = (n2*in-n1*cost)/(n2*in+n1*cost);
		return (rs*rs+rp*rp)/2;
	}

	public static int rgb(int r, int g, int b) {
		return rgbRep*rgbRep*r+rgbRep*g+b;
	}
}
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.lang.Math;

public class Image {
	public int w, h, ss, ww, hh;
	public Ray cam;
	public Vec nor;
	double d;
	public Color [][] px;
	public static final int pixType = BufferedImage.TYPE_INT_RGB;
	public static final String imgType = "png";
	public static final double centerFactor = 0.5;

	public Image(int ww, int hh, int s, Ray cm, double dist, Vec nr) {
		w = ww;
		h = hh;
		ss = s;
		this.ww = w*ss;
		this.hh = h*ss;
		px = new Color[this.ww][this.hh];
		cam = cm;
		nor = nr;
		d = dist;
		if (!nor.cross(cam.u).ddEqual(1.0)) {
			nor = nor.cross(cam.u);
			nor.normalize();
			nor = cam.u.cross(nor);
		}
	}

	public void renderPixels(Raytracer rt) {
		for (int i = 0; i < ww; ++i)
			for (int j = 0; j < hh; ++j)
				px[i][j] = new Color(rt.trace(getRay(i,j),0,1));
	}

	public void export(File file) throws IOException {
		BufferedImage img = new BufferedImage(w,h,pixType);
		for (int i = 0; i < ww; i += ss) {
			for (int j = 0; j < hh; j += ss) {
				Color c = new Color(0,0,0);
				for (int a = i; a < i+ss; ++a)
					for (int b = j; b < j+ss; ++b)
						c = c.add(px[a][b]);
				c = c.div(ss*ss);
				img.setRGB(i/ss,j/ss,Raytracer.rgb(c.r,c.g,c.b));
			}
		}
		ImageIO.write(img,imgType,file);
	}

	public Ray getRay(double x, double y) {
		Vec xx = cam.u.cross(nor);
		xx = xx.mult((x-ww*centerFactor)/ss);
		Vec yy = nor.mult((hh*centerFactor-y)/ss);
		Vec dir = cam.o.add(cam.u.mult(d)).add(xx).add(yy);
		dir.normalize();
		return new Ray(cam.o,dir);
	}
}

class Color {
	int r, g, b;
	public Color(int rr, int gg, int bb) {
		r = rr;
		g = gg;
		b = bb;
	}
	public Color(Vec v) {
		int rr = Raytracer.rgbRep;
		r = (int)Math.min(v.x*rr,rr-1);
		g = (int)Math.min(v.y*rr,rr-1);
		b = (int)Math.min(v.z*rr,rr-1); 
	}
	public Color add(Color c) {
		return new Color(r+c.r,g+c.g,b+c.b);
	}
	public Color div(int s) {
		return new Color(r/s,g/s,b/s);
	}
}
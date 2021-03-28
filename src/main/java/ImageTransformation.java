
import java.awt.image.BufferedImage;
import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class ImageTransformation {
	
	public static void main(String args[]) {
		ImageTransformation it=new ImageTransformation();
		int fi=new File(it.ipath).list().length;
		for(int i=1;i<fi;i++) {
			String n=new Integer(i).toString();
		it.setName(n);
		it.setFilename(n);
		it.GrayScale(args);
		it.EdgeDetect(args);
		}
		System.out.println("...");
		
	}
	
public String ipath=null;
public String gpath=null;
public String epath=null;
public String filename=null;
public String name=null;
public String prefix=null;
public String extention=null;
public void setInPath(String p) {this.ipath=p;}
public void setGrayPath(String p) {this.gpath=p;}
public void setEdgePath(String p) {this.epath=p;}

public void setPrefix(String pre) {this.prefix=pre;} 
public void setExtention(String ext) {this.extention=ext;}

public void setName(String f) {this.name=prefix+f+").jpg";}
public void setFilename(String f) {this.filename=ipath+prefix+f+extention;}
ImageTransformation(){
laodLibraries();
setInPath("C:\\Users\\bilal.iqbal\\Downloads\\fingerprint_bitmaps\\Actual\\");
setGrayPath("C:\\Users\\bilal.iqbal\\Downloads\\fingerprint_bitmaps\\GrayScale\\");
setEdgePath("C:\\Users\\bilal.iqbal\\Downloads\\fingerprint_bitmaps\\EdgeDetect\\");
String pre="thumb (";
String ext=").bmp";
setPrefix(pre);
setExtention(ext);
}

public void laodLibraries() {
File lib = null;
String os = System.getProperty("os.name");
String bitness = System.getProperty("sun.arch.data.model");
if (os.toUpperCase().contains("WINDOWS")) {
if (bitness.endsWith("64")) {lib = new File("Libraries\\" + System.mapLibraryName("opencv_java2411"));} 
else {lib = new File("Libraries//x86//" + System.mapLibraryName("opencv_java2411"));}}
System.load(lib.getAbsolutePath());}

public void Binarized() {
Mat input = Highgui.imread(filename, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
Mat binarized=new Mat();
Imgproc.threshold(input, binarized, 100, 255,Imgproc.THRESH_TOZERO);
Highgui.imwrite("Binary Image",binarized);
}


public void Copy(String args[]) {
	Mat src = Highgui.imread(filename, Highgui.IMREAD_COLOR);
	Highgui.imwrite(epath+name, src);

}



public void GrayScale(String args[]) {
	Mat src = Highgui.imread(filename, Highgui.IMREAD_COLOR);
	Mat gray = new Mat();
	Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
	Highgui.imwrite(gpath+name, gray);
}



public void EdgeDetect(String args[]) {
Mat Canny=new Mat();
Mat src = Highgui.imread(filename, Highgui.IMREAD_GRAYSCALE);// Load an image
Imgproc.Canny(src, Canny, 50, 200, 3, false);// Edge detection
Imgproc.cvtColor(Canny, Canny, Imgproc.COLOR_GRAY2BGR);// Copy edges to the images that will display the results in BGR
Highgui.imwrite(epath+name, Canny);
}


	
public void Resize(String [] args) {
Mat image = Highgui.imread(ipath+filename);
Mat resizeimage = new Mat();
Size sz = new Size(256,512);
Imgproc.resize( image, resizeimage, sz );
Highgui.imwrite(gpath+name, resizeimage);}




public static BufferedImage matToBufferedImage(Mat matrix) {  
int cols = matrix.cols();  
int rows = matrix.rows();  
int elemSize = (int)matrix.elemSize();  
byte[] data = new byte[cols * rows * elemSize];  
int type;  
matrix.get(0, 0, data);  
switch (matrix.channels()) {  
case 1:  
type = BufferedImage.TYPE_BYTE_GRAY;  
break;  
case 3:  
type = BufferedImage.TYPE_3BYTE_BGR;  
// bgr to rgb  
byte b;  
for(int i=0; i<data.length; i=i+3) {  
b = data[i];  
data[i] = data[i+2];  
data[i+2] = b;  
}  
break;  
default:  
return null;  
}  
BufferedImage image2 = new BufferedImage(cols, rows, type);  
image2.getRaster().setDataElements(0, 0, cols, rows, data);  
return image2;  
} 





}

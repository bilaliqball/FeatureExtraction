
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class ImageProcessings extends JFrame {
public static void main(String args[]) throws IOException{ImageProcessings i =new ImageProcessings();}
    


public String path;public void setPath(String p) {this.path=p;}
public String filename=null;public void setFilename(String f) {this.filename=f;}
public BufferedImage i=null;	

public JTextField textField;
public JLabel label;
public JButton upload;
public JButton grayscale;
public JButton rotate;
public JButton resize;
public JButton edge;
public JButton binarize;


public JPanel motherPanel;
public JPanel buttonPanel;
public ImageProcessings () throws IOException {
//FileChooser();
LoadLibries();

upload = new JButton("Upload Image");
grayscale = new JButton("Grayscale Conversion");
rotate = new JButton("Rotation");
resize = new JButton("Resize");
edge = new JButton("Edge Detection");
binarize = new JButton("Binarization");

JPanel textPanel=new JPanel();
textField=new JTextField("0");//textField.setBounds(100,10, 200,30);
textField.setToolTipText("Enter angle orientation");
textField.setLocation(0,0);
textField.setSize(100, 30);
textField.setHorizontalAlignment(0);
textPanel.add(textField);

label=new JLabel("...");
label.setLocation(0,0);
label.setSize(100, 30);
label.setHorizontalAlignment(0);


motherPanel = new JPanel();
motherPanel.setLayout(new BoxLayout(motherPanel, BoxLayout.Y_AXIS));



buttonPanel = new JPanel();buttonPanel.setPreferredSize(new Dimension(160, 35));
buttonPanel.add(upload);
buttonPanel.add(grayscale);
buttonPanel.add(edge);buttonPanel.add(textField);
buttonPanel.add(rotate);
buttonPanel.add(resize);
buttonPanel.add(binarize);

UploadButton up = new UploadButton();upload.addActionListener(up);
GrayscaleButton gr=new GrayscaleButton();grayscale.addActionListener(gr);
RotationButton rt=new RotationButton();rotate.addActionListener(rt);
ResizeButton rs=new ResizeButton(); resize.addActionListener(rs);
EdgeButton ed=new EdgeButton();edge.addActionListener(ed);
BinarizeButton br=new BinarizeButton(); binarize.addActionListener(br);

//motherPanel.add(textPanel);
//motherPanel.add(label);
motherPanel.add(buttonPanel);

add(motherPanel);
setTitle("Image Pre_processings Tasks");
setSize(1024, 920);
setLocationByPlatform(true);
setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
setVisible(true);

}

public void LoadLibries() {
File lib = null;
String os = System.getProperty("os.name");
String bitness = System.getProperty("sun.arch.data.model");
if (os.toUpperCase().contains("WINDOWS")) {
if (bitness.endsWith("64")) {lib = new File("C:\\Users\\bilal.iqbal\\eclipse-workspace\\FeatureExtraction\\" + System.mapLibraryName("opencv_java2411"));} 
else {lib = new File("C:\\Users\\bilal.iqbal\\Pictures\\Libraries\\x84\\" + System.mapLibraryName("opencv_java2411"));}}
System.load(lib.getAbsolutePath());}

public void FileChooser(){
JFileChooser fileChooser = new JFileChooser();
File dir=new File("C:\\Users\\bilal.iqbal\\eclipse-workspace\\ThumbRecognition\\InputDataset\\ThumbPrints");
fileChooser.setCurrentDirectory(dir);
int result = fileChooser.showOpenDialog(this);
if (result == JFileChooser.APPROVE_OPTION) {
File selectedFile = fileChooser.getSelectedFile();//System.out.println("Selected file: " + selectedFile.getAbsolutePath());
String file=selectedFile.getName(); String p=selectedFile.getParent();//System.out.println("Path: "+ path+" "+ "File: "+fn);

String pa[];pa=p.split("\\\\");String absPath="";
String n=pa[pa.length-1];
for(int i=0;i<pa.length;i++){absPath=absPath+pa[i]+"\\\\";}//System.out.println("AbsPath:"+ absPath);
setPath(absPath);
setFilename(file);
}}
    
public Mat Rotation() { 
    String text = textField.getText();
    int angle=Integer.parseInt(text); //System.out.println("Angle:"+angle);
Mat src = Highgui.imread(path+filename);
Mat Canny=new Mat();
Imgproc.Canny(src, Canny, 50, 200, 3, false);// Edge detection
Imgproc.cvtColor(Canny, Canny, Imgproc.COLOR_GRAY2BGR);// Copy edges to the images that will display the results in BGR
Mat rotate = new Mat();
Mat rotationMatrix = Imgproc.getRotationMatrix2D(new org.opencv.core.Point(Canny.cols()/2, Canny.rows()/2),angle, 1);
int in=0; if(angle>45){in=40;}else{in=0;}
Imgproc.warpAffine(Canny, rotate,rotationMatrix, new Size(Canny.cols()+in, Canny.rows()+in));
return rotate;
}

public void Disp(BufferedImage image){
JLabel label = new JLabel(new ImageIcon(image));
JFrame f = new JFrame();
f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
f.getContentPane().add(label);
f.pack();
f.setLocation(200,100);
f.setVisible(true);}

public  BufferedImage matToBufferedImage(Mat matrix) {  
int cols = matrix.cols();  
int rows = matrix.rows();  
int elemSize = (int)matrix.elemSize();  
byte[] data = new byte[cols * rows * elemSize];  
int type;  
matrix.get(0, 0, data);  
switch (matrix.channels()) {  
case 1:  type = BufferedImage.TYPE_BYTE_GRAY;  break;  
case 3:  type = BufferedImage.TYPE_3BYTE_BGR;  byte b;  
for(int i=0; i<data.length; i=i+3) {  
b = data[i];  
data[i] = data[i+2];  
data[i+2] = b;  }  
break;  
default:  return null;  }  
BufferedImage image2 = new BufferedImage(cols, rows, type);  
image2.getRaster().setDataElements(0, 0, cols, rows, data);  
return image2;  } 

public class UploadButton implements ActionListener {
	@Override
public void actionPerformed(ActionEvent e) { 
label.setText("123");FileChooser();
String name=path+filename;System.out.println(name);
Mat src = Highgui.imread(name, Highgui.IMREAD_COLOR);
i=matToBufferedImage(src);
Disp(i);}}

public class GrayscaleButton implements ActionListener {
	@Override
public void actionPerformed(ActionEvent e) {
		String name=path+filename;
Mat src = Highgui.imread(name, Highgui.IMREAD_COLOR);
Mat gray = new Mat();
Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
BufferedImage i=null;
i=matToBufferedImage(gray);
Disp(i);}}

public class RotationButton implements ActionListener {
	@Override
public void actionPerformed(ActionEvent e) { 
    String text = textField.getText();
    int angle=Integer.parseInt(text); //System.out.println("Angle:"+angle);
Mat src = Highgui.imread(path+filename);
Mat Canny=new Mat();
Imgproc.Canny(src, Canny, 50, 200, 3, false);// Edge detection
Imgproc.cvtColor(Canny, Canny, Imgproc.COLOR_GRAY2BGR);// Copy edges to the images that will display the results in BGR
Mat rotate = new Mat();
Mat rotationMatrix = Imgproc.getRotationMatrix2D(new org.opencv.core.Point(Canny.cols()/2, Canny.rows()/2),angle, 1);
Imgproc.warpAffine(Canny, rotate,rotationMatrix, new Size(Canny.rows()+80, Canny.cols()+100));
i=matToBufferedImage(rotate);
Disp(i);}}

public class ResizeButton implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) {  
 Mat src=Rotation();
//Mat src = Imgcodecs.imread(path+filename);
Rect rectCrop=null;int x=src.width(); int y=src.height();
rectCrop = new Rect(40, 40, x-80, y-80);
Mat resize = new Mat(src,rectCrop);
i=matToBufferedImage(resize);
Disp(i);}}

public class EdgeButton implements ActionListener {
	@Override
public void actionPerformed(ActionEvent e) {  
Mat src = Highgui.imread(path+filename, Highgui.IMREAD_GRAYSCALE);// Load an image
Mat Canny=new Mat();
Imgproc.Canny(src, Canny, 50, 200, 3, false);// Edge detection
Imgproc.cvtColor(Canny, Canny, Imgproc.COLOR_GRAY2BGR);// Copy edges to the images that will display the results in BGR
i=matToBufferedImage(Canny);
Disp(i);}}







public class BinarizeButton extends JPanel implements ActionListener {
public LinkedList<point> edgePoints=new LinkedList<>();

class point {
int x;
int y;
public point(int x, int y){
this.x=x;
this.y=y;}
public String tostring(){
return "("+ this.x+","+this.y+")";}
}



public  void generateEdgePoints() throws IOException{
Mat canny=new Mat();
//Mat gray = Imgcodecs.imread(path+filename, Imgcodecs.IMREAD_GRAYSCALE);
Mat gray = Highgui.imread(path+filename, Highgui.IMREAD_GRAYSCALE);
Imgproc.Canny(gray, canny, 50, 200, 3, false);
Imgproc.cvtColor(canny, canny, Imgproc.COLOR_GRAY2BGR);

BufferedImage blackAndWhite =matToBufferedImage(canny);
int width=(int)blackAndWhite.getWidth();
int height=(int)blackAndWhite.getHeight();

String name = filename.substring(0, filename.lastIndexOf("."));
int x,y,c; c=0;
String fileoutput="C:\\Users\\Information Technolo\\Pictures\\Images\\EdgePoints\\"+"EdgePoints"+name+".txt";
try{
FileWriter fileWrite= new FileWriter(fileoutput);
BufferedWriter bufferWrite = new BufferedWriter (fileWrite);
for(int i=0;i<width;i=i+1){
for(int j=0;j<height;j=j+1){
x=blackAndWhite.getRGB(i,j);
y=(Math.abs(x))%16777216;
if (y==1){//System.out.println("(" +i+ " " +j+ ")");
bufferWrite.write(i+ "," +j); bufferWrite.newLine();
edgePoints.add(new BinarizeButton.point(i,j));
c++;}
}}bufferWrite.close();
}catch(FileNotFoundException fnfe){System.out.println(fnfe);}
//System.out.println("Edge points "+c);
}

public void extractEdgePoints() throws IOException{
Mat canny=new Mat();
//Mat gray = Imgcodecs.imread(path+filename, Imgcodecs.IMREAD_GRAYSCALE);
Mat gray = Highgui.imread(path+filename, Highgui.IMREAD_GRAYSCALE);
Imgproc.Canny(gray, canny, 50, 200, 3, false);
Imgproc.cvtColor(canny, canny, Imgproc.COLOR_GRAY2BGR);
Mat x = canny;
Size s = x.size();
final int rows=(int) s.width;
final int cols=(int) s.height;
int r=0,g=0,b=0,rgb=0,argb=0;
int count=0;
Color c=null;
for (int i = 0; i < rows; i++) {
for (int j = 0; j < cols; j++) {
double [] data = x.get(j, i);
r =(int) data[0];
g = (int)data[1];
b = (int)data[2];
c=new Color(r,g,b);
rgb=c.getRGB();//x=blackAndWhite.getRGB(i,j);
argb=(Math.abs(rgb))%16777216;
if (argb==1){//System.out.println("(" +i+ " " +j+ ")");
edgePoints.add(new BinarizeButton.point(i,j));
count++;}
}}



}

@Override
public void paint(Graphics g) {//getEdgePoints()
int count;count=0;
Graphics2D g2 = (Graphics2D) g;//super.paintComponent(g);
g2.setStroke(new BasicStroke(2)); //System.out.println("Size "+edgePoints.size());
for (int i = 0; i < edgePoints.size(); i++) {
BinarizeButton.point p=edgePoints.get(i);
g.setColor(Color.red);
g.drawLine(p.x, p.y, p.x, p.y);
count++; }//System.out.println("number of edge point detected:	"  + count + ".");
}
    
 @Override
public void actionPerformed(ActionEvent e) {  
Mat canny=new Mat();
//Mat gray = Imgcodecs.imread(path+filename, Imgcodecs.IMREAD_GRAYSCALE);
Mat gray = Highgui.imread(path+filename, Highgui.IMREAD_GRAYSCALE);
Imgproc.Canny(gray, canny, 50, 200, 3, false);
Imgproc.cvtColor(canny, canny, Imgproc.COLOR_GRAY2BGR);

BufferedImage blackAndWhite =matToBufferedImage(canny);
int width=(int)blackAndWhite.getWidth();
int height=(int)blackAndWhite.getHeight();

String name = filename.substring(0, filename.lastIndexOf("."));
int x,y,c; c=0;
String fileoutput="C:\\Users\\bilal.iqbal\\Pictures\\Images\\EdgePoints\\"+"EdgePoints"+name+".txt";
try{
FileWriter fileWrite= new FileWriter(fileoutput);
BufferedWriter bufferWrite = new BufferedWriter (fileWrite);
for(int i=0;i<width;i=i+1){
for(int j=0;j<height;j=j+1){
x=blackAndWhite.getRGB(i,j);
y=(Math.abs(x))%16777216;
if (y==1){//System.out.println("(" +i+ " " +j+ ")");
bufferWrite.write(i+ "," +j); bufferWrite.newLine();
edgePoints.add(new BinarizeButton.point(i,j));
c++;}
}}bufferWrite.close();}
catch(FileNotFoundException fnfe){System.out.println(fnfe);} 
catch (IOException ex) {Logger.getLogger(ImageProcessings.class.getName()).log(Level.SEVERE, null, ex);}

JFrame frame = new JFrame();
frame.getContentPane().add(this);
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setSize(600,400);
frame.setVisible(true);

}
}

}
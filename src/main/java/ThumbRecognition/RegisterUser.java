package ThumbRecognition;

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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;


public class RegisterUser extends JFrame {
public static void main(String args[]) throws IOException{RegisterUser i =new RegisterUser(); i.terminate();}
    

public String path;public void setPath(String p) {this.path=p;}
public String filename=null;public void setFilename(String f) {this.filename=f;}
public String can=null;  
public String Directory=null;
public String Candidate=null;

public void setDirectory(String dir) {this.Directory=dir;} 
public void setCandidate(String can) {this.Candidate=Directory+can;} 
public void setCan(String can) {this.can=can;} 
public BufferedImage image=null;
public Mat img=null;
public MatOfKeyPoint keyPoints=null;
public Mat featureDetectedImage=null;

public JTextField textField;
public JLabel label;
public JButton upload;
public JButton grayscale;
public JButton rotate;
public JButton resize;
public JButton edge;
public JButton binarize;
public JButton detectFeature;
public JButton writeFeature;







public JLabel featureCount;
public JLabel featureWrite;


public JPanel labelPanel;
public JPanel motherPanel;
public JPanel buttonPanel;

public RegisterUser () throws IOException {
LoadLibries();

upload = new JButton("Upload Image");
grayscale = new JButton("Grayscale Conversion");
rotate = new JButton("Rotation");
resize = new JButton("Resize");
edge = new JButton("Edge Detection");
binarize = new JButton("Binarization");
detectFeature = new JButton("Detect Features");
writeFeature =new JButton("Write Features");


featureCount=new JLabel("");
featureWrite=new JLabel("");

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
buttonPanel.add(detectFeature);//buttonPanel.add(featureCount);
buttonPanel.add(writeFeature);
buttonPanel.add(featureWrite);

UploadButton up = new UploadButton();upload.addActionListener(up);
GrayscaleButton gr=new GrayscaleButton();grayscale.addActionListener(gr);
RotationButton rt=new RotationButton();rotate.addActionListener(rt);
ResizeButton rs=new ResizeButton(); resize.addActionListener(rs);
EdgeButton ed=new EdgeButton();edge.addActionListener(ed);
BinarizeButton br=new BinarizeButton(); binarize.addActionListener(br);
DetectCandidate dc = new DetectCandidate();detectFeature.addActionListener(dc);
WriteCandidate wc=new WriteCandidate();writeFeature.addActionListener(wc);



//motherPanel.add(textPanel);
//motherPanel.add(label);
motherPanel.add(buttonPanel);
add(motherPanel);

setTitle("REGISTERING NEW USER");
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
if (bitness.endsWith("64")) {lib = new File("Libraries\\" + System.mapLibraryName("opencv_java2411"));} 
else {lib = new File("Libraries\\x84\\" + System.mapLibraryName("opencv_java2411"));}}
System.load(lib.getAbsolutePath());}

public void FileChooser(){
JFileChooser fileChooser = new JFileChooser();
File dir=new File("InputDataset\\NewThumbPrints");
fileChooser.setCurrentDirectory(dir);
int result = fileChooser.showOpenDialog(this);
if (result == JFileChooser.APPROVE_OPTION) {
File selectedFile = fileChooser.getSelectedFile();//System.out.println("Selected file: " + selectedFile.getAbsolutePath());
String file=selectedFile.getName(); String p=selectedFile.getParent();//System.out.println("Path: "+ path+" "+ "File: "+fn);

String pa[];pa=p.split("\\\\");String absPath="";
for(int i=0;i<pa.length;i++){absPath=absPath+pa[i]+"\\\\";}//System.out.println("AbsPath:"+ absPath);
setPath(absPath);
setFilename(file);
setDirectory(absPath);
setCandidate(file);
setCan(file);}}
    
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
return rotate;}

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
String name=path+filename;//System.out.println(name);
Mat src = Highgui.imread(name, Highgui.IMREAD_COLOR);
image=matToBufferedImage(src);
Disp(image);}}

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
image=matToBufferedImage(rotate);
Disp(image);}}

public class ResizeButton implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) {  
 Mat src=Rotation();
//Mat src = Imgcodecs.imread(path+filename);
Rect rectCrop=null;int x=src.width(); int y=src.height();
rectCrop = new Rect(40, 40, x-80, y-80);
Mat resize = new Mat(src,rectCrop);
image=matToBufferedImage(resize);
Disp(image);}}

public class EdgeButton implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) {  
Mat src = Highgui.imread(path+filename, Highgui.IMREAD_GRAYSCALE);// Load an image
Mat Canny=new Mat();
Imgproc.Canny(src, Canny, 50, 200, 3, false);// Edge detection
Imgproc.cvtColor(Canny, Canny, Imgproc.COLOR_GRAY2BGR);// Copy edges to the images that will display the results in BGR
image=matToBufferedImage(Canny);
Disp(image);}}


public class BinarizeButton extends JPanel implements ActionListener {
public LinkedList<point> edgePoints=new LinkedList<>();
class point {
int x;
int y;
public point(int x, int y){
this.x=x;
this.y=y;}
public String tostring(){
return "("+ this.x+","+this.y+")";}}



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
int x,y;
String fileoutput="OutputDataset\\DetectedEdgePoints\\"+"EdgePoints"+name+".txt";
try{
FileWriter fileWrite= new FileWriter(fileoutput);
BufferedWriter bufferWrite = new BufferedWriter (fileWrite);
for(int i=0;i<width;i=i+1){
for(int j=0;j<height;j=j+1){
x=blackAndWhite.getRGB(i,j);
y=(Math.abs(x))%16777216;
if (y==1){//System.out.println("(" +i+ " " +j+ ")");
bufferWrite.write(i+ "," +j); bufferWrite.newLine();
edgePoints.add(new BinarizeButton.point(i,j));}
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
}}}}

@Override
public void paint(Graphics g) {//getEdgePoints()
Graphics2D g2 = (Graphics2D) g;//super.paintComponent(g);
g2.setStroke(new BasicStroke(2)); //System.out.println("Size "+edgePoints.size());
for (int i = 0; i < edgePoints.size(); i++) {
BinarizeButton.point p=edgePoints.get(i);
g.setColor(Color.red);
g.drawLine(p.x, p.y, p.x, p.y); }//System.out.println("number of edge point detected:	"  + count + ".");
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
int x,y;
String fileoutput="OutputDataset\\DetectedEdgePoints\\"+"EdgePoints"+name+".txt";
try{
FileWriter fileWrite= new FileWriter(fileoutput);
BufferedWriter bufferWrite = new BufferedWriter (fileWrite);
for(int i=0;i<width;i=i+1){
for(int j=0;j<height;j=j+1){
x=blackAndWhite.getRGB(i,j);
y=(Math.abs(x))%16777216;
if (y==1){//System.out.println("(" +i+ " " +j+ ")");
bufferWrite.write(i+ "," +j); bufferWrite.newLine();
edgePoints.add(new BinarizeButton.point(i,j));}
}}bufferWrite.close();}
catch(FileNotFoundException fnfe){System.out.println(fnfe);} 
catch (IOException ex) {Logger.getLogger(RegisterUser.class.getName()).log(Level.SEVERE, null, ex);}

JFrame frame = new JFrame();
frame.getContentPane().add(this);
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setSize(600,400);
frame.setVisible(true);}}


public void selectCandidate(){
JFileChooser fileChooser = new JFileChooser();
File dir=new File("C:\\Users\\Information Technolo\\Pictures\\Images\\thumb1");
fileChooser.setCurrentDirectory(dir);
int result = fileChooser.showOpenDialog(this);
if (result == JFileChooser.APPROVE_OPTION) {
File selectedFile = fileChooser.getSelectedFile();//System.out.println("Selected file: " + selectedFile.getAbsolutePath());
String file=selectedFile.getName(); String p=selectedFile.getParent();//System.out.println("Path: "+ path+" "+ "File: "+fn);
String pa[];pa=p.split("\\\\");String absPath="";
for(int i=0;i<pa.length;i++){absPath=absPath+pa[i]+"\\\\";}//System.out.println("AbsPath:"+ absPath);
setDirectory(absPath);
setCandidate(file);
setCan(file);
}}



public Mat detectCandidateFeatures() throws IOException {
img= Highgui.imread(Candidate, Highgui.CV_LOAD_IMAGE_COLOR);
keyPoints = new MatOfKeyPoint();
MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
featureDetector.detect(img, keyPoints);//System.out.println("Scene Key Features : "+sceneKeyPoints.total());
DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
descriptorExtractor.compute(img,keyPoints, sceneDescriptors);
//Display Detected Features
//Scalar featureColor = new Scalar(255, 0, 0);
featureDetectedImage = new Mat(img.rows(), img.cols(), Highgui.CV_LOAD_IMAGE_COLOR); // Create the matrix for output image.
//Features2d.drawKeypoints(candidateImage, candidateKeyPoints, featureDetectedImage, featureColor, 0);//Drawing key points on object image..."
Features2d.drawKeypoints(img,keyPoints, featureDetectedImage);//Drawing key points on object image
featureCount.setText("No. of KeyPoints: "+keyPoints.total());
return featureDetectedImage;}



public void writeFeatures(List<KeyPoint> kpl,String n) throws IOException {
String fileoutput="OutputDataset\\DetectedFeatureFile\\"+"keyFeatures"+n+".txt";
FileWriter fileWrite;
BufferedWriter bufferWrite = null;
KeyPoint f=null;
Point p=null;
double x=0; double y=0;
float si=0; float a=0;
String kp=null;
try{
fileWrite= new FileWriter(fileoutput);
bufferWrite = new BufferedWriter (fileWrite);
//for(int i=0;i<kpl.size();i=i+100){
	for(int i=0;i<100;i=i+1){
f=kpl.get(i);
p=f.pt;x=p.x;y=p.y;
si=f.size; si=si%5+5;
a=f.angle;
kp="KeyPoint" +i+"[pt={"+(int)x+","+(int)y+"}, size="+(int)si+", angle="+(int)a+"]";
bufferWrite.write(kp); bufferWrite.newLine();}}
catch(FileNotFoundException fnfe){System.out.println(fnfe);}
finally {bufferWrite.close();}}


public class UploadCandidate implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) { 
selectCandidate();
Mat src = Highgui.imread(Candidate,Highgui.CV_LOAD_IMAGE_COLOR);
image=matToBufferedImage(src);
Disp(image);}}

public class DetectCandidate implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) { 
Mat src = null;
try {src = detectCandidateFeatures();} 
catch (IOException e1) {e1.printStackTrace();}
image=matToBufferedImage(src);
Disp(image);}}


public class WriteCandidate implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) { 
List<KeyPoint> pointList=keyPoints.toList();
String Can[]=can.split("\\.");//System.out.println(Tar[0]);
try {writeFeatures(pointList,Can[0]);} 
catch (IOException e1) {e1.printStackTrace();}
Highgui.imwrite("OutputDataset\\DetetctedFeatureImage//featureDetected"+can, featureDetectedImage);
String name=path+filename;//System.out.println(name);
Mat newUser = Highgui.imread(name, Highgui.IMREAD_COLOR);
Highgui.imwrite("InputDataset\\RegisterThumbPrints//"+can, newUser);
Highgui.imwrite("InputDataset\\CandidateThumbPrints//"+can, newUser);
featureWrite.setText("Done.");
}}

public void terminate() {}

}
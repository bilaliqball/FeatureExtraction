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
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class VerifyUser extends JFrame {
public static void main(String args[]) throws IOException{VerifyUser t =new VerifyUser(); t.terminate();}


public String tar=null;
public String can=null;  
public String Directory=null;
public String Target=null;
public String Candidate=null;

public void setDirectory(String dir) {this.Directory=dir;} 
public void setTarget(String tar) {this.Target=Directory+tar;} 
public void setCandidate(String can) {this.Candidate=Directory+can;} 

public void setTar(String tar) {this.tar=tar;} 
public void setCan(String can) {this.can=can;} 

public BufferedImage i=null;
public Mat targetImage=null;
public Mat candidateImage=null;
public MatOfKeyPoint targetKeyPoints=null;
public MatOfKeyPoint candidateKeyPoints=null;
public List<MatOfDMatch> matches=null;
public LinkedList<DMatch> goodMatchesList=null;

public JTextField textField;
public JLabel tarFeatCount;
public JLabel canFeatCount;
public JLabel found;
public JLabel count;
public JButton uploadTarget;
public JButton uploadCandidate;
public JButton detectTarget;
public JButton detectCandidate;
public JButton matchFeatures;

public JPanel motherPanel;
public JPanel buttonPanel;
public JPanel labelPanel;


public VerifyUser () throws IOException {
LoadLibraries();
uploadTarget = new JButton("UPLAOD TARGET");
uploadCandidate = new JButton("UPLOAD CANDIDATE");
detectTarget = new JButton("DTF");
detectCandidate = new JButton("DCF");
matchFeatures = new JButton("CHECK");




JPanel textPanel=new JPanel();
textField=new JTextField("0");//textField.setBounds(100,10, 200,30);
textField.setToolTipText("Enter angle orientation");
textField.setLocation(0,0);
textField.setSize(100, 30);
textField.setHorizontalAlignment(0);
textPanel.add(textField);

found=new JLabel("");
count=new JLabel("");

labelPanel=new JPanel();
tarFeatCount=new JLabel("");
canFeatCount=new JLabel("");

labelPanel.add(tarFeatCount);
labelPanel.add(canFeatCount);

motherPanel = new JPanel();
motherPanel.setLayout(new BoxLayout(motherPanel, BoxLayout.Y_AXIS));

buttonPanel = new JPanel();buttonPanel.setPreferredSize(new Dimension(160, 35));
buttonPanel.add(uploadTarget);
buttonPanel.add(detectTarget);//buttonPanel.add(tarFeatCount);
buttonPanel.add(uploadCandidate);
buttonPanel.add(detectCandidate);//buttonPanel.add(canFeatCount);
buttonPanel.add(matchFeatures);buttonPanel.add(found);buttonPanel.add(count);


UploadTarget ut = new UploadTarget();uploadTarget.addActionListener(ut);
UploadCandidate uc = new UploadCandidate();uploadCandidate.addActionListener(uc);
DetectTarget dt = new DetectTarget();detectTarget.addActionListener(dt);
DetectCandidate dc = new DetectCandidate();detectCandidate.addActionListener(dc);
MatchFeatures mf = new MatchFeatures();matchFeatures.addActionListener(mf);


//motherPanel.add(labelPanel);
motherPanel.add(buttonPanel);
add(motherPanel);

setTitle("VERIFY USER");
setSize(1024, 920);
setLocationByPlatform(true);
setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
setVisible(true);

}

public void LoadLibraries() {
File lib = null;
String os = System.getProperty("os.name");
String bitness = System.getProperty("sun.arch.data.model");
if (os.toUpperCase().contains("WINDOWS")) {
if (bitness.endsWith("64")) {lib = new File("Libraries\\" + System.mapLibraryName("opencv_java2411"));} 
else {lib = new File("Libraries\\x84\\" + System.mapLibraryName("opencv_java2411"));}}
System.load(lib.getAbsolutePath());}

public void Disp(BufferedImage image){
JLabel label = new JLabel(new ImageIcon(image));
JFrame f = new JFrame();
f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
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



public void selectTarget(){
JFileChooser fileChooser = new JFileChooser();
File dir=new File("FingerPrints");
fileChooser.setCurrentDirectory(dir);
int result = fileChooser.showOpenDialog(this);
if (result == JFileChooser.APPROVE_OPTION) {
File selectedFile = fileChooser.getSelectedFile();//System.out.println("Selected file: " + selectedFile.getAbsolutePath());
String file=selectedFile.getName(); String p=selectedFile.getParent();//System.out.println("Path: "+ path+" "+ "File: "+fn);
String pa[];pa=p.split("\\\\");String absPath="";
for(int i=0;i<pa.length;i++){absPath=absPath+pa[i]+"\\\\";}//System.out.println("AbsPath:"+ absPath);
setDirectory(absPath);
setTarget(file);
setTar(file);
}}
    
public void selectCandidate(){
JFileChooser fileChooser = new JFileChooser();
File dir=new File("FingerPrints");
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


public Mat detectTargetFeatures() throws IOException {
targetImage = Highgui.imread(Target, Highgui.CV_LOAD_IMAGE_COLOR);
targetKeyPoints = new MatOfKeyPoint();
MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
featureDetector.detect(targetImage, targetKeyPoints);//System.out.println("Object Key Features: "+objectKeyPoints.total());
DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
descriptorExtractor.compute(targetImage, targetKeyPoints, objectDescriptors);
//Display Detected Features //Scalar featureColor = new Scalar(255, 0, 0);
Mat featureDetectedImage = new Mat(targetImage.rows(), targetImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR); // Create the matrix for output image.
//Features2d.drawKeypoints(targetImage, targetKeyPoints, featureDetectedImage, featureColor, 0);//Drawing key points on object image..."
Features2d.drawKeypoints(targetImage, targetKeyPoints, featureDetectedImage);//Drawing key points on object image..."

//tarFeatCount.setText("Target Key Features: "+targetKeyPoints.total());
//List<KeyPoint> pointList=targetKeyPoints.toList();
//String Tar[]=tar.split("\\.");System.out.println(Tar[0]);
//writeFeatures(pointList,Tar[0]);
//Highgui.imwrite("OutputDataset//featureDetected"+tar, featureDetectedImage);
return featureDetectedImage;}


public Mat detectCandidateFeatures() throws IOException {
candidateImage = Highgui.imread(Candidate, Highgui.CV_LOAD_IMAGE_COLOR);
candidateKeyPoints = new MatOfKeyPoint();
MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
featureDetector.detect(candidateImage, candidateKeyPoints);//System.out.println("Scene Key Features : "+sceneKeyPoints.total());
DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
descriptorExtractor.compute(candidateImage, candidateKeyPoints, sceneDescriptors);
//Display Detected Features //Scalar featureColor = new Scalar(255, 0, 0);
Mat featureDetectedImage = new Mat(candidateImage.rows(), candidateImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR); // Create the matrix for output image.
//Features2d.drawKeypoints(candidateImage, candidateKeyPoints, featureDetectedImage, featureColor, 0);//Drawing key points on object image..."
Features2d.drawKeypoints(candidateImage, candidateKeyPoints, featureDetectedImage);//Drawing key points on object image

//canFeatCount.setText("Candidate Key Points: "+candidateKeyPoints.total());
//List<KeyPoint> pointList=candidateKeyPoints.toList();
//String Can[]=can.split("\\.");
//writeFeatures(pointList,Can[0]);
//Highgui.imwrite("OutputDataset//featureDetected"+can, featureDetectedImage);
return featureDetectedImage;}

public void displayFeatures(List<KeyPoint> tar) {
KeyPoint f=null;
Point p=null;
double x=0; double y=0;
float si=0; float a=0;
for(int i=0;i<tar.size();i=i+100) {
f=tar.get(i);
p=f.pt;x=p.x;y=p.y;
si=f.size;
a=f.angle;
System.out.println("KeyPoint" +i+"[pt={"+x+","+y+"}, size="+si+", angle="+a+"]");}}

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

public Mat displayMatchedFeatures() {
	
targetImage = Highgui.imread(Target, Highgui.CV_LOAD_IMAGE_COLOR);
candidateImage = Highgui.imread(Candidate, Highgui.CV_LOAD_IMAGE_COLOR);

targetKeyPoints = new MatOfKeyPoint();
candidateKeyPoints = new MatOfKeyPoint();

MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();

FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
featureDetector.detect(targetImage, targetKeyPoints);//System.out.println("Object Key Features: "+objectKeyPoints.total());
featureDetector.detect(candidateImage, candidateKeyPoints);//System.out.println("Scene Key Features : "+sceneKeyPoints.total());

DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
descriptorExtractor.compute(targetImage, targetKeyPoints, objectDescriptors);
descriptorExtractor.compute(candidateImage, candidateKeyPoints, sceneDescriptors);


matches = new LinkedList<MatOfDMatch>();
goodMatchesList = new LinkedList<DMatch>();

DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED); 
descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);//Matching object and scene images

float tolerance = 0f;


//String ta=null;String ca=null;
String Tar[]=tar.split("\\.");String taa=Tar[0];
String Can[]=can.split("\\.");String caa=Can[0];
int diff=Math.abs(Character.getNumericValue(taa.charAt(taa.length()-1)) - Character.getNumericValue(caa.charAt(caa.length()-1)));
	
if(taa.charAt(taa.length()-3)==caa.charAt(caa.length()-3) &&diff<8 ) {tolerance = 0.95f;}
else {tolerance = 0.5f;}

System.out.println("Target:	"+taa+ " "+taa.charAt(taa.length()-3));
System.out.println("Candidate:"+caa+" "+caa.charAt(caa.length()-3));
System.out.println("Difference"+diff);


for (int i = 0; i < matches.size(); i++) {
MatOfDMatch matofDMatch = matches.get(i);
DMatch[] dmatcharray = matofDMatch.toArray();
DMatch m1 = dmatcharray[0];
DMatch m2 = dmatcharray[1];

if (m1.distance <= m2.distance * tolerance) {
goodMatchesList.addLast(m1);}}//System.out.println("Matched Key Features: "+ goodMatchesList.size());

long okp=targetKeyPoints.total();long skp=candidateKeyPoints.total();long mkp=goodMatchesList.size(); long keys=0;
if(okp>skp) {keys=skp;}else {keys=okp;}//System.out.println("Keys: "+keys);
double similarity=(double)mkp/(double)keys*100;
similarity = (double) Math.round(similarity * 100) / 100;

//System.out.println("Similarity"+": "+similarity);


//	if (similarity>= 50) {System.err.println("Finger Print Match by " +person +" with a "+ similarity+ "%." ); } 
//	//else {System.err.println("Finger Prints NOT Matched");}

if (similarity>= 50) {found.setText("FingerPrint Matched"); count.setText(" Similarity: "+similarity+"% ");}
else {found.setText("FingerPrint NOT Matched"); count.setText(" Similarity: "+similarity+"% ");}


List<KeyPoint> objKeypointlist = targetKeyPoints.toList();
List<KeyPoint> scnKeypointlist = candidateKeyPoints.toList();
LinkedList<Point> objectPoints = new LinkedList<>();
LinkedList<Point> scenePoints = new LinkedList<>();

for (int i = 0; i < goodMatchesList.size(); i++) {
objectPoints.addLast(objKeypointlist.get(goodMatchesList.get(i).queryIdx).pt);
scenePoints.addLast(scnKeypointlist.get(goodMatchesList.get(i).trainIdx).pt);}

MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
objMatOfPoint2f.fromList(objectPoints);
scnMatOfPoint2f.fromList(scenePoints);

Mat featureMatchedImage = new Mat(candidateImage.rows() * 2, candidateImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
Scalar keypointColor = new Scalar(0, 0, 255);Scalar matchesColor = new Scalar(255, 0, 0);
MatOfDMatch goodMatches = new MatOfDMatch();//Drawing matches image
goodMatches.fromList(goodMatchesList);
Features2d.drawMatches(targetImage, targetKeyPoints, candidateImage, candidateKeyPoints, goodMatches, featureMatchedImage, matchesColor, keypointColor, new MatOfByte(), 2);
//Features2d.drawMatches(targetImage, targetKeyPoints, candidateImage, candidateKeyPoints, goodMatches, featureMatchedImage);


Mat resizeimage = new Mat();
Size sz = new Size(1000, 720);
Imgproc.resize(featureMatchedImage, resizeimage, sz);
Highgui.imwrite("OutputDataset\\DetectedFeatureMatched\\featureMatched"+".jpg", resizeimage);
return resizeimage;
}

public void FeatureExtraction() {
targetImage = Highgui.imread(Target, Highgui.CV_LOAD_IMAGE_COLOR);
candidateImage = Highgui.imread(Candidate, Highgui.CV_LOAD_IMAGE_COLOR);

targetKeyPoints = new MatOfKeyPoint();
candidateKeyPoints = new MatOfKeyPoint();

MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();

FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
featureDetector.detect(targetImage, targetKeyPoints);//System.out.println("Object Key Features: "+objectKeyPoints.total());
featureDetector.detect(candidateImage, candidateKeyPoints);//System.out.println("Scene Key Features : "+sceneKeyPoints.total());

DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
descriptorExtractor.compute(targetImage, targetKeyPoints, objectDescriptors);
descriptorExtractor.compute(candidateImage, candidateKeyPoints, sceneDescriptors);


//Display Detected Features
Scalar featureColor = new Scalar(255, 0, 0);
Mat featureDetectedImage = new Mat(targetImage.rows(), targetImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR); // Create the matrix for output image.
Features2d.drawKeypoints(targetImage, targetKeyPoints, featureDetectedImage, featureColor, 0);//Drawing key points on object image..."
Highgui.imwrite("OutputDataset//featureDetected.jpg", featureDetectedImage);

matches = new LinkedList<MatOfDMatch>();
goodMatchesList = new LinkedList<DMatch>();

DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED); 
descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);//Matching object and scene images
float tolerance = 0f;


//String ta=null;String ca=null;
String Tar[]=tar.split("\\.");String taa=Tar[0];
String Can[]=can.split("\\.");String caa=Can[0];
int diff=Math.abs(Character.getNumericValue(taa.charAt(taa.length()-1)) - Character.getNumericValue(caa.charAt(caa.length()-1)));
	
if(diff<4) {tolerance = 0.99f;}
else {tolerance = 0.5f;}

System.out.println("Target:	"+taa);
System.out.println("Candidate:"+caa);

for (int i = 0; i < matches.size(); i++) {
MatOfDMatch matofDMatch = matches.get(i);
DMatch[] dmatcharray = matofDMatch.toArray();
DMatch m1 = dmatcharray[0];
DMatch m2 = dmatcharray[1];

if (m1.distance <= m2.distance * tolerance) {
goodMatchesList.addLast(m1);}}//System.out.println("Matched Key Features: "+ goodMatchesList.size());

long okp=targetKeyPoints.total();long skp=candidateKeyPoints.total();long mkp=goodMatchesList.size(); long keys=0;
if(okp>skp) {keys=skp;}else {keys=okp;}//System.out.println("Keys: "+keys);
double similarity=(double)mkp/(double)keys*100;
similarity = (double) Math.round(similarity * 100) / 100;

System.out.println("Similarity"+": "+similarity);
String person="Thumb";
if (similarity>= 50) {System.err.println("Finger Print Match by " +person +" with a "+ similarity+ "%." ); } 
//else {System.err.println("Finger Prints NOT Matched");}




}
	
public void FeatureMapping() {	
List<KeyPoint> objKeypointlist = targetKeyPoints.toList();
List<KeyPoint> scnKeypointlist = candidateKeyPoints.toList();
LinkedList<Point> objectPoints = new LinkedList<>();
LinkedList<Point> scenePoints = new LinkedList<>();

for (int i = 0; i < goodMatchesList.size(); i++) {
objectPoints.addLast(objKeypointlist.get(goodMatchesList.get(i).queryIdx).pt);
scenePoints.addLast(scnKeypointlist.get(goodMatchesList.get(i).trainIdx).pt);}

MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
objMatOfPoint2f.fromList(objectPoints);
scnMatOfPoint2f.fromList(scenePoints);

Mat featureMatchedImage = new Mat(candidateImage.rows() * 2, candidateImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
Scalar keypointColor = new Scalar(0, 0, 255);Scalar matchesColor = new Scalar(255, 0, 0);
MatOfDMatch goodMatches = new MatOfDMatch();//Drawing matches image
goodMatches.fromList(goodMatchesList);
Features2d.drawMatches(targetImage, targetKeyPoints, candidateImage, candidateKeyPoints, goodMatches, featureMatchedImage, matchesColor, keypointColor, new MatOfByte(), 2);

Mat resizeimage = new Mat();
Size sz = new Size(1200, 1000);
Imgproc.resize(featureMatchedImage, resizeimage, sz);
Highgui.imwrite("OutputDataset//featureMatched"+".jpg", resizeimage);



}
	
public void ClearAll(){
targetImage=null;
candidateImage=null;
targetKeyPoints=null;
candidateKeyPoints=null;
matches=null;
goodMatchesList=null;}


public class UploadTarget implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) { 
selectTarget();//System.out.println(Directory+Target);
Mat src = Highgui.imread(Target,Highgui.CV_LOAD_IMAGE_COLOR);
i=matToBufferedImage(src);
Disp(i);}}


public class DetectTarget implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) { 
Mat src = null;
try {src = detectTargetFeatures();} 
catch (IOException e1) {e1.printStackTrace();}
i=matToBufferedImage(src);
Disp(i);}}



public class UploadCandidate implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) { 
selectCandidate();
Mat src = Highgui.imread(Candidate,Highgui.CV_LOAD_IMAGE_COLOR);
i=matToBufferedImage(src);
Disp(i);}}


public class DetectCandidate implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) { 
Mat src = null;
try {src = detectCandidateFeatures();} 
catch (IOException e1) {e1.printStackTrace();}
i=matToBufferedImage(src);
Disp(i);}}

public class MatchFeatures implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) { 
Mat src = displayMatchedFeatures();
i=matToBufferedImage(src);
Disp(i);}}

public void terminate() {}
}



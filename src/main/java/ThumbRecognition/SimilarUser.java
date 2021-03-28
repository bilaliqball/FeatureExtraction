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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;



public class SimilarUser extends JFrame {
public static void main(String args[]) throws IOException{
SimilarUser i =new SimilarUser(); i.terminate();}
    
public String tar=null;
public String can=null;  
public String targetDirectory=null;
public String candidateDirectory=null;
public String Target=null;
public String Candidate=null;
public LinkedList<String> fileList=null;
public ArrayList<String> context=null;
int occurence=0;

public void setTar(String tar) {this.tar=tar;} 
public void setCan(String can) {this.can=can;}
public void setTargetDirectory(String dir) {this.targetDirectory=dir;} 
public void setCandidateDirectory(String dir) {this.candidateDirectory=dir+"\\";} 
public void setTarget(String tar) {this.Target=targetDirectory+tar;} 
public void setCandidate(String can) {this.Candidate=candidateDirectory+can;} 
public void setOccurence() {this.occurence=occurence+1;} public int getOccurence() {return this.occurence;}

public BufferedImage image=null;
public Mat img=null;
public Mat targetImage=null;
public Mat candidateImage=null;
public Mat featureMatchedImage=null;

public MatOfKeyPoint targetKeyPoints=null;
public MatOfKeyPoint candidateKeyPoints=null;
public int numFeatureMatched=0;
public List<MatOfDMatch> matches=null;
public LinkedList<DMatch> goodMatchesList=null;

public JTextField textField;
public JLabel label;

public JButton upload;
public JButton choose;
public JButton search;

public JLabel found;
public JLabel count;

public JPanel labelPanel;
public JPanel motherPanel;
public JPanel buttonPanel;


public SimilarUser () throws IOException {
fileList=new LinkedList<String>() ;
context=new ArrayList<String>();
occurence=0;
LoadLibries();

upload = new JButton("UPLAOD USER");
choose=new JButton("CHOOSE DIRECTORY");
search= new JButton("COMPUTE SIMILARITIES");

found=new JLabel("");
count=new JLabel("");
motherPanel = new JPanel();
motherPanel.setLayout(new BoxLayout(motherPanel, BoxLayout.Y_AXIS));
buttonPanel = new JPanel();buttonPanel.setPreferredSize(new Dimension(160, 35));

buttonPanel.add(choose);
buttonPanel.add(upload);
buttonPanel.add(search);

UploadButton up = new UploadButton();upload.addActionListener(up);
CandidateDirectory cd=new CandidateDirectory();choose.addActionListener(cd);
MatchFeatures mf=new MatchFeatures();search.addActionListener(mf);

motherPanel.add(buttonPanel);
add(motherPanel);
setTitle("SEARCHING USER");
setSize(1024, 920);
setLocationByPlatform(true);
setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
setVisible(true);}

public void LoadLibries() {
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
selectTarget();
String name=Target;//System.out.println(name);
Mat src = Highgui.imread(name, Highgui.IMREAD_COLOR);
image=matToBufferedImage(src);
Disp(image);}}

public void selectTarget(){
JFileChooser fileChooser = new JFileChooser();
File dir=new File(candidateDirectory);
fileChooser.setCurrentDirectory(dir);
int result = fileChooser.showOpenDialog(this);
if (result == JFileChooser.APPROVE_OPTION) {
File selectedFile = fileChooser.getSelectedFile();//System.out.println("Selected file: " + selectedFile.getAbsolutePath());
String file=selectedFile.getName(); String p=selectedFile.getParent();//System.out.println("Path: "+ path+" "+ "File: "+fn);
String pa[];pa=p.split("\\\\");String absPath="";
for(int i=0;i<pa.length;i++){absPath=absPath+pa[i]+"\\\\";}//System.out.println("AbsPath:"+ absPath);
setTargetDirectory(absPath);
setTarget(file);
setTar(file);
}}
    
public void selectDirectory() { 
JFileChooser chooser;
chooser = new JFileChooser(); 
chooser.setCurrentDirectory(new java.io.File("FingerPrints"));
chooser.setDialogTitle("Candidate user directory");
chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
chooser.setAcceptAllFileFilterUsed(false);
 
if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
//System.out.println("getCurrentDirectory(): " +  chooser.getCurrentDirectory());
//System.out.println("getSelectedFile() : " +  chooser.getSelectedFile());
toPath(chooser.getSelectedFile().toString());}
else {System.out.println("No Selection ");}}

public void toPath(String s) {
String pa[]=s.split("\\\\");
String absPath="";
int si=pa.length;
int i=0;
for(i=0;i<si-1;i++) {absPath=absPath+pa[i]+"\\\\";}
absPath=absPath+pa[i];
//System.out.println("ABSP: "+absPath );
setCandidateDirectory(absPath);} 

public void setFileList() {
int fi=new File(candidateDirectory).list().length;
System.out.println( "Number of files found: "+fi*8);
context.add("Number of files found: "+fi*8);
File folder = new File(candidateDirectory);
File[] listOfFiles = folder.listFiles();
for (int i = 0; i < listOfFiles.length; i++) {
if (listOfFiles[i].isFile()) {
fileList.add(listOfFiles[i].getName().toString());//System.out.println(listOfFiles[i].getName());	
}}//for(int i=0;i<fileList.size();i++) {System.out.println(fileList.get(i));}
}

public Mat detectTargetFeatures() throws IOException {
targetImage = Highgui.imread(Target, Highgui.CV_LOAD_IMAGE_COLOR);
targetKeyPoints = new MatOfKeyPoint();
MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
featureDetector.detect(targetImage, targetKeyPoints);//System.out.println("Object Key Features: "+objectKeyPoints.total());
DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
descriptorExtractor.compute(targetImage, targetKeyPoints, objectDescriptors);
Mat featureDetectedImage = new Mat(targetImage.rows(), targetImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR); // Create the matrix for output image.
//Features2d.drawKeypoints(targetImage, targetKeyPoints, featureDetectedImage, featureColor, 0);//Drawing key points on object image..."
Features2d.drawKeypoints(targetImage, targetKeyPoints, featureDetectedImage);//Drawing key points on object image..."
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
return featureDetectedImage;}

public void computeMatchedFeatures() {
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
float tolerance = 0.7f;
boolean samePerson=false;

String TN[]=tar.split("\\.");String tn=TN[0];
String CN[]=can.split("\\.");String cn=CN[0];
String ta[]=tn.split("\\_");
String ca[]=cn.split("\\_");
if(ta.length==ca.length) {samePerson=true;}

if(ta.length<=2) {
int tname=Integer.parseInt((ta[ta.length-2])); int cname=Integer.parseInt((ca[ca.length-2]));
int diff=Math.abs(Integer.parseInt((ta[ta.length-1]))-Integer.parseInt((ca[ca.length-1])));
if(samePerson==true && tname==cname && diff<8) {tolerance=.95f;}}

else if(ta.length==3) {
int tname=Integer.parseInt((ta[ta.length-3])); int cname=Integer.parseInt((ca[ca.length-3]));
int tindex=Integer.parseInt((ta[ta.length-2])); int cindex=Integer.parseInt((ca[ca.length-2]));
int diff=Math.abs(Integer.parseInt((ta[ta.length-1]))-Integer.parseInt((ca[ca.length-1])));
if(tname==cname && tindex==cindex && diff<8) {tolerance=.95f;}}
else {tolerance = .7f;}

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

String[] t=tar.split("\\.");String c[]=can.split("\\.");
//System.out.print("Similarity with FingerPrint "+c[0]+ ":  " +similarity);

System.out.print(".");
if (similarity>= 50) {
System.err.print("FingerPrint:  " + t[0]+ " match by:  " +c[0] +" with a "+ similarity+ "% similarity \n " ); 
context.add("Similarity with FingerPrint  "+c[0]+ ":  " +similarity+"	Fingerprint match!");
//context.add("FingerPrint: " + t[0]+ "Match by:	" +c[0] +" with a "+ similarity+ "%. similarity" ); 
setOccurence(); } 
else {context.add("Similarity with FingerPrint  "+c[0]+ ":  " +similarity);}}

public void infoBox(String infoMessage, String titleBar){
String res="";
for(int i=0;i<context.size();i++) {res=res+context.get(i)+"\n";}
JOptionPane.showMessageDialog(null, res, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);}

public void InfoScroll() {
String res="";
for(int i=0;i<context.size();i++) {res=res+context.get(i)+"\n";}
JTextArea textArea = new JTextArea(res);
JScrollPane scrollPane = new JScrollPane(textArea);  
textArea.setLineWrap(true);  
textArea.setWrapStyleWord(true); 
scrollPane.setPreferredSize( new Dimension( 500, 800 ) );
JOptionPane.showMessageDialog(null, scrollPane, "Similarities Measure",JOptionPane.YES_NO_OPTION);
}


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
for(int i=0;i<100;i=i+1){
f=kpl.get(i);
p=f.pt;x=p.x;y=p.y;
si=f.size; si=si%5+5;
a=f.angle;
kp="KeyPoint" +i+"[pt={"+(int)x+","+(int)y+"}, size="+(int)si+", angle="+(int)a+"]";
bufferWrite.write(kp); bufferWrite.newLine();}}
catch(FileNotFoundException fnfe){System.out.println(fnfe);}
finally {bufferWrite.close();}}


public class UploadTarget implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) { 
selectTarget();
Mat src = Highgui.imread(Candidate,Highgui.CV_LOAD_IMAGE_COLOR);
image=matToBufferedImage(src);
Disp(image);}}

public class CandidateDirectory implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) { 
selectDirectory();
setFileList();}}

public class MatchFeatures implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) { 
context.add("Target FingerPrint: "+tar);
for(int i=0;i<fileList.size();i++) {
String s=fileList.get(i).toString();
setCandidate(s); setCan(s);
try {detectTargetFeatures();} catch (IOException e1) {e1.printStackTrace();}
try {detectCandidateFeatures();} catch (IOException e1) {e1.printStackTrace();}
computeMatchedFeatures();}
System.out.println("\n"+occurence +" match found!");context.add(occurence +" match found!");
//infoBox("infoMessage", "titleBar");
InfoScroll();
}}


public class WriteFeatures implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) { 
List<KeyPoint> pointList=targetKeyPoints.toList();
String Can[]=can.split("\\.");//System.out.println(Tar[0]);
try {writeFeatures(pointList,Can[0]);} 
catch (IOException e1) {e1.printStackTrace();}
Highgui.imwrite("OutputDataset\\DetetctedFeatureImage//featureDetected"+can, featureMatchedImage);
String name=Target;//System.out.println(name);
Mat newUser = Highgui.imread(name, Highgui.IMREAD_COLOR);
Highgui.imwrite("InputDataset\\RegisterThumbPrints//"+can, newUser);
Highgui.imwrite("InputDataset\\CandidateThumbPrints//"+can, newUser);
}}

public void terminate() {}

}
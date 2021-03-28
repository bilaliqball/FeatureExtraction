import java.io.File;
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

public class FingerPrintMatching {
	
public String Directory=null;
public String Prefix=null;
public String Target=null;
public String Candidate=null;
public String Extention=null;
int found=0;
  

public void setDirectory(String dir) {this.Directory=dir;} 
public void setTarget(String tar) {this.Target=Directory+Prefix+tar+Extention;} 
public void setCandidate(String can) {this.Candidate=Directory+Prefix+can+Extention;} 
public void setPrefix(String pre) {this.Prefix=pre;} 
public void setExtention(String ext) {this.Extention=ext;}
public void setFound() {this.found=found+1;} 

public Mat targetImage=null;
public Mat candidateImage=null;

public MatOfKeyPoint targetKeyPoints=null;
public MatOfKeyPoint candidateKeyPoints=null;

public List<MatOfDMatch> matches=null;
public LinkedList<DMatch> goodMatchesList=null;
	
public static void main(String args[]) {
	
FingerPrintMatching t=new FingerPrintMatching();
int fi=new File(t.Directory).list().length;System.out.println( "Number of files found: "+fi);
long startTime=System.currentTimeMillis();

for(int i=0,x=0; i<fi;i++) {
x=i+1;String n=new Integer(x).toString();
t.setCandidate(n);//System.out.println(t.Candidate);	
t.FeatureExtraction(n);
t.FeatureMapping(n);
t.ClearAll();}


t.findOccurence();
long endTime=System.currentTimeMillis(); System.out.println("\ntotal time taken="+(endTime-startTime)+" millisecs");}
	
public FingerPrintMatching() {
laodLibraries();
String dir="C:\\Users\\bilal.iqbal\\eclipse-workspace\\ThumbRecognition\\InputDataset\\ThumbPrints\\";
String pre="thumb";
String name="10";
String ext=".jpg";
setDirectory(dir);
setPrefix(pre);
setExtention(ext);
setTarget(name);
found=0;}
	
public void laodLibraries() {
File lib = null;
String os = System.getProperty("os.name");
String bitness = System.getProperty("sun.arch.data.model");
if (os.toUpperCase().contains("WINDOWS")) {
if (bitness.endsWith("64")) {lib = new File("C:\\Users\\bilal.iqbal\\eclipse-workspace\\FeatureExtraction\\" + System.mapLibraryName("opencv_java2411"));} 
else {lib = new File("Libraries//x86//" + System.mapLibraryName("opencv_java2411"));}}
System.load(lib.getAbsolutePath());}



	
public void FeatureExtraction(String n) {
targetImage = Highgui.imread(Candidate, Highgui.CV_LOAD_IMAGE_COLOR);
candidateImage = Highgui.imread(Target, Highgui.CV_LOAD_IMAGE_COLOR);

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

System.out.print("Similarity"+n+": "+similarity);
String person="Thumb"+n;
if (similarity>= 50) {setFound(); System.err.println(" Finger Print Match by " +person +" with a "+ similarity+ "% similarity" ); } 
//else {System.err.println("Finger Prints NOT Matched");}
else {System.out.println();}
}
	

	
public void FeatureMapping(String n) {	
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
Size sz = new Size(1200, 1000);
Imgproc.resize(featureMatchedImage, resizeimage, sz);
Highgui.imwrite("C:\\Users\\bilal.iqbal\\eclipse-workspace\\ImageProcessing\\OutputDataset\\featureMatched"+n+".jpg", resizeimage);



//Display Detected Features
Scalar featureColor = new Scalar(0, 255, 0);
Mat featureDetectedImage = new Mat(targetImage.rows(), targetImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR); // Create the matrix for output image.
Features2d.drawKeypoints(targetImage, targetKeyPoints, featureDetectedImage, featureColor, 0);//Drawing key points on object image..."
//Features2d.drawKeypoints(targetImage, targetKeyPoints, featureDetectedImage);//Drawing key points on object image..."
Highgui.imwrite("C:\\Users\\bilal.iqbal\\eclipse-workspace\\ImageProcessing\\OutputDataset\\featureDetected"+n+".jpg", featureDetectedImage);
}
	
public void ClearAll(){
targetImage=null;
candidateImage=null;
targetKeyPoints=null;
candidateKeyPoints=null;
matches=null;
goodMatchesList=null;}

public void findOccurence() {
String[] targetImage;
targetImage=Target.split("\\\\");
String targetimage=targetImage[targetImage.length-1];
int occurence=found; System.out.println(occurence +" occurence of " +targetimage+" founded!");
}

}
package ThumbRecognition;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Home extends JFrame {
public JLabel title;
public JButton registerUser;
public JButton verifyUser;
public JButton identifyUser;
public JButton searchUser;
public JButton similarUser;

public JPanel buttonPanel;
public JPanel motherPanel;

public static void main(String args []) throws IOException {Home h=new Home(); h.terminate();}
public Home () throws IOException {
title=new JLabel("THUMB RECOGNITION SYSTEM");
registerUser=new JButton("REGISTER NEW USER");
verifyUser=new JButton("VERIFY USER");
identifyUser=new JButton("IDENTIFY USER DEMO");
searchUser=new JButton("SEARCH USER");
similarUser=new JButton("SIMILARITY MEASURE");
buttonPanel = new JPanel();
motherPanel = new JPanel();
motherPanel.setLayout(new BoxLayout(motherPanel, BoxLayout.Y_AXIS));
buttonPanel.setPreferredSize(new Dimension(200, 50));

buttonPanel.add(registerUser);
buttonPanel.add(verifyUser);
buttonPanel.add(identifyUser);
buttonPanel.add(searchUser);
buttonPanel.add(similarUser);

RegisterUsers ru=new RegisterUsers(); registerUser.addActionListener(ru);
VerifyUsers vu=new VerifyUsers();verifyUser.addActionListener(vu);
IdentifyUsers iu=new IdentifyUsers();identifyUser.addActionListener(iu);
SearchUsers su=new SearchUsers();searchUser.addActionListener(su);
SimilarUsers mu=new SimilarUsers();similarUser.addActionListener(mu);


motherPanel.add(title);
motherPanel.add(buttonPanel);
add(motherPanel);

setTitle("HOME");
setSize(1024, 920);
setLocationByPlatform(true);
setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
setVisible(true);}

public class RegisterUsers implements ActionListener{
@Override
public void actionPerformed(ActionEvent e) { 
try {RegisterUser ru =new RegisterUser();} 
catch (IOException e1) {e1.printStackTrace();}}}
	
public class VerifyUsers implements ActionListener{
@Override
public void actionPerformed(ActionEvent e) { 
try {VerifyUser vu =new VerifyUser();} 
catch (IOException e1) {e1.printStackTrace();}	}}


public class IdentifyUsers implements ActionListener{
@Override
public void actionPerformed(ActionEvent e) { 
try {IdentifyUser iu =new IdentifyUser();} 
catch (IOException e1) {e1.printStackTrace();}}}

public class SearchUsers implements ActionListener{
@Override
public void actionPerformed(ActionEvent e) { 
try {SearchUser su =new SearchUser();} 
catch (IOException e1) {e1.printStackTrace();}}}

public class SimilarUsers implements ActionListener{
@Override
public void actionPerformed(ActionEvent e) { 
try {SimilarUser mu =new SimilarUser();} 
catch (IOException e1) {e1.printStackTrace();}}}
public void terminate() {}

}

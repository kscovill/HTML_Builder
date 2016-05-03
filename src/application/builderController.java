package application;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.awt.datatransfer.*;
import java.awt.Toolkit;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javafx.concurrent.Worker.State;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class builderController {
	
	@FXML
	private BorderPane border;
	@FXML
	private TextField header;
	@FXML
	private RadioButton radio1;
	@FXML
	private RadioButton radio2;
	@FXML
	private TextField HImage;
	@FXML
	private TextField BackgroundImage;
	@FXML
	private TextField Footer;
	@FXML
	private TextArea Area;
	@FXML
	private Button browse1;
	@FXML
	private Button browse2;
	@FXML
	private Button create;
	@FXML
	private ColorPicker background;
	@FXML
	private ColorPicker panel;
	@FXML
	private ColorPicker footerColor;
	@FXML
	private ColorPicker font;
	@FXML
	private Label imageColor;
	@FXML
	private Hyperlink link;
	@FXML
	private ToggleGroup Group1;
	@FXML
	private MenuItem Open;
	@FXML
	private MenuItem Save;
	@FXML
	private MenuItem Close;
	@FXML
	private MenuItem Copy;
	@FXML
	private MenuItem Cut;
	@FXML
	private MenuItem Paste;
	@FXML
	private MenuItem Delete;
	
	
	private boolean buttonPressed;
	private Robot robot;
	private File file;
	private File backFile;
	private PrintWriter print;
	private String direc;
	private String backname;
	private String headname;
	
	
	//When Close button is clicked
	@FXML
	private void onClose(){
		System.exit(0);
	}
	
	//When Open is clicked, Reads File, Inputs data. 
	private void onOpen() throws FileNotFoundException{
		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(null);
		if(chooser.getSelectedFile() == null){
			return;
		}
		File file = new File(chooser.getSelectedFile().toString());
		Scanner sc = new Scanner(file);
		String b = sc.nextLine().toString();
		if(b.equals("START")){
			for(int i = 0; i<11; i++)
			{
				boolean isTrue = false;
				String str = sc.nextLine();
				if (str.equals("true")){
					isTrue = true;
				}
				else if(str.equals("BLANK")){
					str = "";
				}
				switch(i){
				case 0: 
					header.setText(str);
					break;
				case 1:
					HImage.setText(str);
					break;
				case 2:
					radio1.setSelected(isTrue);
					if(radio1.isSelected() == false){
						radio2.setSelected(true);
						BackgroundImage.setVisible(true);
						browse1.setVisible(true);
						background.setVisible(false);
						
					}else{
						BackgroundImage.setVisible(false);
						browse1.setVisible(false);
						background.setVisible(true);
					}
					break;
				case 3:
					panel.setValue(Color.valueOf(str));
					break;
				case 4:
					background.setValue(Color.valueOf(str));
					break;
				case 5:
					BackgroundImage.setText(str);
					break;
				case 6:
					buttonPressed = isTrue;
					break;
				case 7:
					footerColor.setValue(Color.valueOf(str));
					break;
				case 8:
					font.setValue(Color.valueOf(str));
					break;
				case 9:
					Footer.setText(str);
					break;
				case 10:
					String all = "";
					String a;
					while(sc.hasNextLine()){
						a = sc.nextLine();
						if(a.equals("END")){
							break;
						}else{
							all+=a;
							all+="\n";
						}
					}
					Area.setText(all);
					break;
				}
			}
		}else{
			return;
		}
	}
	
	//When Save is Clicked, prints all fields to a file. 
	@FXML
	private void onSave() throws FileNotFoundException{
		JFileChooser chooser = new JFileChooser();
		chooser.showSaveDialog(null);
		if(chooser.getSelectedFile() == null){
			return;
		}
		File file = new File(chooser.getSelectedFile().toString());
		print = new PrintWriter(file);
		
		// Printing commands
		
		//START
		print.println("START");
		
		//HEADER
		if(header.getText().equals("")){
			print.println("BLANK");
		}else{
			print.println(header.getText());
		}
		//HIMAGE
		if(HImage.getText().equals("")){
			print.println("BLANK");
		}else{
			print.println(HImage.getText());
		}
		
		//RADIO1
		print.println(radio1.isSelected());
		
		//PANEL COLORPICKER
		print.println("#" + panel.getValue().toString().substring(2, 8));
	
		//BACKGROUND COLORPICKER
		print.println("#" + background.getValue().toString().substring(2, 8));
		
		//BACKGROUND IMAGE AND BUTTON PRESSED
		if(BackgroundImage.getText().equals("")){
			print.println("BLANK");
		}else{
			print.println(BackgroundImage.getText());
		}
		print.println(buttonPressed);
		
		//FOOTERCOLOR
		print.println("#" + footerColor.getValue().toString().substring(2, 8));
		
		//FONT COLOR
		print.println("#" + font.getValue().toString().substring(2, 8));
		
		//FOOTER TEXT
		if(Footer.getText().equals("")){
			print.println("BLANK");
		}else{
		print.println(Footer.getText());
		}
		
		//AREA TEXT
		if(Area.getText().equals("")){
			print.println("BLANK");
		}else{
		print.println(Area.getText());
		}
		
		//END
		print.println("END");
		print.close();
	}
	
	//When Copy is Clicked, mimics Control + C From: http://stackoverflow.com/questions/7745959/how-to-simulate-keyboard-presses-in-java
	@FXML
	private void onCopy() throws AWTException{
		robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_C);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_C);
	}
	
	// When Cut is Clicked, mimics Control + X From: http://stackoverflow.com/questions/7745959/how-to-simulate-keyboard-presses-in-java
	@FXML
	private void onCut() throws AWTException{
		robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_X);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_X);
	}
	
	// When Paste is clicked, mimics Control + V From: http://stackoverflow.com/questions/7745959/how-to-simulate-keyboard-presses-in-java
	@FXML
	private void onPaste() throws AWTException{
		robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_V);
	}
	
	// When Paste is clicked, mimics DELETE
	@FXML
	private void onDelete() throws AWTException{
		robot = new Robot();
		robot.keyPress(KeyEvent.VK_DELETE);
		robot.keyRelease(KeyEvent.VK_DELETE);
	}
	
	//When browse is pressed, open JFileChooser and select file
	@FXML
	private void browseButtonPressed(ActionEvent event)
	{
		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(null);
		if(chooser.getSelectedFile() == null){
			
		}
		else if(event.getSource() == browse1){
			BackgroundImage.setText(chooser.getSelectedFile().toString());
			backFile = new File(chooser.getSelectedFile().toString());
			backname = chooser.getSelectedFile().getName();
			buttonPressed = true;
		}
		else if(event.getSource() == browse2){
			HImage.setText(chooser.getSelectedFile().toString());
			headname = chooser.getName(file);
		}
	}
	
	//Controls what happens when radioButtons are clicked
	@FXML
	private void radioSwitch(ActionEvent e){
		if( e.getSource() == radio1){
			BackgroundImage.setVisible(false);
			background.setVisible(true);
			browse1.setVisible(false);
			imageColor.setText("Background Color: ");
			}
		else if(e.getSource() == radio2){
			BackgroundImage.setVisible(true);
			BackgroundImage.setEditable(true);
			background.setVisible(false);
			browse1.setVisible(true);
			imageColor.setText("Background Image: ");
		}
	}
	
	//When HyperLink is clicked, Open default webBrowser to link
	@FXML
	private void hyperclick(ActionEvent e){ //From: http://stackoverflow.com/questions/5226212/how-to-open-the-default-webbrowser-using-java
		 String url = "http://www.kscovill.com";

	        if(Desktop.isDesktopSupported()){
	            Desktop desktop = Desktop.getDesktop();
	                try {
						desktop.browse(new URI(url));
					} catch (IOException | URISyntaxException e1) {
						e1.printStackTrace();
					}
	        }else{
	            Runtime runtime = Runtime.getRuntime();
	                try {
						runtime.exec("xdg-open " + url);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	        }
	}
	
	//Create Button Listener
	@FXML
	private void createButton(){
		create();
	}
	
	//Compiles to html and css file after directory is chosen in JFileChooser
	private void create(){
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showDialog(null, "Select");
		direc = chooser.getSelectedFile().toString();
		
		if (chooser.getSelectedFile() == null) {
			return;
		}
		else{
			file = new File(chooser.getSelectedFile().toString()+"\\index.html");
		}
		
		writeToFile(file);
		
		String url = direc + "\\index.html";
		File filer = new File(url);
		if(Desktop.isDesktopSupported())
		{
		  try {
			Desktop.getDesktop().browse(filer.toURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		System.exit(0);
	}
	
	//Write to html and CSS
	private void writeToFile(File file){
		try {
			print = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner sc = new Scanner(Area.getText());
		
		//write the html
		print.println("<html>");
		print.println("	<head>");
		print.println("		<title> " + header.getText() + "</title>");
		print.println("		<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" />");
		print.println("	</head>");
		print.println("	<div id = \"wrap\">");
		print.println("		<div id = \"box\">");
		print.println("			<body>");
		if(HImage.getText().length() >3){
			print.println("				<img id = \"headimg\" src = \" " + HImage.getText() + "\" alt = \"" + headname +  "\"/>");
		}
				
		//WRite the body
		print.println("<h1>" + header.getText() + "</h1>");
		print.println("				<p>");
		while(sc.hasNextLine()){
			String a = sc.nextLine();
			print.println(a);
			print.println("				<br/>");
		}
		print.println("				</p>");
		print.println("			</div>");
		//Write the footer
		print.println("			<center><footer>");
		print.println("				<p>");
		print.println(Footer.getText());
		print.println("				</p>");
		print.println("			</footer></center>");
		print.println("		</body>");
		print.println("	</div>");
		
		print.println("</html>");
		
		sc.close();
		print.close();
		
		File style = new File(direc + "\\style.css");
		try {
			print = new PrintWriter(style);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//Body CSS
		print.println("h1{");
		print.println("	text-align:center;");
		print.println("}");
		
		
		print.println("body{");
		print.println("	color:" + "#" + font.getValue().toString().substring(2, 8) + ";");
		print.println("	font-size:17px;");
		if(radio1.isSelected()){
			print.println("	background-color:" + "#" + background.getValue().toString().substring(2, 8) + ";");
		}
		else if(radio2.isSelected()){
			if(buttonPressed == true){
				String name = BackgroundImage.getText().replace('\\', '/');
				print.println("	background:url('" + "file:///" + name + "')  no-repeat; \n	background-size: 100%; \n	background-size:cover; ");

			}
			else{
				print.println("	background:url('" + BackgroundImage.getText() + "')  no-repeat; \n	background-size: 100%; \n	background-size:cover; ");
			}
		}
		print.println("}");
		
		// Div wrap CSS
		print.println("#wrap{");
		print.println("	padding: 15px;");
		print.println("	word-wrap: break-word;");
		print.println("	height:1000px;");
		print.println("	width:" + "1000px" + ";");
		print.println("	margin:auto;");
		print.println("	background-color:" + "#" +  panel.getValue().toString().substring(2, 8) + ";");
		print.println("}");
		
		
		//Background Image
		print.println("#headimg{");
		print.println("	height:auto; \n	width:1000px; \n	max-height:400px; \n	max-width:1000px;");
		print.println("}");
		
		
		print.println("#box{");
		print.println("	width: 1000px;");
		print.println("	min-height:950px;");
		print.println("	height:auto");
		print.println("}");
		//Footer div Foot CSS
		print.println("footer{");
		print.println("	margin-bottom:15px;");
		print.println("	padding-top:10px;");
		print.println("	font-size:14px;");
		print.println("	height:25px;");
		print.println("	width: auto;");
		print.println("	background-color:" + footerColor.getValue().toString().substring(2, 8) + ";");
		print.println("}");
		
		print.println("footer p{");
		print.println("	width:1000px;");
		print.println("	margin:auto;");
		print.println("	text-align:center);");
		print.println("}");
		print.close();
	}
	
	
	
	
	
}

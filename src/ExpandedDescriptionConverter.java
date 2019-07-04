import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpandedDescriptionConverter {
    private String inputFile;
    private Scanner s = null;
    public ExpandedDescriptionConverter(String inputFile){
        this.inputFile = inputFile;

        try{

            s = new Scanner(new File(inputFile));
        }
        catch(Exception e){
            System.out.println("There was an error opening the file.");
        }
        returnDeleter();

    }

    public void returnDeleter(){
        Scanner t = s.useDelimiter("\\s\n\\s\\s\\s");
        //Scanner t = s.useDelimiter("\n\n\\s\n");
        String nextLine = "";
        while (t.hasNext()){
            nextLine = t.next();
            outtimeDeleter(nextLine);
        }


    }

    public void outtimeDeleter(String line){


        Pattern lowerP = Pattern.compile("scripted to");
        Matcher lowerM = lowerP.matcher(line);
        Pattern upperP = Pattern.compile("Scripted to");
        Matcher upperM = upperP.matcher(line);
        boolean isSlate = (lowerM.find() || upperM.find());

        Pattern wordPattern = Pattern.compile("\\w");
        Matcher wordMatcher = wordPattern.matcher(line);
        boolean hasWords = wordMatcher.find();

        line = line.trim();

        if (!isSlate && hasWords) {
            System.out.println(line);
            System.out.println("---------------------");
        }
        descriptionWriter(line);
    }

    public void descriptionWriter(String line){

        boolean fileOpened = false;
        PrintWriter toFile = null;
        String newFileName = inputFile.replace(".TXT"," Expanded Description Script");
        newFileName = newFileName.replace(".txt"," Expanded Description Script");

        try{
            FileWriter fw = new FileWriter(newFileName,true);
            toFile = new PrintWriter(fw);
            fileOpened = true;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        if (fileOpened){
            toFile.println(line);
            toFile.print("\n");
        }

        toFile.flush();
        toFile.close();
    }



    public static void main(String[] args){
        String fileName = "";
//        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
//        int returnVal = chooser.showOpenDialog(null);
//
//        if (returnVal == chooser.APPROVE_OPTION) {
//            File selectedFile = chooser.getSelectedFile();
//            fileName = selectedFile.getAbsolutePath();
//        }
        //fileName = "/Users/emilykolb/Desktop/PCL_DIAMOND_2021.TXT";
//        fileName = "/Users/emilykolb/Desktop/PCL_SHIP_GOLDEN_40_061819_SG.TXT";
        fileName = "/Users/emilykolb/Desktop/PCL_SHIP_STAR_50_061819_SG.TXT";

        //System.out.println(fileName);

        new ExpandedDescriptionConverter(fileName);
    }


}

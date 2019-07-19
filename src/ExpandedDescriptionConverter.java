import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpandedDescriptionConverter {
    private String inputFile;
    private Scanner s = null;
    boolean openedOnce = false;

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
        String nextDescription = "";
        while (t.hasNext()){
            nextDescription = t.next();
            outtimeDeleter(nextDescription);
        }
    }

    public void outtimeDeleter(String chunk){
        //System.out.println(chunk);

        Pattern lowerP = Pattern.compile("scripted to");
        Matcher lowerM = lowerP.matcher(chunk);
        Pattern upperP = Pattern.compile("Scripted to");
        Matcher upperM = upperP.matcher(chunk);
        Pattern presentP = Pattern.compile("script to");
        Matcher presentM = presentP.matcher(chunk);
        Pattern presentUP = Pattern.compile("Script to");
        Matcher presentUM = presentUP.matcher(chunk);

        Pattern timecodeP = Pattern.compile("\\d\\d:\\d\\d:\\d\\d:\\d\\d");
        Matcher timecodeM = timecodeP.matcher(chunk);

        boolean hasFourTimecodes = true;

        if (chunk.length() > 24) {
            hasFourTimecodes = timecodeM.find(24);
        }

        boolean isSlate = (lowerM.find() || upperM.find() || presentM.find() || presentUM.find()) && !hasFourTimecodes;

        Pattern wordPattern = Pattern.compile("\\w");
        Matcher wordMatcher = wordPattern.matcher(chunk);
        boolean hasWords = wordMatcher.find();

        if (!hasWords || isSlate) {
            return;
        }

        chunk = chunk.trim();
        String[] wholeLine = chunk.split("\n");
        String fullOuttime = wholeLine[0];
        if (fullOuttime.length() > 10) {
            fullOuttime = fullOuttime.substring(0, 11);
        }

        String[] descriptionArray = Arrays.copyOfRange(wholeLine,1,wholeLine.length);
        String descriptionText = String.join("",descriptionArray);
        String fullDescription = fullOuttime + "\n" + descriptionText;

        descriptionWriter(fullDescription);


    }



    public void descriptionWriter(String line){

        boolean fileOpened = false;
        PrintWriter toFile = null;
        String[] fileChunks = inputFile.split("/");
        String title = (fileChunks[fileChunks.length-1]);
        title = title.replace(".txt","");
        title = title.replace(".TXT","");
        String newFileName = inputFile.replace(".TXT","_ApprovalScript.TXT");
        newFileName = newFileName.replace(".txt","_ApprovalScript.txt");


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
            if (!openedOnce){
                toFile.println(title);
                toFile.println("Description Approval Script");
                toFile.print("\n");
                openedOnce = true;
            }
            toFile.println(line);
            toFile.print("\n");
        }

        toFile.flush();
        toFile.close();
    }



    public static void main(String[] args){
        String fileName = "";
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == chooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            fileName = selectedFile.getAbsolutePath();
        }

        //fileName = "/Users/emilykolb/Desktop/PCL_SHIP_GOLDEN_40_061819_SG.TXT";
        //fileName = "/Users/emilykolb/Desktop/PCL_SHIP_STAR_50_061819_SG.TXT";

        //System.out.println(fileName);

        new ExpandedDescriptionConverter(fileName);
    }


}

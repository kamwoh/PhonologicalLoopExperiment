package com.example.woh.cogsci;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by woh on 07/05/17.
 */

public class ExperimentTask {

    private int experimentID = 1;
    private int taskNo = 1;
    private int taskDuration = 0;
    private int totalWord = 6;
    private MainActivity mcontext;
    protected ArrayList<String> task1 = new ArrayList<>();
    protected ArrayList<String> task2 = new ArrayList<>();
    protected ArrayList<String> task1_a = new ArrayList<>();
    protected ArrayList<String> task2_a = new ArrayList<>();
    private int correct1, correct2;
    private long timeTaken1, timeTaken2;
    private Random r = new Random();

    public ExperimentTask(int id,MainActivity context) {
        experimentID = id;
        mcontext = context;
        taskNo = 1;
        task1_a.add("1.");
        task1_a.add("2.");
        task1_a.add("3.");
        task1_a.add("4.");
        task1_a.add("5.");
        task1_a.add("6.");

        task2_a.add("1.");
        task2_a.add("2.");
        task2_a.add("3.");
        task2_a.add("4.");
        task2_a.add("5.");
        task2_a.add("6.");
        run();
    }

    public String getExperimentName() {
        switch(experimentID) {
            case 1:
                return "Experiment 1: Phonological Similarity Effect";
            case 2:
                return "Experiment 2: Word Length Effect";
            default:
                return "Experiment 3: Articulacy Suppression";
        }
    }

    public String getExperimentID() {
        return String.valueOf(experimentID);
    }

    private void run() {
        loadFile(experimentID);
    }

    public int getTaskDuration() {
        if(taskNo==1) return task1.size()*experimentID;
        else return task2.size()*experimentID;
    }

    public ArrayList<String> getWordList() {
        if(taskNo==1) return task1;
        else return task2;
    }

    public boolean isFulled() {
        if(taskNo==1) return task1_a.size() == task1.size();
        else return task2_a.size() == task2.size();
    }

    private void calculateResult() {
        correct1=0;
        correct2=0;
        for(int i=0;i<totalWord;i++){
            try {
                if (task1.get(i).toLowerCase().equals(task1_a.get(i).toLowerCase())) correct1++;
            } catch(Exception e) {}
            try {
                if (task2.get(i).toLowerCase().equals(task2_a.get(i).toLowerCase())) correct2++;
            } catch(Exception e) {}
        }
    }

    public int getTotalWord() {
        return totalWord;
    }

    public Result getResult() {
        calculateResult();
        return new Result(mcontext.getUserDatabase(), this, mcontext);
    }

    public ArrayList<String> getUserInputList() {
        if(taskNo==1) return task1_a;
        else return task2_a;
    }

    public void updateUserInput(int index, String userInput) {
        if(taskNo==1) task1_a.set(index, userInput);
        else task2_a.set(index, userInput);
    }

    public void nextTask() {
        taskNo = 2;
    }

    public int getTaskNo() {
        return taskNo;
    }

    public void loadFile(int experiment){
        String file = "wordsE"+experiment+".txt";
        for(int i=0;i<experiment;i++) taskDuration+=5;
        loadList(file);
    }

    private void loadList(String file){
        AssetManager am = mcontext.getAssets();
        try{
            InputStream path = am.open(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(path));
            String line = br.readLine();
            int toRead = r.nextInt(Integer.parseInt(line))+1;

            //skip pass the unwanted list
            for(int i=0;i<toRead-1;i++){line = br.readLine();line = br.readLine();}
            //

            for(String s:br.readLine().split(", ")) task1.add(s);
            for(String s:br.readLine().split(", ")) task2.add(s);
            br.close();
        }catch(FileNotFoundException e){
			/*
				ABORT MISSION
				NO WORD LIST FOUND
			*/
            Log.e("GG",e.toString());
        }catch(IOException e){
            // ERROR ???
            Log.e("GG.com",e.toString());
        }
    }

    public int[] getCorrect() {
        return new int[]{correct1, correct2};
    }

    public long[] getTimeTaken() {
        return new long[]{timeTaken1, timeTaken2};
    }

    public void setTimeTaken(long timeTaken) {
        if(taskNo==1) timeTaken1 = timeTaken;
        else timeTaken2 = timeTaken;
    }

    public String getInstruction() {
        String instruction = "";
        switch(experimentID) {
            case 1:
                instruction += "There will be two tasks in this experiment, you must complete both tasks before proceeding to the next experiment.\n";
                instruction += "On each task, a sequence of letters will appear, the list is presented for five seconds.\n";
                instruction += "After that, you will need to enter the letters as in the sequence it was presented to you.\n\n";
                instruction += "For example:\n";
                instruction += "If first letter in the sequence was \'F\'\n";
                instruction += "Then you need to enter the letter \'F\' in the answer space provided\n\n";
                instruction += "There is no way to correct mistakes, so be careful!";
                return instruction;
            case 2:
                instruction += "This task contains two tasks, you must complete tasks. "+
                        "On every task, there will be a sequence of words presented and you will be given time to read and memorize every word.\n" +
                        "\n" +
                        "Once the time is up, you are required to enter the words according to the sequence that it was presented.\n" +
                        "\n" +
                        "Focus is key my friend, good luck!";
                return instruction;
            default:
                if(taskNo==1) {
                    instruction += "You are about to begin Task 1 of Experiment 3. In this task, read the words presented out loud.\n" +
                            "Then, write the words in the correct sequence.\n\n" +
                            "The words are random, so make sure you are not in a public place to avoid looking like a weirdo.";
                } else {
                    instruction += "You are about to begin Task 2 of Experiment 3. In this task, " +
                            "you must add the word \"The\" before every word. [For " +
                            "example : the buffalo, the house ]\n" +
                            "\n" +
                            "Write the words only in the correct sequence, without \n" +
                            "the word \"The\". [For example: buffalo, house]";
                }
                return instruction;
        }
    }

    public String getFunFact() {
        String funFact = "";
        switch(experimentID) {
            case 1:
                funFact += "Most people struggle more in Task 1 " +
                        "because of the similar sounding letters, " +
                        "it confuses them!";
                return funFact;
            case 2:
                funFact += "Many people struggle more in Task 2 because of the longer words.\n" +
                        "Our memory works better for lists of words that are shorter and simpler!";
                return funFact;
            default:
                funFact += "Task 2 may have been a bigger challenge to " +
                        "complete since every word became a lot longer" +
                        "with the addition of the word \"The\". This addition " +
                        "also causes a phonological similarity effect, which " +
                        "can be quite confusing.";
                return funFact;
        }
    }
}

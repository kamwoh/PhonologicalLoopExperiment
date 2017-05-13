package com.woh.cogsci;

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

    public ExperimentTask(int id, MainActivity context) {
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
        switch (experimentID) {
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
        if (taskNo == 1) return task1.size() * experimentID;
        else return task2.size() * experimentID;
    }

    public ArrayList<String> getWordList() {
        if (taskNo == 1) return task1;
        else return task2;
    }

    public boolean isFulled() {
        if (taskNo == 1) return task1_a.size() == task1.size();
        else return task2_a.size() == task2.size();
    }

    private void calculateResult() {
        correct1 = 0;
        correct2 = 0;
        for (int i = 0; i < 6; i++) {
            Log.i(task1.get(i), task1_a.get(i));
            Log.i(task2.get(i), task2_a.get(i));
            if (task1.get(i).trim().equalsIgnoreCase(task1_a.get(i).trim())) correct1++;
            if (task2.get(i).trim().equalsIgnoreCase(task2_a.get(i).trim())) correct2++;
        }
        Log.i(""+correct1, ""+correct2);
    }

    public int getTotalWord() {
        return totalWord;
    }

    public Result getResult() {
        calculateResult();
        return new Result(mcontext.getUserDatabase(), this, mcontext);
    }

    public ArrayList<String> getUserInputList() {
        if (taskNo == 1) return task1_a;
        else return task2_a;
    }

    public void updateUserInput(int index, String userInput) {
        if (taskNo == 1) task1_a.set(index, userInput);
        else task2_a.set(index, userInput);
    }

    public void nextTask() {
        taskNo = 2;
    }

    public int getTaskNo() {
        return taskNo;
    }

    public void loadFile(int experiment) {
        String file = "wordsE" + experiment + ".txt";
        for (int i = 0; i < experiment; i++) taskDuration += 5;
        loadList(file);
    }

    private void loadList(String file) {
        AssetManager am = mcontext.getAssets();
        try {
            InputStream path = am.open(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(path));
            if (experimentID == 1) {
                String line = br.readLine();
                int toRead = r.nextInt(Integer.parseInt(line)) + 1;
                for (int i = 0; i < toRead - 1; i++) {
                    line = br.readLine();
                    line = br.readLine();
                }//skip pass the unwanted list
                for (String s : br.readLine().split(", ")) task1.add(s);
                for (String s : br.readLine().split(", ")) task2.add(s);
            } else if (experimentID == 2) {
                ArrayList<String> t1 = new ArrayList<>();
                for (String s : br.readLine().split(", ")) t1.add(s);
                ArrayList<String> t2 = new ArrayList<>();
                for (String s : br.readLine().split(", ")) t2.add(s);
                for (int i = 0; i < 6; i++) {
                    task1.add(t1.remove(r.nextInt(t1.size())));
                }
                for (int i = 0; i < 6; i++) {
                    task2.add(t2.remove(r.nextInt(t2.size())));
                }
            } else {
                ArrayList<String> t1 = new ArrayList<>();
                for (String s : br.readLine().split(", ")) t1.add(s);
                for (int i = 0; i < 6; i++) {
                    task1.add(t1.remove(r.nextInt(t1.size())));
                }
                for (int i = 0; i < 6; i++) {
                    task2.add(t1.remove(r.nextInt(t1.size())));
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            /*
				ABORT MISSION
				NO WORD LIST FOUND
			*/
            Log.e("GG", e.toString());
        } catch (IOException e) {
            // ERROR ???
            Log.e("GG.com", e.toString());
        }
    }

    public int[] getCorrect() {
        return new int[]{correct1, correct2};
    }

    public long[] getTimeTaken() {
        return new long[]{timeTaken1, timeTaken2};
    }

    public void setTimeTaken(long timeTaken) {
        if (taskNo == 1) timeTaken1 = timeTaken;
        else timeTaken2 = timeTaken;
    }

    public String getInstruction() {
        String instruction = "";
        switch (experimentID) {
            case 1:
                instruction += "1. A sequence of letters will appear, each letter is presented for one second.\n";
                instruction += "2. Memorise the letters.\n";
                instruction += "3. Enter the letters in the correct sequence.\n\n";
                instruction += "Focus is key my friend, good luck!";
                return instruction;
            case 2:
                instruction += "1. A sequence of words will appear, each word is presented for two seconds.\n" +
                        "2. Memorise the words.\n" +
                        "3. Enter the words in the correct sequence.\n\n" +
                        "Focus is key my friend, good luck!";
                return instruction;
            default:
                if (taskNo == 1) {
                    instruction += "1. A sequence of words will appear, each word is presented for three seconds.\n" +
                            "2. Memorise and read the words presented out loud.\n" +
                            "3. Enter the words in the correct sequence.\n\n" +
                            "The words are random, so make sure you are not in a public place to avoid looking like a weirdo.";
                } else {
                    instruction += "1. A sequence of words will appear, each word is presented for three seconds.\n" +
                            "2. Memorise and read the words and add the word presented out loud.\n" +
                            "3. You must add the word \"The\" before every word when you read it.\n" +
                            "4. Enter the original words without \"The\" in the correct sequence.\n" +
                            "5. Read the word like: [The buffalo, the house]\n" +
                            "6. Enter the word like: [buffalo, house]";
                }
                return instruction;
        }
    }

    public String getFunFact() {
        String funFact = "";
        switch (experimentID) {
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

package com.example.woh.cogsci;

import android.content.Context;
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
    private Context mcontext;

    public ExperimentTask(int id,Context context) {
        experimentID = id;
        mcontext = context;
        taskNo = 1;
        run();
    }

    public String getExperimentName() {
        return "Experiment OneTwoThree";
    }

    private void run() {
        loadFile(experimentID);
    }

    public int getTaskDuration() {
        return taskDuration;
    }

    public ArrayList<String> getWordList() {
        if(taskNo==1) return task1;
        else return task2;
    }

    public void getResult() {

    }

    public ArrayList<String> getUserInputList() {
        ArrayList<String> dummy = new ArrayList<>();
        dummy.add("Word 1");
        dummy.add("Word 2");
        dummy.add("Word 3");
        dummy.add("Word 4");
        return dummy;
    }

    public void saveUserInput(String userInput) {

    }

    public void nextTask() {
        taskNo = 2;
    }

    public int getTaskNo() {
        return taskNo;
    }


    protected ArrayList<String> task1 = new ArrayList<>();
    protected ArrayList<String> task2 = new ArrayList<>();
    private Random r = new Random();

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
}

package com.example.woh.cogsci;

import java.util.ArrayList;

/**
 * Created by woh on 07/05/17.
 */

public class ExperimentTask {

    private int experimentID;
    private int taskNo;
    private int taskDuration;

    public ExperimentTask(int id) {
        experimentID = id;
        taskNo = 1;
        run();
    }

    public String getExperimentName() {
        return "Experiment OneTwoThree";
    }

    private void run() {
        switch(experimentID) {
            case 1:
                experimentOne();
                break;
            case 2:
                experimentTwo();
                break;
            case 3:
                experimentThree();
                break;
        }
    }

    //load respective text
    private void experimentOne() {
        taskDuration = 5;
    }

    private void experimentTwo() {
        taskDuration = 10;
    }

    private void experimentThree() {
        taskDuration = 15;
    }

    public int taskDuration() {
        return taskDuration;
    }

    public ArrayList<String> getWordList() {
        ArrayList<String> dummy = new ArrayList<>();
        dummy.add("Word 1");
        dummy.add("Word 2");
        dummy.add("Word 3");
        dummy.add("Word 4");
        return dummy;
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

    //load task one word
    public void taskOne() {

    }

    public void taskTwo() {

    }

    public int getTaskNo() {
        return taskNo;
    }

}

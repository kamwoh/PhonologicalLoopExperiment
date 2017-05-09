package com.example.woh.cogsci;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by woh on 08/05/17.
 */

public class Result {

    private String experimentNumber;
    private int[] correct;
    private long[] timeTakenToSubmit;
    private ExperimentTask experimentTask;
    private DatabaseReference userDatabase;
    private MainActivity mainActivity;

    public void pushToDatabase() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    File file = new File("result_"+experimentNumber+".txt");
                    if(!file.exists()) file.createNewFile();
                    FileOutputStream fos = mainActivity.openFileOutput("result_"+experimentNumber+".txt", Context.MODE_PRIVATE);
                    String write = "task1,"+correct[0]+","+timeTakenToSubmit[0]+"\n";
                    write += "task2,"+correct[1]+","+timeTakenToSubmit[1]+"\n";
                    fos.write(write.getBytes());
                    fos.close();
                }catch(Exception e) {}

                DatabaseReference newResult = userDatabase.child("result").child(experimentTask.getExperimentID()).push();
                DatabaseReference task1 = newResult.child("task 1");
                DatabaseReference task2 = newResult.child("task 2");
                task1.child("correct").setValue(correct[0]);
                task1.child("time_taken").setValue(timeTakenToSubmit[0]);
                task2.child("correct").setValue(correct[1]);
                task2.child("time_taken").setValue(timeTakenToSubmit[1]);
            }
        });
        thread.start();
    }

    public Result(DatabaseReference userDatabase, ExperimentTask experimentTask, MainActivity mainActivity) {
        this.userDatabase = userDatabase;
        this.experimentTask = experimentTask;
        this.mainActivity = mainActivity;
        experimentNumber = experimentTask.getExperimentID();
        correct = experimentTask.getCorrect();
        timeTakenToSubmit = experimentTask.getTimeTaken();
    }

}
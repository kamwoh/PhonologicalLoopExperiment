package com.example.woh.cogsci;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by woh on 08/05/17.
 */

public class Result {

    private int experimentNumber;
    private int[] correct;
    private long[] timeTakenToSubmit;
    private ExperimentTask experimentTask;
    private DatabaseReference userDatabase;


    public void pushToDatabase() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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

    public Result(DatabaseReference userDatabase, ExperimentTask experimentTask) {
        this.userDatabase = userDatabase;
        this.experimentTask = experimentTask;
        correct = experimentTask.getCorrect();
        timeTakenToSubmit = experimentTask.getTimeTaken();
    }

}
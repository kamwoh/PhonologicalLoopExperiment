package com.example.woh.cogsci;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by woh on 08/05/17.
 */

public class Result {

    private int experimentNumber;
    private int correct;
    private int timeTakenToSubmit;
    private ExperimentTask experimentTask;
    private DatabaseReference userDatabase;


    public void pushToDatabase() {
        DatabaseReference newResult = userDatabase.child("result").child(experimentTask.getExperimentID()).push();
        newResult.child("correct").setValue(correct);
        newResult.child("time_taken").setValue(timeTakenToSubmit);
    }

    public Result(DatabaseReference userDatabase, ExperimentTask experimentTask) {
        this.userDatabase = userDatabase;
        this.experimentTask = experimentTask;
    }

}
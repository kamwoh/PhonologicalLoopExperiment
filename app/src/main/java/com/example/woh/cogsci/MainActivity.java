package com.example.woh.cogsci;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Register
 * 1. register
 * App flow
 * 1. welcome
 * 2. wait_start
 * 3. wait_experiment
 * After choose experiment
 * 4. before_task_show_experiment
 * 5. before_task_ready
 * 6. before_task_count_down
 * 7. task_display_word
 * 8. task_prompt_input
 * 9. if task 1, go to 4 else go to 10
 * 10. after_task_show_result
 * 11. after_task_show_explaination
 * 12. go to 1
 */
public class MainActivity extends AppCompatActivity {

    public final Handler handler = new Handler();
    private int experimentID;
    private int taskDuration = 15;
    private ExperimentTask experimentTask;

    private View.OnClickListener taskButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.experimentButton1:
                    experimentID = 1;
                    break;
                case R.id.experimentButton2:
                    experimentID = 2;
                    break;
                case R.id.experimentButton3:
                    experimentID = 3;
                    break;
            }
            experimentTask = new ExperimentTask(experimentID);
            MainActivity.this.setupBeforeTaskShowExperiment();
        }
    };

    private View.OnClickListener startButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.wait_experiment_3);
            Button experimentButton1 = (Button) findViewById(R.id.experimentButton1);
            Button experimentButton2 = (Button) findViewById(R.id.experimentButton2);
            Button experimentButton3 = (Button) findViewById(R.id.experimentButton3);
            experimentButton1.setOnClickListener(taskButtonOnClickListener);
            experimentButton2.setOnClickListener(taskButtonOnClickListener);
            experimentButton3.setOnClickListener(taskButtonOnClickListener);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_1);
        CountDownTimer countDownTimer = new CountDownTimer(2*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                MainActivity.this.setContentView(R.layout.wait_start_2);
                Button startButton = (Button) MainActivity.this.findViewById(R.id.startButton);
                startButton.setOnClickListener(startButtonOnClickListener);
            }
        }.start();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }

    public void setupBeforeTaskShowExperiment() {
        setContentView(R.layout.before_task_show_experiment_1);
        TextView experimentName = (TextView) findViewById(R.id.experimentName);
        experimentName.setText(experimentTask.getExperimentName());

        CountDownTimer countDownTimer = new CountDownTimer(2*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                MainActivity.this.setupBeforeTaskReady();
            }
        }.start();
    }

    public void setupBeforeTaskReady() {
        setContentView(R.layout.before_task_ready_2);
        TextView beforeTaskLabel = (TextView) findViewById(R.id.beforeTaskLabel);
        beforeTaskLabel.setText("Task "+experimentTask.getTaskNo());

        CountDownTimer countDownTimer = new CountDownTimer(2*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                MainActivity.this.setupBeforeTaskCountDown();
            }
        }.start();
    }

    public void setupBeforeTaskCountDown() {
        setContentView(R.layout.before_task_count_down_3);
        BeforeTaskTimer beforeTaskTimer = new BeforeTaskTimer(3, this);
        beforeTaskTimer.start();
    }

    public void setupTaskDisplayWord() {
        setContentView(R.layout.task_display_word_1);
        TextView taskLabel = (TextView) findViewById(R.id.taskLabel);
        taskLabel.setText("Task "+experimentTask.getTaskNo());

        ArrayAdapter<String> wordListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, experimentTask.getWordList());
        ListView taskWordListView = (ListView) findViewById(R.id.taskWordListView);
        taskWordListView.setAdapter(wordListAdapter);

        TaskTimer taskTimer = new TaskTimer(taskDuration, this);
        taskTimer.start();
    }

    public void setupTaskPromptInput() {
        setContentView(R.layout.task_prompt_input_2);
        //prompt input
        Button taskDoneButton = (Button) findViewById(R.id.taskDoneButton);
        Button taskAddItemButton = (Button) findViewById(R.id.taskAddItemButton);
        final ListView itemList = (ListView) findViewById(R.id.itemList);
        final ArrayAdapter<String> itemListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, experimentTask.getUserInputList());
        itemList.setAdapter(itemListAdapter);

        taskDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (experimentTask.getTaskNo() == 1) {
                    experimentTask.nextTask();
                    setupBeforeTaskReady(); //task 2
                } else {
                    setupAfterTaskShowResult();
                }
            }
        });

        taskAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show prompt input box
                experimentTask.saveUserInput("User input from prompt input");
                itemListAdapter.notifyDataSetChanged();
            }
        });
    }

    public void setupAfterTaskShowResult() {
        setContentView(R.layout.after_task_show_result_1);

    }

}

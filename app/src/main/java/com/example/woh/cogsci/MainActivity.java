package com.example.woh.cogsci;

import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public final Handler handler = new Handler();
    TextView tv;
    int i, j;
    int currentTime;
    int timer;
    int state;
    private int experimentID;
    private int taskNumber;
    private int taskDuration = 15;

    private View.OnClickListener taskButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            taskNumber = 1;
            switch(v.getId()) {
                case R.id.experimentButton1:
                    experimentID = 1;
                    break;
                case R.id.experimentButton2:
                    experimentID = 2;
                    break;
                case R.id.taskButton3:
                    experimentID = 3;
                    break;
            }
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
        setContentView(R.layout.welcome);
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
    }

    public void setupBeforeTaskShowExperiment() {
        setContentView(R.layout.before_task_show_experiment_1);
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
        taskLabel.setText("Task "+taskNumber);
        TaskTimer taskTimer = new TaskTimer(taskDuration, this);
        taskTimer.start();
        //display words based on experimentID
    }

    public void setupTaskPromptInput() {
        setContentView(R.layout.task_prompt_input_2);
        //prompt input
    }


}

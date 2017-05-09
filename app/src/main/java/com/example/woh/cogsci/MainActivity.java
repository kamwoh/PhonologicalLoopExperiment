package com.example.woh.cogsci;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private long timeTakenTask1, timeTakenTask2;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userDatabase;
    private ExperimentTask experimentTask;

    private View.OnClickListener taskButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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
            experimentTask = new ExperimentTask(experimentID, MainActivity.this);
            MainActivity.this.setupBeforeTaskShowExperiment();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_1);
        User.setAndroidID(Secure.getString(this.getContentResolver(), Secure.ANDROID_ID));

        firebaseDatabase = FirebaseDatabase.getInstance();
        userDatabase = firebaseDatabase.getReference().child(User.getAndroidID());
        User.pushToDatabase(userDatabase);

        new CountDownTimer(2 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                setupLanding();
            }
        }.start();

    }

    /**
     * 0
     */
    public void setupLanding() {
        setContentView(R.layout.wait_start_2);
        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
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
        });

        Button exitButton = (Button) MainActivity.this.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

        Button resultButton = (Button) MainActivity.this.findViewById(R.id.resultButton);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupShowAllResult();
            }
        });
    }

    /**
     * 1
     */
    public void setupBeforeTaskShowExperiment() {
        setContentView(R.layout.before_task_show_experiment_1);
        TextView experimentName = (TextView) findViewById(R.id.experimentName);
        experimentName.setText(experimentTask.getExperimentName());

        new CountDownTimer(2 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                MainActivity.this.setupBeforeTaskInstruction();
            }
        }.start();
    }

    /**
     * 2
     */
    public void setupBeforeTaskInstruction() {
        setContentView(R.layout.before_task_instruction_2);
        TextView beforeTaskLabel = (TextView) findViewById(R.id.beforeTaskLabel);
        beforeTaskLabel.setText("Task " + experimentTask.getTaskNo());
        Button okButton = (Button) findViewById(R.id.instructionOKButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupBeforeTaskCountDown();
            }
        });
        TextView instructionDetail = (TextView) findViewById(R.id.instructionDetail);
        instructionDetail.setText(experimentTask.getInstruction());
    }

    /**
     * 3
     */
    public void setupBeforeTaskCountDown() {
        setContentView(R.layout.before_task_count_down_3);
        BeforeTaskTimer beforeTaskTimer = new BeforeTaskTimer(3, this);
        beforeTaskTimer.start();
    }

    /**
     * 4
     */
    public void setupTaskDisplayWord() {
        setContentView(R.layout.task_display_word_1);
        TextView taskLabel = (TextView) findViewById(R.id.taskLabel);
        taskLabel.setText("Task " + experimentTask.getTaskNo());

        ArrayAdapter<String> wordListAdapter = new ArrayAdapter<>(this, R.layout.text_style1, experimentTask.getWordList());
        ListView taskWordListView = (ListView) findViewById(R.id.taskWordListView);
        taskWordListView.setAdapter(wordListAdapter);

        TaskTimer taskTimer = new TaskTimer(experimentTask.getTaskDuration(), this);
        taskTimer.start();
    }

    /**
     * 5 -> 2 or 6
     */
    public void setupTaskPromptInput() {
        setContentView(R.layout.task_prompt_input_2);
        //prompt input
        Button taskDoneButton = (Button) findViewById(R.id.taskDoneButton);
        Button taskAddItemButton = (Button) findViewById(R.id.taskAddItemButton);
        final ListView itemList = (ListView) findViewById(R.id.itemList);
        final ArrayAdapter<String> itemListAdapter = new ArrayAdapter<>(this, R.layout.text_style1, experimentTask.getUserInputList());
        itemList.setAdapter(itemListAdapter);

        final long startTime = System.currentTimeMillis();

        taskDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD
                setupAfterTaskShowResult();
=======
                experimentTask.setTimeTaken((System.currentTimeMillis() - startTime) / 1000);
>>>>>>> d7b61f4412ba025316e1c173f8064139796a4c2b
                if (experimentTask.getTaskNo() == 1) {
                    experimentTask.nextTask();
<<<<<<< HEAD
                    setupBeforeTaskReady(); //task 2
=======
                    setupBeforeTaskInstruction(); //task 2
                } else {
                    setupAfterTaskShowResult();
>>>>>>> d7b61f4412ba025316e1c173f8064139796a4c2b
                }
            }
        });

        taskAddItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (experimentTask.isFulled()) {
                    Toast.makeText(MainActivity.this, "Maximum only " + experimentTask.getTotalWord() + " word", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder popUp = new AlertDialog.Builder(MainActivity.this);
                    final EditText et = new EditText(MainActivity.this);
                    popUp.setView(et);
                    popUp.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            experimentTask.saveUserInput(et.getText().toString());
                            itemListAdapter.notifyDataSetChanged();
                        }
                    });
                    AlertDialog ad = popUp.create();
                    ad.setTitle("Enter the word: ");
                    ad.show();
                }
            }
        });

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder popUp = new AlertDialog.Builder(MainActivity.this);
                final EditText et = new EditText(MainActivity.this);
                et.setText(((TextView) view).getText());
                popUp.setView(et);
                popUp.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        experimentTask.updateUserInput(position, et.getText().toString());
                        itemListAdapter.notifyDataSetChanged();
                    }
                });
                AlertDialog ad = popUp.create();
                ad.setTitle("Enter the word: ");
                ad.show();
            }
        });
    }

    /**
     * 6
     */
    public void setupAfterTaskShowResult() {
        setContentView(R.layout.after_task_show_result_1);
        Result result = experimentTask.getResult();
        result.pushToDatabase();

        //Things in the result layout
        ListView resultGivenWords = (ListView) findViewById(R.id.result_givenWords);
        ListView resultAnswer = (ListView) findViewById(R.id.result_answer);
        Button resultNextButton = (Button) findViewById(R.id.result_next);
        TextView resultTask = (TextView) findViewById(R.id.result_textView6);

        //Results gathered from the tasks
        ArrayAdapter<String> givenWord = new ArrayAdapter<>(this, R.layout.text_style1, experimentTask.getWordList());
        ArrayAdapter<String> userInput = new ArrayAdapter<>(this, R.layout.text_style1, experimentTask.getUserInputList());

        for(int i=0;i<experimentTask.getWordList().size();i++){
//            resultGivenWords.
        }

        resultTask.setText("Task "+experimentTask.getTaskNo());
        resultGivenWords.setAdapter(givenWord);
        resultAnswer.setAdapter(userInput);

        //On clicking "NEXT" button
        resultNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                return;
            }
        });

    }

    /**
     * 7 -> 0
     */
    public void setupShowFunFact() {
        setContentView(R.layout.fun_fact_experiment_2_3);
        TextView funFactDetail = (TextView) findViewById(R.id.funFactDetail);
        funFactDetail.setText(experimentTask.getFunFact());
        Button funFactOKButton = (Button) findViewById(R.id.funFactOKButton);
        funFactOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupLanding();
            }
        });
    }

    /**
     * 0.1 -> 0
     */
    public void setupShowAllResult() {
        setContentView(R.layout.all_result);
        Button okButton = (Button) findViewById(R.id.result_OKButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupLanding();
            }
        });

        TextView e1t1, e1t2, e2t1, e2t2, e3t1, e3t2;
        e1t1 = (TextView) findViewById(R.id.result_task1_e1_value);
        e1t2 = (TextView) findViewById(R.id.result_task2_e1_value);
        e2t1 = (TextView) findViewById(R.id.result_task1_e2_value);
        e2t2 = (TextView) findViewById(R.id.result_task2_e2_value);
        e3t1 = (TextView) findViewById(R.id.result_task1_e3_value);
        e3t2 = (TextView) findViewById(R.id.result_task2_e3_value);

        String[][] result = Result.getAllResult(this);
        e1t1.setText(result[0][0]);
        e1t2.setText(result[0][1]);
        e2t1.setText(result[1][0]);
        e2t2.setText(result[1][1]);
        e3t1.setText(result[2][0]);
        e3t2.setText(result[2][1]);
    }

    public DatabaseReference getUserDatabase() {
        return userDatabase;
    }
}

package com.woh.cogsci;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;

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
    private int exitCount = 0;
    private int experimentID;
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

        try {
            firebaseDatabase = FirebaseDatabase.getInstance();
            userDatabase = firebaseDatabase.getReference().child(User.getAndroidID());
            User.pushToDatabase(userDatabase);
        } catch(Exception e) {} //in case any connection error

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
        exitCount = 0;
        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupExperimentPage();
            }
        });
        final Button exitButton = (Button) MainActivity.this.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.exit(0);
                if(exitCount==0) {
                    Toast.makeText(MainActivity.this, "Help us plss :(", Toast.LENGTH_SHORT).show();
                    exitCount++;
                } else if(exitCount==1) {
                    Toast.makeText(MainActivity.this, "We need your help :(", Toast.LENGTH_SHORT).show();
                    exitCount++;
                } else if(exitCount==2) {
                    Toast.makeText(MainActivity.this, "Nooooo", Toast.LENGTH_SHORT).show();
                    exitCount++;
                } else {
                    System.exit(0);
                }
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

    public void setupExperimentPage(){
        setContentView(R.layout.wait_experiment_3);
        Button experimentButton1 = (Button) findViewById(R.id.experimentButton1);
        Button experimentButton2 = (Button) findViewById(R.id.experimentButton2);
        Button experimentButton3 = (Button) findViewById(R.id.experimentButton3);
        experimentButton1.setOnClickListener(taskButtonOnClickListener);
        experimentButton2.setOnClickListener(taskButtonOnClickListener);
        experimentButton3.setOnClickListener(taskButtonOnClickListener);
        Button experimentBack = (Button) findViewById(R.id.experimentBack);
        experimentBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setupLanding();
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

        final ArrayList<String> wordList = experimentTask.getWordList();
        final Iterator<String> iterator = wordList.iterator();
        final TextView timer = (TextView) findViewById(R.id.timerLabel);
        final TextView wordDisplay = (TextView) findViewById(R.id.display_wordDisplay);
        wordDisplay.setText(iterator.next());
        timer.setText((experimentTask.getTaskDuration()-1)+"");

//        final ArrayAdapter<String> wordListAdapter = new ArrayAdapter<>(this, R.layout.text_style1, wordList);
//        ListView taskWordListView = (ListView) findViewById(R.id.taskWordListView);
//        taskWordListView.setAdapter(wordListAdapter);
        Log.i("duration", experimentTask.getTaskDuration()+"");
        new MyTimer(experimentTask.getTaskDuration()-1,  1000){

            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf((millisUntilFinished/1000)));
            }

            @Override
            public void onFinish() {
                setupTaskPromptInput();
            }
        }.start();

        new MyTimer(experimentTask.getTaskDuration(), experimentID*1000){
            @Override
            public void onTick(long millisUntilFinished) {
                String word = "";
                if(iterator.hasNext())
                    word = iterator.next();
                wordDisplay.setText(word);
            }

            @Override
            public void onFinish() {}
        }.start();

//        TaskTimer taskTimer = new TaskTimer(experimentTask.getTaskDuration(), this);
//        taskTimer.start();
    }

    /**
     * 5 -> 2 or 6
     */
    public void setupTaskPromptInput() {
        setContentView(R.layout.task_prompt_input_2);
        //prompt input
        Button taskDoneButton = (Button) findViewById(R.id.taskDoneButton);
//        Button taskAddItemButton = (Button) findViewById(R.id.taskAddItemButton);
        final ListView itemList = (ListView) findViewById(R.id.itemList);
        final ArrayAdapter<String> itemListAdapter = new ArrayAdapter<>(this, R.layout.text_style1, experimentTask.getUserInputList());
        itemList.setAdapter(itemListAdapter);

        final long startTime = System.currentTimeMillis();

        taskDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experimentTask.setTimeTaken((System.currentTimeMillis() - startTime) / 1000);
                setupAfterTaskShowResult();
            }
        });

//        taskAddItemButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (experimentTask.isFulled()) {
//                    Toast.makeText(MainActivity.this, "Maximum only " + experimentTask.getTotalWord() + " word", Toast.LENGTH_SHORT).show();
//                } else {
//                    AlertDialog.Builder popUp = new AlertDialog.Builder(MainActivity.this);
//                    final EditText et = new EditText(MainActivity.this);
//                    popUp.setView(et);
//                    popUp.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            experimentTask.update(et.getText().toString());
//                            itemListAdapter.notifyDataSetChanged();
//                        }
//                    });
//                    AlertDialog ad = popUp.create();
//                    ad.setTitle("Enter the word: ");
//                    ad.show();
//                }
//            }
//        });
        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder popUp = new AlertDialog.Builder(MainActivity.this);
                final EditText et = new EditText(MainActivity.this);
                if(!((TextView)view).getText().equals((position+1)+"."))
                    et.setText(((TextView) view).getText());
                popUp.setView(et);
//                imm.showSoftInput(et, 0);

                popUp.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(!et.getText().toString().equals("")) {
                            experimentTask.updateUserInput(position, et.getText().toString());
                            itemListAdapter.notifyDataSetChanged();
                        } else {
                            et.setText((position+1)+".");
                        }
                    }
                });
                AlertDialog ad = popUp.create();
                ad.setTitle("Enter the word: ");
                ad.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                ad.show();
            }
        });
    }

    /**
     * 6
     */
    public void setupAfterTaskShowResult() {
        setContentView(R.layout.after_task_show_result_2);

        //Things in the result layout
        Button resultNextButton = (Button) findViewById(R.id.resultNextButton);
        TextView resultTask = (TextView) findViewById(R.id.resultTaskX);
        ArrayList<TextView> rs = new ArrayList<>();
        ArrayList<TextView> ys = new ArrayList<>();
        rs.add((TextView) findViewById(R.id.resultRS1));
        rs.add((TextView) findViewById(R.id.resultRS2));
        rs.add((TextView) findViewById(R.id.resultRS3));
        rs.add((TextView) findViewById(R.id.resultRS4));
        rs.add((TextView) findViewById(R.id.resultRS5));
        rs.add((TextView) findViewById(R.id.resultRS6));
        ys.add((TextView) findViewById(R.id.resultYS1));
        ys.add((TextView) findViewById(R.id.resultYS2));
        ys.add((TextView) findViewById(R.id.resultYS3));
        ys.add((TextView) findViewById(R.id.resultYS4));
        ys.add((TextView) findViewById(R.id.resultYS5));
        ys.add((TextView) findViewById(R.id.resultYS6));

        //Results gathered from the tasks
        final ArrayList<String> wordList = experimentTask.getWordList();
        final ArrayList<String> userAns = experimentTask.getUserInputList();

        for(int i=0;i<wordList.size();i++){
            rs.get(i).setText(wordList.get(i));
            if(userAns.size()>i){
                ys.get(i).setText(userAns.get(i));
                if((userAns.get(i).trim()).compareToIgnoreCase(wordList.get(i).trim())!=0){
                    ys.get(i).setBackground(getResources().getDrawable(R.drawable.rounded_rect));
                    ys.get(i).setTextColor(Color.WHITE);
                }
            }else{
                ys.get(i).setText("");
            }
        }

        resultTask.setText("Task "+experimentTask.getTaskNo());
        //On clicking "NEXT" button
        resultNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(experimentTask.getTaskNo()==1){
                    experimentTask.nextTask();
                    setupBeforeTaskInstruction(); //task 2
                }else{
                    setupShowFunFact();
                }
            }
        });

    }

    /**
     * 7 -> 0
     */
    public void setupShowFunFact() {
        setContentView(R.layout.fun_fact_experiment_2_3);
        Result result = experimentTask.getResult();
        result.pushToDatabase();
        TextView funFactDetail = (TextView) findViewById(R.id.funFactDetail);
        funFactDetail.setText(experimentTask.getFunFact());
        Button funFactOKButton = (Button) findViewById(R.id.funFactOKButton);
        funFactOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupExperimentPage();
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

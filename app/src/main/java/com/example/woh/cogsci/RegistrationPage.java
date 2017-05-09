package com.example.woh.cogsci;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RegistrationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_registration_page);

// ---------- initial checking if file exists ------------
        final String fileName = "registerBasicData.txt";
        try{
            String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
            File file = new File(path);
            if (file.exists()) {
//------------if file exists redirect to wait_experiement_3 ----------
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        final RadioButton radioMale = (RadioButton)findViewById(R.id.buttonMale);
        final RadioButton radioFemale = (RadioButton)findViewById(R.id.buttonFemale);
        final EditText ageInput = (EditText)findViewById(R.id.ageInput);
        final Button buttonFinish = (Button)findViewById(R.id.buttonNext);
        final Button buttonCheck = (Button) findViewById(R.id.buttonCheck);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please fill in your age and gender");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();

        radioMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioFemale.isChecked()){
                    radioFemale.setChecked(false);
                }
            }
        });

        radioFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioMale.isChecked()){
                    radioMale.setChecked(false);
                }
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "";

                content += ageInput.getText().toString();
                content += ",";
                if(radioFemale.isChecked()){
                    content+= "Female";
                }else if(radioMale.isChecked()){
                    content+= "Male";
                }

                FileOutputStream outputStream;

                try{
                    outputStream = openFileOutput(fileName, context.MODE_PRIVATE);
                    outputStream.write(content.getBytes());
                    outputStream.close();
                    Log.v("done .......", "...");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });


//-------- button event for Checking if the file is exists and print out the content ------------------------
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
                    File file = new File(path);
                    if (file.exists()){
                        Log.v("exists.....", "file");
                        FileInputStream fis = context.openFileInput(fileName);
                        InputStreamReader isr = new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while((line = br.readLine())!=null){
                            sb.append(line);
                        }
                        Log.v("content ---> ", sb.toString());

                    }else{
                        Log.v("don't", "exists");
                    }
                    Log.v("done .......", "...");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
//---------------------------------------------  end --------------------------------------------------------
    }
}

package com.example.woh.cogsci;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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
import android.widget.Toast;

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
        setContentView(R.layout.activity_registration_page);
        final Context context = this;

// ---------- initial checking if file exists ------------
        final String fileName = "registerBasicData.txt";
        try {
            String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
            File file = new File(path);
            if (file.exists()) {
                FileInputStream fis = openFileInput(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                String[] s = br.readLine().split(","); //if any exception, will have to register
                int age = Integer.parseInt(s[0]);
                String gender = s[1];
                User.setAge(age);
                User.setGender(gender);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
//------------if file exists redirect to wait_experiement_3 ----------
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        final RadioButton radioMale = (RadioButton) findViewById(R.id.buttonMale);
        final RadioButton radioFemale = (RadioButton) findViewById(R.id.buttonFemale);
        final EditText ageInput = (EditText) findViewById(R.id.ageInput);
        ageInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        ageInput.setRawInputType(Configuration.KEYBOARD_12KEY);
        final Button buttonFinish = (Button) findViewById(R.id.buttonNext);
//        final Button buttonCheck = (Button) findViewById(R.id.buttonCheck);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please fill in your age and gender");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                if (radioFemale.isChecked()) {
                    radioFemale.setChecked(false);
                }
            }
        });

        radioFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioMale.isChecked()) {
                    radioMale.setChecked(false);
                }
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "";
                int age;
                try {
                    age = Integer.parseInt(ageInput.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(RegistrationPage.this, "Only number!", Toast.LENGTH_SHORT).show();
                    return;
                }
                content += age;
                content += ",";
                String gender = "";
                if (radioFemale.isChecked()) {
                    gender = "Female";
                } else if (radioMale.isChecked()) {
                    gender = "Male";
                } else {
                    Toast.makeText(RegistrationPage.this, "Please tick your gender!", Toast.LENGTH_SHORT).show();
                    return;
                }
                content += gender;
                User.setAge(age);
                User.setGender(gender);

                FileOutputStream outputStream;

                try {
                    outputStream = openFileOutput(fileName, context.MODE_PRIVATE);
                    outputStream.write(content.getBytes());
                    outputStream.close();
                    Intent intent = new Intent(RegistrationPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Log.v("done .......", "...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


//-------- button event for Checking if the file is exists and print out the content ------------------------
//        buttonCheck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
//                    File file = new File(path);
//                    if (file.exists()){
//                        Log.v("exists.....", "file");
//                        FileInputStream fis = context.openFileInput(fileName);
//                        InputStreamReader isr = new InputStreamReader(fis);
//                        BufferedReader br = new BufferedReader(isr);
//                        StringBuilder sb = new StringBuilder();
//                        String line;
//                        while((line = br.readLine())!=null){
//                            sb.append(line);
//                        }
//                        Log.v("content ---> ", sb.toString());
//
//                    }else{
//                        Log.v("don't", "exists");
//                    }
//                    Log.v("done .......", "...");
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
//---------------------------------------------  end --------------------------------------------------------
    }
}

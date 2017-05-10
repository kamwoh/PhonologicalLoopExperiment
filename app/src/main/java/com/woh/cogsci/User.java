package com.woh.cogsci;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by woh on 09/05/17.
 */

public class User {

    private static String androidID;
    private static int age;
    private static String gender;

    public static void pushToDatabase(final DatabaseReference userDatabase) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                userDatabase.child("age").setValue(getAge());
                userDatabase.child("gender").setValue(getGender());
            }
        });
        thread.start();
    }

    public static void setAndroidID(String androidID) {
        User.androidID = androidID;
    }

    public static String getAndroidID() {
        return androidID;
    }

    public static int getAge() {
        return age;
    }

    public static void setAge(int age) {
        User.age = age;
    }

    public static void setGender(String gender) {
        User.gender = gender;
    }

    public static String getGender() {
        return gender;
    }

}

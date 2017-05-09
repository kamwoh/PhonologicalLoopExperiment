package com.example.woh.cogsci;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by woh on 09/05/17.
 */

public class User {

    private static String androidID;

    public static void pushToDatabase(DatabaseReference userDatabase) {
        userDatabase.child("age").setValue(getAge());
        userDatabase.child("gender").setValue(getGender());
    }

    public static void setAndroidID(String androidID) {
        User.androidID = androidID;
    }
    
    public static String getAndroidID() {
        return androidID;
    }

    public static int getAge() {
        return 10;
    }

    public static String getGender() {
        return "Male";
    }

}

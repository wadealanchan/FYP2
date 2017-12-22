package com.example.alan.fyp;

/**
 * Created by em on 24/06/16.
 */
public interface CustomDialogListener {

    void onAction(Object object);
    void onDialogPositive(String Name, String Email,String Password);
    void onDialogNegative(Object object);
}

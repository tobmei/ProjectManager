package com.example.projectmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectmanager.model.Customer;
import com.example.projectmanager.model.Project;
import com.example.projectmanager.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Utility {

    //Hole Zeit in 00:00:00 Format
    public static String getFormattedTime(long seconds){
        String secondsString, minutesString, hoursString;
        long minutes = seconds/60;
        long hours = seconds/60/60;

        seconds = seconds % 60;
        secondsString = String.valueOf(seconds);
        if(seconds == 0)
            secondsString = "00";
        if(seconds < 10 && seconds >0)
            secondsString = "0" + seconds;

        minutes = minutes % 60;
        minutesString = String.valueOf(minutes);
        if(minutes == 0)
            minutesString = "00";
        if(minutes < 10 && minutes >0)
            minutesString = "0" + minutes;

        hoursString = String.valueOf(hours);
        if(hours == 0)
            hoursString = "00";
        if(hours < 10 && hours > 0)
            hoursString = "0" + hours;

        return hoursString+":"+minutesString+":"+secondsString;
    }

    public static String getCurrentTime(){
        return new SimpleDateFormat("dd.MM.yyyy").format(new Date());
    }

    public static String loremIpsum = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
            "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam " +
            "erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea " +
            "rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum " +
            "dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed " +
            "diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, " +
            "sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. " +
            "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit " +
            "amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam " +
            "nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed d" +
            "iam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. " +
            "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit " +
            "amet.";

}

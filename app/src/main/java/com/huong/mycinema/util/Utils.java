package com.huong.mycinema.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by HuongPN on 10/17/2018.
 */
public class Utils {

    public static String getConvertedTime(long timestamp){

        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        return dateFormat.format(date);
    }

    public static Timestamp getConvertedTime(String dataPicker){

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            Date parsedDate = dateFormat.parse(dataPicker);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp;
        } catch(Exception e) { //this generic but you can control nother types of exception
            // look the origin of excption
        }

//        return timestamp;
        return null;
    }

    public static String convertLongTime(String date){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String datePicker = "";
        try{
            Date d =df.parse(date);
            long milliseconds = d.getTime();
            datePicker = String.valueOf(milliseconds);
        }catch (ParseException e){

        }
        return datePicker;
    }
}

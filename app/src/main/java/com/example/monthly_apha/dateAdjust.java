package com.example.monthly_apha;

import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class dateAdjust {
    SimpleDateFormat desiredFormat = new SimpleDateFormat("dd/MM");
    String desiredDateString;

    public String adjustFormat(String da){
            String[] possibleFormats = {"d/M/yyyy", "dd/MM/yyyy", "d/M", "dd/MM"};
            String inputDateString = da;
            Date date = null;
            for (String format : possibleFormats) {
                try {
                    date = new SimpleDateFormat(format).parse(inputDateString);
                    break;  // Break the loop once a match is found
                } catch (Exception e) {
                    // Ignore the exception and try the next format
                }
            }

            if (date != null) {
                // Now format the Date object into the desired format "dd/MM"
                desiredDateString = desiredFormat.format(date);

                // Get the current year
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);

                // Add the current year to the input date string
                String datewYear = desiredDateString + "/" + currentYear;

                return datewYear;
            } else {
                return "incorrect";
            }
    }
    public String getTodaysDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
        String sdate = dateFormat.format(new Date());
        return sdate;
    }
}


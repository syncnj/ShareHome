package sharehome.com.androidsharehome2;
import android.widget.Switch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Zirui Tao on 12/13/2017.
 */

public class DateTimeUtils {
    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public static String getDateDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String result = String.format("%d day(s), %d hour(s), %d minute(s), %d second(s)%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        return result;
    }
    public static String getFormattedDateRecurrence(long minutes){

        long hoursInMins =  60;
        long daysInMins = hoursInMins * 24;
        long weekInMins = daysInMins * 7;
        long elapsedWeeks = minutes / weekInMins;
        minutes = minutes % weekInMins;
        long elapsedDays= minutes/daysInMins;
        minutes = minutes % hoursInMins;
        String result = "";
        switch (Long.valueOf(elapsedWeeks).intValue()){
            case 1:{
                result = "1 week";
                break;
            }
            case 0:{
                result = "1 day";
                break;
            }
            default: result = "1 month";
                break;
        }

        return result;

    }
}

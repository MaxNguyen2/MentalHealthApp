package com.example.max.mentalhealthapp;
        import com.github.mikephil.charting.components.AxisBase;
        import com.github.mikephil.charting.formatter.IAxisValueFormatter;

        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Locale;

public class DateAxisFormatter implements IAxisValueFormatter {
    private long referenceTimestamp; // minimum timestamp in your data set
    private SimpleDateFormat mDataFormat;
    private Date mDate;

    public DateAxisFormatter(long timestamp) {
        referenceTimestamp = timestamp;
        mDataFormat = new SimpleDateFormat("M/dd", Locale.ENGLISH);
        mDate = new Date();
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // convertedTimestamp = originalTimestamp - referenceTimestamp
        long convertedTimestamp = (long) value;
        // Retrieve original timestamp
        long originalTimestamp = referenceTimestamp + convertedTimestamp;
        // Convert timestamp to hour:minute
        return getHour(originalTimestamp);
    }

    private String getHour(long timestamp){
        try{
            mDate.setTime(timestamp);
            return mDataFormat.format(mDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
}
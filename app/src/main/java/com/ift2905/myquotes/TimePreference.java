/**
 * Code form :
 * https://stackoverflow.com/questions/5533078/timepicker-in-preferencescreen
 */

package com.ift2905.myquotes;

import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.content.Context;
import android.content.res.TypedArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimePreference extends DialogPreference {
    private int lastHour=0;
    private int lastMinute=0;
    private TimePicker picker=null;

    public static int getHour(String time) {
        String[] pieces=time.split(":");
        Log.d("MY_QUOTES",pieces[0]);

        return(Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[1]));
    }

    public TimePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    @Override
    protected View onCreateDialogView() {
        picker=new TimePicker(getContext());

        return(picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            lastHour=picker.getCurrentHour();
            lastMinute=picker.getCurrentMinute();

            setSummary(getSummary());

            String lastMinuteString = String.valueOf(lastMinute);
            String time = String.valueOf(lastHour) + ":" + (lastMinuteString.length() == 1 ? "0" + lastMinuteString : lastMinuteString);

            if (callChangeListener(time)) {
                persistString(time);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return(a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

        String time;
        String defaultValueStr = (defaultValue != null) ? defaultValue.toString() : "00:00";
        if (restoreValue)
            time = getPersistedString(defaultValueStr);
        else {
            time = defaultValueStr;
            if (shouldPersist())
                persistString(defaultValueStr);
        }

        lastHour=getHour(time);
        lastMinute=getMinute(time);

        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, lastHour);
        cal.set(Calendar.MINUTE, lastMinute);
        DateFormat sdf = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT);

        return sdf.format(cal.getTime());
    }

}
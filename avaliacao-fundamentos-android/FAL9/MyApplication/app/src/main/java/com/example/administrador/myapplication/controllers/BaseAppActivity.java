package com.example.administrador.myapplication.controllers;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.administrador.myapplication.R;
import com.example.administrador.myapplication.util.AppUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Created by Rodrigo on 01/06/2015.
 */
public abstract class BaseAppActivity extends AppCompatActivity {

    protected boolean verifyMandatoryFields(EditText... fields) {
        boolean isValid = true;
        for (EditText field : fields) {
            field.setError(null);
            if (TextUtils.isEmpty(field.getText())) {
                field.setError(getString(R.string.msg_mandatory));
                if (isValid) {
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    protected boolean verifyDateFields(EditText... fields) {
        final Calendar serviceOrderCalendar = Calendar.getInstance(AppUtil.LOCALE_PT_BR);
        boolean isValid = true;
        for (EditText field : fields) {
            field.setError(null);
            if (!TextUtils.isEmpty(field.getText())) {
                try {
                    final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", AppUtil.LOCALE_PT_BR);
                    dateFormat.setLenient(false);
                    serviceOrderCalendar.setTime(dateFormat.parse(field.getText().toString().trim()));
                } catch (ParseException parseException) {
                    field.setError(super.getString(R.string.msg_invalid_date));
                    if (isValid) {
                        isValid = false;
                    }
                }
            }
        }
        return isValid;
    }

    protected boolean verifyTimeFields(EditText... fields) {
        final Calendar serviceOrderCalendar = Calendar.getInstance(AppUtil.LOCALE_PT_BR);
        boolean isValid = true;
        for (EditText field : fields) {
            field.setError(null);
            if (!TextUtils.isEmpty(field.getText())) {
                try {
                    final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", AppUtil.LOCALE_PT_BR);
                    timeFormat.setLenient(false);
                    timeFormat.parse(field.getText().toString().trim());
                    if (serviceOrderCalendar != null) {
                        final String[] timeTextArray = field.getText().toString().trim().split("[:]");
                        serviceOrderCalendar.set(Calendar.HOUR, Integer.valueOf(timeTextArray[0]));
                        serviceOrderCalendar.set(Calendar.MINUTE, Integer.valueOf(timeTextArray[1]));
                        serviceOrderCalendar.set(Calendar.SECOND, Integer.valueOf(timeTextArray[2]));
                    }
                } catch (ParseException parseException) {
                    field.setError(this.getString(R.string.msg_invalid_time));
                    return false;
                }
            }
        }
        return isValid;
    }

    protected boolean verifyValueFields(EditText... fields) {
        boolean isValid = true;
        for (EditText field : fields) {
            field.setError(null);
            if (!TextUtils.isEmpty(field.getText())) {
                final Pattern pattern = Pattern.compile("[0-9]+([.][0-9]{2})");
                if (!pattern.matcher(field.getText().toString().trim()).matches()) {
                    field.setError(super.getString(R.string.msg_invalid_value));
                    return false;
                }

            }
        }
        return isValid;
    }


}

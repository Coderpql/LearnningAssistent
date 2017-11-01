package com.learning.wow.learningassistence.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import java.text.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.learning.wow.learningassistence.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.SimpleFormatter;

/**
 * Created by wow on 2017/10/5.
 */

public class DatePickerFragment extends DialogFragment {
    private static final String ARG_DATE = "date";
    public static final String EXTRA_DATE = "com.learning.wow.learningassistence.DatePicker";
    private Date mDate;
    public static DatePickerFragment newInstance(String date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date_picker,null);

        final String date = (String) getArguments().getSerializable(ARG_DATE);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
           mDate = dateFormat.parse(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        Calendar calendar =Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePicker datePicker = (DatePicker)view.findViewById(R.id.date_picker);
        datePicker.init(year,month,day,null);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        Date mdate = new GregorianCalendar(year,month,day).getTime();
                        sendDate(Activity.RESULT_OK,mdate);
                    }
                })
                .create();
    }
    public void sendDate(int resultCode,Date date){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}

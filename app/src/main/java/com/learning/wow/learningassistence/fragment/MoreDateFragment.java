package com.learning.wow.learningassistence.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.learning.wow.learningassistence.R;

import java.util.Date;

/**
 * Created by wow on 2017/10/4.
 */

public class MoreDateFragment extends Fragment {
    private EditText mIDEditText;
    private TextView mBirthdayTextView;
    private EditText mSchoolEditText;
    private EditText mEmailEditText;
    private EditText mPhoneEditText;
    private SharedPreferences.Editor editor;
    private Date mDateNow;
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private String mDate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_data_fragment,container,false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.more_data_toolbar);
        mIDEditText = (EditText)view.findViewById(R.id.id_edit_text);
        mBirthdayTextView = (TextView)view.findViewById(R.id.birthday_text_view);
        mSchoolEditText = (EditText)view.findViewById(R.id.school_edit_text);
        mEmailEditText = (EditText)view.findViewById(R.id.email_edit_text);
        mPhoneEditText = (EditText)view.findViewById(R.id.phone_edit_text);
        SharedPreferences pref = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        String ID = pref.getString("id","");
        String School = pref.getString("school","");
        String Email = pref.getString("email","");
        String Phone = pref.getString("phone","");
        String DateNow = pref.getString("date","");
        mIDEditText.setText(ID);
        mSchoolEditText.setText(School);
        mEmailEditText.setText(Email);
        mPhoneEditText.setText(Phone);
        if ( DateNow == "" ){
            mDateNow = new Date();
            mDate = (String) DateFormat.format("yyyy-MM-dd",mDateNow);
            editor.putString("date",mDate);
            editor.apply();
            mBirthdayTextView.setText(mDate);
        }else {
            mDate = pref.getString("date","");
            mBirthdayTextView.setText(mDate);
        }
        mBirthdayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mDate);
                dialog.setTargetFragment(MoreDateFragment.this,REQUEST_DATE);
                dialog.show(manager,DIALOG_DATE);
            }
        });
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        editor = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.ok,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ok_item:
                String ID = mIDEditText.getText().toString();
                String School = mSchoolEditText.getText().toString();
                String Email = mEmailEditText.getText().toString();
                String Phone = mPhoneEditText.getText().toString();
                editor.putString("id",ID);
                editor.putString("email",Email);
                editor.putString("school",School);
                editor.putString("phone",Phone);
                editor.apply();
                Intent intent = new Intent();
                getActivity().setResult(Activity.RESULT_OK,intent);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         switch (requestCode){
             case REQUEST_DATE:
                 if (resultCode == Activity.RESULT_OK){
                     Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                     String mDate = (String) DateFormat.format("yyyy-MM-dd",date);
                     editor.putString("date",mDate);
                     editor.apply();
                     mBirthdayTextView.setText(mDate);
                 }
                 break;
             default:
                 break;
         }
    }
}

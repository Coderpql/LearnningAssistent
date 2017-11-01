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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.learning.wow.learningassistence.MainActivity;
import com.learning.wow.learningassistence.R;
import com.learning.wow.learningassistence.db.Note;
import com.learning.wow.learningassistence.fragmentActivity.NoteActivity;
import com.learning.wow.learningassistence.fragmentActivity.NoteDateActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wow on 2017/10/8.
 */

public class NoteDateFragment extends Fragment {

    private String date;
    private Note note;
    private EditText mTitleEditText;
    private TextView mDateTextView;
    private EditText mDataEditText;
    private ImageButton mDeleteButton;
    private static final String EXTRA_DATE = "noteDate";
    private static final int REQUEST_DATE = 1;
    private static final String DIALOG_DATE = "noteDateTODatePicker";
    public static final String NOTE_ID = "moteid";

    public static Intent newInstance(Context packageContext, String date){
        Intent intent = new Intent(packageContext, NoteDateActivity.class);
        intent.putExtra(EXTRA_DATE,date);
        return intent;
    }
    public static Intent NewInstance(Context packageContext, int id){
        Intent intent = new Intent(packageContext, NoteDateActivity.class);
        intent.putExtra(EXTRA_DATE,id);
        return intent;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_data,container,false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.note_data_toolbar);
        mTitleEditText = (EditText)view.findViewById(R.id.note_title_text);
        mDataEditText = (EditText)view.findViewById(R.id.note_data_text);
        mDateTextView = (TextView)view.findViewById(R.id.note_date_text);
        mDeleteButton = (ImageButton)view.findViewById(R.id.delete_note_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataSupport.delete(Note.class,note.getId());
                Intent intent = new Intent();
                intent.putExtra(NOTE_ID,note.getId());
                getActivity().setResult(Activity.RESULT_OK,intent);
                getActivity().finish();
            }
        });
        date = getActivity().getIntent().getStringExtra(EXTRA_DATE);
        if(date != null){
            mDateTextView.setText(date);
            note = new Note();
        }else {
            int mId = getActivity().getIntent().getIntExtra(EXTRA_DATE,0);
            note = DataSupport.find(Note.class, mId);
            Date date1 = new Date();
            date = (String) DateFormat.format("yyyy-MM-dd",date1);
            mTitleEditText.setText(note.getTitle());
            mDateTextView.setText(note.getDate());
            mDataEditText.setText(note.getData());
        }
        mDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(date);
                dialog.setTargetFragment(NoteDateFragment.this,REQUEST_DATE);
                dialog.show(manager,DIALOG_DATE);
            }
        });
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return view;
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
                saveDate(note);
                Intent intent = new Intent();
                intent.putExtra(NOTE_ID,note.getId());
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
                    mDateTextView.setText(mDate);
                }
                break;
            default:
                break;
        }
    }
    public void saveDate(Note note1){
        String title = mTitleEditText.getText().toString();
        String date = mDateTextView.getText().toString();
        String data = mDataEditText.getText().toString();
        note1.setTitle(title);
        note1.setDate(date);
        note1.setData(data);
        note1.save();
    }

}

package com.learning.wow.learningassistence.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learning.wow.learningassistence.R;
import com.learning.wow.learningassistence.db.Note;
import com.learning.wow.learningassistence.fragmentActivity.NoteActivity;
import com.learning.wow.learningassistence.fragmentActivity.NoteDateActivity;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.Date;
import java.util.List;

/**
 * Created by wow on 2017/9/28.
 */

public class NoteFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Note> mNotes;
    private NoteAdapter mNoteAdapter;
    private int position;
    private static final int NEW_NOTE_DATE = 0;
    private static final int OPEN_NOTE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_fragment,container,false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.note_data_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        FloatingActionButton add = (FloatingActionButton)view.findViewById(R.id.add_note);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                String mDate = (String) DateFormat.format("yyyy-MM-dd",date);
                Intent intent = NoteDateFragment.newInstance(getActivity(),mDate);
                startActivityForResult(intent,NEW_NOTE_DATE);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotes = DataSupport.findAll(Note.class);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == NEW_NOTE_DATE){
            updateUI();
            mNotes = DataSupport.findAll(Note.class);
            updateUI();
            mNoteAdapter.notifyDataSetChanged();
            int id =data.getIntExtra(NoteDateFragment.NOTE_ID,0);
            MoveToPosition(new LinearLayoutManager(getActivity()),mRecyclerView,id-1);
        }
        if (requestCode == OPEN_NOTE){
            mNotes = DataSupport.findAll(Note.class);
            updateUI();
            mNoteAdapter.notifyDataSetChanged();
            int id =data.getIntExtra(NoteDateFragment.NOTE_ID,0);
            MoveToPosition(new LinearLayoutManager(getActivity()),mRecyclerView,id-1);
        }

    }
    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Note mNote;
        private TextView titleTextView;
        private TextView dateTextView;
        public NoteHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView)itemView.findViewById(R.id.note_title_text_view);
            dateTextView = (TextView)itemView.findViewById(R.id.note_date_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            position = mRecyclerView.getChildAdapterPosition(v);
            mNote = mNotes.get(position);
            Intent intent = NoteDateFragment.NewInstance(getActivity(),mNote.getId());
            startActivityForResult(intent,OPEN_NOTE);
        }

        public void bindNote(Note note){
             mNote = note;
            titleTextView.setText(mNote.getTitle());
            dateTextView.setText(mNote.getDate());
        }
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {

        private List<Note> mnotes;

        public NoteAdapter(List<Note> notes) {
            mnotes = notes;
        }

        @Override
        public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.note_data_list, parent, false);
            return new NoteHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteHolder holder, int position) {
            Note note = mnotes.get(position);
            holder.bindNote(note);
        }

        @Override
        public int getItemCount() {
            return mnotes.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
            mNoteAdapter = new NoteAdapter(mNotes);
            mRecyclerView.setAdapter(mNoteAdapter);
    }
    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {


        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }

    }
}

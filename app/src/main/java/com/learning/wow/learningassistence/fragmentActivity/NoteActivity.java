package com.learning.wow.learningassistence.fragmentActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.learning.wow.learningassistence.SingleFragmentActivity;
import com.learning.wow.learningassistence.fragment.NoteFragment;

/**
 * Created by wow on 2017/9/28.
 */

public class NoteActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new NoteFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}

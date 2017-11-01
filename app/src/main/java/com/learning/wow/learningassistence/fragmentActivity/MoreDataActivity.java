package com.learning.wow.learningassistence.fragmentActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.learning.wow.learningassistence.SingleFragmentActivity;
import com.learning.wow.learningassistence.fragment.MoreDateFragment;

/**
 * Created by wow on 2017/10/4.
 */

public class MoreDataActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new MoreDateFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
